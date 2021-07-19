package AST;
import Type.*;
import IR.*;

import java.util.ArrayList;

public class FunctionDecl extends ASTNode {
    public Declaration declaration;
    public FormalParameters formalParameters;

    public FunctionDecl (Declaration d, FormalParameters f) {
        this.declaration = d;
        this.formalParameters = f;
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