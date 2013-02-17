//-----------------------------------------
// CLASS: ListNode
//
// REMARKS: A node class that contains a BinarySearchTree.
//
// INPUT: A BinarySearchTree
//
// OUTPUT: Linked Node structure
//
//-----------------------------------------

#ifndef LNODE_H
#define LNODE_H

#include "BinarySearchTree.h"

using namespace std;

class ListNode
{
	friend class LinkedList;

	private:
		BinarySearchTree *data;
		ListNode *link;

	public:
		virtual int compareTo(const ListNode *input);
		ListNode();
		ListNode(BinarySearchTree *newData, ListNode *newLink);
		virtual ~ListNode();

};

#endif
