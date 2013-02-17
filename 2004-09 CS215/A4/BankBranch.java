//-----------------------------------------
// CLASS: BankBranch
//
// REMARKS: Contains the data and methods that a bank
//			branch would be responsible for, such as
//			a list of customers, a branch ID and methods
//			to add customers, add accounts to those customers,
//			and more.
//
// INPUT: Requests
//
// OUTPUT: Responses to the requests, be they errors
//		   or successes
//
//-----------------------------------------

class BankBranch
{

	private static int nextID = 2701;
	public int branchID;
	private CustList customers;

	public BankBranch()
	{
		branchID = nextID++; //Next branchID is one more than the last
		customers = new CustList();
	}

//------------------------------------------------------
// addCustomer
//
// PURPOSE: Adds a customer to the branch's CustList
// PARAMETERS: String name - the name of the new customer
//			   String address - the address of the new customer
// EXTERNAL REFERENCES: References to CustList, Customer
//------------------------------------------------------

	public String addCustomer(String name, String address)
	{
		return(customers.insert(name, address)).toString();
	}

//------------------------------------------------------
// addAccount
//
// PURPOSE: Inserts an account in a particular customer's AcctList
// PARAMETERS: char type - the type of account to add
//			   int custID - the ID of the customer who the account will be added to
// EXTERNAL REFERENCES: References to Customer, CustList
//------------------------------------------------------

	public String addAccount(char type, int custID)
	{
		Customer cust;
		String output;

		cust = customers.search(custID); //Find the correct Customer to add an account to

		//If that customer can be found
		if(cust != null)
			//Add the right type of account
			output = cust.addAccount(type, branchID).toString();
		else
			output = "Error - customer not found.";

		return output;
	}

//------------------------------------------------------
// withdrawal
//
// PURPOSE: Withdraws an amount from a particular Customer's Account
// PARAMETERS: int custID - the ID of the Customer
//			   int acctID - the ID of the Account to withdraw from
//			   int amount - the amount to withdraw
// EXTERNAL REFERENCES: References to Customer, CustList
//------------------------------------------------------

	public String withdrawal(int custID, int acctID, int amount)
	{
		Customer cust;
		String output;

		if(amount > 0)
		{
			cust = customers.search(custID);
			output = cust.withdrawal(acctID, amount);
		}
		else
			output = "Error - cannot withdrawal negative amounts.";

		return output;
	}

//------------------------------------------------------
// deposit
//
// PURPOSE: Deposits an amount to a particular Customer's Account
// PARAMETERS: int custID - the ID of the Customer
//			   int acctID - the ID of the Account to deposit to
//			   int amount - the amount to deposit
// EXTERNAL REFERENCES: References to Customer, CustList
//------------------------------------------------------

	public String deposit(int custID, int acctID, int amount)
	{
		Customer cust;
		String output;

		if(amount > 0)
		{
			cust = customers.search(custID);
			output = cust.deposit(acctID, amount);
		}
		else
			output = "Error - cannot deposit negative amounts.";

		return output;
	}

//------------------------------------------------------
// search
//
// PURPOSE: Searches for a customer with a specific ID and name
// PARAMETERS: int custID - the ID of the customer to find
//			   String name - the name of the customer to find
// EXTERNAL REFERENCES: References to Customer, CustList
//------------------------------------------------------

	public String search(int custID, String name)
	{
		Customer cust;
		String output = "Not found";

		cust = customers.search(custID);

		//If the custID can be found, make sure the name matches the passed name
		if(cust != null)
		{
			if(cust.getName().equals(name))
				output = cust.toString();
		}

		return output;
	}

//------------------------------------------------------
// search
//
// PURPOSE: Searches for a customer with a specific ID
// PARAMETERS: int custID - the ID of the customer to find
// EXTERNAL REFERENCES: References to Customer, CustList
//------------------------------------------------------

	public String search(int custID)
	{
		Customer cust;
		String output = "Not found";

		cust = customers.search(custID);

		//Make sure the custID can be found
		if(cust != null)
			output = cust.toString();

		//If custID is 0, give a different error message
		if(custID == 0)
			output = "Error - must provide a customer ID or name";

		return output;

	}

//------------------------------------------------------
// search
//
// PURPOSE: Searches for any customers with a particular name
// PARAMETERS:  String name - the name of the customer to find
// EXTERNAL REFERENCES: References to CustList
//------------------------------------------------------

	public String search(String name)
	{
		return customers.search(name);
	}

//------------------------------------------------------
// find
//
// PURPOSE: Finds an Account with a specific ID
// PARAMETERS: int custID - the ID of the customer who the account belongs to
//			   int acctID - the ID of the Account to find
// EXTERNAL REFERENCES: References to Customer, CustList, Account
//------------------------------------------------------

	public String find(int custID, int acctID)
	{

		Customer cust;
		Account acct;
		String output = "Not found";

		cust = customers.search(custID);

		//Make sure a customer was found with that ID
		if(cust != null)
		{
			acct = cust.find(acctID);

			//Make sure that account could be found
			if(acct != null)
				output = acct.toString();
		}

		return output;

	}

}