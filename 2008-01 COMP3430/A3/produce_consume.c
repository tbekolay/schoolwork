/*
   fileIO.c
   COMP 3430 Operating Systems

   To compile: gcc -lpthread fileIO.c

*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/types.h>

#define BLOCKING 0              // blocking send/receive
#define NON_BLOCKING IPC_NOWAIT // non-blocking send/receive
#define ANY_MSG 0               // receive message of any type
#define SHUTDOWN_T 2            // type of message that indicates shutdown
#define VALID_T 1               // type of message that contains valid data
#define MSG_SIZE 81             // max. size of message
#define MYPORT 96723            // unique value to create key
#define PERMS 0644              // (octal) owner may R/W, others R only
#define MAX_CONSUMERS 10
#define DELIMS " \n"

// the message buffer record
typedef struct msgbuf
{
   long mtype;	// message type (must be > 0)
   char the_message[MSG_SIZE];	// the message
} Message, *Message_ptr;

// message queue parameters
key_t myKey;	// unique key
int queueId;	// message queue identifier
int flags;
size_t msgsize = sizeof(Message);


int counts[MAX_CONSUMERS];

void* consumer(void* _tid)
{
  Message_ptr message;
  long tid = (long)_tid;
  char* token, *save_ptr;

  counts[tid] = 0; // initialize count

  // get first message
  message = (Message_ptr)malloc(msgsize);
  msgrcv(queueId, message, msgsize, ANY_MSG, BLOCKING);

  while(message->mtype != SHUTDOWN_T)
  {
    token = strtok_r(message->the_message, DELIMS, &save_ptr);
    while (token != NULL)
    {
      //printf("%s ", token);
      counts[tid]++;
      token = strtok_r(NULL, DELIMS, &save_ptr);
    }

    msgrcv(queueId, message, msgsize, ANY_MSG, BLOCKING);
  }

  pthread_exit(NULL);
}

int main (int argc, char * argv[])
{
  char buffer[MSG_SIZE];
  FILE *input_file;
  pthread_t threads[MAX_CONSUMERS];
  Message_ptr message;
  int num_consumers, sum, rc, t;

  // argument checking
  if(argc < 3)
  {
    printf("Usage: %s num_threads <input file>\n", argv[0]);
    exit(0);
  }

  num_consumers = atoi(argv[1]);
  if (num_consumers > MAX_CONSUMERS) num_consumers = MAX_CONSUMERS;

  // try to open the input file
  input_file = fopen(argv[2], "r");
  if(input_file == NULL)
  {
    printf("Unable to open %s\n", argv[2]);
    exit(0);
  }

  // create an IPC key for our message queue
  myKey = ftok(".", MYPORT);

  // create the message queue
  flags = IPC_CREAT | IPC_EXCL | PERMS;
  queueId = msgget(myKey, flags);
  if(queueId < 0)
  {
    printf("Error creating message queue.\n");
    exit(0);
  }

  // launch consumer threads
  for(t = 0; t < num_consumers; t++)
  {
    //printf("Creating thread number %d\n", t);
    rc = pthread_create(&threads[t], NULL, consumer, (void *)(long)t);

    if(rc)
    {
      printf("ERROR - return code from pthread_create() is %d\n", rc);
      exit(-1);
    }
  }// for

  // read the input file
  fgets(buffer, MSG_SIZE, input_file);
  while(!feof(input_file))
  {
    // Make message
    message = (Message_ptr)malloc(msgsize);
    message->mtype = VALID_T;
    strncpy(message->the_message, buffer, MSG_SIZE-1);

    // Put message into queue
    if(msgsnd(queueId, message, msgsize, NON_BLOCKING == -1)) printf("Error!");

    fgets(buffer, MSG_SIZE, input_file);
  }
  fclose(input_file);

  // finished reading input file; insert shutdown messages
  for(t = 0; t < num_consumers; t++)
  {
    // Make message
    message = (Message_ptr)malloc(msgsize);
    message->mtype = SHUTDOWN_T;

    // Put message into queue
    if(msgsnd(queueId, message, msgsize, NON_BLOCKING == -1)) printf("Error!");
  }

  // wait for consumer threads
  for(t = 0; t < num_consumers; t++)
  {
    pthread_join(threads[t], NULL);
  }

  sum = 0;

  printf(" thread ID | # of words found\n");
  printf("-----------------------------\n");
  for(t = 0; t < num_consumers; t++)
  {
    printf("     %d     |  %d\n", t, counts[t]);
    sum += counts[t];
  }

  printf("\nTotal number of words: %d\n", sum);


  // close the message queue
  printf("removing message queue%d\n", queueId);
  msgctl(queueId, IPC_RMID, NULL);

  printf("End of processing\n");
  pthread_exit(NULL);
} // end main

