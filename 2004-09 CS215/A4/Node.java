//-----------------------------------------
// CLASS: Node
//
// REMARKS: Nodes make up the structure of LinkedLists
//			by providing information as well as a link
//			to the next Node.
//
// INPUT: An object, and the next node
//
// OUTPUT: A link to the object and a link to the next Node.
//
//-----------------------------------------

class Node
{

	private Object data;
	protected Node link;

	public Node(Object newData, Node newLink)
	{
		data = newData;
		link = newLink;
	}

//------------------------------------------------------
// getData
//
// PURPOSE: Returns the object that is part of it
//------------------------------------------------------

	public Object getData()
	{
		return data;
	}

	public String toString()
	{
		return data.toString();
	}

}