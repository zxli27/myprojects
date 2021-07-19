package IR;
import CodeGen.CodeGenVisitor;
import java.lang.StringBuilder;

public class IRBinaryOp extends IRInstruction {
    public enum IRBiOp {
        ADD,
        SUBSTRACT,
        LESSTHAN,
        EQUAL,
        MULTIPLY
    };
    public Temp dest;
    public Temp lhs;
    public Temp rhs;
    public IRBiOp op;

    public IRBinaryOp(Temp dest, Temp left, Temp right, IRBiOp op){
        this.dest = dest;
        this.lhs = left;
        this.rhs = right;
        this.op = op;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := ");
        s.append(this.lhs);
        s.append(" ");
        s.append(this.lhs.type.toChar());
        if (op==IRBiOp.ADD){
            s.append("+");
        }
        else if (op==IRBiOp.SUBSTRACT){
            s.append("-");
        }
        else if (op==IRBiOp.LESSTHAN){
            s.append("<");
        }
        else if (op==IRBiOp.EQUAL){
            s.append("==");
        }
        else {
            s.append("*");
        }
        s.append(" ");
        s.append(this.rhs);
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}