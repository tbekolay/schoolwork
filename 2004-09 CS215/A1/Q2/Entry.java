//-----------------------------------------
// CLASS: Entry
//
// REMARKS: Stores and returns keys and
//			objects, depending on where the entry
//			is in the tree.
//
// INPUT: A Key (IntegerKeys) and Objects (Either
//		  LeafNodes, InternalNodes or Students)
//
// OUTPUT: Can access data members
//
//-----------------------------------------

// a key-value pair entry for the node
class Entry
{

	private Key key;
	private Object next; // can point to internal nodes, leaf nodes, or data objects

//------------------------------------------------------
// Entry
//
// PURPOSE: Entry constructor
// PARAMETERS:  int newKey, Object newNext - The data members
// EXTERNAL REFERENCES:  Requires Key class
//------------------------------------------------------

	public Entry(Key newKey, Object newNext)
	{

		key = newKey;
		next = newNext;
	}

//------------------------------------------------------
// toString
//
// PURPOSE: Outputs a string that contains the information in the next object
//------------------------------------------------------

	public String toString()
	{
		return( next.toString() );
	}


//------------------------------------------------------
// setNext
//
// PURPOSE: Sets the next object in an Entry object
// PARAMETERS: Object obj - the object that will be bound to the Entry
//------------------------------------------------------
	public void setNext( Object obj )
	{
		next = obj;
	}

//------------------------------------------------------
// getNext
//
// PURPOSE: Returns the next object
//------------------------------------------------------

	public Object getNext()
	{
		return( next );
	}

//------------------------------------------------------
// getKey
//
// PURPOSE: Returns the key object
// EXTERNAL REFERENCES:  Requires Key class
//------------------------------------------------------

	public Key getKey()
	{
		return( key );
	}

}