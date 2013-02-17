//-----------------------------------------
// CLASS: LockedInRetirement
//
// REMARKS: A specific type of Account.  Has a
//			fee to deposit; cannot withdrawal.
//
// INPUT: The ID of the branch which the customer is associated with.
//
// OUTPUT: A LockedInRetirement account that can be associated with a customer.
//
//-----------------------------------------

class LockedInRetirement extends Account
{

	private final int INIT_FEE = 200;
	private int managefee;

	public LockedInRetirement(int branchID)
	{
		super(branchID);
		managefee = INIT_FEE;
	}

//------------------------------------------------------
// deposit
//
// PURPOSE: Deposits the amount - management fee into the account
// PARAMETERS: int amount - the amount to be deposited
// EXTERNAL REFERENCES: References to Account
//------------------------------------------------------

	public boolean deposit(int amount)
	{
		//Must subtract the management fee from the amount being deposited
		return (super.deposit(amount - managefee));
	}

//------------------------------------------------------
// withdrawal
//
// PURPOSE: Assures that one cannot withdraw from LockedInRetirement account
// PARAMETERS: int amount - not used, but must be there for polymorphism.
//------------------------------------------------------

	public boolean withdrawal(int amount)
	{
		//This is an illegal operation in LockedInRetirement accounts
		return false;
	}

	public String toString()
	{
		return( "L, " + super.toString() + ", " + managefee );
	}

}

