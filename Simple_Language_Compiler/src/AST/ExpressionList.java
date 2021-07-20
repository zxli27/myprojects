package AST;
import Type.*;
import IR.*;

import java.util.ArrayList;

public class ExpressionList extends ASTNode {
    public ArrayList<Expression> el;
    public int size;

    public ExpressionList() {
        this.el = new ArrayList<Expression>();
        size = 0;
    }

    public ArrayList<Expression> getExpressionList() {
        return this.el;
    }

    public void add(Expression e) {
        this.el.add(e);
        size++;
    }

    public Expression get(int index) {
        return this.el.get(index);
    }


    public int size() {
        return this.size;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}