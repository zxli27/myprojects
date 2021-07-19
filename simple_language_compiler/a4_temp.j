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
L_0;
;			T1 := TRUE;
	ldc 1
	istore 1
;			T0 := T1;
	iload 1
	istore 0
;			T2 := T0;
	iload 0
	istore 2
;			T2 := Z! T2;
	iload 2
	ldc 1
	ixor
	istore 2
;			IF T2 GOTO L0;
	iload 2
	ifne L0
;			T3 := "1";
	ldc "1"
	astore 3
;			PRINTU T3;
	getstatic java/lang/System/out Ljava/io/PrintStream;
	aload 3
	invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
;			GOTO L1;
	goto L1
;			L0:;
L0:
;			L1:;
L1:
;			RETURN;
	return
L_1;
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
