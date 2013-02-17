/*
   COMP 3430 Operating Systems
   message.c
   A simple example of using a message queue to
   communicate between two processes.

   To compile: gcc message.c
*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/msg.h>
#include <sys/ipc.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>

#define TRUE 1
#define FALSE 0
#define BLOCKING 0     // blocking send/receive
#define NON_BLOCKING IPC_NOWAIT
#define MYPORT 96723   // unique value to create key
#define FIRST_MSG 0    // receive first message in queue
#define DATA_T1 1      // type code for a data message
#define DATA_T2 2
#define PERMS 0644     // (octal) owner may R/W, others R only
#define MSG_SIZE 100   // max. size of message

// global declarations
// (so both parent and child can access)

int ret_code;

// the message buffer record
typedef struct msgbuf{
   long mtype;  // message type (must be > 0)
   char the_message[MSG_SIZE];  // the message
}Message, *Messageptr;

// message queue parameters
key_t myKey;  // unique key
int queueId;  // message queue identifier
int flags;
size_t msgsize = sizeof(Message);

int main(int argc, char *argv[]){

   Messageptr message1;
   char msg[MSG_SIZE];
   char *msgptr;

   // create an IPC key for our message queue
   myKey = ftok(".", MYPORT);

   // create the message queue
   // IPC_CREAT - create a queue with the given key
   //             if it does not already exist.
   // IPC_EXCL - return error if queue with the given
   //            key already exists.
   // PERMS - specify permissions for queue.
   flags = IPC_CREAT | IPC_EXCL | PERMS;
   queueId = msgget(myKey, flags);
   if(queueId < 0){
      printf("Error creating message queue.\n");
      exit(0);
   }
   printf("This is parent process %d\n", getpid());
   printf("queueId = %d\n", queueId);

   // get a message from the user
   printf("Enter a message to send: ");
   msgptr = fgets(msg, MSG_SIZE, stdin);

   // fork a new process
   if(fork()){
      // send a message to the queue
      message1 = (Messageptr)malloc(msgsize);
      message1->mtype = DATA_T2;
      strncpy(message1->the_message, msgptr, MSG_SIZE-1);
      ret_code = msgsnd(queueId, message1, msgsize, BLOCKING);
      printf("Message sent\n");
      sleep(2);
   }else{
      sleep(1);
      // child process will read message from queue
      printf("Inside child process %d\n", getpid());
      message1 = (Messageptr)malloc(msgsize);

      ret_code = msgrcv(queueId, message1, msgsize, DATA_T1, NON_BLOCKING);
      printf("Return value of first msgrcv: %d\n\n", ret_code);

      ret_code = msgrcv(queueId, message1, msgsize, FIRST_MSG, BLOCKING);
      printf("Return value of second msgrcv: %d\n", ret_code);
      printf("Child process read message type %d %s",
        message1->mtype, message1->the_message);
      exit(0);
   }

   // close the message queue
   printf("removing message queue %d\n", queueId);
   msgctl(queueId, IPC_RMID, NULL);

   printf("End of processing\n");
   return 0;
} /* end main */
