/*
shell.c
-------

Not-so simple shell program.
Uses globbing to expand wildcard arguments.

By Trevor Bekolay, 6796723
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <glob.h>

#define BUFSIZE 81
#define DELIMS " \t"

int main(int argc, char *argv[])
{

  glob_t globber;             // globber for wildcard expansion
  int prompt = 1;             // number to show in the prompt
  int ret_code;               // return code
  int len;                    // length of entered command
  char buffer[BUFSIZE];       // room for 80 chars plus \0
  char* cmd;                  // pointer to entered command
  char* token;                // pointer to keep track of tokens from strtok

  /* Print a prompt. */
  printf("%d%% ", prompt);

  while(fgets(buffer, BUFSIZE, stdin))
  {
    /* Check for the newline character and overwrite with \0. */
    len = strlen(buffer);

    if(buffer[len-1] == '\n')
    {
      buffer[len-1] = '\0';
    }


    if(fork())
    {
      /* Parent process waits here. */
      wait(&ret_code);

      if(ret_code != 0)
      {
        printf("Error during execution of %s.\n", buffer);
      }
    }

    else
    {
      /* Get the command (the first token). */
      token = strtok(buffer, DELIMS);
      cmd = strdup(token);

      /* We have to do our first glob call here,
         as we can't use GLOB_APPEND on the first glob call. */
      glob(token, GLOB_NOCHECK, NULL, &globber);
      token = strtok(NULL, DELIMS);

      while (token)
      {
        /* Glob to expand each token if needed. Since we use GLOB_NOCHECK,
           args with no wildcards will get returned as is. */
        glob(token, GLOB_NOCHECK|GLOB_APPEND, NULL, &globber);
        token = strtok(NULL, DELIMS);
      }

      /* Execute our globbed command. */
      ret_code = execvp(cmd, &globber.gl_pathv[0]);

      if(ret_code != 0)
      {
        printf("Could not execute %s.\n", cmd);
      }

      /* If we get to here, we'll free our pointers (cmd and the globber). */
      free(cmd);
      globfree(&globber);

      exit(0);
    }

    /* Prompt again. */
    printf("%d%% ", ++prompt);
  } /* end while */

  printf("All done.\n");
} /* end main */
