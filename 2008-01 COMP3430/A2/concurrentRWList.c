/*
  concurrentRWList.c
  Unordered linked list implementation.
  No dummy node used (watch for special cases).
*/

#include <stdlib.h>
#include <stdio.h>  // to get a definition of NULL

// include header file for linked list data structure definitions
# include "concurrentRWList.h"
# include "a2parameters.h"

// routine to initialize a new list.
void listInit(concurrentListPtr list)
{
  list->head = NULL;
  pthread_rwlock_init(&(list->lock), NULL);
} // end listInit


// routine to insert a new <key,value> pair into the list.
void listInsert(concurrentListPtr list, int key, int value)
{
  listNodePtr newNode;

  newNode = (listNodePtr) malloc(sizeof(listNode));
  newNode->key = key;
  newNode->data = value;
  pthread_rwlock_wrlock(&(list->lock));
  if(list->head == NULL)
  {
    // first insertion into empty list
    newNode->next = NULL;
    list->head = newNode;
  }
  else
  {
    // insert new node at head of list - easy
    newNode->next = list->head;
    list->head = newNode;
  }
  pthread_rwlock_unlock(&(list->lock));
} // end listInsert

// routine to delete first occurrence of
// a node with the given key from the list.
void listDelete(concurrentListPtr list, int key)
{
  listNodePtr curr, prev;

  prev = NULL;
  pthread_rwlock_wrlock(&(list->lock));
  curr = list->head;
  
  while(curr != NULL)
  {
    if (curr->key == key)
    {
      // found node to delete
      if (prev == NULL)
      {
        // deleting node at head of list
        list->head = curr->next;
        free(curr);
      }
      else
      {
        // deleting node past head of list
        prev->next = curr->next;
        free(curr);
      }
      
      pthread_rwlock_unlock(&(list->lock));
      return;
    }
    else
    {
      prev = curr;
      curr = curr->next;
    }
  }// while

  pthread_rwlock_unlock(&(list->lock));
} // end listDelete

// routine to return the data from the node with the given key.
int listFind(concurrentListPtr list, int key)
{
  listNodePtr curr;
  int result = -1;  // indicates "not found"

  pthread_rwlock_rdlock(&(list->lock));
  curr=list->head;
  
  while(curr != NULL)
  {
    if(curr->key == key)
    { //  Why didn't I just code
      result = curr->data;  // 'return(curr->data)' in this if stmt?
      pthread_rwlock_unlock(&(list->lock));
      return result;
    }
    else
    {
      curr = curr->next;
      usleep(MICROSLEEP);
    }
  }// while

  pthread_rwlock_unlock(&(list->lock));
  return(result);
} // end listFind

void traverse(concurrentListPtr list)
{
  listNodePtr curr;
  pthread_rwlock_rdlock(&(list->lock));
  
  curr = list->head;
  
  while(curr != NULL)
  {
    printf("%d %d\n", curr->key, curr->data);
    curr = curr->next;
    usleep(MICROSLEEP);
  }
  
  pthread_rwlock_unlock(&(list->lock));
}// traverse

