package IR;
import CodeGen.CodeGenVisitor;

public class IRBooleanLiteral extends IRInstruction {
    public Temp dest;
    public boolean value;

    public IRBooleanLiteral (Temp dest, boolean value){
        this.dest = dest;
        this.value = value;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := ");
        s.append(this.value ? "TRUE" : "FALSE");
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}