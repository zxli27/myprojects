package AST;
import Type.*;
import IR.*;

public class WhileStatement extends Statement {
    public Expression expr;
    public Block block;

    public WhileStatement(Expression e, Block b){
        this.expr = e;
        this.block = b;
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