/**
 * 74.216 SECTION L01
 * INSTRUCTOR    Rasit Eskicioglu
 * ASSIGNMENT    Assignment 2, question 1
 * AUTHOR        Trevor Bekolay, 6796723
 **/

#include <stdio.h>
#include <string.h>
#include <ctype.h>

/* Prototypes */
int palindrome(char*);

/**
 * PURPOSE: Main is a driver function used to test
 *			the palindrome method. On a successful run,
 *			the user will see a a message after Testing: line.
 * RETURN   0 - Standard exit code
 **/


int main()
{

	char* test;

	printf("Assignment 2, Question 1.  074.216, L01.  Trevor Bekolay, 6796723\n");

	/*This should be a palindrome.*/
	test = "a b..c c!b!!a";
	printf("Testing: %s\n",test);

	/*If so, output successful message.*/
	if(palindrome(test))
		printf("%s is a palindrome!\n",test);
	else
		printf("Error!");

	/*This should be a palindrome.*/
	test = "  aaa!!aaaa..aaaaa ";
	printf("Testing: %s\n",test);

	/*If so, output successful message.*/
	if(palindrome(test))
		printf("%s is a palindrome!\n",test);
	else
		printf("Error!");

	/*This should not be a palindrome.*/
	test = "a,.b cb.,!ca";
	printf("Testing: %s\n",test);

	/*If so, output not successful message.*/
	if(!palindrome(test))
		printf("%s is not a palindrome!\n",test);
	else
		printf("Error!");

	/*This should not be a palindrome.*/
	test = "     abbbaa";
	printf("Testing: %s\n",test);

	/*If so, output not successful message.*/
	if(!palindrome(test))
		printf("%s is not a palindrome!\n",test);
	else
		printf("Error!");

	printf("\nProgram completed normally. Thank goodness!\n");

	return(0);

}

/**
 * PURPOSE: Palindrome takes in a string and determines if it is
 *			a palindrome; that is, it reads the same forwards and
 *			backwards, ignoring non-alphabetic characters.
 * PARAM	char *string - pointer to the string to be tested
 * RETURN   0 - If not a palindrome
 *			1 - If string is a palindrome
 **/


int palindrome(char *string)
{

	int left = 0;
	int right;
	int pdrome = 1; /*Initially assume we have a palindrome*/

	/*The right pointer initially points to the last item in the char array.*/
	right = strlen(string) - 1;

	/*While the left and right pointers have not crossed and we
	still believe it is a palindrome, continue checking*/
	while(left < right && pdrome)
	{

		/*Advance the left pointer to the first alphabetic character*/
		while(!isalpha((int)string[left]))
		{
			left++;
		}

		/*Advance the right pointer to the first alphabetic character*/
		while(!isalpha((int)string[right]))
		{
			right--;
		}

		/*Compare the character at left and right*/
		if( string[left] != string[right] )
		{
			pdrome = 0;
		}
		/*If they are the same, continue*/
		else
		{
			left++;
			right--;
		}

	}

	return pdrome;

}
