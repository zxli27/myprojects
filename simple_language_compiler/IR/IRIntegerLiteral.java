package IR;
import CodeGen.CodeGenVisitor;

public class IRIntegerLiteral extends IRInstruction {
    public Temp dest;
    public int value;

    public IRIntegerLiteral (Temp dest, int value){
        this.dest = dest;
        this.value = value;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := ");
        s.append(Integer.toString(this.value));
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}