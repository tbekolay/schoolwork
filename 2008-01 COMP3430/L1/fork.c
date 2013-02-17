/*
fork.c
------

Simple example of some code that creates processes
using the fork() system call.

A number of other system calls are also made. They are:

getuid() which returns the numeric userid of the user executing
                                                                        
                                        the process.

getpid() which returns the numeric process id (PID) of the process.

sleep() which causes the process to sleep for a designated
number of seconds.

exit() which terminates the program with the specified completion status
(zero is normal completion).

For those unfamiliar with C, the %d format code used in the printf()
call causes an integer variable to be formatted for output as a
decimal ('d') number.

The purpose of the variable j is to illustrate that the child process
is a duplicate of the parent process, including any variable allocation.

*/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{

  int i;
  int pid;
  int j=890;

  for(i=0; i<3; i++)
  {
    printf("Parent process before the fork, uid= %d, pid=%d\n",
      getuid(),getpid());
    pid=fork();
    if(pid!=0)
    {
      /* parent process executes here */
      printf("Parent process after the fork, uid= %d, pid=%d\n",
        getuid(),getpid());
    }
    else
    {
      /* child process execute here */
      printf("Child process after the fork, uid= %d, pid=%d\n",
        getuid(),getpid());
      printf("j = %d\n",j);
      sleep(1);
      printf("Child process exiting, uid= %d, pid=%d\n",
        getuid(),getpid());
      exit(0);
    }
    j++;
  } /* end for */
  printf("Parent process exiting, uid= %d, pid=%d\n",
    getuid(),getpid());
} /* end main */

