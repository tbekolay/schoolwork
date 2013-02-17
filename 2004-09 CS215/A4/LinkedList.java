//-----------------------------------------
// CLASS: LinkedList
//
// REMARKS: General linked list structure, with facilities
//			to insert from the front and in an ordered fashion,
//			and deletion from the front or back.
//
// INPUT: Objects
//
// OUTPUT: A structure to insert Objects
//
//-----------------------------------------

abstract class LinkedList
{

	protected Node top;

	public LinkedList()
	{

		// Creates a dummy entry
		top = new Node(null, null);

	}

//------------------------------------------------------
// insertFirst
//
// PURPOSE: Inserts a node at the front of the list.
// PARAMETERS: Object data - the object to be added
// EXTERNAL REFERENCES: References to Node
//------------------------------------------------------

	public void insertFirst(Object data)
	{
		top.link = new Node(data,top.link);
	}

	public String toString()
	{

		Node curr = top.link;
		String output = "";

		while(curr != null)
		{
			output += ", " + curr.toString();
			curr = curr.link;
		}

		return output;

	}

}
