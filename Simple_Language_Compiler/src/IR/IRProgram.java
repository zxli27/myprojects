package IR;
import CodeGen.CodeGenVisitor;

import java.lang.StringBuilder;
import java.util.ArrayList;

public class IRProgram {
    public String name;
    public ArrayList<IRFunction> functions;

    public IRProgram(String name) {
        this.name = name;
        this.functions = new ArrayList<IRFunction>();
    }

    public void addFunction(IRFunction function) {
        this.functions.add(function);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("PROG ");
        s.append(this.name+"\n");
        for (int i=0; i<this.functions.size(); i++){
            s.append(this.functions.get(i));
            s.append("\n");
        }
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}
