#include <iostream>
#include "node.h"

//------------------------------------------------------
// Node
//
// PURPOSE: Node constructor
// PARAMETERS:  int newValue: Value to be inserted in the Node
//				Nod newLink: Link to be inserted in the Node
//------------------------------------------------------

HuffNode::HuffNode(int newValue, Node *newLink) : value(newValue), link(newLink)
{}

//------------------------------------------------------
// ~Node
//
// PURPOSE: Node destructor. Ensures link does not persist.
//------------------------------------------------------

HuffNode::~HuffNode()
{
	link = NULL;
}
