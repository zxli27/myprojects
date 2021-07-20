package AST;
import Type.*;
import IR.*;

import java.util.ArrayList;

public class FunctionCall extends Expression {
    public Identifier id;
    public ArrayList<Expression> expressionList;

    public FunctionCall (Identifier i) {
        this.id = i;
        this.expressionList = new ArrayList<Expression>();
    }

    public void add(Expression e) {
        expressionList.add(e);
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