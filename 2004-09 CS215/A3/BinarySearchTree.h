//-----------------------------------------
// CLASS: BinarySearchTree
//
// REMARKS: A linked structure of TreeNodes such that each node
//			has two children; the left child smaller than itself
//			and the right child larger.
//
// INPUT: A root TreeNode
//
// OUTPUT: Linked BinarySearchTree structure as discussed
//
//-----------------------------------------
#ifndef BST_H
#define BST_H
#include "TreeNode.h"

using namespace std;

class BinarySearchTree
{

	private:
		TreeNode *top;

	public:
		virtual int compareTo( const BinarySearchTree *input );
		BinarySearchTree();
		BinarySearchTree(TreeNode *root);
		virtual ~BinarySearchTree();

};

#endif
