package AST;
import Type.*;
import IR.*;

import java.util.ArrayList;

public class FormalParameters extends ASTNode {
    public ArrayList<Declaration> parameterArray;
    public int size;

    public FormalParameters() {
        this.parameterArray = new ArrayList<Declaration>();
        size = 0;
    }

    public void add(Declaration d) {
        this.parameterArray.add(d);
        size++;
    }

    public Declaration get(int index) {
        return this.parameterArray.get(index);
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