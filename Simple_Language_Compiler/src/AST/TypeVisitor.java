package AST;

import Environment.*;
import java.util.ArrayList;
import Type.*;

public class TypeVisitor {
    private Environment<String, FunctionDecl> fEnv;
    private Environment<String, Type> vEnv;
    private Type funcReturnType;

    public TypeVisitor () {
        fEnv = new StackEnvironment<String, FunctionDecl>();
        vEnv = new StackEnvironment<String, Type>();
    }

    public Type visit(AddExpression e) throws SemanticException{
        Type left = e.leftExpr.accept(this);
        Type right = e.rightExpr.accept(this);
        if (left.equals(right) && (
            left.equals(new IntegerType()) ||
            left.equals(new FloatType()) ||
            left.equals(new CharType()) ||
            left.equals(new StringType()))) {
        } else {
            throw new SemanticException(
                "Cannot add type " + left + " with type " + right + ".",
                e.line,
                e.offset);
        }
        return left;
    }
    public Type visit(ArrayAssignmentStatement s) throws SemanticException{
        Type left = s.arrayReference.accept(this);
        Type right = s.expr.accept(this);
        if (!left.equals(right)) {
            throw new SemanticException(
                "Array type does not match expression.",
                s.line,
                s.offset);
        }
        return null;
    }
    public Type visit(ArrayReference r) throws SemanticException{
        Type arrType = r.id.accept(this);
        if (!(arrType instanceof ArrayType)) {
            throw new SemanticException(
                "Variable " + r.id.id + "is not of type Array.",
                r.line,
                r.offset);
        }
        Type index = r.expr.accept(this);
        if (!index.equals(new IntegerType())) {
            throw new SemanticException(
                "Array index expression is not of type int.",
                r.line,
                r.offset);
        }
        return ((ArrayType)arrType).type;
    }
    public Type visit(AssignmentStatement s) throws SemanticException{
        Type left = s.id.accept(this);
        Type right = s.expr.accept(this);
        if (!left.equals(right)) {
            throw new SemanticException(
                "Variable " + s.id.id + " type does not match expression.",
                s.line,
                s.offset);
        }
        return null;
    }
    public Type visit(Block b) throws SemanticException{
        for (int i = 0; i<b.statementArray.size(); i++) {
            b.statementArray.get(i).accept(this);
        }
        return null;
    }
    public Type visit(BooleanLiteral l) throws SemanticException{
        return new BooleanType();
    }
    public Type visit(CharacterLiteral l) throws SemanticException{
        return new CharType();
    }
    public Type visit(Declaration d) throws SemanticException{
        if (this.vEnv.inCurrentScope(d.id.id)){
            throw new SemanticException(
                "Variable " + d.id.id + " already exists.",
                d.id.line,
                d.id.offset);
        }
        if (d.type.type.equals(new VoidType())){
            throw new SemanticException(
                "Variable " + d.id.id + " should not be of type void.",
                d.id.line,
                d.id.offset);
        }
        this.vEnv.add(d.id.id, d.type.type);
        return null;
    }
    public Type visit(EmptyStatement s) throws SemanticException{
        return null;
    }
    public Type visit(EqualExpression e) throws SemanticException{
        Type left = e.leftExpr.accept(this);
        Type right = e.rightExpr.accept(this);
        if (left.equals(right) && (
            left.equals(new IntegerType()) ||
            left.equals(new FloatType()) ||
            left.equals(new CharType()) ||
            left.equals(new StringType()) ||
            left.equals(new BooleanType()))) {
        } else {
            throw new SemanticException(
                "Cannot compare type " + left + " with type " + right + ".",
                e.line,
                e.offset);
        }
        return new BooleanType();
    }

    public Type visit(ExpressionStatement s) throws SemanticException{
        s.expr.accept(this);
        return null;
    }

    public Type visit(FloatLiteral l) throws SemanticException{
        return new FloatType();
    }

    public Type visit(FormalParameters p) throws SemanticException{
        Declaration d;
        for (int i = 0; i < p.size; i++) {
            p.get(i).accept(this);

        }
        return null;
    }

    public Type visit(Function f) throws SemanticException{
        this.vEnv.beginScope();
        f.functionDecl.accept(this);
        f.functionBody.accept(this);
        this.vEnv.endScope();
        return null;
    }

    public Type visit(FunctionBody f) throws SemanticException{

        for (int i = 0; i<f.variableDeclarationArray.size(); i++) {
            f.variableDeclarationArray.get(i).accept(this);
        }

        for (int i = 0; i<f.statementArray.size(); i++) {
            f.statementArray.get(i).accept(this);
        }
        return null;
    }
    public Type visit(FunctionCall f) throws SemanticException{
        if (this.fEnv.inCurrentScope(f.id.id)){
            FormalParameters fp = this.fEnv.lookup(f.id.id).formalParameters;
            ArrayList<Expression> e = f.expressionList;
            if(e.size() != fp.size) {
                throw new SemanticException(
                    "Function " + f.id.id + " parameter length does not match.",
                    f.line,
                    f.offset);
            }
            for (int i = 0; i < e.size(); i++) {
                Type p = e.get(i).accept(this);
                if(!p.equals(fp.get(i).type.type)){
                    throw new SemanticException(
                        "Function " + f.id.id + " parameter " + fp.get(i).id.id + " type do not match.",
                        e.get(i).line,
                        e.get(i).offset);
                }
            }
        }
        else {
            throw new SemanticException("Function " + f.id.id + " does not exist.",
                f.line,
                f.offset);
        }
        return this.fEnv.lookup(f.id.id).declaration.type.type;
    }

    public Type visit(FunctionDecl f) throws SemanticException{
        this.funcReturnType = f.declaration.type.type;
        f.formalParameters.accept(this);
        return null;
    }

    public Type visit(Identifier i) throws SemanticException{
        if (!this.vEnv.inCurrentScope(i.id)) {
            throw new SemanticException("Variable " + i.id + " is not declared.",
                i.line,
                i.offset);
        }
        return this.vEnv.lookup(i.id);
    }
    public Type visit(IfStatement s) throws SemanticException{
        Type conditionType = s.expr.accept(this);
        if (!conditionType.equals(new BooleanType())){
            throw new SemanticException(
                "If statement condition must be of type boolean.",
                s.expr.line,
                s.expr.offset);
        }
        s.ifBlock.accept(this);
        if (s.elseBlock != null) {
            s.elseBlock.accept(this);
        }
        return null;
    }
    public Type visit(IntegerLiteral l) throws SemanticException{
        return new IntegerType();
    }
    public Type visit(LessThanExpression e) throws SemanticException{
        Type left = e.leftExpr.accept(this);
        Type right = e.rightExpr.accept(this);
        if (left.equals(right) && (
            left.equals(new IntegerType()) ||
            left.equals(new FloatType()) ||
            left.equals(new CharType()) ||
            left.equals(new StringType()))) {
        } else {
            throw new SemanticException(
                "Cannot compare type " + left + " with type " + right + ".",
                e.line,
                e.offset);
        }
        return new BooleanType();
    }
    public Type visit(MultiplyExpression e) throws SemanticException{
        Type left = e.leftExpr.accept(this);
        Type right = e.rightExpr.accept(this);
        if (left.equals(right) && (
            left.equals(new IntegerType()) ||
            left.equals(new FloatType()))) {
        } else {
            throw new SemanticException(
                "Cannot multiply type " + left + " with type " + right + ".",
                e.line,
                e.offset);
        }
        return left;
    }
    public Type visit(ParenthesesExpression e) throws SemanticException{
        return e.expr.accept(this);
    }
    public Type visit(PrintlnStatement s) throws SemanticException{
        Type t = s.expr.accept(this);
        if ( (t instanceof ArrayType) || (t instanceof VoidType) ) {
            throw new SemanticException(
                "Array or void type cannot be printed.",
                s.expr.line,
                s.expr.offset);
        }
        return null;
    }
    public Type visit(PrintStatement s) throws SemanticException{
        Type t = s.expr.accept(this);
        if ( (t instanceof ArrayType) || (t instanceof VoidType)) {
            throw new SemanticException(
                "Array or void type cannot be printed.",
                s.expr.line,
                s.expr.offset);
        }
        return null;
    }

    public Type visit(Program p) throws SemanticException{
        this.fEnv.beginScope();
        for (int i = 0; i<p.size; i++) {
            FunctionDecl f = p.funcList.get(i).functionDecl;
            Declaration d = f.declaration;
            String fName = d.id.id;
            Type type = d.type.type;
            if (this.fEnv.inCurrentScope(fName)) {
                throw new SemanticException(
                    "Function " + fName + " already exists.",
                    d.line,
                    d.offset);
            }
            if (fName.equals("main")){
                if (!type.equals(new VoidType())) {
                    throw new SemanticException(
                        "Main function must return type void.",
                        d.type.line,
                        d.type.offset);
                }
                if (f.formalParameters.size != 0) {
                    throw new SemanticException(
                        "Main function must have no formal parameters.",
                        f.formalParameters.line,
                        f.formalParameters.offset);
                }
            }
            this.fEnv.add(fName, f);
        }

        if (!this.fEnv.inCurrentScope("main")) {
            throw new SemanticException(
                "Main function does not exist.",
                p.line,
                p.offset);
        }

        for (int i = 0; i<p.size; i++) {
            p.funcList.get(i).accept(this);
        }
        this.fEnv.endScope();
        return null;
    }

    public Type visit(ReturnStatement s) throws SemanticException{
        if (s.expr == null && !funcReturnType.equals(new VoidType())){
            throw new SemanticException(
                "Return expression is empty and does not match the function return type.",
                s.line,
                s.offset);
        }
        else if (funcReturnType.equals(new VoidType()) && s.expr != null ){
            throw new SemanticException(
                "Return expression should be empty when the function return type is void.",
                s.line,
                s.offset);
        }
        else if (s.expr == null){
            return null;
        }
        Type returnExprType = s.expr.accept(this);
        if (!returnExprType.equals(funcReturnType)){
            throw new SemanticException(
                "Return expression does not match function return type.",
                s.line,
                s.offset);
        }
        return null;
    }
    public Type visit(StringLiteral l) throws SemanticException{
        return new StringType();
    }
    public Type visit(SubstractExpression e) throws SemanticException{
        Type left = e.leftExpr.accept(this);
        Type right = e.rightExpr.accept(this);
        if (left.equals(right) && (
            left.equals(new IntegerType()) ||
            left.equals(new FloatType()) ||
            left.equals(new CharType()))) {
        } else {
            throw new SemanticException(
                "Cannot equal type " + left + " with type " + right + ".",
                e.line,
                e.offset);
        }
        return left;
    }
    public Type visit(TypeNode t) throws SemanticException{
        return t.type;
    }
    public Type visit(VariableDeclaration d) throws SemanticException{
        if (this.vEnv.inCurrentScope(d.id.id)){
            throw new SemanticException(
                "Variable " + d.id.id + " already exists.",
                d.id.line,
                d.id.offset);
        }
        if (d.type.type.equals(new VoidType())){
            throw new SemanticException(
                "Variable " + d.id.id + " should not be of type void.",
                d.id.line,
                d.id.offset);
        }
        this.vEnv.add(d.id.id, d.type.type);
        return null;
    }
    public Type visit(WhileStatement s) throws SemanticException{
        Type conditionType = s.expr.accept(this);
        if (!conditionType.equals(new BooleanType())){
            throw new SemanticException(
                "While statement condition must be of type boolean.",
                s.expr.line,
                s.expr.offset);
        }
        s.block.accept(this);
        return null;
    }
}