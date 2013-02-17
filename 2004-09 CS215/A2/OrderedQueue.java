//-----------------------------------------
// CLASS: OrderedQueue
//
// REMARKS: A specific LinkedList in which Objects
//			enter in an ordered fashion and Objects
//			are taken off from the front.
//
// INPUT: Objects
//
// OUTPUT: The Objects in a specific structure
//
//-----------------------------------------

abstract class OrderedQueue extends LinkedList
{

//------------------------------------------------------
// OrderedQueue
//
// PURPOSE: OrderedQueue constructor, sets a dummy node.
// EXTERNAL REFERENCES:  References to Node
//------------------------------------------------------

	public OrderedQueue()
	{

		//Creates a dummy node
		top = new Node(new Integer(-1), null);

	}


//------------------------------------------------------
// enter
//
// PURPOSE: Inserts the passed value into the OrderedQueue.
//			Position in the queue is based on the value.
// PARAMETERS:  Object data - The data to be inserted into the list.
// EXTERNAL REFERENCES:  References to Node and LinkedList classes
//------------------------------------------------------

	public void enter(Object data)
	{

		//Calls the linked list's ordered queue method
		super.insertOrdered(data);

	}

//------------------------------------------------------
// top
//
// PURPOSE: returns the next object that will leave
//------------------------------------------------------

	public Object top()
	{
		return top.link.getData();
	}



//------------------------------------------------------
// leave
//
// PURPOSE: Returns the current largest value, and deletes it from
//			the queue.
// EXTERNAL REFERENCES:  References to Node and LinkedList classes.
//------------------------------------------------------

	public Object leave()
	{
		return super.deleteFirst();
	}

}