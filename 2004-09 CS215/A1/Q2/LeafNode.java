//-----------------------------------------
// CLASS: LeafNode
//
// REMARKS: LeafNodes contain key values and corresponding
//			Student objects
//
// INPUT: None, all leafnodes are created equal
//
// OUTPUT: Has methods to return various Student records.
//
//-----------------------------------------

class LeafNode extends Node
{
	private static int numberOfEntries = 8;
	private Node leftNode;		// left node at the same level
	private Node rightNode;		// right node at the same level

//------------------------------------------------------
// LeafNode
//
// PURPOSE: LeafNode constructor.
//------------------------------------------------------

	public LeafNode()
	{
		entries = new Entry[numberOfEntries];
		totalEntries = 0;
		fillFactor = 0.69;
	}

//------------------------------------------------------
// split
//
// PURPOSE: Splits the current Node, returning the Entry that will go in
//			the internal node
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

	public Entry split()
	{
		int i;
		int count = 0;
		int total = totalEntries;
		int split;
		LeafNode newNode = new LeafNode();

		//Split down the middle (-1 as no dummy node)
		split = (totalEntries / 2) - 1;

		//Copy the entries from split to total into the new node
		//Delete them from the first node
		for( i = split; i < total; i++ )
		{
			newNode.addEntry(entries[i], count++);
			entries[i] = null;
			totalEntries--;
		}

		//Link the nodes together
		this.rightNode = newNode;
		newNode.leftNode = this;

		return( new Entry(newNode.entries[0].getKey(), newNode) );
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

		int i;
		Student curr;

		String output = "";

		//Move through the leafnode, check each entry's address and add those that match
		for( i = 0; i < totalEntries; i++ )
		{

			curr = (Student)entries[i].getNext();
			if( curr.checkAddress(key) )
				output.concat(curr.getNames());
		}

		//Continue traversing the linked list of leafnodes, if possible.
		if( rightNode != null )
			return( output.concat(rightNode.search(key)) );
		else
			return( output );

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


		int i = 0;

		boolean found = false;
		String output = "";

		//Traverse the node until you are at or after key1
		while( !found && i < totalEntries )
		{
			if(key1.compareTo(entries[i].getKey()) >= 0)
				found = true;
			else
				i++;
		}

		found = false;

		//Begin collecting data
		while( !found && i < totalEntries )
		{

			output.concat(entries[i].getNext().toString());

			if(key1.compareTo(entries[i].getKey()) == 0)
				found = true;
			else
				i++;
		}

		//If you hit key2, output what you have
		if( found )
			return( output );
		//If you haven't hit key2, and there are more nodes, search them
		else if( rightNode != null )
			return( output.concat(rightNode.search(key1, key2)) );
		//If you haven't hit key2 but you've hit the end, output what you have
		else
			return( output );

	}

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts the passed value into its appropriate place in the list.
// PARAMETERS:  Key key - the key which identifies the object and its place in the tree.
//				Object obj - the object which we want to insert
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------


	public void insert(Key key, Object obj)
	{
		addEntry(new Entry(key, obj), indexOfNotGreater(key));
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

		//Collect the toString methods of all entries in the node
		while( i < totalEntries && entries[i] != null)
			output.concat(entries[i++].toString());

		return output;
	}
}