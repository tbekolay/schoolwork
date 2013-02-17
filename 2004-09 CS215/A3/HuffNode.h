//-----------------------------------------
// CLASS: HuffNode
//
// REMARKS: A node class with various fields, that make up
//			a HuffmanTree.
//
// INPUT: Integer values, a string and Node links
//
// OUTPUT: Part of a HuffmanTree
//
//-----------------------------------------
#ifndef HNODE_H
#define HNODE_H

#include <string>

using namespace std;

class HuffNode
{
	friend class HuffmanTree;
	friend class FileHandler;

	private:
		int frequency;
		int split;
		string data;
		HuffNode *left;
		HuffNode *right;

	public:
		bool isLeafNode();
		HuffNode(const int newFreq, const int newSplit, const string newData, HuffNode *newLeft, HuffNode *newRight);
		~HuffNode();

};

#endif
