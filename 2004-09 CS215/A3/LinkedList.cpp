#include <iostream>
#include "LinkedList.h"

//------------------------------------------------------
// LinkedList
//
// PURPOSE: LinkedList constructor; creates a dummy node.
// EXTERNAL REFERENCES:  Requires ListNode class
//------------------------------------------------------

LinkedList::LinkedList()
{

	// Creates a dummy entry
	top = new ListNode(NULL, NULL);

}

//------------------------------------------------------
// ~LinkedList
//
// PURPOSE: LinkedList destructor
// EXTERNAL REFERENCES:  Requires ListNode class
//------------------------------------------------------


LinkedList::~LinkedList()
{

	ListNode *temp = NULL;
	ListNode *next = top;
	top = NULL;

	// Traverse the list, deleting each node
	while(next != NULL)
	{
		temp = next->link;
		delete next;
		next = temp;
	}

}

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts the passed value into the front of the list.
// PARAMETERS:  BinarySearchTree data: The data to be inserted into the list.
// EXTERNAL REFERENCES:  Requires BinarySearchTree, ListNode classes
//------------------------------------------------------

void LinkedList::insert(BinarySearchTree *data)
{

	top->link = new ListNode(data, top->link);

}

//------------------------------------------------------
// insertOrdered
//
// PURPOSE: Inserts the passed value into its appropriate place in the list.
// PARAMETERS:  BinarySearchTree data: The data to be inserted into the list.
// EXTERNAL REFERENCES:  Requires ListNode, BinarySearchTree classes
//------------------------------------------------------


void LinkedList::insertOrdered(BinarySearchTree *data)
{

	ListNode *next = top->link;
	ListNode *prev = top;

	// Traverse the list looking for the correct position
	while(next != NULL && next->data->compareTo(data) < 1) //CHECK!
	{
		prev = next;
		next = next->link;
	}

	// Insert the data at that position
	prev->link = new ListNode(data, next);
	cout << "Data added." << endl;

}
