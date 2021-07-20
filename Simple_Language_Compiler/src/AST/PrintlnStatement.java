package AST;
import Type.*;
import IR.*;

public class PrintlnStatement extends Statement {
    public Expression expr;

    public PrintlnStatement(Expression e){
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