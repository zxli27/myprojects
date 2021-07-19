package IR;
import CodeGen.CodeGenVisitor;
import java.lang.StringBuilder;

public class IRReturn extends IRInstruction {
    public Temp temp;

    public IRReturn(Temp temp) {
        this.temp = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("RETURN");
        if (this.temp != null) {
            s.append(" ");
            s.append(this.temp);
        }
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }

}
