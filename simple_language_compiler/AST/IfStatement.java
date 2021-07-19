package AST;
import Type.*;
import IR.*;

public class IfStatement extends Statement {
    public Expression expr;
    public Block ifBlock;
    public Block elseBlock;
    
    public IfStatement(Expression e, Block b) {
        this.expr = e;
        this.ifBlock = b;
        this.elseBlock = null;
    }

    public IfStatement(Expression e, Block b1, Block b2) {
        this.expr = e;
        this.ifBlock = b1;
        this.elseBlock = b2;
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
