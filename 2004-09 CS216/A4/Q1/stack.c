#include "stack.h"
#define NULL 0

/**
 * PURPOSE: Push adds an integer item to the top of the passed Stack
 * PARAM:	stack: the stack that will be pushed onto
 *			data: the data to be pushed
 * RETURN  VOID
 **/

void push(Stack *stack, int data)
{

	/*malloc space for a new node*/
	StackNode *newnode = (StackNode*)malloc(sizeof(StackNode));

	newnode->data = data; /*set passed data as its data member*/
	newnode->next = stack->top;
	stack->top = newnode; /*set newnode to be the stack's new top*/

}

/**
 * PURPOSE: Pop deletes then returns the top element of the stack.
 * PARAM    stack: the stack to be popped from
 * RETURN   int: the popped data
 **/

int pop(Stack *stack)
{

	int output = -1; /*if the stack is empty, return -1*/

	/*if it isn't empty*/
	if(stack->top != NULL)
	{
		output = stack->top->data; /*set the data to be returned*/
		stack->top = stack->top->next; /*delete the top node*/
	}

	return output;

}
