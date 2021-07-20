package IR;
import CodeGen.CodeGenVisitor;

import AST.*;
import Type.*;
import Environment.*;
import java.util.ArrayList;

public class TempVisitor {

    private Environment<String, Type> fEnv;
    private Environment<String, Temp> vEnv;
    private TempFactory tFactory;
    private LabelFactory lFactory;
    private ArrayList<IRInstruction> funcInstrs;
    public IRProgram program;

    public TempVisitor(String programName){
        this.fEnv = new StackEnvironment<String, Type>();
        this.vEnv = new StackEnvironment<String, Temp>();
        this.program = new IRProgram(programName);
    }

    public Temp visit(AddExpression e){
        Temp lhs = e.leftExpr.accept(this);
        Temp rhs = e.rightExpr.accept(this);
        Temp dest = this.tFactory.getTemp(lhs.type);
        IRInstruction ir = new IRBinaryOp(dest, lhs, rhs, IRBinaryOp.IRBiOp.ADD);
        this.funcInstrs.add(ir);

        return dest;
    }

    public Temp visit(ArrayAssignmentStatement s){
        Temp id = s.arrayReference.id.accept(this);
        Temp index = s.arrayReference.expr.accept(this); 
        Temp expr = s.expr.accept(this);
        IRInstruction assignment = new IRArrayAssignmentInstruction(id, index, expr);
        this.funcInstrs.add(assignment);
        return null;
    }

    public Temp visit(ArrayReference r){
        Temp id = r.id.accept(this);
        Temp index = r.expr.accept(this);
        Temp dest = this.tFactory.getTemp(((ArrayType)id.type).type);
        IRInstruction ir = new IRArrayRef(dest, id, index);
        this.funcInstrs.add(ir);
        return dest;
    }

    public Temp visit(AssignmentStatement s){
        Temp id = s.id.accept(this);
        Temp expr = s.expr.accept(this);
        IRInstruction assignment = new IRAssignmentInstruction(id, expr);
        this.funcInstrs.add(assignment);
        return null;
    }

    public Temp visit(Block b){
        for (int i = 0; i<b.statementArray.size(); i++) {
            b.statementArray.get(i).accept(this);
        }
        return null;
    }

    public Temp visit(BooleanLiteral l){
        Temp dest = this.tFactory.getTemp(new BooleanType());
        IRInstruction ir = new IRBooleanLiteral(dest, l.value);
        this.funcInstrs.add(ir);
        return dest;
    }

    public Temp visit(CharacterLiteral l){
        Temp dest = this.tFactory.getTemp(new CharType());
        IRInstruction ir = new IRCharacterLiteral(dest, l.value);
        this.funcInstrs.add(ir);
        return dest;
    }

    public Temp visit(Declaration d){
        Type type = d.type.type;
        String name = d.id.id;
        Temp temp = this.tFactory.getTemp(type, TempEntry.TempClass.PARAMETER, name);
        this.vEnv.add(name, temp);
        //this.funcInstrs.add(new IRDeclaration(temp, type));
        return null;
    }

    public Temp visit(EmptyStatement s){
        return null;
    }

    public Temp visit(EqualExpression e){
        Temp lhs = e.leftExpr.accept(this);
        Temp rhs = e.rightExpr.accept(this);
        Temp dest = this.tFactory.getTemp(new BooleanType());
        IRInstruction ir = new IRBinaryOp(dest, lhs, rhs, IRBinaryOp.IRBiOp.EQUAL);
        funcInstrs.add(ir);

        return dest;
    }

    public Temp visit(ExpressionStatement s){
        s.expr.accept(this);
        return null;
    }

    public Temp visit(FloatLiteral l){
        Temp dest = this.tFactory.getTemp(new FloatType());
        IRInstruction ir = new IRFloatLiteral(dest, l.value);
        this.funcInstrs.add(ir);
        return dest;
    }

    public Temp visit(FormalParameters p){
        for (int i = 0; i < p.size; i++) {
            p.get(i).accept(this);
        }
        return null;
    }

    public Temp visit(Function f){
        this.vEnv.beginScope();

        this.tFactory = new TempFactory();
        this.lFactory = new LabelFactory();
        this.funcInstrs = new ArrayList<IRInstruction>();
        String name = f.functionDecl.declaration.id.id;
        Type returnType = f.functionDecl.declaration.type.type;
        ArrayList<Type> parameterTypes = new ArrayList<Type>();
        for (int i=0; i<f.functionDecl.formalParameters.size; i++) {
            parameterTypes.add(f.functionDecl.formalParameters.parameterArray.get(i).type.type);
        }
        IRFunction func = new IRFunction(name, returnType, parameterTypes);

        f.functionDecl.accept(this);
        f.functionBody.accept(this);

        func.instructions = this.funcInstrs;
        func.tFactory = this.tFactory;
        func.lFactory = this.lFactory;
        this.program.addFunction(func);
        this.vEnv.endScope();
        return null;
    }

    public Temp visit(FunctionBody f){
        for (int i = 0; i<f.variableDeclarationArray.size(); i++) {
            f.variableDeclarationArray.get(i).accept(this);
        }

        for (int i = 0; i<f.statementArray.size(); i++) {
            f.statementArray.get(i).accept(this);
        }
 
        // an empty return instruction
        IRInstruction returnInstr = new IRReturn(null);
        this.funcInstrs.add(returnInstr);
        return null;
    }

    public Temp visit(FunctionCall f){
        String funcName = f.id.id;
        ArrayList<Temp> paras = new ArrayList<>(); 
        for (int i=0; i<f.expressionList.size(); i++) {
            paras.add(f.expressionList.get(i).accept(this));
        }
        Type returnType = this.fEnv.lookup(funcName);
        Temp t;
        IRInstruction functionCall;
        if (returnType.equals(new VoidType())) {
            t = null;
            functionCall = new IRFunctionCallVoid(funcName, paras);
        } else {
            t = this.tFactory.getTemp(returnType);
            functionCall = new IRFunctionCallWithReturn(t, funcName, paras);
        }
        this.funcInstrs.add(functionCall);
        return t;
    }

    public Temp visit(FunctionDecl f){
        f.formalParameters.accept(this);
        return null;
    }

    public Temp visit(Identifier i){
        return vEnv.lookup(i.id);
    }

    public Temp visit(IfStatement s){
        IRInstruction ir;
        Label l1 = this.lFactory.getLabel();
        Label l2 = this.lFactory.getLabel();
        Temp t = s.expr.accept(this);
        if (this.tFactory.isParameterOrLocal(t)){
            Temp t2 = this.tFactory.getTemp(new BooleanType());
            ir = new IRAssignmentInstruction(t2, t);
            this.funcInstrs.add(ir);
            t = t2;
        }
        ir = new IRUnaryOp(t,t, IRUnaryOp.IRUOp.INVERT);
        this.funcInstrs.add(ir);
        ir = new IRIfStatement(t, l1);
        this.funcInstrs.add(ir);
        s.ifBlock.accept(this);
        ir = new IRIfGoto(l2);
        this.funcInstrs.add(ir);
        this.funcInstrs.add(l1);
        if (s.elseBlock != null) {
            s.elseBlock.accept(this);
        }
        this.funcInstrs.add(l2);
        return null;
    }

    public Temp visit(IntegerLiteral l){
        Temp dest = this.tFactory.getTemp(new IntegerType());
        IRInstruction ir = new IRIntegerLiteral(dest, l.value);
        this.funcInstrs.add(ir);
        return dest;
    }
    public Temp visit(LessThanExpression e){
        Temp lhs = e.leftExpr.accept(this);
        Temp rhs = e.rightExpr.accept(this);
        Temp dest = this.tFactory.getTemp(new BooleanType());
        IRInstruction ir = new IRBinaryOp(dest, lhs, rhs, IRBinaryOp.IRBiOp.LESSTHAN);
        funcInstrs.add(ir);

        return dest;
    }
    public Temp visit(MultiplyExpression e){
        Temp lhs = e.leftExpr.accept(this);
        Temp rhs = e.rightExpr.accept(this);
        Temp dest = this.tFactory.getTemp(lhs.type);
        IRInstruction ir = new IRBinaryOp(dest, lhs, rhs, IRBinaryOp.IRBiOp.MULTIPLY);
        funcInstrs.add(ir);

        return dest;
    }
    public Temp visit(ParenthesesExpression e){
        return e.expr.accept(this);
    }
    public Temp visit(PrintlnStatement s){
        Temp temp = s.expr.accept(this);
        IRInstruction println = new IRPrintln(temp);
        this.funcInstrs.add(println);
        return null;
    }
    public Temp visit(PrintStatement s){
        Temp temp = s.expr.accept(this);
        IRInstruction println = new IRPrint(temp);
        this.funcInstrs.add(println);
        return null;
    }

    public Temp visit(Program p){
        this.fEnv.beginScope();

        for (int i = 0; i<p.size; i++) {
            Declaration d = p.funcList.get(i).functionDecl.declaration;
            String fName = d.id.id;
            Type fType = d.type.type;
            this.fEnv.add(fName, fType);
        }
        for (int i = 0; i<p.size; i++) {
            p.funcList.get(i).accept(this);
        }

        this.fEnv.endScope();
        return null;
    }
    public Temp visit(ReturnStatement s){
        Temp temp = null;
        if (s.expr != null) {
            temp = s.expr.accept(this);
        }
        IRInstruction r = new IRReturn(temp);
        this.funcInstrs.add(r);
        return null;
    }
    public Temp visit(StringLiteral l){
        Temp dest = this.tFactory.getTemp(new StringType());
        IRInstruction ir = new IRStringLiteral(dest, l.value);
        this.funcInstrs.add(ir);
        return dest;
    }
    public Temp visit(SubstractExpression e){
        Temp lhs = e.leftExpr.accept(this);
        Temp rhs = e.rightExpr.accept(this);
        Temp dest = this.tFactory.getTemp(lhs.type);
        IRInstruction ir = new IRBinaryOp(dest, lhs, rhs, IRBinaryOp.IRBiOp.SUBSTRACT);
        funcInstrs.add(ir);

        return dest;
    }
    public Temp visit(TypeNode t){
        return null;
    }

    public Temp visit(VariableDeclaration d){
        Type type = d.type.type;
        String name = d.id.id;
        Temp temp = this.tFactory.getTemp(type, TempEntry.TempClass.LOCAL, name);
        this.vEnv.add(name, temp);
        IRInstruction decl;
        if (type instanceof ArrayType){
            decl = new IRArrayDeclaration(temp, (ArrayType)type);
            this.funcInstrs.add(decl);
        } else {
            decl = new IRDeclaration(temp, type);
        }
        
        return null;
    }

    public Temp visit(WhileStatement s){
        IRInstruction ir;
        Label l1 = this.lFactory.getLabel();
        Label l2 = this.lFactory.getLabel();
        this.funcInstrs.add(l2);
        Temp t = s.expr.accept(this);
        if (this.tFactory.isParameterOrLocal(t)){
            Temp t2 = this.tFactory.getTemp(new BooleanType());
            ir = new IRAssignmentInstruction(t2, t);
            this.funcInstrs.add(ir);
            t = t2;
        }
        ir = new IRUnaryOp(t,t, IRUnaryOp.IRUOp.INVERT);
        this.funcInstrs.add(ir);
        
        ir = new IRIfStatement(t, l1);
        this.funcInstrs.add(ir);
        s.block.accept(this);
        ir = new IRIfGoto(l2);
        this.funcInstrs.add(ir);
        this.funcInstrs.add(l1);
        return null;
    }
}
