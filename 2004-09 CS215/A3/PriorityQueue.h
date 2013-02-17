//-----------------------------------------
// CLASS: PriorityQueue
//
// REMARKS: A PriorityQueue built on a linked list.
//			The smallest values leave first.
//
// INPUT: HuffmanTrees
//
// OUTPUT: PriorityQueue of HuffNodes ordered ascendingly
//
//-----------------------------------------

#ifndef PQUEUE_H
#define PQUEUE_H

#include "LinkedList.h"
#include "ListHNode.h"

using namespace std;

class HuffmanTree;

class PriorityQueue : private LinkedList
{
	private:
		ListHNode *top;

	public:
		bool isEmpty();
		bool isOneLeft();
		void enter(HuffmanTree *data);
		void deleteFirst();
		HuffmanTree *leave();
		PriorityQueue();
		~PriorityQueue();

};

#endif
