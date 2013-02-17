#include <iostream>
#include "PriorityQueue.h"

//------------------------------------------------------
// PriorityQueue
//
// PURPOSE: PriorityQueue constructor, sets a dummy node.
// EXTERNAL REFERENCES:  Requires ListHNode class
//------------------------------------------------------

PriorityQueue::PriorityQueue()
{

	//Creates a dummy node
	top = new ListHNode(NULL, NULL);

}

//------------------------------------------------------
// ~PriorityQueue
//
// PURPOSE: PriorityQueue destructor.  Frees memory from all nodes.
// EXTERNAL REFERENCES:  Requires ListHNode class
//------------------------------------------------------

PriorityQueue::~PriorityQueue()
{

	ListHNode *temp = NULL;
	ListHNode *next = top;
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
// isEmpty
//
// PURPOSE: Determines if the queue has any non-dummy entries
//------------------------------------------------------

bool PriorityQueue::isEmpty()
{
	return( top->link == NULL );
}

//------------------------------------------------------
// isOneLeft
//
// PURPOSE: Determines if the queue has only one non-dummy entry.
//------------------------------------------------------

bool PriorityQueue::isOneLeft()
{
	return( (top->link != NULL) && (top->link->link == NULL) );
}

//------------------------------------------------------
// enter
//
// PURPOSE: Inserts the passed value into the PriorityQueue.
//			Position in the queue is based on the root of the HuffmanTree.
// PARAMETERS:  HuffmanTree *data: The tree to be inserted into the list.
// EXTERNAL REFERENCES:  Requires ListHNode and HuffmanTree
//------------------------------------------------------

void PriorityQueue::enter(HuffmanTree *data)
{

	ListHNode *next = top->link;
	ListHNode *prev = top;

	// Traverse the list looking for the correct position
	while(next != NULL && next->data->compareTo(data) < 1)
	{
		prev = next;
		next = next->link;
	}

	// Insert the data at that position
	prev->link = new ListHNode(data, next);

}

//------------------------------------------------------
// leave
//
// PURPOSE: Returns the current smallest value, and deletes it from
//			the queue.  Returns NULL if the queue is empty.
// EXTERNAL REFERENCES:  Requires HuffmanTree class.
//------------------------------------------------------

HuffmanTree* PriorityQueue::leave()
{

	//Sets output to NULL if queue is empty
	HuffmanTree *output = NULL;

	//Gets the first node's value to return, then
	//calls the deleteFirst method to remove it
	if(top->link != NULL)
	{
		output = top->link->data;
		this->deleteFirst();
	}
	else
	{
		cout << "Queue is empty.  Returning NULL!" << endl;
	}

	return output;
}

//------------------------------------------------------
// deleteFirst
//
// PURPOSE: Deletes the first non-dummy entry in the queue
// EXTERNAL REFERENCES:  Requires ListHNode class.
//------------------------------------------------------

void PriorityQueue::deleteFirst()
{

	ListHNode *temp;

	// If there are at least 2 non-dummy nodes, delete the first and create new link
	if(top->link != NULL && top->link->link != NULL)
	{
		temp = top->link->link;
		delete top->link;
		top->link = temp;
	}

	// If there is only one non-dummy node, delete it
	else if(top->link != NULL && top->link->link == NULL)
	{
		delete top->link;
		top->link = NULL;
	}

	// If there are no non-dummy entries, print a message
	else //if(top->link == NULL)
	{
		cout << "List is empty." << endl;
	}

}
