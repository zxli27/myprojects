package AST;

public interface Visitor {
    public void visit(AddExpression e);
    public void visit(ArrayAssignmentStatement s);
    public void visit(ArrayReference r);
    public void visit(AssignmentStatement s);
    public void visit(Block b);
    public void visit(BooleanLiteral l);
    public void visit(CharacterLiteral l);
    public void visit(Declaration d);
    public void visit(EmptyStatement s);
    public void visit(EqualExpression e);
    public void visit(ExpressionList e);
    public void visit(ExpressionStatement s);
    public void visit(FloatLiteral l);
    public void visit(FormalParameters p);
    public void visit(Function f);
    public void visit(FunctionBody f);
    public void visit(FunctionCall f);
    public void visit(FunctionDecl f);
    public void visit(Identifier i);
    public void visit(IfStatement s);
    public void visit(IntegerLiteral l);
    public void visit(LessThanExpression e);
    public void visit(MultiplyExpression e);
    public void visit(ParenthesesExpression e);
    public void visit(PrintlnStatement s);
    public void visit(PrintStatement s);
    public void visit(Program p);
    public void visit(ReturnStatement s);
    public void visit(StringLiteral l);
    public void visit(SubstractExpression e);
    public void visit(TypeNode t);
    public void visit(VariableDeclaration d);
    public void visit(WhileStatement s);
}
