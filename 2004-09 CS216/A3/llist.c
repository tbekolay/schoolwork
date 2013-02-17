#include <stdio.h>
#include <stdlib.h>
#include "llist.h"
#define TRUE 1
#define FALSE 0

struct NODE
{
	struct NODE *next;
	char *data;
	int numlines;
	int lines[10];
};

typedef struct NODE node;

struct LLIST
{
	node* top;
};

/**
 * PURPOSE: Describe what the function does!
 * PARAM   [one for each parameter]
 * RETURN  VOID or description of the value returned
 **/

void printlist(llist* list)
{

	node *curr = list->top;

	while(curr != NULL)
	{
		printf("Word: %s, Numlines: %d, Lines: %d,%d\n",curr->data,curr->numlines,curr->lines[0],curr->lines[1]);
		curr = curr->next;
	}

}

/**
 * PURPOSE: createllist allocates space for the top node of a linked list.
 * RETURN   llist* - a pointer to the first node of the list.
 **/

llist *createllist()
{
	return( (llist*)malloc(sizeof(node)) );
}

void destroylist(llist* list)
{

	node *curr;
	node *next;



}

/**
 * PURPOSE: addllist - adds an inputted string to the linkedlist
 * PARAM    llist* list - the list to be added to
 *			char* input - the string inputted
 *			int line - the line number the string was found on
 * RETURN   VOID
 **/

void addllist(llist* list, char* input, int line)
{

	int found = FALSE;
	node *prev = NULL;
	node *curr = list->top;
	node *word = (node*)malloc(sizeof(node));

	while( curr != NULL && !found )
	{
		if( strcmp(curr->data, input) == 0 )
			found = TRUE;
		else
		{
			prev = curr;
			curr = curr->next;
		}
	}

	if(found)
	{
		if(curr->numlines < 10)
		{
			curr->lines[curr->numlines] = line;
			curr->numlines++;
		}
	}
	else
	{

		word->next = curr;
		word->data = input;
		word->numlines = 1;
		word->lines[0] = line;

		if(prev == NULL)
		{
			if(curr == NULL)
				list->top = word;
			else
				curr->next = word;
		}
		else
		{
			prev->next = word;
		}
	}
}