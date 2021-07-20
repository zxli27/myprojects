package IR;
import CodeGen.CodeGenVisitor;

import Type.*;

public class Temp {
    public Type type;
    public int number;

    public Temp(Type type, int number) {
        this.type = type;
        this.number = number;
    }

    public String toString() {
        return "T"+String.valueOf(this.number);
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}
