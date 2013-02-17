#include <iostream>
#include "BinarySearchTree.h"

//------------------------------------------------------
// compareTo
//
// PURPOSE: Compares two BinarySearchTrees, outputting a number based on which is bigger.
// PARAMETERS:  const BinarySearchTree *input - The BinarySearchTree to be compared against
//------------------------------------------------------

int BinarySearchTree::compareTo( const BinarySearchTree *input )
{

	int compare = 1;

	if( (this->top->value) < (input->top->value) )
		compare = -1;
	else if( this->top->value == input->top->value )
		compare = 0;

	return compare;

}

//------------------------------------------------------
// BinarySearchTree
//
// PURPOSE: Default BinarySearchTree constructor; self-explanatory
//------------------------------------------------------

BinarySearchTree::BinarySearchTree()
{
	top = NULL;
}

//------------------------------------------------------
// BinarySearchTree
//
// PURPOSE: BinarySearchTree constructor; self-explanatory
//------------------------------------------------------

BinarySearchTree::BinarySearchTree(TreeNode *root)
{
	top = root;
}

//------------------------------------------------------
// ~BinarySearchTree
//
// PURPOSE: BinarySearchTree destructor
// EXTERNAL REFERENCES:  Requires TreeNode class
//------------------------------------------------------

BinarySearchTree::~BinarySearchTree()
{
	BinarySearchTree *leftTree;
	BinarySearchTree *rightTree;

	leftTree = new BinarySearchTree(top->left);
	rightTree = new BinarySearchTree(top->right);

	delete top;
	top = NULL;

	delete leftTree; //Will recursively delete its left and right contents
	leftTree = NULL;
	delete rightTree; //Will recursively delete its left and right contents
	rightTree = NULL;

}
