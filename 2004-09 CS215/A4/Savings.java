//-----------------------------------------
// CLASS: Savings
//
// REMARKS: A specific type of Account. Has no special characteristics.
//
// INPUT: The ID of the branch which the customer is associated with.
//
// OUTPUT: A Savings account that can be associated with a customer.
//
//-----------------------------------------

class Savings extends Account
{

	public Savings(int branchID)
	{
		super(branchID);
	}

	public String toString()
	{
		return("S, " + super.toString() );
	}

}