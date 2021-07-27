#!/bin/sh

make
for name in  "while" "if" "array" "type" "para" "func_return" "multi_loop" "recursion" "factorial" "expr"
do 
    echo ">>>>>>>>>>>>>>a3_$name test<<<<<<<<<<<<<<<"
    java Compiler tests/a3_$name.ul
    # the location of codegen should be different!
    java jasmin.Main a3_$name.j
    java a3_$name
done

make clean
rm ./*.j