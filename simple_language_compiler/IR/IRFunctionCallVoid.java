package IR;
import CodeGen.CodeGenVisitor;

import java.util.ArrayList;

public class IRFunctionCallVoid extends IRInstruction {
    public String functionName;
    public ArrayList<Temp> expressionList;

    public IRFunctionCallVoid(String fName, ArrayList<Temp> exprs){
        this.functionName = fName;
        this.expressionList = exprs;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("CALL ");
        s.append(functionName);
        s.append("(");
        for (int i=0; i<this.expressionList.size();i++){
            s.append(this.expressionList.get(i));
        }
        s.append(");");
        return s.toString();
    }

    public void accept(CodeGenVisitor v){
        v.visit(this);
    }
}