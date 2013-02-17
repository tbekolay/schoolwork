/*
   main_mutex.c
   COMP 3430 Assignment 2 Winter 2008
   
   This program utilizes the concurrent list code developed
   as part of Lab 2.

*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>

#include "concurrentList.h"
#include "a2parameters.h"


// global declarations
concurrentList the_list;

// counters and mutex for the reader threads
int found, not_found;
pthread_mutex_t the_key = PTHREAD_MUTEX_INITIALIZER;


// the function for the reader threads
void *reader(void *dummy)
{
  int i, random;
  
  for (i = 0; i < NUMREADS; i++)
  {
    random = randInt(KEYMIN,KEYMAX);
    if (listFind(&the_list, random) != NOT_FOUND)
    {
      pthread_mutex_lock(&the_key);
      found++;
    }
    else
    {
      pthread_mutex_lock(&the_key);
      not_found++;
    }
    pthread_mutex_unlock(&the_key);
    sleep(1);
  }
}// reader

int main(int argc, char *argv[])
{

  pthread_t the_reader[READERS];
  int i, random;
  time_t start_time, end_time;
  
  // initialize the counters
  found = 0;
  not_found = 0;
  
  // record the start time
  start_time = time(NULL);

  // initialize the concurrent list
  listInit(&the_list);
  
  // create the reader threads
  for (i = 0; i < READERS; i++)
  {
    pthread_create(&the_reader[i], NULL, reader, NULL);
  }

  // start writing to the list
  for (i = 0; i < NUMWRITES; i++)
  {
    random = randInt(KEYMIN,KEYMAX);
    listInsert(&the_list, random, random);
  }

  // wait for the reader threads
  for (i = 0; i < READERS; i++)
  {
    pthread_join(the_reader[i], NULL);
  }

  // print the time taken and the counter values
  end_time = time(NULL);
  printf("%d items were found. %d items were not found.\n\tTime taken: %d seconds.", found, not_found, end_time-start_time);
  
  
  printf("\nEnd of processing\n");
  return 0;
} /* end main */
