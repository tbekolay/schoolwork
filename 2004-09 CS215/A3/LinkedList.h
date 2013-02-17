//-----------------------------------------
// CLASS: LinkedList
//
// REMARKS: An abstract linked list implementation, with BinarySearchTree data.
//
// INPUT: BinarySearchTree data
//
// OUTPUT: Unordered linked list.
//
//-----------------------------------------

#ifndef LLIST_H
#define LLIST_H

#include "ListNode.h"

using namespace std;

class LinkedList
{
	private:
		ListNode *top;

	public:
		virtual bool isEmpty() = 0;
		virtual void insert(BinarySearchTree *data);
		virtual void insertOrdered(BinarySearchTree *data);
		virtual void deleteFirst() = 0;
		LinkedList();
		virtual ~LinkedList();

};

#endif
