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
L_0;
;			T2 := NEWARRAY I 3;
;			T3 := 0;
;			T4 := 1;
;			T2[T3] := T4;
;			T5 := 6;
;			T6 := 0;
;			T7 := T2[T6];
;			T8 := T5 I+ T7;
;			T0 := T8;
;			T9 := 0;
;			T1 := T9;
;			PRINTLNI T0;
;			T10 := T2[T1];
;			PRINTLNI T10;
;			RETURN;
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
	invokestatic a3_array/__main()V
	return
.end method

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method
