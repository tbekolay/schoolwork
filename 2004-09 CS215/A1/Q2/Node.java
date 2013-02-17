//-----------------------------------------
// CLASS: Node
//
// REMARKS: Abstract Node class defines how the Internal
//			and Leaf nodes will operate.
//
// INPUT: int numberOfEntries - Defined in Node's subclasses
//
// OUTPUT: None (abstract)
//
//-----------------------------------------

abstract class Node
{
	protected Entry[] entries;		// an array of key-value pair entries
	protected int totalEntries;		// total entries so far
	protected double fillFactor;

	abstract public Entry split();
	abstract public String search (String key);
	abstract public String search(Key key1, Key key2);
	abstract public void insert(Key key, Object obj);


//------------------------------------------------------
// Node
//
// PURPOSE: Default Node constructor
//------------------------------------------------------

	protected Node() {}

//------------------------------------------------------
// Node
//
// PURPOSE: Defines a node of length numberOfEntries with a dummy node.
// PARAMETERS:  int numberOfEntries - Number of Entries per node
// EXTERNAL REFERENCES:  Requires Entry class
//------------------------------------------------------

	protected Node(int numberOfEntries)
	{
		entries = new Entry[numberOfEntries];
		entries[0] = new Entry(new IntegerKey(Integer.MIN_VALUE), null);
		totalEntries = 1;
		fillFactor = 0.69;
	}
	protected Node(Object obj) {}

//------------------------------------------------------
// isFull
//
// PURPOSE: Returns true if the node has reached the fillFactor
// EXTERNAL REFERENCES:  Requires Entry class
//------------------------------------------------------

	public boolean isFull()
	{
		return( ((double)totalEntries / (double)entries.length) > fillFactor );
	}


//------------------------------------------------------
// createRootNode()
//
// PURPOSE: Creates a root node in a new Tree
// EXTERNAL REFERENCES:  Requires InternalNode and LeafNode subclasses
//------------------------------------------------------

	public static Node createRootNode()
	{
		// An empty tree consists of two nodes:
		// 1) an empty internal node, and 2) an empty child node.
		// An empty node means a node with a dummy entry only.

		Node root = new InternalNode();
		root.entries[0].setNext( new LeafNode() );
		return root;
	}

//------------------------------------------------------
// indexOfNotGreater
//
// PURPOSE: Returns the greatest index of the entries array that is
//			not greater than the key
// PARAMETERS:  Key key is the value to be tested.
// EXTERNAL REFERENCES:  Requires Key and Entry classes
//------------------------------------------------------

	public int indexOfNotGreater(Key key)
	{
		boolean keepGoing = true;
		int count = 0;

		//Go through the entry array until you get to one that is bigger than the key
		while(keepGoing && entries[count] != null)
		{
			if(key.compareTo(entries[count].getKey()) > 0)
				keepGoing = false;
			else
				count++;
		}

		return( count );
	}

//------------------------------------------------------
// addEntry
//
// PURPOSE: Inserts the passed value into its appropriate place in the list.
// PARAMETERS:  Entry entry - the new entry. int indexNotGreater - the location
//              in the Node
// EXTERNAL REFERENCES:  Requires Entry class
//------------------------------------------------------

	//adds entry at the right point in the entries array
	public void addEntry(Entry entry, int indexNotGreater)
	{
		int i;

		for( i = totalEntries - 1 ; i >= indexNotGreater ; i-- )
		{
			entries[i + 1] = entries[i];
		}

		entries[indexNotGreater] = entry;

		totalEntries++;
	}

}