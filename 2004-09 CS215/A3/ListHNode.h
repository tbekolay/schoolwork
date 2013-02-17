//-----------------------------------------
// CLASS: ListHNode
//
// REMARKS: A node class that contains a HuffmanTree.
//
// INPUT:  A HuffmanTree
//
// OUTPUT: Linked Node structure
//
//-----------------------------------------

#ifndef LHNODE_H
#define LHNODE_H

#include "HuffmanTree.h"

using namespace std;

class ListHNode
{

	friend class PriorityQueue;

	private:
		HuffmanTree *data;
		ListHNode *link;

	public:
		int compareTo(const ListHNode *input);
		ListHNode(HuffmanTree *newData, ListHNode *newLink);
		~ListHNode();

};

#endif
