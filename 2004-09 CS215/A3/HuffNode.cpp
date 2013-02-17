#include <string>
#include "HuffNode.h"

//------------------------------------------------------
// HuffNode
//
// PURPOSE: HuffNode constructor; self-explanatory
//------------------------------------------------------

HuffNode::HuffNode(const int newFreq, const int newSplit, const string newData, HuffNode *newLeft, HuffNode *newRight) : frequency(newFreq), split(newSplit), data(newData), left(newLeft), right(newRight)
{}

//------------------------------------------------------
// ~HuffNode
//
// PURPOSE: HuffNode destructor. Ensures link does not persist.
//------------------------------------------------------

HuffNode::~HuffNode()
{
	left = NULL;
	right = NULL;
}

//------------------------------------------------------
// isLeafNode
//
// PURPOSE: Determines if the current HuffNode is a leaf node or not.
//------------------------------------------------------

bool HuffNode::isLeafNode()
{
	return (left == NULL && right == NULL);
}
