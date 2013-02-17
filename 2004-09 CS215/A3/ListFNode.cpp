#include <iostream>
#include "ListFNode.h"

//------------------------------------------------------
// compareTo
//
// PURPOSE: Compares two ListFNodes, outputting a number based on which is bigger.
// PARAMETERS:  const ListFNode *input - The ListFNode to be compared against
//------------------------------------------------------

int ListFNode::compareTo(const ListFNode *input)
{

	return( this->data->compareTo(input->data) );

}

//------------------------------------------------------
// ListFNode
//
// PURPOSE: Default ListFNode constructor; self-explanatory
//------------------------------------------------------

ListFNode::ListFNode()
{
	data = NULL;
	link = NULL;
}

//------------------------------------------------------
// ListFNode
//
// PURPOSE: ListFNode constructor; self-explanatory
//------------------------------------------------------

ListFNode::ListFNode(Frequency *newData, ListFNode *newLink) : data(newData), link(newLink)
{}

//------------------------------------------------------
// ListFNode
//
// PURPOSE: ListFNode copy constructor; self-explanatory
//------------------------------------------------------

ListFNode::ListFNode(const ListFNode *input)
{
	data = input->data;
	if( input->link != NULL )
		link = new ListFNode(input->link);
	else
		link = NULL;
}

//------------------------------------------------------
// ~ListFNode
//
// PURPOSE: ListFNode destructor. Ensures link does not persist.
//------------------------------------------------------

ListFNode::~ListFNode()
{
	link = NULL;
}
