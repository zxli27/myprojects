package IR;
import CodeGen.CodeGenVisitor;

import Type.*;
import java.lang.StringBuilder;

public class IRArrayDeclaration extends IRInstruction {
    public Temp dest;
    public ArrayType type;

    public IRArrayDeclaration (Temp dest, ArrayType type){
        this.dest = dest;
        this.type = type;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dest);
        s.append(" := NEWARRAY ");
        s.append(this.type.type.toChar());
        s.append(" ");
        s.append(this.type.size);
        s.append(";");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}