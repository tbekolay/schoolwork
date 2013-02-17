/*
myexec.c
--------

Simple example of some code that causes a running process to execute a
different program (entered from standard input). Handling of an error
situation is also illustrated.

The fgets() function is a 'safe' version of gets() for obtaining user
input. The gets function does no checking for buffer overflow.

One quirk about using fgets is that it leaves the newline in the input
buffer, which will cause exec to fail every time, so we must get rid
of the newline before passing it to exec.

*/

#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define BUFSIZE 81

int main(int argc, char *argv[])
{

  int ret_code;		// return code
  int len;		// length of entered command
  char buffer[BUFSIZE];	// room for 80 chars plus \0
  char *cmd;		// pointer to entered command

  // Print a prompt and read a command from standard input
  printf(" > ");
  cmd = fgets(buffer, BUFSIZE, stdin);

  // did the user enter a command?
  if(cmd != NULL)
  {
    // check for the newline character and overwrite with \0
    len = strlen(buffer);
    if(buffer[len-1] == '\n')
    {
      buffer[len-1] = '\0';
    }
    // execute the command
    ret_code = execl(cmd, cmd, NULL);
    if(ret_code != 0)
    {
      printf("Error executing %s.\n", cmd);
    }
  }
  printf("All done.\n");
} // main

