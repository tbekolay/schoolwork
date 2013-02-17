/**
 * 74.216 SECTION L01
 * INSTRUCTOR    Rasit Eskicioglu
 * ASSIGNMENT    Assignment 3
 * AUTHOR        Trevor Bekolay, 6796723
 **/

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include "llist.h"
#include "table.h"
#define NONOPT -1
#define DICT_MAX 25
#define MAX_LINE 512
#define DEFAULT_SIZE 53

/**
 * PURPOSE: main drives the spellchecking program. It builds a dictionary
 *			from a file, then reads in another file from stdin and checks
 *			the spelling on it.
 * PARAM    int argc - the number of command line arguments
 *			char *argv[] - an array of pointers to command line arguments
 * RETURN   int - normal return code
 **/

int main( int argc, char *argv[] )
{

	FILE *dictionary;
	char compmethod;
	char *filename;
	char *test;
	char *nextword;
	int choice;
	int i;
	int line = 0;
	int size = DEFAULT_SIZE; //hash table size
	char buffer[MAX_LINE];
	llist *miswords;
	table *dict;

	if(( choice = getopt( argc, argv, "f:" )) == NONOPT )
	{
		printf("Please pass a command arguments -f <filename>.\n");
		return 0;
	}

	do
	{
		switch ( choice )
		{
		case 'f' :
			filename = (char*)optarg;
			printf ("Reading in file %s\n",filename);
			break;
		case '?' :
			printf("Error! Please use only the -f options (refer to README).\n");
			return 0;
		}
	} while(( choice = getopt( argc, argv, "f:" )) != NONOPT );

	printf("Assignment 3.  074.216, L01.  Trevor Bekolay, 6796723\n\n");
	printf("Which compression algorithm would you like to use? (1 or 2).\n
			\t1. Division\n
			\t2. MAD (Multiply, add, divide)\n");
	printf("How large would you like the hash table?\n
			\t1. 53\n
			\t2. 119\n
			\t3. 1013\n");

	compmethod = getchar();

	if( compmethod != '1' && compmethod != '2' )
	{
		printf("Input not recognized.  Resorting to Division method.\n");
		compmethod = '1';
	}

	i = getchar();

	if( i == '2')
	{
		size = 119;
	}
	else if( i == '3')
	{
		size = 1013;
	}

	printf("Using hash table with size %d!\n",size);

	dict = createtable(size);

	printf("Loading dictionary...\n");
	dictionary = fopen(filename, "r");

	test = fgets(buffer, DICT_MAX - 1, dictionary);

	while( test != NULL )
	{

		for(i = 0; i < strlen(buffer); i++)
			buffer[i] = tolower(buffer[i]);

		printf("%s",buffer);

		addtable( dict, size, buffer, compmethod );

		test = fgets(buffer, DICT_MAX - 1, dictionary);

	}

	printf("Dictionary loaded! Searching for misspelled words...\n");

	miswords = createllist();

	test = fgets(buffer, MAX_LINE -1, stdin);

	while( test != NULL )
	{
		line++;

		nextword = strtok(buffer, " ,.-");

		while( nextword != NULL)
		{
			for(i = 0; i < strlen(nextword) - 1; i++)
				nextword[i] = tolower(nextword[i]);

			if( searchtable(dict, size, nextword, compmethod) )
			{
				addllist( miswords, buffer, line );
			}
			nextword = strtok(NULL, " ,.-");
		}

		test = fgets(buffer, MAX_LINE -1, stdin);

	}


	return 0;

}



