# SEEsh

SEEsh is a self-created shell program using C.

## Program running
1. gcc -o \<executable name\> SEEsh.c
2. ./\<executable\>
3. Users can also create a .SEEsh file to store commands so these commands will be executed when starting shell

## Built-in commands:
* cd [dir]: change the working directory to dir or to home if dir is omitted;
* pwd: print the full filename of the current working directory
* help: display information about built-in commands
* exit: SEEsh should exist
* set var [value]: If environment variable var does not exist, then SEEsh should create it. SEEsh should set the value to var, or an empty string if value is omitted. If both var and value are omitted, set should display all environment variables and values
* unset var: SEEsh should destroy the environment variable var";