#include <iostream>
#include "TreeNode.h"

//------------------------------------------------------
// TreeNode
//
// PURPOSE: Default TreeNode constructor; self-explanatory
//------------------------------------------------------

TreeNode::TreeNode()
{
	value = 0;
	left = NULL;
	right = NULL;
}

//------------------------------------------------------
// TreeNode
//
// PURPOSE: TreeNode constructor; self-explanatory
//------------------------------------------------------

TreeNode::TreeNode(int newValue, TreeNode *newLeft, TreeNode *newRight) : value(newValue), left(newLeft), right(newRight)
{}

//------------------------------------------------------
// ~TreeNode
//
// PURPOSE: TreeNode destructor. Ensures links do not persist.
//------------------------------------------------------

TreeNode::~TreeNode()
{
	left = NULL;
	right = NULL;
}

//------------------------------------------------------
// isLeafNode
//
// PURPOSE: Determines if the current TreeNode is a leaf node.
//------------------------------------------------------

bool TreeNode::isLeafNode()
{
	return (left == NULL && right == NULL);
}
