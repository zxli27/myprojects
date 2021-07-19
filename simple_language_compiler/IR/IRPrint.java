package IR;
import CodeGen.CodeGenVisitor;

public class IRPrint extends IRInstruction {
    public Temp temp;

    public IRPrint(Temp temp) {
        this.temp = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("PRINT");
        s.append(this.temp.type.toChar());
        s.append(" ");
        s.append(this.temp);
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}