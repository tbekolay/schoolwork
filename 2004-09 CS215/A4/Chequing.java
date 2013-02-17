//-----------------------------------------
// CLASS: Chequing
//
// REMARKS: A specific type of Account.  Has a
//			service charge to withdrawal.
//
// INPUT: The ID of the branch which the customer is associated with.
//
// OUTPUT: A chequing account that can be associated with a customer.
//
//-----------------------------------------

class Chequing extends Account
{

	private final static int INIT_CHARGE = 75;
	private int charge;

	public Chequing(int branchID)
	{
		super(branchID);
		charge = INIT_CHARGE;
	}

//------------------------------------------------------
// withdrawal
//
// PURPOSE: Withdraws a passed amount plus a service charge from the account
// PARAMETERS: int amount - the amount to be withdrawn
// EXTERNAL REFERENCES: References to Account
//------------------------------------------------------

	public boolean withdrawal(int amount)
	{
		return( super.withdrawal(amount + charge) );
	}

	public String toString()
	{
		return( "C, " + super.toString() + ", " + charge );
	}

}

