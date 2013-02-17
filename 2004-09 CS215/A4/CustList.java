//-----------------------------------------
// CLASS: Cust
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

class CustList extends LinkedList
{

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts a Customer object at the front of the CustList.
// PARAMETERS: Customer cust - the Customer to be added
// EXTERNAL REFERENCES: References to LinkedList, Customer
//------------------------------------------------------

	public void insert(Customer cust)
	{
		//Call LinkedList's insertFirst method
		super.insertFirst(cust);
	}

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts a Customer object at the front of the CustList.
// PARAMETERS: String name - the name of the Customer to be added
//			   String address - the address of the Customer to be added
// EXTERNAL REFERENCES: References to LinkedList, Customer
//------------------------------------------------------

	public Customer insert(String name, String address)
	{
		Customer ins = new Customer(name, address); //Create a new Customer object
		insert( ins ); //Add it to the CustList
		return ins; //Return it
	}

//------------------------------------------------------
// search
//
// PURPOSE: Returns a string with all of the Customers with the
//			name to be searched for.
// PARAMETERS: String customer - the name to be searched for
// EXTERNAL REFERENCES: References to Node, Customer
//------------------------------------------------------

	public String search(String customer)
	{
		Node curr = top.link;
		String output = "";
		Customer temp;

		//Loop through the whole CustList
		while(curr != null)
		{
			temp = (Customer)curr.getData();
			//Test the current Customer
			if(customer.equals(temp.getName()))
			{
				//If it matches, add it to the string, with a ":" at the end
				output += temp.toString() + ":";
			}
			curr = curr.link;
		}

		//If we found at least one match
		if(!output.equals(""))
			//Take off the ":" at the very end
			output = output.substring(0, output.length() - 1);
		else
			//If we found no matches, say so
			output = "Not found";

		return output;
	}

//------------------------------------------------------
// search
//
// PURPOSE: Returns the Customer with the passed custID
// PARAMETERS: int custID - the ID of the Customer to search for
// EXTERNAL REFERENCES: References to Node, Customer
//------------------------------------------------------

	public Customer search(int custID)
	{
		Node curr = top.link;
		Customer output = null;
		Customer temp;
		boolean found = false;

		//Loop through the CustList until we find it, or we are at the end
		while(curr != null && !found)
		{
			temp = (Customer)curr.getData();
			//Test if the current Customer matches the passed ID
			if(temp.custID == custID)
			{
				//If so, this will break out of the loop after this iteration.
				found = true;
				output = temp;
			}
			curr = curr.link;
		}

		return output;
	}

}