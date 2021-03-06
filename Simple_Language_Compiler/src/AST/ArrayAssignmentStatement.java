package AST;
import Type.*;
import IR.*;

public class ArrayAssignmentStatement extends Statement {
    public Expression expr;
    public ArrayReference arrayReference;

    public ArrayAssignmentStatement(ArrayReference a, Expression e){
        this.arrayReference = a;
        this.expr = e;
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