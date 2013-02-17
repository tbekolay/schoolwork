//-----------------------------------------
// CLASS: Account
//
// REMARKS: Abstract class that contains information related
//			to general accounts that a customer would have.
//
// INPUT: The ID of the branch which the customer is associated with.
//
// OUTPUT: An account that can be associated with a customer.
//
//-----------------------------------------

abstract class Account
{

	private static int nextID = 1357992; //Next account ID
	private int branchID;
	public int acctID;
	protected long balance;

	public Account(int newBranchID)
	{
		branchID = newBranchID;
		acctID = nextID;
		nextID += 13; //Next account ID is 13 more than the last
		balance = 0;
	}

//------------------------------------------------------
// deposit
//
// PURPOSE: Increases the balance of an account by the passed value.
// PARAMETERS: int amount - the amount to add to the account
//------------------------------------------------------

	public boolean deposit(int amount)
	{
		boolean done = false;

		//Since you are allowed to deposit negative numbers--
		//which happens if the deposit doesn't cover fees,
		//we must make sure the ending balance is positive.
		if(balance + amount >= 0)
		{
			balance += amount;
			done = true;
		}

		return done;
	}

//------------------------------------------------------
// withdrawal
//
// PURPOSE: Decreases the balance of an account by the passed value
// PARAMETERS: int amount - the amount to be decreased from the account
//------------------------------------------------------

	public boolean withdrawal(int amount)
	{

		boolean done = false;

		//Ensure that we are not withdrawing more than is in the account
		if(balance - amount >= 0)
		{
			balance -= amount;
			done = true;
		}

		return done;

	}

//------------------------------------------------------
// getBalance
//
// PURPOSE: Accessor for the account balance
//------------------------------------------------------

	public long getBalance()
	{
		return balance;
	}

	public String toString()
	{
		return (branchID + "-" + acctID + ", " + balance);
	}

}