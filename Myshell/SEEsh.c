#define _GNU_SOURCE
#include <sys/wait.h>  //for wait
#include <stdio.h>  //for io
#include <unistd.h>  //for sys calls
#include <stdlib.h> //for malloc
#include <string.h>  //for string operations
#include<signal.h>
#include<unistd.h>

extern char **environ;
char *builtin_info="\
cd [dir]: change the working directory to dir or to home if dir is omitted;\n\
pwd: print the full filename of the current working directory\n\
help: display information about built-in commands\n\
exit: SEEsh should exist\n\
set var [value]: If environment variable var does not exist, then SEEsh should create it. SEEsh should set the value of var to value, or to the empty string if value is omitted. If both var and value are omitted, set should display all environment variables and values\n\
unset var: SEEsh should destroy the environment variable var";

/*!
 * \brief print all items in the history list
 * \param historyList The pointer to the history list
 * \param tokens a pointer to a list containing tokens of a command
 * \return -2 if the command syntax is wrong, 0 if succeeds
 */
int history(char **tokens,char **historyList){
  if(tokens[1]!=NULL){
    return -2;
  }
  else{
    int i=0;
    while(historyList[i+1]!=NULL){
      printf("%s\n",historyList[i++]);
    }
    return 0;
  }
}
/*!
 * \brief find the latest command in history list that matches the prefix and assign it to command variable, or keep the command the same
 * \param command the pointer to a command
 * \param historyList the pointer to a history list
 * \param phc the present history item number
 * \return void
 */
void prefix(char *command, char **historyList, int phc){
  int length=strlen(command)-1;
  char *pre=malloc(sizeof(char)*(length+1));
  if(!pre){
    printf("SEEsh: allocation error.\n");
    return;
  }
  int i;
  for(i=1;i<=length;i++){
    pre[i-1]=command[i];
  }
  pre[i-1]='\0';
  for(i=phc-1;i>=0;i--){
    if(strncmp(historyList[i],pre,length)==0){
      strcpy(command,historyList[i]);
    }
  }
}
/*!
 * \brief change directory
 * \param tokens the pointer to a list containing tokens of a command 
 * \return -2 if the command syntax is wrong, -1 if the operation fails, 0 if succeeds
 */
int cd(char **tokens){
  if(tokens[1]==NULL){
    if(chdir(getenv("HOME"))==-1){
      return -1;
    }
  }
  else if(tokens[2]==NULL){
    if(chdir(tokens[1])==-1){
      return -1;
    }
  }
  else{
    return -2;
  }
  return 0;
}
/*!
 * \brief find the present working directory
 * \param tokens the pointer to a list containing tokens of a command
 * \return -2 if the command syntax is wrong, -1 if the operation fails, 0 if succeeds
 */
int pwd(char **tokens){
  char pwd[500];
  if(tokens[1]==NULL&&getcwd(pwd, 500)!=NULL){
    printf("the present working directory is %s\n",pwd);
    return 0;
  }
  else if(tokens[1]!=NULL){
    return -2;
  }
  else{
    return -1;
  }
}

/*!
 * \brief print the information about this shell's built-in commands
 * \param tokens the pointer to a list containing tokens of a command
 * \return -2 if the command syntax is wrong, 0 if succeeds
 */
int help(char **tokens){ 
  if(tokens[1]!=NULL){
    return -2;
  }
  printf("The following are built-in commands information of SEEsh:\n");
  printf("%s\n",builtin_info);
  return 0;
}
/*!
 * \brief set environment variables
 * \param tokens the pointer to a list containing tokens of a command
 * \return -2 if command syntax is wrong, -1 if the set fails, 0 if succeeds
 */
int set(char **tokens){
  if(tokens[1]==NULL){
    int i=0;
    while(environ[i]){
      printf("%s\n",environ[i]);
      i++;
    }
    return 0;
  }
  char *value;
  if(tokens[2]==NULL){
    value="";
  }
  else if(tokens[3]==NULL){
    value=tokens[2];
  }
  else{
    return -2;
  }
  if(setenv(tokens[1],value,1)==-1){
    return -1;
  }
  return 0;
}
/*!
 * \brief destroy an environment variable
 * \param tokens a pointer to a list containing tokens of a command
 * \return -2 if the command syntax is wrong, -1 if the operation fails, 0 if succeeds, 1 if the variable doesn't exist
 */
int unset(char **tokens){
  if(tokens[1]==NULL || tokens[2]!=NULL){
    return -2;
  }
  else{
    if(getenv(tokens[1])==NULL){
      return 1;
    }
    else{
      if(unsetenv(tokens[1])!=0){
        return -1;
      }
    }
  }
  return 0;
}
/*!
 * \brief split the PATH environment variable into a path list
 * \param PATHlist the list used to hold the paths in PATH environment variable
 * \return void
 */
void findpath(char **PATHlist){
  char *PATH=getenv("PATH");
  strtok(PATH,"\n");
  char *item=strtok(PATH,":");
  int i=0;
  if(item){
    PATHlist[i++]=item;
    item=strtok(PATH," ");
  }
  PATHlist[i]=NULL;
}

/*!
 * \brief execute a new process
 * \param tokens a pointer to a list containing tokens of a command
 * \return -3 if the execution fails, -4 if fork fails, 0 if succeeds
 */
int execute_process(char **tokens){
  pid_t pid;
  pid=fork();
  char *PATHlist[20];
  findpath(PATHlist);
  int i=0;
  if(pid==0){
    while(PATHlist[i]!=NULL){
      char str[100];
      strcpy(str,PATHlist[i++]);
      strcat(str,tokens[0]);
      if(execvp(str,tokens)!=-1){
        return 0;
      }
    }
    return -3;
  }
  else if(pid<0){
    return -4;
  }
  else{
    wait(NULL);
  }
  return 0;
}

/*!
 * \brief invoke different command methods
 * \param tokens a pointer to a list containing tokens of a command
 * \return -1 if the process is the child process in which execvp() fails, 0 if the process is the parent process 
 */
int builtin(char **tokens, char **historyList) {
  int re;
  if(strcmp(tokens[0],"cd")==0){
    re=cd(tokens);
  }
  else if(strcmp(tokens[0],"pwd")==0){
    re=pwd(tokens);
  }
  else if(strcmp(tokens[0],"help")==0){
    re=help(tokens);
  }
  else if(strcmp(tokens[0],"set")==0){
    re=set(tokens);
  }
  else if(strcmp(tokens[0],"unset")==0){
    re=unset(tokens);
  }
  else if(strcmp(tokens[0],"history")==0){
    re=history(tokens,historyList);
    }
  else{
    re=execute_process(tokens); 
  }
  if(re==-2){
    printf("the syntax of the command is not correct.\n");
  }
  else if(re==-1){
    printf("the built-in command operation fails.\n");
  }
  else if(re==-3){
    printf("the execution of the program fails or it is not a built-in command.\n");
    return -1;
  }
  else if(re==-4){
    printf("the fork() system call fails.\n");
  }
  else if(re==1){
    printf("the environment variable doesn't exist.\n");
  }
  return 0;
}
/*!
 * \brief read a command from keyboard and put it into command pointer
 * \param command a pointer used to point to a command
 * \return -2 if input is CTRL+D, -1 if read fails, 0 if succeeds
 */
int readCommand(char *command){
    size_t commandSize=512;
    int status=getline(&command,&commandSize,stdin);  //store the inputed command line into command
    if(status==-1){
      free(command);
      return -2;
    }
    strtok(command,"\n"); 
    if(commandSize>512){
      printf("SEEsh: the command line is too long\n");
      return -1;
    }
    return 0;   
}
/*!
 * \brief itokenize a command
 * \param command a pointer to a command
 * \return NULL if tokenization fails, a dynamic token list if succeeds
 */
char **tokenize(char *command){
    char **tokens;
    int tokensSize=64;
    tokens=malloc(sizeof(char*)*tokensSize);
    int i=0;
    if(!tokens){           //assure the allocation is correct
      printf("SEEsh: allocation error\n");
      free(tokens);
      return NULL;
    }
    char *token=strtok(command," ");
    while(token){
      tokens[i++]=token;
      if(i>=tokensSize){
        tokensSize*=2;
        tokens=realloc(tokens,sizeof(char*)*tokensSize);
        if(!tokens){
          printf("SEEsh: allocation error\n");
          free(tokens);
          return NULL;
        }
      }
      token=strtok(NULL, " "); 
    }
    tokens[i]=NULL;
    return tokens;
}
/*!
 * \brief record the command into a history list
 * \param historyList a pointer to a history list
 * \param phc a pointer to the number of the present history items in the history list
 * \param command a pointer to a command
 * \param historyCount a int pointer to the length of the history list
 * \return -1 if record fails, 0 if succeeds
 */
int commandRecord(char **historyList, int *phc,char *command,int *historyCount){
    historyList[*phc]=malloc(sizeof(char)*512);
    if(!historyList[*phc]){
        printf("SEEsh: allocation error\n");
        return -1;
    }

    strcpy(historyList[*phc],command);
    *phc=*phc+1;
    if(*phc>=*historyCount){
        *historyCount=(*historyCount)*2;
        historyList=realloc(historyList,sizeof(char*)*(*historyCount));
        if(!historyList){
          printf("SEEsh: allocation error\n");
          return -1;
        }
    }
    historyList[*phc]=NULL;
    return 0;
}

/*!
 * \brief open file .SEEshrc and executes the commands in it
 * \param 
 * \return -1 if reading file fails; 0 if succeed
 */
int SEEshrc(char **historyList, int *phc, int *historyCount){
  char *r=malloc(sizeof(char)*512);
  if(!r){
    return -1;
  }
  char SEEshrcLocation[110];
  strcpy(SEEshrcLocation,getenv("HOME")); 
  strcat(SEEshrcLocation,"/.SEEshrc");
  FILE *f=fopen(SEEshrcLocation,"r");
  if(!f){
    return -1;
  }
  char **tokens;
  while(fgets(r, 512, f)){
    strtok(r,"\n"); 
    printf("? %s\n",r);
    if(strncmp(r,"!",1)==0){
      prefix(r,historyList,*phc);
    }
    //record the command into history list
    if(commandRecord(historyList, phc, r, historyCount)==-1){
      printf("recording of the command fails.\n");
      continue;
    }
    tokens=tokenize(r);
    if(tokens==NULL){
      return -1;
    }
    builtin(tokens,historyList);
    free(tokens);
  }
  free(r);
  fclose(f);
  return 0;
}
/*!
 * \brief ignore control+c and start a new line
 * \param sig an integer from signal() method
 * \return void
 */
void nothing(int sig){
  printf("\n");
}

int main(int argc,  char **argv){

  signal(SIGINT, nothing);

  char *command=NULL;   
  char **tokens=NULL;   
  int historyCount=32;
  int phc=0;
  char **historyList=malloc(sizeof(char *)*historyCount);
  if(historyList==NULL){
    printf("SEEsh: history list allocation error'\n");
    exit(0);
  }
  //run commands in .SEEshrc file
  if(SEEshrc(historyList,&phc,&historyCount)==-1){
    printf("Reading file .SEEshrc fails.\n");
  } 
  while(1){
    printf("? ");  
    //read a command from keyboard
    command=malloc(sizeof(char)*512);
    if(!command){           //assure the allocation is correct
      printf("SEEsh: allocation error\n");
      free(command);
      continue;
    }
    int status=readCommand(command);
    if(status==-1){
      free(command);
      continue;
    }
    else if(status==-2){
      printf("\n");
      free(command);
      for(int i=0;i<phc;i++){
          free(historyList[i]);
        }
      free(historyList);
      return 0;
    }
    //if the command starts with !, match it with history commands
    if(strncmp(command,"!",1)==0){
      prefix(command,historyList,phc);
    }
    //record the command into history list
    if(commandRecord(historyList, &phc, command, &historyCount)==-1){
      printf("recording of the command fails.\n");
      continue;
    }
    //tokenize the command
    tokens=tokenize(command);
    if(tokens==NULL){
      free(command);
      continue;
    }
    //execute the command
    if(strncmp(tokens[0],"exit",30)==0){
      if(tokens[1]!=NULL){
        printf("The syntax of the command is not correct.\n");
      }
      else{
        free(command);
        free(tokens);
        for(int i=0;i<phc;i++){
          free(historyList[i]);
        }
        free(historyList);
        return 0;
      }
    }
    else{
      if(builtin(tokens,historyList)==-1){
        free(command);
        free(tokens);
        for(int i=0;i<phc;i++){
          free(historyList[i]);
        }
        free(historyList);
        return 0;
      }
    }
    
    //free
    free(command);
    free(tokens);
  }
  return 0;

}

