/**
 * 74.216 SECTION L01
 * INSTRUCTOR    Rasit Eskicioglu
 * ASSIGNMENT    Assignment 2, question 2
 * AUTHOR        Trevor Bekolay, 6796723
 **/

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#define INPUT_MAX 81

/* Prototypes */
int thecount(char*);

/**
 * PURPOSE: Main reads in an unspecified number of lines from
 *			stdin, and calls the 'thecount' method to find
 *			the number of "the"s in the input lines.
 * RETURN   0 - Standard exit code
 **/

int main()
{

	char input[INPUT_MAX];
	char *test; /*Captures fgets output.*/
	int sum = 0; /*Running total of "the"s.*/

	printf("Assignment 1, Question 2.  074.216, L01.  Trevor Bekolay, 6796723\n");
	printf("Please enter your favorite string.\n");

	/*Set the first letter to blank; acts like a dummy.*/
	input[0] = ' ';
	/*Gets input from stdin.*/
	test = fgets(input + sizeof(char), INPUT_MAX - 1, stdin);

	/*Stops when fgets returns NULL.*/
	while(test != NULL)
	{
		/*Echo the input.*/
		printf("%s",input + sizeof(char));
		/*Add to the running sum.*/
		sum += thecount(input);
		test = fgets(input + sizeof(char), INPUT_MAX - 1, stdin);
	}

	printf("\nA total of %d instances of the word \"the\" found!\n",sum);
	printf("Program completed normally. Neat!\n");

	return(0);

}

/**
 * PURPOSE: thecount takes in a pointer to a string and searches
 *			it for instances of the word "the".  It outputs
 *			the number to stdout and returns it.
 * PARAM	char *string - pointer to the string to be tested
 * RETURN   The number of "the"s in the input string
 **/


int thecount(char *string)
{

	int count = 0; /*Number of 'the's.*/
	int index = 0;

	/*Continue through the string until we hit a newline character,
	EOF character, or the end of the string.*/
	while(string[index] != '\n' && string[index] != EOF && index < INPUT_MAX)
	{
		/*If the current character is not a space, move to the next.*/
		if(!isspace((int)string[index]))
		{
			index++;
		}

		/*If it is a space, check the next three chracters, to see if they are
		't' 'h' and 'e', then that there is a space after the 'e'.*/
		else
		{
			if(tolower(string[++index]) == 't')
				if(tolower(string[++index]) == 'h')
					if(tolower(string[++index]) == 'e')
						
if(isspace((int)string[++index]))
							count++;
		}
	}

	printf("%d instances of the word \"the\" found!\n",count);

	return count;

}
