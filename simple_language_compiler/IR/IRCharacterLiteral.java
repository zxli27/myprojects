package IR;
import CodeGen.CodeGenVisitor;

public class IRCharacterLiteral extends IRInstruction {
    public Temp dest;
    public char value;

    public IRCharacterLiteral (Temp dest, char value){
        this.dest = dest;
        this.value = value;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := \u0027");
        s.append(this.value);
        s.append("\u0027;");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}