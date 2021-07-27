package CodeGen;

import IR.*;
import Type.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenVisitor{
    private final IRProgram program;
    private final StringBuilder sb;
    private int labelNumber;

    public CodeGenVisitor(IRProgram program){
        this.program = program;
        this.sb = new StringBuilder();
        this.labelNumber = 0;
        this.visit(this.program);

    }

    public void visit(IRArrayAssignmentInstruction a){
        this.sb.append("\taload " + a.id.number + "\n");
        this.sb.append("\tiload " + a.index.number + "\n");
        if (a.right.type.equals(new FloatType())) {
            this.sb.append("\tfload " + a.right.number+"\n");
            this.sb.append("\tfastore"+"\n");
        } else if (a.right.type.equals(new StringType()) || a.right.type instanceof ArrayType) {
            this.sb.append("\taload " + a.right.number+"\n");
            this.sb.append("\taastore"+"\n");
        } else {
            this.sb.append("\tiload " + a.right.number+"\n");
            this.sb.append("\tiastore"+"\n");
        }
    }

    public void visit(IRArrayDeclaration a){
        this.sb.append("\tldc "+((ArrayType)a.type).size+"\n");
        this.sb.append("\tnewarray "+((ArrayType)a.type).type+"\n");
        this.sb.append("\tastore "+a.dest.number+"\n");
    }

    public void visit(IRArrayRef a){
        
    }

    public void visit(IRAssignmentInstruction a){
        Temp left = a.lhs;
        Temp right = a.rhs;
        if (left.type.equals(new IntegerType())
            || left.type.equals(new CharType())
            || left.type.equals(new BooleanType())){
            this.sb.append("\tiload ");
            this.sb.append(right.number);
            this.sb.append("\n");
            this.sb.append("\tistore ");
            this.sb.append(left.number);
        } else if (left.type.equals(new FloatType())){
            this.sb.append("\tfload ");
            this.sb.append(right.number);
            this.sb.append("\n");
            this.sb.append("\tfstore ");
            this.sb.append(left.number);
        } else if (left.type.equals(new StringType()) || left.type instanceof ArrayType){
            this.sb.append("\taload ");
            this.sb.append(right.number);
            this.sb.append("\n");
            this.sb.append("\tastore ");
            this.sb.append(left.number);
        } 
        this.sb.append("\n");

    }

    public void visit(IRBinaryOp o){
        if (o.rhs.type.equals(new IntegerType())){
            this.sb.append("\tiload " + o.lhs.number+"\n");
            this.sb.append("\tiload " + o.rhs.number+"\n");
            if (o.op == IRBinaryOp.IRBiOp.ADD) {
                this.sb.append("\tiadd\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } else if (o.op == IRBinaryOp.IRBiOp.SUBSTRACT){
                this.sb.append("\tisub\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } else if (o.op == IRBinaryOp.IRBiOp.MULTIPLY){
                this.sb.append("\timul\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } else {
                this.sb.append("\tisub\n");
                int l1=this.labelNumber++;
                int l2=this.labelNumber++;
                if (o.op == IRBinaryOp.IRBiOp.LESSTHAN) {
                    this.sb.append("\tiflt L_"+ l1+"\n");
                } else {
                    this.sb.append("\tifeq L_"+ l1+"\n");
                }
                this.sb.append("\tldc 0\n");
                this.sb.append("\tgoto L_"+l2+"\n");
                this.sb.append("L_"+l1+":\n");
                this.sb.append("\tldc 1\n");
                this.sb.append("L_"+l2+":\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } 
        } else if (o.rhs.type.equals(new CharType())){
            this.sb.append("\tiload " + o.lhs.number+"\n");
            this.sb.append("\tiload " + o.rhs.number+"\n");
            if (o.op == IRBinaryOp.IRBiOp.ADD) {
                this.sb.append("\tiadd\n");
                this.sb.append("\ti2c\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } else if (o.op == IRBinaryOp.IRBiOp.SUBSTRACT){
                this.sb.append("\tisub\n");
                this.sb.append("\ti2c\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } else {
                this.sb.append("\tisub\n");
                int l1=this.labelNumber++;
                int l2=this.labelNumber++;
                if (o.op == IRBinaryOp.IRBiOp.LESSTHAN) {
                    this.sb.append("\tiflt L_"+ l1+"\n");
                } else {
                    this.sb.append("\tifeq L_"+ l1+"\n");
                }
                this.sb.append("\tldc 0\n");
                this.sb.append("\tgoto L_"+l2+"\n");
                this.sb.append("L_"+l1+":\n");
                this.sb.append("\tldc 1\n");
                this.sb.append("L_"+l2+":\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } 
        } else if (o.rhs.type.equals(new FloatType())) {
            this.sb.append("\tfload " + o.lhs.number+"\n");
            this.sb.append("\tfload " + o.rhs.number+"\n");
            if (o.op == IRBinaryOp.IRBiOp.ADD) {
                this.sb.append("\tfadd\n");
                this.sb.append("\tfstore "+o.dest.number+"\n");
            } else if (o.op == IRBinaryOp.IRBiOp.SUBSTRACT){
                this.sb.append("\tfsub\n");
                this.sb.append("\tfstore "+o.dest.number+"\n");
            } else if (o.op == IRBinaryOp.IRBiOp.MULTIPLY){
                this.sb.append("\tfmul\n");
                this.sb.append("\tfstore "+o.dest.number+"\n");
            } else {
                this.sb.append("\tfcmpg\n");
                int l1=this.labelNumber++;
                int l2=this.labelNumber++;
                if (o.op == IRBinaryOp.IRBiOp.LESSTHAN) {
                    this.sb.append("\tiflt L_"+ l1+"\n");
                } else {
                    this.sb.append("\tifeq L_"+ l1+"\n");
                }
                this.sb.append("\tldc 0\n");
                this.sb.append("\tgoto L_"+l2+"\n");
                this.sb.append("L_"+l1+":\n");
                this.sb.append("\tldc 1\n");
                this.sb.append("L_"+l2+":\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } 
        } else if (o.rhs.type.equals(new StringType())) {
            if (o.op == IRBinaryOp.IRBiOp.ADD) {
                this.sb.append("\tnew java/lang/StringBuffer\n");
                this.sb.append("\tdup\n");
                this.sb.append("\tinvokenonvirtual java/lang/StringBuffer/<init>()V\n");
                this.sb.append("\taload " + o.lhs.number+"\n");
                this.sb.append("\tinvokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;\n");
                this.sb.append("\taload " + o.rhs.number+"\n");
                this.sb.append("\tinvokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;\n");
                this.sb.append("\tinvokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;\n");
                this.sb.append("\tastore "+o.dest.number+"\n");
            } else {
                this.sb.append("\taload " + o.lhs.number+"\n");
                this.sb.append("\taload " + o.rhs.number+"\n");
                this.sb.append("\tinvokevirtual java/lang/String/compareTo(Ljava/lang/String;)I\n");
                int l1=this.labelNumber++;
                int l2=this.labelNumber++;
                if (o.op == IRBinaryOp.IRBiOp.LESSTHAN) {
                    this.sb.append("\tiflt L_"+ l1+"\n");
                } else {
                    this.sb.append("\tifeq L_"+ l1+"\n");
                }
                this.sb.append("\tldc 0\n");
                this.sb.append("\tgoto L_"+l2+"\n");
                this.sb.append("L_"+l1+":\n");
                this.sb.append("\tldc 1\n");
                this.sb.append("L_"+l2+":\n");
                this.sb.append("\tistore "+o.dest.number+"\n");
            } 
        } else { 
            this.sb.append("\tiload " + o.lhs.number+"\n");
            this.sb.append("\tiload " + o.rhs.number+"\n");
            this.sb.append("\tixor\n");
            this.sb.append("\tldc 1\n");
            this.sb.append("\tixor\n");
            this.sb.append("\tistore "+o.dest.number+"\n");
        }
        
    }

    public void visit(IRBooleanLiteral l){
        this.sb.append("\tldc ");
        if (l.value) {
            this.sb.append(1);
        }
        else {
            this.sb.append(0);
        }
        this.sb.append("\n");
        this.sb.append("\tistore ");
        this.sb.append(l.dest.number);
        this.sb.append("\n");
    }

    public void visit(IRCharacterLiteral l){
        this.sb.append("\tldc ");
        this.sb.append((int)l.value);
        this.sb.append("\n");
        this.sb.append("\tistore ");
        this.sb.append(l.dest.number);
        this.sb.append("\n");   
    }

    public void visit(IRDeclaration d){

    }

    public void visit(IRFloatLiteral l){
        this.sb.append("\tldc ");
        this.sb.append(l.value);
        this.sb.append("\n");
        this.sb.append("\tfstore ");
        this.sb.append(l.dest.number);
        this.sb.append("\n");
    }

    public void visit(IRFunction f){
        this.sb.append(".method public static ");
        String name = f.name;
        if (name.equals("main")) {
            this.sb.append("__");
        }
        this.sb.append(name);
        this.sb.append("(");
        for (int i=0; i<f.parameterTypes.size(); i++){
            this.sb.append(this.typeAbbr(f.parameterTypes.get(i)));
        }
        this.sb.append(")");
        this.sb.append(this.typeAbbr(f.returnType));
        this.sb.append("\n\t.limit locals ");
        int tempNum = f.tFactory.pos+1;
        this.sb.append(tempNum);
        this.sb.append("\n");
        TempEntry t;
        int l1=this.labelNumber++;
        int l2=this.labelNumber++;
        for (int i=0; i<tempNum; i++){
            this.sb.append("\t.var ");
            this.sb.append(i);
            this.sb.append(" is T");
            t = f.tFactory.tempArray[i];
            this.sb.append(t.number);
            this.sb.append("  "+this.typeAbbr(t.type)+" from L_");
            this.sb.append(l1);
            this.sb.append(" to L_");
            this.sb.append(l2);
            this.sb.append("\n");
        }
        this.sb.append("\t.limit stack 16\n");
        this.sb.append("L_");
        this.sb.append(l1);
        this.sb.append(":\n");
        for (int i=0; i<tempNum; i++){
            t = f.tFactory.tempArray[i];
            if (t.tClass == TempEntry.TempClass.PARAMETER){
                continue;
            }
            if (t.type.equals(new IntegerType())) {
                this.sb.append("\tldc 0\n");
                this.sb.append("\tistore "+i+"\n");
            } else if (t.type.equals(new FloatType())) {
                this.sb.append("\tldc 0.0\n");
                this.sb.append("\tfstore "+i+"\n");
            } else if (t.type.equals(new CharType())) {
                this.sb.append("\tldc 0\n");
                this.sb.append("\tistore "+i+"\n");
            } else if (t.type.equals(new StringType())) {
                this.sb.append("\taconst_null\n");
                this.sb.append("\tastore "+i+"\n");
            } else if (t.type.equals(new BooleanType())) {
                this.sb.append("\tldc 0\n");
                this.sb.append("\tistore "+i+"\n");
            } else {
                this.sb.append("\taconst_null\n");
                this.sb.append("\tastore "+i+"\n");
            }
        }
        IRInstruction instr;
        for (int i=0; i< f.instructions.size(); i++){
            instr = f.instructions.get(i);
            this.sb.append(";\t\t\t");
            this.sb.append(instr);
            this.sb.append("\n");
            instr.accept(this);
        }

        this.sb.append("L_");
        this.sb.append(l2);
        this.sb.append(":\n");

        this.sb.append(".end method\n");
    }

    public void visit(IRFunctionCallVoid f){
        for (Temp t : f.expressionList) {
            if (t.type.equals(new FloatType())) {
                this.sb.append("\tfload ");
            } else if (t.type.equals(new StringType()) || t.type instanceof ArrayType) {
                this.sb.append("\taload ");
            } else {
                this.sb.append("\tiload ");
            }
            this.sb.append(t.number);
            this.sb.append("\n");
        }
        this.sb.append("\tinvokestatic ");
        this.sb.append(this.program.name+"/"+f.functionName+"(");
        for (Temp t : f.expressionList) {
            this.sb.append(this.typeAbbr(t.type));
        }
        this.sb.append(")");
        this.sb.append(this.typeAbbr(f.returnType));
        this.sb.append("\n");
    }

    public void visit(IRFunctionCallWithReturn f){
        for (Temp t : f.expressionList) {
            if (t.type.equals(new FloatType())) {
                this.sb.append("\tfload ");
            } else if (t.type.equals(new StringType()) || t.type instanceof ArrayType) {
                this.sb.append("\taload ");
            } else {
                this.sb.append("\tiload ");
            }
            this.sb.append(t.number);
            this.sb.append("\n");
        }
        this.sb.append("\tinvokestatic ");
        this.sb.append(this.program.name+"/"+f.functionName+"(");
        for (Temp t : f.expressionList) {
            this.sb.append(this.typeAbbr(t.type));
        }
        this.sb.append(")");
        this.sb.append(this.typeAbbr(f.returnType));
        this.sb.append("\n");
        if (f.dest.type.equals(new FloatType())) {
            this.sb.append("\tfstore "+ f.dest.number+"\n");
        } else if (f.dest.type.equals(new StringType()) || f.dest.type instanceof ArrayType) {
            this.sb.append("\tastore "+ f.dest.number+"\n");
        } else {
            this.sb.append("\tistore "+ f.dest.number+"\n");
        }

    }

    public void visit(IRIfGoto a){
        this.sb.append("\tgoto L"+a.go.number);
        this.sb.append("\n");
    }

    public void visit(IRIfStatement s){
        s.expr.accept(this);
        this.sb.append("\tifne L"+s.go.number);
        this.sb.append("\n");
    }

    public void visit(IRIntegerLiteral l){
        this.sb.append("\tldc ");
        this.sb.append(l.value);
        this.sb.append("\n");
        this.sb.append("\tistore ");
        this.sb.append(l.dest.number);
        this.sb.append("\n");
    }

    public void visit(IRPrint p){
        this.sb.append("\tgetstatic java/lang/System/out Ljava/io/PrintStream;\n");
        String sign="";
        if (p.temp.type.equals(new IntegerType())) {
            this.sb.append("\tiload ");
            sign="I";
        } else if (p.temp.type.equals(new FloatType())) {
            this.sb.append("\tfload ");
            sign="F";
        } else if (p.temp.type.equals(new CharType())) {
            this.sb.append("\tiload ");
            sign="C";
        } else if (p.temp.type.equals(new StringType())) {
            this.sb.append("\taload ");
            sign="Ljava/lang/String;";
        } else if (p.temp.type.equals(new BooleanType())) {
            this.sb.append("\tiload ");
            sign="Z";
        }
        this.sb.append(p.temp.number);
        this.sb.append("\n\tinvokevirtual java/io/PrintStream/print("+sign+")V\n");
    }

    public void visit(IRPrintln p){
        this.sb.append("\tgetstatic java/lang/System/out Ljava/io/PrintStream;\n");
        String sign="";
        if (p.temp.type.equals(new IntegerType())) {
            this.sb.append("\tiload ");
            sign="I";
        } else if (p.temp.type.equals(new FloatType())) {
            this.sb.append("\tfload ");
            sign="F";
        } else if (p.temp.type.equals(new CharType())) {
            this.sb.append("\tiload ");
            sign="C";
        } else if (p.temp.type.equals(new StringType())) {
            this.sb.append("\taload ");
            sign="Ljava/lang/String;";
        } else if (p.temp.type.equals(new BooleanType())) {
            this.sb.append("\tiload ");
            sign="Z";
        }
        this.sb.append(p.temp.number);
        this.sb.append("\n\tinvokevirtual java/io/PrintStream/println("+sign+")V\n");
    }

    public void visit(IRProgram p){
        this.sb.append(".source ");
        this.sb.append(this.program.name);
        this.sb.append(".ir\n");
        this.sb.append(".class public ");
        this.sb.append(this.program.name);
        this.sb.append("\n");
        this.sb.append(".super java/lang/Object\n\n");
        for (int i=0; i< p.functions.size(); i++){
            p.functions.get(i).accept(this);
            this.sb.append("\n");
        }
        this.sb.append(";--------------------------------------------;\n");
        this.sb.append(";                                            ;\n");
        this.sb.append("; Boilerplate                                ;\n");
        this.sb.append(";                                            ;\n");
        this.sb.append(";--------------------------------------------;\n\n");
         this.sb.append(".method public static main([Ljava/lang/String;)V\n");
        this.sb.append("\t; set limits used by this method\n");
        this.sb.append("\t.limit locals 1\n");
        this.sb.append("\t.limit stack 4\n");
        this.sb.append("\tinvokestatic ");
        this.sb.append(this.program.name);
        this.sb.append("/__main()V\n");
        this.sb.append("\treturn\n");
        this.sb.append(".end method\n\n");
        this.sb.append("; standard initializer\n");
        this.sb.append(".method public <init>()V\n");
        this.sb.append("\taload_0\n");
        this.sb.append("\tinvokenonvirtual java/lang/Object/<init>()V\n");
        this.sb.append("\treturn\n");
        this.sb.append(".end method\n");
    }

    public void visit(IRReturn r){
        if (r.temp != null) {
            String sign="";
            if (r.temp.type.equals(new IntegerType())) {
                this.sb.append("\tiload ");
                sign="i";
            } else if (r.temp.type.equals(new FloatType())) {
                this.sb.append("\tfload ");
                sign="f";
            } else if (r.temp.type.equals(new CharType())) {
                this.sb.append("\tiload ");
                sign="i";
            } else if (r.temp.type.equals(new StringType())) {
                this.sb.append("\taload ");
                sign="a";
            } else if (r.temp.type.equals(new BooleanType())) {
                this.sb.append("\tiload ");
                sign="i";
            } else {
                this.sb.append("\taload ");
                sign="a";
            }
            this.sb.append(r.temp.number);
            this.sb.append("\n");
            this.sb.append("\t"+sign+"return\n");
        } else{
            this.sb.append("\treturn\n");
        }
    }

    public void visit(IRStringLiteral l){
        this.sb.append("\tldc \"");
        this.sb.append(l.value);
        this.sb.append("\"\n");
        this.sb.append("\tastore ");
        this.sb.append(l.dest.number);
        this.sb.append("\n");
        
    }

    public void visit(IRUnaryOp op){
        op.rhs.accept(this);
        this.sb.append("\tldc 1\n");
        this.sb.append("\tixor\n");
        this.sb.append("\tistore ");
        this.sb.append(op.dest.number);
        this.sb.append("\n");
    }

    public void visit(Label l){
        this.sb.append("L"+l.number);
        this.sb.append(":\n");
    }

    public void visit(Temp t){
        if (t.type.equals(new IntegerType())) {
            this.sb.append("\tiload ");
        } else if (t.type.equals(new FloatType())) {
            this.sb.append("\tfload ");
        } else if (t.type.equals(new CharType())) {
            this.sb.append("\tiload ");
        } else if (t.type.equals(new StringType())) {
            this.sb.append("\taload ");
        } else if (t.type.equals(new BooleanType())) {
            this.sb.append("\tiload ");
        }
        this.sb.append(t.number);
        this.sb.append("\n");
    }


    private String typeAbbr(Type ty) {
        Type t = ty;
        String abbr = "";
        if (t instanceof ArrayType) {
            abbr = "[";
            t = ((ArrayType)t).type;
        } 
        if (t.equals(new IntegerType())) {
            return abbr+"I";
        } else if (t.equals(new FloatType())) {
            return abbr+"F";
        } else if (t.equals(new BooleanType())) {
            return abbr+"Z";
        } else if (t.equals(new CharType())) {
            return abbr+"C";
        } else if (t.equals(new StringType())) {
            return abbr+"Ljava/lang/String;";
        } else if (t.equals(new VoidType())) {
            return abbr+"V";
        } else {
            return null;
        }
    }

    public void toFile() throws IOException {
        String filename = this.program.name + ".j";
        FileWriter fileWriter = new FileWriter(filename);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            bufferedWriter.write(this.sb.toString());
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            bufferedWriter.close();
        }

    }
}