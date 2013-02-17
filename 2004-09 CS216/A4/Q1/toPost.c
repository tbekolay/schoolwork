#include <math.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>
#include "toPost.h"
#include "stack.h"
/* isoperator macro: determines if the passed character is one of 6 predefined operators*/
#define isoperator(i) ( i=='+' || i=='-' || i=='*' || i=='/' || i=='^' || i=='%' )
#define NUMOPS 6

/**
 * PURPOSE: getChar returns a character based on the number passed
 * PARAM	operators in: the enumerated operators number to be converted
 * RETURN   char: the character that it represents
 **/

static char getChar(operators in)
{
	char output;

	/*Based on the enumeration, determine the correct char*/
	if(in == PLUS)
		output = '+';
	else if(in == MINUS)
		output = '-';
	else if(in == TIMES)
		output = '*';
	else if(in == DIVIDE)
		output = '/';
	else if(in == EXPONENT)
		output = '^';
	else /*if(in == MOD)*/
		output = '%';

	return output;
}

/**
 * PURPOSE: getOp returns an operator number depending on the char passed
 * PARAM    char in: the character to be converted
 * RETURN  operators: the enumerated operators representation
 **/

static operators getOp(char in)
{
	operators output;

	/*Based on the enumeration, determine the correct operators*/
	if(in == '+')
		output = PLUS;
	else if(in == '-')
		output = MINUS;
	else if(in == '*')
		output = TIMES;

	else if(in == '/')
		output = DIVIDE;
	else if(in == '^')
		output = EXPONENT;
	else /*if(in == '%')*/
		output = MOD;

	return output;
}

/**
 * PURPOSE: trim gets rid of extra spaces in a postfix expression.
 *			Note that this has no bearing on wether the expression
 *			can be evaluated; it is for beautification purposes only.
 * PARAM   char *in: the ugly string to be trimmed
 * RETURN  char *: The beautified string after trimming
 **/

static char *trim(char *in)
{
	int i;
	int length = strlen(in);
	/*Set aside space for the new string*/
	char *output = (char*)malloc(length);
	output[0] = '\0';

	/*Loop through, adding the character to the new string
	only if it isn't a space preceeded by a space*/
	for(i=0; i<length; i++)
	{
		if(in[i] != ' ')
			strncat(output, &in[i], 1);
		else if(in[i - 1] != ' ')
			strncat(output, &in[i], 1);
	}

	return output;
}

/**
 * PURPOSE: performOp performs the passed operation on the
 *			passed operands and returns the result
 * PARAM    operators op: a numerical representation of the operation
 *			to be performed
 *			int operand1: the first operand
 *			int operand2: the second operand
 * RETURN  int: the result of the calculation
 **/

static int performOp(operators op, int operand1, int operand2)
{
	int output;

	/*Depending on the passed op, a different operation is performed*/
	if(op == PLUS)
		output = operand1 + operand2;
	else if(op == MINUS)
		output = operand1 - operand2;
	else if(op == TIMES)
		output = operand1 * operand2;
	else if(op == DIVIDE)
		output = operand1 / operand2;
	else if(op == EXPONENT)
		/*This is the only unusual operator, as '^' is not by default
		the operator for exponentiation.*/
		output = (int)pow( (double)operand1, (double)operand2 );
	else /*if(in == MOD)*/
		output = operand1 % operand2;

	return output;
}

/**
 * PURPOSE: toPostFix changes the passed infix equation to the
 *			equivalent postfix equation
 * PARAM    char *infix: the equation in infix notation
 * RETURN  char *: The equation in postfix notation
 **/

char *toPostfix(char *infix)
{
	int i; /*Loop counter*/
	operators currop;
	char curropc;
	int bracket = 0; /*Adds precendence when a bracket has appeared*/
	int length = strlen(infix);
	char *postfix = (char*)malloc(length); /*Make space for the new postfix expression*/

	Stack *stack = (Stack*)malloc(sizeof(Stack)); /*Initialize a stack*/
	stack->top = NULL;

	postfix[0] = '\0';

	/*Loop through each character of the infix string.*/
	for(i=0; i<length; i++)
	{

		/*If the character is an operator*/
		if(isoperator(infix[i]))
		{
			/*Find the number representation of it and add
			'bracket'*/
			currop = getOp(infix[i]) + bracket;

			/*If the stack is empty, push the operator*/
			if(stack->top == NULL)
			{
				push(stack, currop);
			}
			else
			{

				/*If it isn't empty, pop the stack until it is empty or until
				the current operator has a higher precedence than the top of the stack*/
				while( (stack->top != NULL) && (stack->top->data > currop) )
				{
					/*We must mod the popped operator by NUMOPS in case it was
					within brackets*/
					curropc = getChar(pop(stack) % NUMOPS);
					strncat(postfix, &curropc, 1);
				}
				/*Then push the current operation*/
				push(stack, currop);
			}
		}

		/*If the character is an opening bracket, add NUMOPS to bracket*/
		else if(infix[i] == '(')
		{
			bracket += NUMOPS;
		}

		/*If the character is a closing bracket, subtract NUMOPS from bracket*/
		else if(infix[i] == ')')
		{
			bracket -= NUMOPS;
		}

		/*Copy all other non '\n' characters to the string*/
		else if(infix[i] != '\n')
		{
			strncat(postfix, &infix[i], 1);
		}
	}

	/*If there are operators left over, pop them from the stack*/
	while(stack->top != NULL)
	{
		/*We must mod the popped operator by NUMOPS in case it was
		within brackets*/
		curropc = getChar(pop(stack) % NUMOPS);
		strncat(postfix, &curropc, 1);
	}

	return trim(postfix);
}

/**
 * PURPOSE: evalPostfix takes an expression in postfix notation
 *			and outputs the integer it evaluates to.
 * PARAM:   char *expr: The postfix expression
 * RETURN  int: The result of the evaluated expression
 **/

int evalPostfix(char *expr)
{
	int length = strlen(expr);
	char *temp = (char*)malloc(length); /*Set aside room for the temporary string*/
	int operand1;
	int operand2;
	int i; /*Loop counter */

	/*Initialize a stack*/
	Stack *stack = (Stack*)malloc(sizeof(Stack));
	stack->top = NULL;

	/*Loop through every character*/
	for(i=0; i < length; i++)
	{

		/*If the character is a digit, get all the digits in that number (up until
		a space) and convert it to an integer.*/
		if(isdigit(expr[i]))
		{
			temp[0] = '\0';
			while(isdigit(expr[i]))
			{
				strncat(temp, &expr[i++], 1);
			}
			push(stack, atoi(temp));
		}

		/*If the character is an operator*/
		if(isoperator(expr[i]))
		{
			/*Pop the stack to get the two operands*/
			operand2 = pop(stack);
			operand1 = pop(stack);
			/*Evaluate operand1 (operator) operand2 and push it to the stack*/
			push( stack, performOp(getOp(expr[i]), operand1, operand2) );
		}
	}

	/*In the end, the stack will have one element; the answer*/
	return pop(stack);

}