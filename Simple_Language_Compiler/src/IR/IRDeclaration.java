package IR;

import Type.*;

public class IRDeclaration extends IRInstruction {
    public Temp dest;
    public Type type;

    public IRDeclaration (Temp dest, Type type){
        this.dest = dest;
        this.type = type;
    }


}