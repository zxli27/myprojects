package AST;
import Type.*;
import IR.*;

public class SubstractExpression extends Expression {
    
    public Expression leftExpr;
    public Expression rightExpr;

    public SubstractExpression(Expression e1, Expression e2) {
        this.leftExpr = e1;
        this.rightExpr = e2;
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