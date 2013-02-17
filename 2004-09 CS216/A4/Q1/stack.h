#ifndef STACK_H
#define STACK_H

typedef struct stackNode {
   int data;
   struct stackNode *next;
} StackNode;

typedef struct STACK {
	StackNode *top;
} Stack;

void push(Stack *stack, int data);
int pop(Stack *stack);

#endif
