package AST;
import Type.*;
import IR.*;

public class CharacterLiteral extends Literal {
    public char value;

    public CharacterLiteral(char c) {
        this.value = c;
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
