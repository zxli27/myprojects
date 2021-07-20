grammar ul;

options {
    backtrack=true;
}

@header
{
    import AST.*;
    import Type.*;
}                                               

@members
{
protected void mismatch (IntStream input, int ttype, BitSet follow)
        throws RecognitionException
{
        throw new MismatchedTokenException(ttype, input);
}
public Object recoverFromMismatchedSet (IntStream input,
                                      RecognitionException e,
                                      BitSet follow)
        throws RecognitionException
{
        reportError(e);
        throw e;
}
}

@rulecatch {
        catch (RecognitionException ex) {
                reportError(ex);
                throw ex;
        }
}

/*
 * This is a subset of the ulGrammar to show you how
 * to make new production rules.
 * You will need to:
 *  - change type to be compoundType and include appropriate productions
 *  - introduce optional formalParameters
 *  - change functionBody to include variable declarations and statements 
 */

program returns [Program p]
@init 
{
        p = new Program();
}
@after{
        p.line = 1;
        p.offset = 0;
}
: 
        (f = function {p.add(f);} )+ EOF
	;

function returns [Function f]
: 
        fd = functionDecl fb = functionBody
        {
                f = new Function(fd, fb);
                f.line=fd.line;
                f.offset=fd.offset;
        }
	;

functionDecl returns [FunctionDecl fd]
: 
        ct = compoundType id = identifier OPARENTHESES fp = formalParameters CPARENTHESES
        {
                Declaration d = new Declaration(ct, id);
                d.line=ct.line;
                d.offset=ct.offset;
                fd = new FunctionDecl(d, fp);
                fd.line = ct.line;
                fd.offset = ct.offset;
        }
	;

//not sure if this works
formalParameters returns [FormalParameters fp] 
@init
{
        fp = new FormalParameters();
}
: 
        (ct1 = compoundType id1 = identifier 
        {
                fp.add(new Declaration(ct1, id1));
                fp.line = ct1.line;
                fp.offset = ct1.offset;
        } ) 
        (COMMA ct2 = compoundType id2 = identifier {fp.add(new Declaration(ct2, id2));} )*
        |
        ;


functionBody returns [FunctionBody fb]
@init
{
        fb = new FunctionBody();
}
: 
        OBRACE (vd = varDecl {fb.addVarDecl(vd);} )* (s = statement {fb.addStatement(s);} )* CBRACE
        {
                fb.line = $OBRACE.line;
                fb.offset = $OBRACE.pos;
        }
	;

varDecl returns [VariableDeclaration vd]: 
        ct = compoundType id = identifier SEMICOLON
        {
                vd = new VariableDeclaration(ct, id);
                vd.line = ct.line;
                vd.offset = ct.offset;
        }
        ;

statement returns [Statement s]:
        SEMICOLON {s = new EmptyStatement(); s.line = $SEMICOLON.line; s.offset = $SEMICOLON.pos;}
         | es = exprStatement {s = es;}
         | is = ifStatement {s = is;}
         | ws = whileStatement {s = ws;}
         | ps = printStatement {s = ps;}
         | pls = printlnStatement {s = pls;}
         | rs = returnStatement {s = rs;}
         | as = assignStatement {s = as;} 
         | ars = arrayAssignStatement {s = ars;}
        ;

exprStatement returns [ExpressionStatement es]
: 
        e = expr SEMICOLON
        {
                es = new ExpressionStatement(e);
                es.line = e.line;
                es.offset = e.offset;
        }
        ;

ifStatement returns [IfStatement is]
@after{
        is.line = e.line;
        is.offset = e.offset;
}:
        IF OPARENTHESES e = expr CPARENTHESES b1 = block ELSE b2 = block {is = new IfStatement(e, b1, b2);}
        | IF OPARENTHESES e = expr CPARENTHESES b = block {is = new IfStatement(e, b);}
        ;

whileStatement returns [WhileStatement ws]: 
        WHILE OPARENTHESES e = expr CPARENTHESES b = block 
        {
                ws = new WhileStatement(e, b);
                ws.line = $WHILE.line;
                ws.offset = $WHILE.pos;        
        }
        ;

printStatement returns [PrintStatement ps]: 
        PRINT e = expr SEMICOLON 
        {
                ps = new PrintStatement(e);
                ps.line = $PRINT.line;
                ps.offset = $PRINT.pos;        
        }
        ;
        
printlnStatement returns [PrintlnStatement pls]: 
        PRINTLN e = expr SEMICOLON 
        {
                pls = new PrintlnStatement(e);
                pls.line = $PRINTLN.line;
                pls.offset = $PRINTLN.pos;
        }
        ;

returnStatement returns [ReturnStatement rs]
@init
{
        rs = new ReturnStatement();
}
: 
        RETURN (e = expr {rs.addExpression(e);} )? SEMICOLON
        {
                rs.line = $RETURN.line;
                rs.offset = $RETURN.pos;
        }
        ;

assignStatement returns [AssignmentStatement as]: 
        id = identifier EQUAL e = expr SEMICOLON
        {
                as = new AssignmentStatement(id, e);
                as.line = id.line;
                as.offset = id.offset;
        }
        ;     

arrayAssignStatement returns [ArrayAssignmentStatement ars]:
        ar = arrayRef EQUAL e = expr SEMICOLON
        {
                ars = new ArrayAssignmentStatement(ar, e);
                ars.line = ar.line;
                ars.offset = ar.offset;
        }
        ;

block returns [Block b]
@init
{
        b = new Block();
}
: 
        OBRACE (s = statement {b.add(s);})* CBRACE
        {
                b.line = $OBRACE.line;
                b.offset = $OBRACE.pos;
        }
        ;

expr returns [Expression e]
@init{
        Expression it = null;
}
@after{
        e = it;
}
: 
        e1 = lessThanExpr {it = e1;}
        ( DOUBLEEQUAL e2 = lessThanExpr 
        {
                it = new EqualExpression(it, e2);
                it.line = $DOUBLEEQUAL.line;
                it.offset = $DOUBLEEQUAL.pos;
        } )*  
        ;

lessThanExpr returns [Expression e]
@init{
        Expression it = null;
}
@after{
        e = it;
}: 
        e1 = plusMinusExpr {it = e1;}
        ( LESSTHAN e2 = plusMinusExpr 
        {
                it = new LessThanExpression(it, e2);
                it.line = $LESSTHAN.line;
                it.offset = $LESSTHAN.pos;
        } )* 
        ;

plusMinusExpr returns [Expression e]
@init{
        Expression it = null;
}
@after{
        e = it;
}: 
        e1 = mulExpr {it = e1;}
        ( op = (PLUS|MINUS) e2 = mulExpr 
        {
                if (op.getText().equals("+")) {
                        it = new AddExpression(it, e2);
                } else {
                        it = new SubstractExpression(it, e2);
                }
                it.line = $op.line;
                it.offset = $op.pos;
        })* 
        ;

mulExpr returns [Expression e]
@init{
        Expression it = null;
}
@after{
        e = it;
}: 
        e1 = atom {it = e1;}
        ( MULTIPLY e2 = atom 
        {
                it = new MultiplyExpression(it, e2);
                it.line = $MULTIPLY.line;
                it.offset = $MULTIPLY.pos;
        })*      
        ;

atom returns [Expression e]:    
        i = identifier {e = i;}
        | l = literal {e = l;}
        | f = functionCall {e = f;}
        | a = arrayRef {e = a;}
        | OPARENTHESES exp = expr CPARENTHESES {e = new ParenthesesExpression(exp); e.line = $OPARENTHESES.line;e.offset = $OPARENTHESES.pos;}
    ;

functionCall returns [FunctionCall fc]:   
        id = identifier {fc = new FunctionCall(id); fc.line = id.line; fc.offset = id.offset;}
        OPARENTHESES (e1 = expr {fc.add(e1);}  (COMMA e2 = expr {fc.add(e2);} )* )? CPARENTHESES
        ;

arrayRef returns [ArrayReference ar]:    
        id = identifier OBRACKET e = expr CBRACKET
        {
                ar = new ArrayReference(id, e);
                ar.line = id.line;
                ar.offset = id.offset;
        }
        ;


literal returns [Literal l]
@after{
        l.line = $v.line;
        l.offset = $v.pos;
}: 
        v = INTEGERCONSTANT {l = new IntegerLiteral(Integer.parseInt($v.text));}
        | v = STRINGCONSTANT {String txt = $v.text; l = new StringLiteral(txt.substring(1, txt.length()-1));}
        | v = FLOATCONSTANT {l = new FloatLiteral(Float.parseFloat($v.text));}
        | v = CHARACTERCONSTANT {l = new CharacterLiteral($v.text.charAt(1));}
        | v = TRUE {l = new BooleanLiteral(true);}
        | v = FALSE {l = new BooleanLiteral(false);}
        ;

identifier returns [Identifier id]: 
        i = ID 
        {
                id = new Identifier($i.text);
                id.line = $i.line;
                id.offset = $i.pos;
        }
	;

compoundType returns [TypeNode tn]:	
        t = type {tn = t;}
        |  t2 = type OBRACKET i = INTEGERCONSTANT CBRACKET {
                        Type te = new ArrayType(t2.type, Integer.parseInt($i.text));
                        tn = new TypeNode(te);
                        tn.line = t2.line;
                        tn.offset = t2.offset;
                }
	;

type returns [TypeNode te]
:	
        typ = TYPE
        {
                String ty = $typ.text;
                Type t;
                if(ty.equals("int")) {
                        t = new IntegerType();
                } else if(ty.equals("float")) {
                        t = new FloatType();
                } else if(ty.equals("string")) {
                        t = new StringType();
                } else if(ty.equals("char")) {
                        t = new CharType();
                } else if(ty.equals("boolean")) {
                        t = new BooleanType();
                } else if(ty.equals("void")) {
                        t = new VoidType();
                } else {
                        t = null;   
                }
                te = new TypeNode(t);
                te.line = $typ.line;
                te.offset = $typ.pos;
        }
	;

/* Lexer */
	 
IF	: 'if'
	;

ELSE    : 'else'
        ;

TRUE    : 'true'
        ;

FALSE   : 'false'
        ;

WHILE   : 'while'
        ;

PRINT   : 'print'
        ;

PRINTLN : 'println'
        ;

RETURN  : 'return'
        ;

OPARENTHESES : '('
        ;

CPARENTHESES : ')'
        ;

OBRACKET : '['
        ;

CBRACKET : ']'
        ;

OBRACE : '{'
        ;

CBRACE : '}'
        ;

SEMICOLON : ';'
        ;    

COMMA    : ','
        ;       

EQUAL   : '='
        ;

DOUBLEEQUAL : '=='
        ;

LESSTHAN : '<'
        ;

PLUS    :'+'
        ;

MINUS   : '-'
        ;

MULTIPLY : '*'
        ;

/* Fixme: add the other types here */
TYPE	: 'int'
          | 'float'
          | 'char'
          | 'string'
          | 'boolean'
          | 'void'
	; 
/*
 * FIXME:
 * Change this to match the specification for identifier
 * 
 */

INTEGERCONSTANT : (('1'..'9')('0'..'9')* | '0')
        ;

STRINGCONSTANT: '\u0022' ('a'..'z' | 'A'..'Z' | ' '|'0'..'9' | '!'|','|'.'|':'|'_'|'{'|'}')+ '\u0022'
        ;

FLOATCONSTANT: (('1'..'9')('0'..'9')* | '0') '.' ('0'..'9')+
        ;

CHARACTERCONSTANT: '\u0027' ('a'..'z' | 'A'..'Z'|' '|'0'..'9' | '!'|','|'.'|':'|'_'|'{'|'}') '\u0027'
        ;

ID	: ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* 
	;

/* These two lines match whitespace and comments 
 * and ignore them.
 * You want to leave these as lAST in the file.  
 * Add new lexical rules above 
 */
WS      : ( '\t' | ' ' | ('\r' | '\n') )+ { $channel = HIDDEN;}
        ;

COMMENT : '//' ~('\r' | '\n')* ('\r' | '\n') { $channel = HIDDEN;}
        ;