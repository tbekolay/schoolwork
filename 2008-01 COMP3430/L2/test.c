#include <stdio.h>
#include <pthread.h>

#include "list.h"

#define NUM_THREADS 10

void *Thread(void *dummy)
{
   int tid = (int)pthread_self();
   listNodePtr list = (listNodePtr)dummy;

   printf("\nThread <%d>: \n", tid);

   listInsert(&list, tid, tid*2);
   if (listFind(list, tid) == tid*2)
   {
     printf("Success! listFind(%d) == %d\n", tid, tid*2);
   }
   else
   {
     printf("Error! listFind(%d) != %d\n", tid, tid*2);
   }

   listDelete(&list, tid);

   pthread_exit(NULL);
} // end firstThread
  listNodePtr list = NULL;

int main (int argc, char * argv[])
{
   pthread_t tids[NUM_THREADS];

   int rc, i;

   for(i=0; i<NUM_THREADS; i++)
   {
     pthread_create(&tids[i],NULL,Thread,(void*)list);

   }

   for(i=0; i<NUM_THREADS; i++)
   {
     pthread_join(tids[i],NULL);
   }

   pthread_exit(NULL);
} // end main

