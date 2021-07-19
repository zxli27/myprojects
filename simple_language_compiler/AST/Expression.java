package AST;
import Type.*;
import IR.*;

public abstract class Expression extends ASTNode {
    public abstract void accept (Visitor v);
    public abstract Type accept (TypeVisitor v);
    public abstract Temp accept (TempVisitor v);
    // public abstract Value accept (InterpreterVisitor v);

}