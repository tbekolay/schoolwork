/**
 * 74.216 SECTION L01
 * INSTRUCTOR    Rasit Eskicioglu
 * ASSIGNMENT    Assignment 4, question 1
 * AUTHOR        Trevor Bekolay, 6796723
 **/

#include "stack.h"
#include "toPost.h"
#include <stdio.h>
#define EXPRLEN 40

/**
 * PURPOSE: Main is the driver for the program.  Input expressions
 * 			in infix, and ye shall receive postfix expressions and
 *			the evaluated result.
 * RETURN  INT return code
 **/

int main()
{
	char *test;	/*To determines if we've hit EOF*/
	char infix[EXPRLEN + 1]; /*Gets input*/
	char *postfix;

	/*Banner*/
	printf("Assignment 4, Question 1.  074.216, L01.  Trevor Bekolay, 6796723\n");
	printf("Please enter formulas.  Spaces are required after all numbers! But aside from that go nuts. Ctrl+d (or EOF) will end the porgram.\n");

	/*Get the first line of input*/
	test = fgets(infix, EXPRLEN, stdin);

	/*Continue getting input until EOF*/
	while(test != NULL)
	{
		printf("Infix equation: %s\n",infix);

		/*Changes infix to postfix*/
		postfix = toPostfix(infix);
		printf("Postfix equation: %s\n",postfix);

		/*Evaluate and print result*/
		printf("Evaluates to: %d\n\n",evalPostfix(postfix));

		/*Read in another expression*/
		test = fgets(infix, EXPRLEN, stdin);
	}

	printf("~End of processing~");

	return 0;
}
