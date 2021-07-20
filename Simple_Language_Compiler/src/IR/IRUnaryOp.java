package IR;
import CodeGen.CodeGenVisitor;



public class IRUnaryOp extends IRInstruction {
    public enum IRUOp {
        INVERT,
        NEGATIVE
    };
    public Temp dest;
    public Temp rhs;
    public IRUOp op;

    public IRUnaryOp(Temp dest, Temp right, IRUOp op){
        this.dest = dest;
        this.rhs = right;
        this.op = op;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := ");
        if (op==IRUOp.INVERT){
            s.append("Z! ");
        }
        else {
            s.append(this.rhs.type.toChar());
            s.append("- ");
        }
        s.append(this.rhs);
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}