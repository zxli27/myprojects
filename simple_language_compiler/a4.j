.source a4.ir
.class public a4
.super java/lang/Object

.method public static __main()V
	.limit locals 4
	.var 0 is T0  Z from L_0 to L_1
	.var 1 is T1  Z from L_0 to L_1
	.var 2 is T2  Z from L_0 to L_1
	.var 3 is T3  Ljava/lang/String; from L_0 to L_1
	.limit stack 16
L_0:
	ldc 0
	istore 0
	ldc 0
	istore 1
	ldc 0
	istore 2
	aconst_null 
	astore 3
.line 8
;		    T1 := TRUE;
	ldc 1
	istore 1
.line 9
;		    T0 := T1;
	iload 1
	istore 0
.line 10
;		    T2 := T0;
	iload 0
	istore 2
.line 11
;		    T2 := Z! T2;
	iload 2
	ldc 1
	ixor
	istore 2
.line 12
;		    IF T2 GOTO L0;
	iload 2
	ifne L0
.line 13
;		    T3 := "1";
	ldc "1"
	astore 3
.line 14
;		    PRINTU T3;
	getstatic java/lang/System/out Ljava/io/PrintStream;
	aload 3
	invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
.line 15
;		    GOTO L1;
	goto L1
.line 16
;		L0:;
L0:
.line 17
;		L1:;
L1:
.line 18
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
	invokestatic a4/__main()V
	return
.end method

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method
