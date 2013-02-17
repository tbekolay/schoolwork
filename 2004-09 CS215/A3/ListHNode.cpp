#include <iostream>
#include "ListHNode.h"

//------------------------------------------------------
// compareTo
//
// PURPOSE: Compares two ListHNodes, outputting a number based on which is bigger.
// PARAMETERS:  const ListHNode *input - The ListNode to be compared against
//------------------------------------------------------

int ListHNode::compareTo(const ListHNode *input)
{

	return( this->data->compareTo(input->data) );

}

//------------------------------------------------------
// ListHNode
//
// PURPOSE: ListHNode constructor; self-explanatory
//------------------------------------------------------

ListHNode::ListHNode(HuffmanTree *newData, ListHNode *newLink) : data(newData), link(newLink)
{}

//------------------------------------------------------
// ~ListHNode
//
// PURPOSE: ListHNode destructor. Ensures link does not persist.
//------------------------------------------------------

ListHNode::~ListHNode()
{
	link = NULL;
}
