package AST;
import Type.*;
import IR.*;

public class AssignmentStatement extends Statement {
    public Expression expr;
    public Identifier id;

    public AssignmentStatement(Identifier i, Expression e){
        this.id = i;
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