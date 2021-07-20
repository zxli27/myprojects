package IR;
import CodeGen.CodeGenVisitor;
import java.lang.StringBuilder;

public class IRAssignmentInstruction extends IRInstruction {
    public Temp lhs;
    public Temp rhs;


    public IRAssignmentInstruction(Temp left, Temp right){
        this.lhs = left;
        this.rhs = right;
    }

    public String toString() {

        StringBuilder s = new StringBuilder();
        s.append(lhs);
        s.append(" := ");
        s.append(rhs);
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}