//-----------------------------------------
// CLASS: ListFNode
//
// REMARKS: A node class with an Frequency data field.
//
// INPUT: Frequency data.
//
// OUTPUT: Linked Node structure
//
//-----------------------------------------

#ifndef LFNODE_H
#define LFNODE_H

#include "Frequency.h"

using namespace std;

class ListFNode
{
	friend class FileHandler;
	friend class FreqList;

	private:
		Frequency *data;
		ListFNode *link;

	public:
		int compareTo(const ListFNode *input);
		ListFNode();
		ListFNode(const ListFNode *input);
		ListFNode(Frequency *newData, ListFNode *newLink);
		~ListFNode();

};

#endif
