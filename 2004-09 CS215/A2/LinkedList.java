//-----------------------------------------
// CLASS: LinkedList
//
// REMARKS: General linked list structure, with facilities
//			to insert from the front and in an ordered fashion,
//			and deletion from the front or back.
//
// INPUT: Objects
//
// OUTPUT: A structure to manipulate the order of Objects
//
//-----------------------------------------

abstract class LinkedList
{

	protected Node top;

	public LinkedList()
	{

		// Creates a dummy entry
		top = new Node(new Integer(-1), null);

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

//------------------------------------------------------
// deleteLast
//
// PURPOSE: Deletes the node at the back of the list
// EXTERNAL REFERENCES:  References to Node
//------------------------------------------------------

	public Object deleteLast()
	{

		Node curr = top;
		Node prev = null;
		Object deleted = null;

		while(curr.link != null)
		{
			prev = curr;
			curr = curr.link;
		}

		if(curr != top)
		{
			deleted = curr.getData();
			prev.link = null;
		}

		return deleted;

	}

//------------------------------------------------------
// insertOrdered
//
// PURPOSE: Inserts the passed value into its appropriate place in the list.
// PARAMETERS:  Object data -  The data to be inserted into the list.
// EXTERNAL REFERENCES:  Refernces to Node
//------------------------------------------------------

	public void insertOrdered(Object data)
	{

		Node next = top.link;
		Node prev = top;

		// Traverse the list looking for the correct position
		while(next != null && next.data.toString().compareTo(data.toString()) < 0 )
		{
			prev = next;
			next = next.link;
		}

		// Insert the data at that position
		prev.link = new Node(data, next);

	}

//------------------------------------------------------
// deleteFirst
//
// PURPOSE: Deletes the first entry in the linked list, aside from the dummy.
// EXTERNAL REFERENCES:  References to Node
//------------------------------------------------------

	public Object deleteFirst()
	{

		Object deleted = null;

		// If there is a non-dummy node, delete it
		if(top.link != null)
		{
			deleted = top.link.getData();
			top.link = top.link.link;
		}

		return deleted;

	}

//------------------------------------------------------
// isEmpty
//
// PURPOSE: Determines if the LinkedList is empty or not
//------------------------------------------------------

	public boolean isEmpty()
	{
		return(top.link == null);
	}

	public String toString()
	{

		Node curr = top.link;
		String output = "";

		while(curr != null)
		{
			output += curr.toString();
			curr = curr.link;
		}

		return output;

	}

}