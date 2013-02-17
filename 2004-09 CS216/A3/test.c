#include <string.h>
#include <stdio.h>
#include "table.c"

int main()
{

	table *tbl;
	char *word;
	int size = 53;
	char compmethod = '1';

	printf("Testing all of table (EEE!!)'s methods!!\n\n");
	printf("Creating tbl, adding stuff, searching for stuff.\n");

	tbl = createtable(size);

	word = "hoi";
	printf("Adding word %s\n",word);
	addtable(tbl, size, word, compmethod);

	printtable(tbl, size);

	word = "boi";
	printf("Adding word %s\n",word);
	addtable(tbl, size, word, compmethod);

	printtable(tbl, size);

	word = "owijf";
	printf("Adding word %s\n",word);
	addtable(tbl, size, word, compmethod);

	printtable(tbl, size);

/*

	llist *listt;
	char *word;
	int line;

	printf("Testing all of llist's methods!!\n\n");
	printf("Creating llist listt.  Adding stuff to it.\n");

	listt = createllist();

	word = "hoi";
	line = 11;

	printf("Adding misspelled word %s at line %d\n",word,line);
	addllist(listt, word, line);

	printlist(listt);

	word = "zuh";
	line = 11;

	printf("Adding misspelled word %s at line %d\n",word,line);
	addllist(listt, word, line);

	printlist(listt);


	word = "ajia";
	line = 12;

	printf("Adding misspelled word %s at line %d\n",word,line);
	addllist(listt, word, line);

	printlist(listt);

	word = "hoi";
	line = 15;

	printf("Adding misspelled word %s at line %d\n",word,line);
	addllist(listt, word, line);

	printlist(listt);




	char* buffer = "a wefe";
	int size = 53;
	char compmethod = '2';
	int shash;

	printf("Testing all of hash's methods!!\n\n");
	printf("Using string %s, size %d, compmethod %c\n",buffer,size,compmethod);

	shash = hash( buffer, size, compmethod );

	printf("Result of hashing: %d\n",shash);
*/
	return 0;

}