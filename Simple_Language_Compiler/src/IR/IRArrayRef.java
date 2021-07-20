package IR;
import CodeGen.CodeGenVisitor;
import java.lang.StringBuilder;
import Type.*;

public class IRArrayRef extends IRInstruction {
    public Temp dest;
    public Temp index;
    public Temp id;

    public IRArrayRef(Temp dest, Temp id, Temp index){
        this.dest = dest;
        this.index = index;
        this.id = id;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := ");
        s.append(this.id);
        s.append("[");
        s.append(this.index);
        s.append("];");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}