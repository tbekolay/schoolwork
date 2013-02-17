/*
  list.c
  Unordered linked list implementation.
  No dummy node used (watch for special cases).
  Note that the pointer to the list is passed as
  a reference parameter (why?).
*/

#include <stdlib.h>
#include <stdio.h>  // to get a definition of NULL
#include <pthread.h>

// include header file for linked list data structure definitions
# include "list.h"

static pthread_mutex_t my_mutex = PTHREAD_MUTEX_INITIALIZER;

// routine to insert a new <key,value> pair into the list.
void listInsert(listNodePtr *list, int key, int value){
  pthread_mutex_lock(&my_mutex);

  listNodePtr newNode;

  newNode = (listNodePtr) malloc(sizeof(listNode));
  newNode->key = key;
  newNode->data = value;
  if(*list == NULL){
    // first insertion into empty list
    newNode->next = NULL;
    *list = newNode;
  }else{
    // insert new node at head of list - easy
    newNode->next = *list;
    *list = newNode;
  }

  pthread_mutex_unlock(&my_mutex);
} // end listInsert

// routine to delete first occurrence of
// a node with the given key from the list.
void listDelete(listNodePtr *list, int key){
  pthread_mutex_lock(&my_mutex);
  listNodePtr curr, prev;

  prev = NULL;
  curr = *list;
  while(curr != NULL){
    if (curr->key == key){
      // found node to delete
      if (prev == NULL){
        // deleting node at head of list
        *list = curr->next;
        free(curr);
      }else{
        // deleting node past head of list
        prev->next = curr->next;
        free(curr);
      }

      pthread_mutex_unlock(&my_mutex);
      return;
    }else{
      prev = curr;
      curr = curr->next;
    }
  }// while

  pthread_mutex_unlock(&my_mutex);
} // end listDelete

// routine to return the data from the node with the given key.
int listFind(listNodePtr list, int key){
  pthread_mutex_lock(&my_mutex);

  listNodePtr curr;
  int result = -1;  // indicates "not found"

  curr=list;
  while(curr != NULL){
    if(curr->key == key){ //  Why didn't I just code
      result = curr->data;  // 'return(curr->data)' in this if stmt?

      pthread_mutex_unlock(&my_mutex);
      return result;
    }else
      curr = curr->next;
  }// while

  pthread_mutex_unlock(&my_mutex);
  return(result);
} // end listFind

/*
void traverse(listNodePtr list){
  listNodePtr curr = list;
  while(curr != NULL){
    printf("%d %d\n", curr->key, curr->data);
    curr = curr->next;
  }
}// traverse
*/
