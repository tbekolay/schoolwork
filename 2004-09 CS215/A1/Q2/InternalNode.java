//-----------------------------------------
// CLASS: InternalNode
//
// REMARKS: Internal Nodes contain keys and pointers to either
//			other internal nodes or leaf nodes.
//
// INPUT: Can accept and object as input
//
// OUTPUT: Always contains a dummy node, which may point to
//		   a leaf node or internal node.
//
//-----------------------------------------

class InternalNode extends Node
{
	private static int numberOfEntries = 8;

//------------------------------------------------------
// InternalNode
//
// PURPOSE: Default InternalNode constructor
// EXTERNAL REFERENCES:  Requires superclass Node
//------------------------------------------------------

	public InternalNode()
	{
		super(numberOfEntries);
	}

//------------------------------------------------------
// InternalNode
//
// PURPOSE: Overloaded InternalNode constructor.
// PARAMETERS:  Object obj - What the root's dummy node will point to
// EXTERNAL REFERENCES:  Requires supclass Node
//------------------------------------------------------

	public InternalNode(Object obj)
	{
		super(numberOfEntries);
		entries[0].setNext(obj);
	}

//------------------------------------------------------
// split
//
// PURPOSE: Splits the current Node, returning the Entry that will go in
//			the internal node
// EXTERNAL REFERENCES:  Requires Entry class
//------------------------------------------------------

	public Entry split()
	{

		int i;
		int count = 0;
		int total = totalEntries;
		int split;
		Entry output;
		InternalNode newNode = new InternalNode();

		//Split at the middle
		split = totalEntries / 2;
		newNode.entries[0].setNext(entries[split].getNext());

		//Runs from split +1 to the end of the first internal node
		//Copies the last half of the entries to a new internal node
		for( i = split + 1; i < total; i++ )
		{
			newNode.addEntry(entries[i], count++);
			entries[i] = null;
			totalEntries--;
		}

		output = new Entry(entries[split].getKey(), newNode);
		entries[split] = null;
		totalEntries--;

		return( output );

	}

//------------------------------------------------------
// toString
//
// PURPOSE: Returns the String representation of the node.
//------------------------------------------------------

	public String toString()
	{

		String output = "";

		int i = 0;

		//Adds to the string the entries in the node.
		while( i < totalEntries && entries[i] != null)
			output.concat(entries[i++].toString());

		return( output );

	}

//------------------------------------------------------
// search
//
// PURPOSE: Finds people in the tree that live on a particular street
// PARAMETERS:  String key - the street name
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

	public String search(String key)
	{

		Node temp;

		temp = (Node)entries[0].getNext();

		return( temp.search(key) );

	}

//------------------------------------------------------
// search
//
// PURPOSE: Find students that are between two certain key values.
// PARAMETERS:  Key key1, Key key2 - the two values
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

	public String search(Key key1, Key key2)
	{

		Node temp;

		temp = (Node)entries[indexOfNotGreater(key1)].getNext();

		return( temp.search(key1, key2) );

	}

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts the passed object into its appropriate place in the list.
// PARAMETERS:  Key key - the key which identifies the object and its place in the tree.
//				Object obj - the object which we want to insert
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

	public void insert(Key key, Object obj)
	{

		Node next;
		int index;

		index = indexOfNotGreater(key);

		//Get the next node from the array at the correct index
		next = (Node)entries[index - 1].getNext();
		next.insert( key, obj );

		//If the next node is full after insertion, split it
		if( next.isFull() )
			addEntry(next.split(), index);

	}

}