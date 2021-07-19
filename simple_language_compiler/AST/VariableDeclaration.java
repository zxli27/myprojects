package AST;
import Type.*;
import IR.*;

public class VariableDeclaration extends ASTNode {
    public TypeNode type;
    public Identifier id;

    public VariableDeclaration (TypeNode t, Identifier i) {
        this.type = t;
        this.id = i;
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