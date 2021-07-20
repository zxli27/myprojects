package AST;
import Type.*;
import IR.*;

public class Identifier extends Expression {
    public String id;

    public Identifier (String s) {
        this.id = s;
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