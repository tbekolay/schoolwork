#include <iostream>
#include <string>
#include "HuffmanTree.h"

using namespace std;

//------------------------------------------------------
// compareTo
//
// PURPOSE: Compares two HuffmanTrees, outputting a number based on which is bigger.
// PARAMETERS:  const HuffmanTree *input - The HuffmanTree to be compared against
//------------------------------------------------------

int HuffmanTree::compareTo( const HuffmanTree *input )
{

	int compare = 1;

	//Compare the frequencies of the top nodes of each
	if( this->top->frequency < input->top->frequency )
		compare = -1;
	//If they are the same, check the ASCII values of the word in the top
	else if( this->top->frequency == input->top->frequency )
	{
		if( this->top->data[0] < input->top->data[0] )
			compare = -1;
	}

	return compare;

}

//------------------------------------------------------
// merge
//
// PURPOSE: Merges the current HuffmanTree and the passed HuffmanTree
//			into one by adding a new root and linking the other two to
//			the new root.
// PARAMETERS:  const HuffmanTree *higher: The larger HuffmanTree to be merged.
// EXTERNAL REFERENCES:  Requires HuffNode class
//------------------------------------------------------

HuffmanTree *HuffmanTree::merge( const HuffmanTree *higher )
{

	HuffNode *newRoot; //The new root of the new tree
	int newFreq; //The new frequency
	int newSplit; //The new index that the word will be split
	string newData; //The new word

	newFreq = top->frequency + higher->top->frequency;
	newSplit = top->data.length();
	newData = top->data + higher->top->data;

	//Create a new root
	newRoot = new HuffNode(newFreq, newSplit, newData, top, higher->top);

	//Return a new tree with the new root
	return( new HuffmanTree( newRoot ) );

}

//------------------------------------------------------
// HuffmanTree
//
// PURPOSE: Default HuffmanTree constructor; self-explanatory
//------------------------------------------------------

HuffmanTree::HuffmanTree()
{
	top = NULL;
}

//------------------------------------------------------
// HuffmanTree
//
// PURPOSE: HuffmanTree constructor; self-explanatory
//------------------------------------------------------

HuffmanTree::HuffmanTree( const Frequency *input )
{
	top = new HuffNode( input->freq, 0, input->word, NULL, NULL );
}

//------------------------------------------------------
// HuffmanTree
//
// PURPOSE: HuffmanTree constructor; self-explanatory
//------------------------------------------------------

HuffmanTree::HuffmanTree( HuffNode *root )
{
	top = root;
}

//------------------------------------------------------
// ~HuffmanTree
//
// PURPOSE: HuffmanTree destructor. Ensures links and data do not persist.
//------------------------------------------------------

HuffmanTree::~HuffmanTree()
{
	HuffmanTree *leftTree;
	HuffmanTree *rightTree;

	leftTree = new HuffmanTree(top->left);
	rightTree = new HuffmanTree(top->right);

	delete top;
	top = NULL;

	delete leftTree; //Will recursively delete its left and right contents
	leftTree = NULL;
	delete rightTree; //Will recursively delete its left and right contents
	rightTree = NULL;
}
