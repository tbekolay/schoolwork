//-----------------------------------------
// CLASS: LinkedList
//
// REMARKS: An ordered linked list implementation, with integer data.
//
// INPUT: Integer values
//
// OUTPUT: Ordered list
//
//-----------------------------------------

#include "node.h"

class LinkedList
{
	protected:
		Node *top; //Points to the first entry in the list, which is the largest

	public:
		LinkedList();
		virtual ~LinkedList();
		void insertOrdered(int data);
		void deleteFirst();

};
