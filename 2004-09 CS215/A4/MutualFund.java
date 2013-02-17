//-----------------------------------------
// CLASS: MutualFund
//
// REMARKS: A specific type of Account.  Balance is in
//			terms of 1/1000th's of a share, and shares
//			have a price associated with them.
//
// INPUT: The ID of the branch which the customer is associated with.
//
// OUTPUT: A MutualFund account that can be associated with a customer.
//
//-----------------------------------------

class MutualFund extends Account
{

	private final int INIT_PRICE = 1329;
	private int shareprice;

	public MutualFund(int branchID)
	{
		super(branchID);
		shareprice = INIT_PRICE;
	}

//------------------------------------------------------
// deposit
//
// PURPOSE: Deposits the amount into the Account
// PARAMETERS: int amount - the amount (in cents) to deposit
// EXTERNAL REFERENCES: References to Account
//------------------------------------------------------

	public boolean deposit(int amount)
	{
		//Convert cents to 1/1000th shares:
		return super.deposit(amount * 1000 / shareprice);
	}

//------------------------------------------------------
// withdrawal
//
// PURPOSE: Withdraws the amount from the Account
// PARAMETERS: int amount - the amount (in cents) to withdraw
// EXTERNAL REFERENCES: References to Account
//------------------------------------------------------

	public boolean withdrawal(int amount)
	{
		//Convert cents to 1/1000th shares:
		return super.withdrawal(amount * 1000 / shareprice);
	}

	public String toString()
	{
		return( "M, " + super.toString() + ", " + ( (getBalance() / 1000) * shareprice ) + ", " + shareprice );
	}

}