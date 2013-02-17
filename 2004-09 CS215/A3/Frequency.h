//-----------------------------------------
// CLASS: Frequency
//
// REMARKS: A storage class that contains an integer and string data field
//
// INPUT: An integer and a string
//
// OUTPUT: Frequency class containing both
//
//-----------------------------------------
#ifndef FREQ_H
#define FREQ_H

#include <string>

using namespace std;

class Frequency
{
	friend class FreqList;
	friend class HuffmanTree;
	friend class FileHandler;

	private:
		int freq;
		string word;

	public:
		int compareTo( const Frequency *input );
		Frequency();
		Frequency(const int newFreq, const string newWord);
		~Frequency();

};

#endif
