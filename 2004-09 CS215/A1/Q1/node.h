//-----------------------------------------
// CLASS: Node
//
// REMARKS: A node class with an integer data field.
//
// INPUT: Integer values and Node links
//
// OUTPUT: Linked Node structure
//
//-----------------------------------------

class Node
{
	public:
		int value;
		Node *link;
		Node(int newValue, Node *newLink);
		~Node();

};
