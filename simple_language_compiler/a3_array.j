.source a3_array.ir
.class public a3_array
.super java/lang/Object

.method public static __main()V
	.limit locals 11
	.var 0 is T0  I from L_0 to L_1
	.var 1 is T1  I from L_0 to L_1
	.var 2 is T2  [I from L_0 to L_1
	.var 3 is T3  I from L_0 to L_1
	.var 4 is T4  I from L_0 to L_1
	.var 5 is T5  I from L_0 to L_1
	.var 6 is T6  I from L_0 to L_1
	.var 7 is T7  I from L_0 to L_1
	.var 8 is T8  I from L_0 to L_1
	.var 9 is T9  I from L_0 to L_1
	.var 10 is T10  I from L_0 to L_1
	.limit stack 16
L_0:
	ldc 0
	istore 0
	ldc 0
	istore 1
	aconst_null 
	astore 2
	ldc 0
	istore 3
	ldc 0
	istore 4
	ldc 0
	istore 5
	ldc 0
	istore 6
	ldc 0
	istore 7
	ldc 0
	istore 8
	ldc 0
	istore 9
	ldc 0
	istore 10
.line 15
;		    T2 := NEWARRAY I 3;
	ldc 3
	newarray int
	astore 2
.line 16
;		    T3 := 0;
	ldc 0
	istore 3
.line 17
;		    T4 := 1;
	ldc 1
	istore 4
.line 18
;		    T2[T3] := T4;
	aload 2
	iload 3
	iload 4
	iastore
.line 19
;		    T5 := 6;
	ldc 6
	istore 5
.line 20
;		    T6 := 0;
	ldc 0
	istore 6
.line 21
;		    T7 := T2[T6];
	aload 2
	iload 6
	iaload
	istore 7
.line 22
;		    T8 := T5 I+ T7;
	iload 5
	iload 7
	iadd
	istore 8
.line 23
;		    T0 := T8;
	iload 8
	istore 0
.line 24
;		    T9 := 0;
	ldc 0
	istore 9
.line 25
;		    T1 := T9;
	iload 9
	istore 1
.line 26
;		    PRINTLNI T0;
	getstatic java/lang/System/out Ljava/io/PrintStream;
	iload 0
	invokevirtual java/io/PrintStream/println(I)V
.line 27
;		    T10 := T2[T1];
	aload 2
	iload 1
	iaload
	istore 10
.line 28
;		    PRINTLNI T10;
	getstatic java/lang/System/out Ljava/io/PrintStream;
	iload 10
	invokevirtual java/io/PrintStream/println(I)V
.line 29
;		    RETURN;
	return
L_1:
.end method

;--------------------------------------------;
;                                            ;
; Boilerplate                                ;
;                                            ;
;--------------------------------------------;

.method public static main([Ljava/lang/String;)V
	; set limits used by this method
	.limit locals 1
	.limit stack 4
	invokestatic a3_array/__main()V
	return
.end method

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method
