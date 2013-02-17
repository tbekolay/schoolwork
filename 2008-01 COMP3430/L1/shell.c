#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#define BUFSIZE 81

int main(int argc, char *argv[]){

  int ret_code;		// return code
  int len;		// length of entered command
  char buffer[BUFSIZE];	// room for 80 chars plus \0
  char *cmd;
  int pid;


  // Print a prompt and read a command from standard input
  printf(" > ");
  cmd = fgets(buffer, BUFSIZE, stdin);

  // did the user enter a command?
  while(cmd != NULL)
  {
    // check for the newline character and overwrite with \0
    len = strlen(buffer);
    if(buffer[len-1] == '\n')
    {
      buffer[len-1] = '\0';
    }

    pid=fork();

    if (pid != 0)
    {
      wait();
    }
    else // execute the command
    {
      ret_code = execl(cmd, cmd, NULL);
      if(ret_code != 0)
      {
        printf("Error executing %s.\n", cmd);
        exit(0);
      }
    }

    fflush(stdout);
    // Print a prompt and read a command from standard input
    printf(" > ");
    cmd = fgets(buffer, BUFSIZE, stdin);
  }

  printf("All done.\n");
} // main

