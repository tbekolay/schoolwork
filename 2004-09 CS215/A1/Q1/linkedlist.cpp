#include <iostream>
#include "linkedlist.h"
#define MAX 32767 //for making the dummy node

//------------------------------------------------------
// LinkedList
//
// PURPOSE: LinkedList constructor
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

LinkedList::LinkedList()
{

	// Creates a dummy entry
	top = new Node(MAX, NULL);

}

//------------------------------------------------------
// ~LinkedList
//
// PURPOSE: LinkedList destructor
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------


LinkedList::~LinkedList()
{

	Node *temp = NULL;
	Node *next = top;
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
// insertOrdered
//
// PURPOSE: Inserts the passed value into its appropriate place in the list.
// PARAMETERS:  int data: The data to be inserted into the list.
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------


void LinkedList::insertOrdered(int data)
{

	Node *next = top->link;
	Node *prev = top;

	// Traverse the list looking for the correct position
	while(next != NULL && next->value > data)
	{
		prev = next;
		next = next->link;
	}

	// Insert the data at that position
	prev->link = new Node(data, next);
	cout << data << " added." << endl;

}

//------------------------------------------------------
// deleteFirst
//
// PURPOSE: Deletes the first entry in the linked list, aside from the dummy.
//			Prints a message if the list is empty.
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

void LinkedList::deleteFirst()
{

	Node *temp;

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
