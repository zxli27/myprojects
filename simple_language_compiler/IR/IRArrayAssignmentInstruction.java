package IR;
import CodeGen.CodeGenVisitor;
import java.lang.StringBuilder;

public class IRArrayAssignmentInstruction extends IRInstruction {
    public Temp id;
    public Temp index;
    public Temp right;


    public IRArrayAssignmentInstruction(Temp id, Temp index, Temp right){
        this.id = id;
        this.index = index;
        this.right = right;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.id);
        s.append("[");
        s.append(this.index);
        s.append("]");
        s.append(" := ");
        s.append(this.right);
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}