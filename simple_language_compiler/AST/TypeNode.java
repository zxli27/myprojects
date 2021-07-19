package AST;
import Type.*;
import IR.*;

import Type.Type;

public class TypeNode extends ASTNode {
    public Type type;

    public TypeNode(Type type) {
        this.type = type;
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
