package IR;
import CodeGen.CodeGenVisitor;

public abstract class IRInstruction {
    public abstract void accept(CodeGenVisitor v);
}