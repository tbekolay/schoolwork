#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "hash.h"
#include "table.h"
#define TRUE 1
#define FALSE 0

struct NODE
{
	struct NODE *next;
	char *data;
};

typedef struct NODE node;

struct TABLE
{
	node *words;
};

/**
 * PURPOSE: createtable allocates space for an array of size nodes.
 * PARAM    int size - the number of nodes we wish to allocate space for
 * RETURN   table* - points to the first node on the table
 **/

table *createtable( int size )
{
	table *tbl;

	tbl = (table*)malloc(size * sizeof(node));

	tbl->words = (node*)tbl;

	return tbl;

}

/**
 * PURPOSE: Describe what the function does!
 * PARAM   [one for each parameter]
 * RETURN  VOID or description of the value returned
 **/

void printtable(table* tbl, int size)
{

	node *curr;
	int i;

	for(i = 0; i < size; i++)
	{
		curr = &(tbl->words[i]);
		while(curr != NULL && curr->data != NULL)
		{
			printf("%d: %s\n",i,curr->data);
			curr = curr->next;
		}

	}

}

/**
 * PURPOSE: addtable hashes and adds a word to a hash table
 * PARAM    table* tbl - the table we wish to add to
 *			int size - the size of the table
 *			char* input - the string we wish to add
 *			char compmethod - the method we will use to compress it
 * RETURN   VOID
 **/

void addtable( table *tbl, int size, char *input, char compmethod)
{

	int index = hash(input, size, compmethod);
	node *word = (node*)malloc(sizeof(node));
	node *temp;

	if(searchtable(tbl, size, input, compmethod) == FALSE)
	{
		word->data = input;
		word->next = NULL;

		if(tbl->words[index].data == NULL )
		{
			tbl->words[index] = *word;
		}
		else
		{
			temp = (node*)malloc(sizeof(node));
			temp->next = NULL;
			temp->data = (char*)malloc(strlen(tbl->words[index].data));
			strcpy(temp->data, tbl->words[index].data);
			word->next = temp;
			tbl->words[index] = *word;
		}
	}
}

/**
 * PURPOSE: searchtable searches the has table for a desired word.
 *			Returns TRUE or FALSE
 * PARAM    table *tbl - the table to search
 *			int size - the size of the table to search
 *			char *input - the word to be searched for
 *			char compmethod - the method we will use to compress it
 * RETURN   int - whether or not the string was found - TRUE or FALSE
 **/

int searchtable(table *tbl, int size, char *input, char compmethod)
{

	int index = hash(input, size, compmethod);
	int found = FALSE;
	node* curr = &(tbl->words[index]);

	printf("Searching at: %d\n",index);

	if( curr->data != NULL )
	{
		if( strcmp(input, tbl->words[index].data) == 0 )
			found = TRUE;
		curr = tbl->words[index].next;
	}

	return found;
}