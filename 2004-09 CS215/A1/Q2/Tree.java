//-----------------------------------------
// CLASS: Tree
//
// REMARKS: The structure of the BPlus tree.
//			Mainly creates the root and interacts with it.
//
// INPUT: None, all trees are the same when created.
//		  Accepts keys and Student objects when inserting
//
// OUTPUT: Outputs strings based on use input
//
//-----------------------------------------

class Tree
{
	private Node root;

//------------------------------------------------------
// Tree
//
// PURPOSE: Tree constructor
// EXTERNAL REFERENCES:  Requires Node class
//------------------------------------------------------

	public Tree()
	{

		root = Node.createRootNode();
	}

//------------------------------------------------------
// search
//
// PURPOSE: Finds people in the tree that live on a particular street
// PARAMETERS:  String key - the street name
//------------------------------------------------------

	//returns whether key is in the tree

	public String search(String key)
	{
		return( root.search(key) );
	}

//------------------------------------------------------
// search
//
// PURPOSE: Find students that are between two certain key values.
// PARAMETERS:  Key key1, Key key2 - the two values
//------------------------------------------------------

	//returns toString values between key1 and key2
	public String search(Key key1, Key key2)
	{
		return( root.search(key1, key2) );
	}

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts the passed value into its appropriate place in the list.
// PARAMETERS:  Key key - the key which identifies the object and its place in the tree.
//				Object obj - the object which we want to insert
// EXTERNAL REFERENCES:  Requires Node and InternalNode classes
//------------------------------------------------------

	//inserts a student object based on key
	public void insert(Key key, Object obj)
	{


		InternalNode newRoot;

		root.insert(key, obj);

		//If root node is full, split it and create a new root
		if( root.isFull() )
		{
			newRoot = new InternalNode( root );
			newRoot.addEntry( root.split(), 0 );
			root = newRoot;
		}

	}

//------------------------------------------------------
// toString
//
// PURPOSE: Returns the string representation
//------------------------------------------------------


	//Prints out the content of the tree
	public String toString()
	{

		return( root.toString() );

	}

}