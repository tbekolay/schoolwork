/*
  concurrentList.h
*/

#include <pthread.h>

// the type definition for a linked list node
typedef struct ln
{
  int key;
  int data;
  struct ln *next;
} listNode, *listNodePtr;

typedef struct cl
{
  pthread_rwlock_t lock;
  listNodePtr head;
} concurrentList, *concurrentListPtr;

// prototype definitions for the list operations
void listInit(concurrentListPtr list);

void listInsert(concurrentListPtr list,  int key, int value);

void listDelete(concurrentListPtr list, int key);

int listFind(concurrentListPtr list, int key);

void traverse(concurrentListPtr list);
