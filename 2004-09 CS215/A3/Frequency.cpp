#include <iostream>
#include <string>
#include "Frequency.h"

//------------------------------------------------------
// compareTo
//
// PURPOSE: Compares two Frequencies, outputting a number based on which is bigger.
// PARAMETERS:  const Frequency *input - The Frequency to be compared against
//------------------------------------------------------

int Frequency::compareTo( const Frequency *input )
{

	int compare = 1;

	if( freq < input->freq )
		compare = -1;
	else if( freq == input->freq )
		compare = 0;

	return compare;

}

//------------------------------------------------------
// Frequency
//
// PURPOSE: Default Frequency constructor; self-explanatory
//------------------------------------------------------


Frequency::Frequency()
{
	freq = 0;
	word = "";
}

//------------------------------------------------------
// Frequency
//
// PURPOSE: Frequency constructor; self-explanatory
//------------------------------------------------------


Frequency::Frequency(const int newFreq, const string newWord) : freq(newFreq), word(newWord)
{}

//------------------------------------------------------
// ~Frequency
//
// PURPOSE: Frequency destructor.
//------------------------------------------------------

Frequency::~Frequency()
{}
