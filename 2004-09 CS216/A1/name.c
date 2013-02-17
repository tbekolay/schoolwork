/**
 * 74.216 SECTION L0
 * INSTRUCTOR    Rasit Eskicioglu
 * ASSIGNMENT    Assignment 1, question 2
 * AUTHOR        Trevor Bekolay, 6796723
 **/

#include <stdio.h>
#define INPUT_MAX 50
#define NAME_MAX 18

/**
 * PURPOSE: Main takes in input from stdin and parses out the first
 *          token and prints it.  Continues until 'q' or EOF is reached. 
 * RETURN   0 - Standard exit code
 **/

int main()
{
	/* char arrays have specific sizes as fgets will be used
	   to get input */
	char input[INPUT_MAX];
	char name[NAME_MAX];
	char* test;
	int i = 0;
	int j = 0;

	printf("Assignment 1, Question 2.  074.216, L01.  Trevor Bekolay, 6796723\n");
	printf("Please enter some names!  Type q to quit.\n");
	/* get a string no longer than INPUT_MAX */
	fgets(input, INPUT_MAX, stdin);

	/* this while loop stops when the user types 'q' or the end of a
 	   file has been reached */
	while(input[i] != 'q' && test != NULL)
	{

		/* advance in the string past blank spaces */
		while(input[i] == ' ' && i < INPUT_MAX)
		{
			i++;
		}

		/* add the characters after the blank spaces into the name
		   string, until another blank space is reached. */
		while(input[i] != ' ' && input[i] != '\n' && input[i] != EOF && i < INPUT_MAX && j < (NAME_MAX - 1)) 
		{
			name[j++] = input[i++];
		}

		/* terminates the name string */
		name[j] = '\0';		
		printf("%s***%s***\n",input,name);

		/* resets the variables for the next time through the while loop */
		i = 0;
		j = 0;
		test = fgets(input, INPUT_MAX, stdin);
	}

	printf("Program completed normally. Wasn't that fun?\n");

	return (0);

}
