//-----------------------------------------
// CLASS: FreqList
//
// REMARKS: An unordered linked list implementation, with Frequency data.
//
// INPUT: Frequency value, or a string
//
// OUTPUT: Unordered list of Frequencies
//
//-----------------------------------------

#ifndef FREQLIST_H
#define FREQLIST_H

#include "ListFNode.h"
#include "LinkedList.h"

using namespace std;

class Frequency;

class FreqList : private LinkedList
{
	friend class FileHandler;

	private:
		ListFNode *top;
		ListFNode *search(const string input);
		void increment(Frequency *input);

	public:
		FreqList();
		FreqList(const FreqList *input);
		~FreqList();
		int length();
		bool isEmpty();

		void insert(const string input);
		void insert(Frequency *input);
		Frequency *leave();
		void deleteFirst();

};

#endif
