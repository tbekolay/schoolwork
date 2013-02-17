/**
 * 74.216 SECTION L01
 * INSTRUCTOR    Rasit Eskicioglu
 * ASSIGNMENT    Assignment 4, question 2
 * AUTHOR        Trevor Bekolay, 6796723
 **/

#include <stdio.h>
#include "tree.h"
#define WORDLEN 20

/**
 * PURPOSE: main prompts for up to 31 words, puts it into a balanced tree,
 *			then outputs that tree in the specified way.
 * RETURN  int: return code
 **/

int main()
{

	int count = 0; /*counter*/
	char *test = (char*)1; /*test (must not be null to begin with)*/
	char buffer[WORDLEN + 1]; /*for input*/
	Tree tree = NULL; /*initially, tree is empty*/

	/*Print banner*/
	printf("Assignment 4, Question 2.  074.216, L01.  Trevor Bekolay, 6796723\n");
	printf("Please enter up to 31 words of up to %d characters (do you really need more?)!  Ctrl+D will stop reading in words. Starting ... now.\n",WORDLEN);

	/*Continue until EOF or we have hit 31 words*/
	while(test != NULL && count < 31)
	{
		/*Prompt for input*/
		test = fgets(buffer, WORDLEN, stdin);
		/*Insert the input into a BST*/
		tree = insert(tree, buffer);
		count++;
	}

	/*If we had an EOF, then count is one higher than it should be.*/
	if(test == NULL)
		count--;

	/*Balance the tree*/
	tree = buildBalancedTree(tree, count);

	printf("\n");
	/*Print out the balanced tree*/
	printTree(tree);

	printf("\n\n~End of processing~\n");

	return 0;

}
