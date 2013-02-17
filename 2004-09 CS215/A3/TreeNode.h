//-----------------------------------------
// CLASS: TreeNode
//
// REMARKS: A node class for use in a general BinarySearchTree,
//			with an integer value
//
// INPUT: Integer values and TreeNode links
//
// OUTPUT: Linked TreeNode structure for use in a BinarySearchTree
//
//-----------------------------------------
#ifndef TREENODE_H
#define TREENODE_H

using namespace std;

class TreeNode
{
	friend class BinarySearchTree;

	private:
		int value;
		TreeNode *left;
		TreeNode *right;

	public:
		virtual bool isLeafNode();
		TreeNode();
		TreeNode(int newValue, TreeNode *newLeft, TreeNode *newRight);
		virtual ~TreeNode();

};

#endif
