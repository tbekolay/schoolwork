#include <iostream>
#include "ListNode.h"

//------------------------------------------------------
// compareTo
//
// PURPOSE: Compares two ListNodes, outputting a number based on which is bigger.
// PARAMETERS:  const ListNode *input - The ListNode to be compared against
//------------------------------------------------------

int ListNode::compareTo(const ListNode *input)
{
	return( this->data->compareTo(input->data) );
}

//------------------------------------------------------
// ListNode
//
// PURPOSE: Default ListNode constructor; self-explanatory
//------------------------------------------------------

ListNode::ListNode()
{
	data = NULL;
	link = NULL;
}

//------------------------------------------------------
// ListNode
//
// PURPOSE: ListNode constructor; self-explanatory
//------------------------------------------------------

ListNode::ListNode(BinarySearchTree *newData, ListNode *newLink) : data(newData), link(newLink)
{}

//------------------------------------------------------
// ~ListNode
//
// PURPOSE: ListNode destructor. Ensures link does not persist.
//------------------------------------------------------

ListNode::~ListNode()
{
	link = NULL;
}
