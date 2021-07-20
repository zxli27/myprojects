package IR;
import CodeGen.CodeGenVisitor;

public class IRIfGoto extends IRInstruction {
    public Label go;

    public IRIfGoto(Label g) {
        this.go = g;
    }

    public String toString(){
        return "GOTO L"+Integer.toString(go.number)+";"; 
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}