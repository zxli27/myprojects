package IR;
import CodeGen.CodeGenVisitor;

public class IRStringLiteral extends IRInstruction {
    public Temp dest;
    public String value;

    public IRStringLiteral (Temp dest, String value){
        this.dest = dest;
        this.value = value;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := \"");
        s.append(this.value);
        s.append("\";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}