//-----------------------------------------
// CLASS: HuffmanTree
//
// REMARKS: A linked collection of HuffNodes, using the same form
//			as a BinarySearchTree.
//
// INPUT: Frequency data, or a HuffNode
//
// OUTPUT: Linked HuffNode structure in BinarySearchTree form.
//
//-----------------------------------------
#ifndef HTREE_H
#define HTREE_H

#include "HuffNode.h"
#include "BinarySearchTree.h"
#include "Frequency.h"

class HuffmanTree : private BinarySearchTree
{

	friend class FileHandler;

	private:
		HuffNode *top;

	public:
		int compareTo( const HuffmanTree *input );
		HuffmanTree *merge( const HuffmanTree *higher );
		HuffmanTree();
		HuffmanTree( const Frequency *input );
		HuffmanTree( HuffNode *root );
		~HuffmanTree();

};

#endif
