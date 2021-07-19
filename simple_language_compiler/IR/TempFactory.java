package IR;

import Type.*;

public class TempFactory {
    public TempEntry[] tempArray;
    public int pos;

    public TempFactory() {
        this.tempArray = new TempEntry[65536];
        this.pos = -1;
    }

    public Temp getTemp(Type type) {
        this.pos++;
        this.tempArray[pos] = new TempEntry(pos, type);
        return new Temp(type, pos);
    }

    public Temp getTemp(Type type, TempEntry.TempClass c, String name) {
        this.pos++;
        this.tempArray[pos] = new TempEntry(pos, type, c, name);
        return new Temp(type, pos);
    }

    public void returnTemp(Temp t) {
        return;
    }

    public boolean isParameterOrLocal(Temp t){
        return this.tempArray[t.number].isParameterOrLocal();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        TempEntry t;
        for (int i=0; i<tempArray.length; i++) {
            t = tempArray[i];
            if (t==null){
                break;
            }
            s.append("    TEMP ");
            s.append(t.number);
            s.append(":");
            s.append(t.type.toChar());
            s.append(";\n");
        }
        return s.toString();
    }

}
