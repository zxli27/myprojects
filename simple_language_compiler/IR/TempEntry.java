package IR;
import CodeGen.CodeGenVisitor;

import Type.*;

public class TempEntry {
    public enum TempClass {
        PARAMETER,
        LOCAL,
        TEMP
    };
    
    public int number;
    public boolean inUse;
    public TempClass tClass;
    public Type type;
    public String name;

    public TempEntry (int number, Type type) {
        this.type = type;
        this.number = number;
        this.tClass = TempClass.TEMP;
        this.name = "";
        this.inUse = true;
    }

    public TempEntry (int number, Type type, TempClass c, String name) {
        this.number = number;
        this.type = type;
        this.tClass = c;
        this.name = name;
        this.inUse = true;
    }

    public boolean isParameterOrLocal() {
        return this.tClass == TempClass.PARAMETER || this.tClass == TempClass.LOCAL;
    }
}
