#include <math.h>
#include <assert.h>
#include "hash.h"
#define HORNER 31
#define MAD_A 7
#define MAD_B 13

/**
 * PURPOSE: compdiv performs the compression algorithm
 *			using basic division.
 * PARAM	int input - the hashed number
 *			int size - the size of the hash table
 * RETURN   int - the final compressed index
 **/


static int compdiv(int input, int size)
{
	return( abs(input) % size );
}

/**
 * PURPOSE: compdiv performs the compression algorithm
 *			using the MAD (multiply, add, divide) algorithm.
 * PARAM	int input - the hashed number
 *			int size - the size of the hash table
 * RETURN   int - the final compressed index
 **/

static int compmad(int input, int size)
{
	return( abs(((MAD_A * input) + MAD_B)) % size );
}

/**
 * PURPOSE: hash returns the hash value an inputted string
 * PARAM  	char* input - the input string
 *			int size - the size of the hash table
 *			char compmethod - a char representing the compression method to use
 * RETURN   int - the hash value of the string
 **/

int hash(char* input, int size, char compmethod)
{
	int i;
	int sum = 0;
	int (*comp)(int,int);

	for( i = (strlen(input) - 1); i >= 0; i-- )
	{
		sum += (int)input[i];
		sum *= HORNER;
	}

	if(compmethod == '1')
	{
		comp = compdiv;
	}
	else
	{
		comp = compmad;
	}

	assert( comp(sum, size) >= 0 );
	assert( comp(sum, size) < size);

	return( comp(sum, size) );
}