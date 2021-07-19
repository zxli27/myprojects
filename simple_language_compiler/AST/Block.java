package AST;
import Type.*;
import IR.*;

import java.util.ArrayList;

public class Block extends ASTNode {
    public ArrayList<Statement> statementArray;

    public Block() {
        this.statementArray = new ArrayList<Statement>();
    }

    public void add(Statement s) {
        this.statementArray.add(s);
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