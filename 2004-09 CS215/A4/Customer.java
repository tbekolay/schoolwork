//-----------------------------------------
// CLASS: LinkedList
//
// REMARKS: An extension of a linked list, geared
//			towards using Customer objects instead of
//			general Objects.
//
// INPUT: Customer objects
//
// OUTPUT: A structure to link customer objects, and
//		   to search for a specific one, or many based on name.
//
//-----------------------------------------

class Customer
{

	private static int nextID = 2051300;
	public int custID;
	private String name;
	private String address;
	private AcctList accounts;

	public Customer(String newName, String newAddress)
	{
		custID = nextID;
		nextID += 21; //Next custID is 21 more than the last
		name = newName;
		address = newAddress;
		accounts = new AcctList();
	}

//------------------------------------------------------
// addAccount
//
// PURPOSE: Inserts an account of a certain type into the
//			acct list of the current customer.
// PARAMETERS: char type - the type of account
//			   int branchID - the branch ID the customer belongs to
// EXTERNAL REFERENCES: References to Account, and AcctList
//------------------------------------------------------

public String addAccount(char type, int branchID)
	{

		String output;
		Account acct;

		//Create the account based on the type passed.
		if(type == 'S')
		{
			acct = new Savings(branchID);
			accounts.insert( acct );
			output = acct.toString();
		}
		else if(type == 'C')
		{
			acct = new Chequing(branchID);
			accounts.insert( acct );
			output = acct.toString();
		}
		else if(type == 'L')
		{
			acct = new LockedInRetirement(branchID);
			accounts.insert( acct );
			output = acct.toString();
		}
		else if(type == 'M')
		{
			acct = new MutualFund(branchID);
			accounts.insert( acct );
			output = acct.toString();
		}
		else
		{
			output = "Error - Incorrect account type.";
		}

		return output;

	}

//------------------------------------------------------
// withdrawal
//
// PURPOSE: Withdraws from the balance of the account pointed to
//			by the acctID passed.
// PARAMETERS: int acctID - Identifies the account to withdraw from.
//			   int amount - The amount to withdraw.
// EXTERNAL REFERENCES: References to Account, AcctList
//------------------------------------------------------

	public String withdrawal(int acctID, int amount)
	{

		Account acct;
		String output = "Error - account does not exist."; //Set initial value

		acct = accounts.find(acctID); //Find the right account

		//If that account exists
		if(acct != null)
		{
			//Perform the withdrawal; if it was successful, return the toString of that account
			if( acct.withdrawal(amount) )
			{
				output = acct.toString();
			}
			//Otherwise print error message
			else
			{
				output = "Error - cannot withdrawal amount from account " + acctID + ".\nEnsure that you have enough 	funds in your account, and that you have access to withdrawal from it.";
			}
		}

		return output;

	}

//------------------------------------------------------
// deposit
//
// PURPOSE: Deposits into the balance of the account pointed to
//			by the acctID passed.
// PARAMETERS: int acctID - Identifies the account to deposit to.
//			   int amount - The amount to deposit.
// EXTERNAL REFERENCES: References to Account, AcctList
//------------------------------------------------------

	public String deposit(int acctID, int amount)
	{

		Account acct;
		String output = "Error - account not found."; //Set initial value

		acct = accounts.find(acctID); //Find the right account

		//If that account exists
		if(acct != null)
		{
			//Perform the deposit; if it was successful, return the toString of that account
			if( acct.deposit(amount) )
			{
				output = acct.toString();
			}
			//Otherwise print error message
			else
			{
				output = "Error - cannot deposit amount from account " + acctID + ".\nEnsure that you have enough funds to cover any fees that may apply.";
			}
		}

		return output;

	}

//------------------------------------------------------
// getName
//
// PURPOSE: Accessor to the name data field.
//------------------------------------------------------

	public String getName()
	{
		return name;
	}

//------------------------------------------------------
// find
//
// PURPOSE: Finds an account in an AcctList
// PARAMETERS: int acctID - ID of the account to be found
// EXTERNAL REFERENCES: References to AcctList
//------------------------------------------------------

	public Account find(int acctID)
	{
		return( accounts.find(acctID) );
	}

	public String toString()
	{
		String output;

		output = custID + ", " + name + ", " + address;
		output += accounts.toString();

		return output;
	}

}