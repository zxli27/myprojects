package AST;
import Type.*;
import IR.*;

import java.util.ArrayList;

public class Program extends ASTNode {
    public ArrayList<Function> funcList;
    public int size;

    public Program (){
        funcList = new ArrayList<Function>();
        size = 0;
    }

    public void add(Function f) {
        this.funcList.add(f);
        size++;
    }

    public Function get(int index) {
        return this.funcList.get(index);
    }

    public int size() {
        return this.size;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    public Temp accept(TempVisitor v) {
        return v.visit(this);
    }
}