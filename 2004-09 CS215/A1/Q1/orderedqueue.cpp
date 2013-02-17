#include <iostream>
#include "orderedqueue.h"
#define MAX 32767

//------------------------------------------------------
// OrderedQueue
//
// PURPOSE: OrderedQueue constructor, sets a dummy node.
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

OrderedQueue::OrderedQueue()
{

	//Creates a dummy node
	top = new Node(MAX, NULL);

}

//------------------------------------------------------
// ~OrderedQueue
//
// PURPOSE: OrderedQueue destructor.  Frees memory from all nodes.
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

OrderedQueue::~OrderedQueue()
{

	Node *temp = NULL;
	Node *next = top;
	top = NULL;

	//Traverses the queue and deletes all created nodes.
	while(next != NULL)
	{
		temp = next->link;
		delete next;
		next = temp;
	}

}

//------------------------------------------------------
// enter
//
// PURPOSE: Inserts the passed value into the OrderedQueue.
//			Position in the queue is based on the value.
// PARAMETERS:  int data: The data to be inserted into the list.
// EXTERNAL REFERENCES:  Requires Node and LinkedList classes
//------------------------------------------------------

void OrderedQueue::enter(int data)
{

	//Calls the linked list's ordered queue method
	LinkedList::insertOrdered(data);

}

//------------------------------------------------------
// leave
//
// PURPOSE: Returns the current largest value, and deletes it from
//			the queue.  Returns -1 if the queue is empty.
// EXTERNAL REFERENCES:  Requires Node and LinkedList classes.
//------------------------------------------------------

int OrderedQueue::leave()
{

	//Sets output to -1 if queue is empty
	int output = -1;

	//Gets the first node's value to return, then
	//call's linkedlists's deleteFirst method to remove it
	if(top->link != NULL)
	{
		output = top->link->value;
		LinkedList::deleteFirst();
	}
	else
	{
		cout << "Queue is empty.  Returning -1!" << endl;
	}

	return output;
}

