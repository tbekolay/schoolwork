//-----------------------------------------
// CLASS: AcctList
//
// REMARKS: An extension of a linked list, geared
//			towards using Account objects instead of
//			general Objects.
//
// INPUT: Account objects
//
// OUTPUT: A structure to link account objects, and
//		   to search for a specific one.
//
//-----------------------------------------

class AcctList extends LinkedList
{

//------------------------------------------------------
// insert
//
// PURPOSE: Inserts an Account at the front of the AcctList
// PARAMETERS: Account acct - the Account to be added
// EXTERNAL REFERENCES: References to LinkedList, and Account
//------------------------------------------------------

	public void insert(Account acct)
	{
		//Call LinkedList's insertFrist method
		super.insertFirst(acct);
	}

//------------------------------------------------------
// find
//
// PURPOSE: Returns the Account object with the passed acctID
// PARAMETERS: int acctID - the ID of the Account to be found
// EXTERNAL REFERENCES: References to Node, Account
//------------------------------------------------------

	public Account find(int acctID)
	{
		Node curr = top.link;
		Account output = null;
		Account temp;
		boolean found = false;

		//Loop through the AcctList until we have found it, or reached the end.
		while(curr != null && !found)
		{
			temp = (Account)curr.getData();
			//Test if the current acctID matches the passed one.
			if(temp.acctID == acctID)
			{
				found = true; //Exits the loop on next iteration
				output = temp;
			}
			curr = curr.link;
		}

		return output;
	}

}