package IR;
import CodeGen.CodeGenVisitor;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;
import Type.Type;

public class IRFunction extends IRInstruction {
    public String name;
    public Type returnType;
    public List<Type> parameterTypes;
    public String signature;

    public TempFactory tFactory;
    public LabelFactory lFactory;
    public List<IRInstruction> instructions;

    public IRFunction(String name, Type returnType, List<Type> parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Type t : parameterTypes) {
            sb.append(t.toChar());
        }
        sb.append(")");
        sb.append(this.returnType.toChar());
        this.signature = sb.toString();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("FUNC ");
        s.append(name+" ");
        s.append(this.signature);
        s.append("\n{\n");
        s.append(tFactory);
        for (int i=0; i<this.instructions.size();i++){
            if (!(this.instructions.get(i) instanceof Label)){
                s.append("    ");
            }
            
            s.append(this.instructions.get(i));
            s.append("\n");
        }
        s.append("}");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }

}
