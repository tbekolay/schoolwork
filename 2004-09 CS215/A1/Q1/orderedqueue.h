//-----------------------------------------
// CLASS: OrderedQueue
//
// REMARKS: An OrderedQueue build on a linked list.
//			The largest values leave first.
//
// INPUT: Integer data
//
// OUTPUT: Queue ordered descendingly
//
//-----------------------------------------


#include "linkedlist.h"

class OrderedQueue : private LinkedList
{
	public:
		OrderedQueue();
		~OrderedQueue();
		void enter(int data);
		int leave();
		void print();

};
