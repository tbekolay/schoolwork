#include <iostream>
#include "FreqList.h"

//------------------------------------------------------
// FreqList
//
// PURPOSE: Default FreqList constructor; create a dummy ListFNode
// EXTERNAL REFERNCES: Requires ListFNode class.
//------------------------------------------------------


FreqList::FreqList()
{

	// Creates a dummy entry
	top = new ListFNode(NULL, NULL);

}

//------------------------------------------------------
// FreqList
//
// PURPOSE: FreqList copy constructor; self-explanatory
// EXTERNAL REFERNCES: Requires ListFNode class.
//------------------------------------------------------


FreqList::FreqList(const FreqList *input)
{
	top = new ListFNode( input->top );
}


//------------------------------------------------------
// ~FreqList
//
// PURPOSE: FreqList destructor
// EXTERNAL REFERENCES:  Requires ListFNode class
//------------------------------------------------------


FreqList::~FreqList()
{

	ListFNode *temp = NULL;
	ListFNode *next = top;
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
// length
//
// PURPOSE: Determines the number of entries in the FreqList
// EXTERNAL REFERENCES:  Requires ListFNode
//------------------------------------------------------

int FreqList::length()
{

	ListFNode *curr = top->link; //Begin at the first non-dummy entry
	int count = 0;

	//Traverse until we hit null, increasing a counter each time.
	while(curr != NULL)
	{
		curr = curr->link;
		count++;
	}

	return count;

}

//------------------------------------------------------
// isEmpty
//
// PURPOSE: Determines if the list has any non-dummy entries
//------------------------------------------------------

bool FreqList::isEmpty()
{
	return( top->link == NULL );
}

//------------------------------------------------------
// search
//
// PURPOSE: Searches for and returns a ListFNode containing the passed string
// PARAMETERS:  const string input - the string to search for
// EXTERNAL REFERENCES:  Requires ListFNode class
//------------------------------------------------------

ListFNode* FreqList::search(const string input)
{

	bool found = false; //If the word has been found
	ListFNode *next = top->link; //Start at the first non-dummy entry

	//Continue if we haven't hit NULL, and it has not been found yet
	while(next != NULL && !found)
	{
		//Check if the current ListFNode contains the string

		if(next->data->word == input)
			found = true;
		else
			next = next->link;
	}

	//Will contain NULL if the string was not found
	return next;

}

//------------------------------------------------------
// increment
//
// PURPOSE: Increases the freq member of a passed Frequency object by 1.
// PARAMETERS:  Frequency *input - the object to be incremented
// EXTERNAL REFERENCES:  Requires Frequency class
//------------------------------------------------------

void FreqList::increment(Frequency *input)
{

	input->freq = input->freq + 1;

}

//------------------------------------------------------
// insert
//
// PURPOSE: Either adds the passed string into the list or increments an
//			existing node with the same string
// PARAMETERS:  const string input - the string to be added or incremented
// EXTERNAL REFERENCES:  Requires ListFNode, Frequency classes
//------------------------------------------------------

void FreqList::insert(const string input)
{

	ListFNode *found = search(input); //Find the node with that string, if it exists

	//If it doesn't exist, create a new ListFNode
	if(found == NULL)
		top->link = new ListFNode(new Frequency(1, input), top->link);
	//If it does, increment its frequency
	else
		increment(found->data);

}

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts the passed value into the front of the list.
// PARAMETERS:  Frequency *input - the value to be added to the front
// EXTERNAL REFERENCES:  Requires Frequency, ListFNode classes
//------------------------------------------------------

void FreqList::insert(Frequency *input)
{
	top->link = new ListFNode(input, top->link);
}

//------------------------------------------------------
// leave
//
// PURPOSE: Deletes and returns the item at the front of the list
// EXTERNAL REFERENCES:  Requires Frequency, ListFNode classes
//------------------------------------------------------

Frequency* FreqList::leave()
{

	Frequency *output = NULL; //The output

	//If there is a non dummy entry, return it
	if(top->link != NULL)
	{
		output = top->link->data;
		//Delete the first non-dummy entry
		this->deleteFirst();
	}

	return output;

}


//------------------------------------------------------
// deleteFirst
//
// PURPOSE: Deletes the first entry in the linked list, aside from the dummy.
//			Prints a message if the list is empty.
// EXTERNAL REFERENCES:  Requires ListFNode class
//------------------------------------------------------

void FreqList::deleteFirst()
{

	ListFNode *temp;

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
