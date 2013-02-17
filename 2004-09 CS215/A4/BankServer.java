//-----------------------------------------
// NAME		: Trevor Bekolay
// STUDENT NUMBER	: 6796723
// COURSE		: 074.215
// SECTION		: L02
// INSTRUCTOR	: Ryan Wegner
// ASSIGNMENT	: 4
// QUESTION	: 1
//
// REMARKS: This is a bank server program, that manages
// 			customers and the accounts associated with each
//			customer.  It can create customers, add accounts
//			of various types, deposit and withdrawal from those
//			accounts, and search for customer or account records.
//
// INPUT:  Input is strictly structured: (C, A, W, D, S, F) + arguments
//
// OUTPUT: The program will output responses based on the
//		   requests; whether they be successful or unsuccessful.
//
//-----------------------------------------

import java.io.*;

public class BankServer
{

	public BankBranch branch; //In a true server, this would likely be a BranchList of branches

	public static void main(String[] args)
	{

		String inLine;
		BankServer server = new BankServer(); //Create a new instance of a server
		InputStreamReader standardIn;
		BufferedReader inStd;

		//Catch I/O exceptions
		try
		{

			standardIn = new InputStreamReader( System.in );
			inStd = new BufferedReader( standardIn );

			System.out.println("Assignment 4, 074.215 L02, Trevor Bekolay, 6796723.\nPlease enter a request!\n");

			//Get a line of input
			inLine = inStd.readLine();

			//"Q" will stop the program.  Otherwise, it will continue to accept input
			while( !inLine.equals("Q") )
			{
				//Echo input
				System.out.println( "Request = " + inLine );
				//Output a response
				System.out.println( "Response = " + server.HandleRequest(inLine) + "\n");
				//Continue reading in requests
				inLine = inStd.readLine();
			}

		}

		//Print an error for unparsable input.
		catch( IOException ex )
		{
			System.out.println( "Unparsable request!  Program exiting." );
		}

		System.out.println("End of processing.");
		System.exit(0);

	}


	public BankServer()
	{
		branch = new BankBranch();
	}

//------------------------------------------------------
// HandleRequest
//
// PURPOSE: Takes requests as input and returns responses to those requests
// PARAMETERS: String inLine - the request
// EXTERNAL REFERENCES: References to BankBranch
//------------------------------------------------------

	public String HandleRequest(String inLine)
	{

		int i; //Loop counter
		String input[]; //For parsing the request
		String output = ""; //The response

		input = inLine.split(","); //Split the request by ","

		//Trim the extra spaces off of each piece
		for( i=0; i<input.length; i++)
		{
			input[i] = input[i].trim();
		}

		//The operation is determined by the first letter, so
		//we use input[0] to see what operation we must do
		if(input[0].equals("C"))
		{
			//Check the number of arguments
			if(input.length == 3 && !input[1].equals("") && !input[2].equals(""))
			{
				//Call the addCustomer method
				output = branch.addCustomer(input[1], input[2]);
			}
			else
			{
				output = "Error - wrong number of arguments.";
			}
		}

		else if(input[0].equals("A"))
		{
			int custID;

			//Check the number of arguments
			if(input.length == 3)
			{
				//Try to parse some into integers; if it fails, return error
				try
				{
					custID = Integer.parseInt(input[2]);
					//Call branch's addAccount method
					output = branch.addAccount(input[1].charAt(0), custID);
				}
				catch(NumberFormatException ex)
				{
					output = "Error - an argument is not a number when it should be a number.";
				}

			}
			else
			{
				output = "Error - wrong number of arguments.";
			}
		}
		else if(input[0].equals("W"))
		{
			int custID;
			int branchID;
			int acctID;
			int amount;
			int temp;
			String temp1 = "";
			String temp2 = "";

			//Check the number of arguments
			if(input.length == 4)
			{
				//Split input[2] by "-"
				temp = input[2].indexOf("-");
				if(temp >= 0)
				{
					temp1 = input[2].substring(0,temp);
					temp2 = input[2].substring(temp+1,input[2].length());
				}

				//Try to parse some into integers; if it fails, return error
				try
				{
					custID = Integer.parseInt(input[1]);
					branchID = Integer.parseInt(temp1);
					acctID = Integer.parseInt(temp2);
					amount = Integer.parseInt(input[3]);
					//Call the withdrawal method
					output = this.withdrawal(custID, branchID, acctID, amount);
				}
				catch(NumberFormatException ex)
				{
					output = "Error - an argument is not a number when it should be a number.";
				}

			}
			else
			{
				output = "Error - wrong number of arguments.";
			}

		}
		else if(input[0].equals("D"))
		{
			int custID;
			int branchID;
			int acctID;
			int amount;
			int temp;
			String temp1 = "";
			String temp2 = "";

			//Check the number of arguments
			if(input.length == 4)
			{
				//Split input[2] by "-"
				temp = input[2].indexOf("-");
				if(temp >= 0)
				{
					temp1 = input[2].substring(0,temp);
					temp2 = input[2].substring(temp+1,input[2].length());
				}

				//Try to parse some into integers; if it fails, return error
				try
				{
					custID = Integer.parseInt(input[1]);
					branchID = Integer.parseInt(temp1);
					acctID = Integer.parseInt(temp2);
					amount = Integer.parseInt(input[3]);
					//Call the deposit method
					output = this.deposit(custID, branchID, acctID, amount);
				}
				catch(NumberFormatException ex)
				{
					output = "Error - an argument is not a number when it should be a number.";
				}

			}
			else
			{
				output = "Error - wrong number of arguments.";
			}
		}
		else if(input[0].equals("S"))
		{
			int custID;

			//Check the number of arguments
			//In this case, we call a different search method based on the number of arguments.
			if(input.length == 2)
			{

				//Try to parse some into integers; if it fails, return error
				try
				{
					custID = Integer.parseInt(input[1]);
					output = branch.search(custID);
				}
				catch(NumberFormatException ex)
				{
					output = "Error - an argument is not a number when it should be a number.";
				}
			}
			else if(input.length == 3)
			{

				//Try to parse some into integers; if it fails, return error
				try
				{
					custID = Integer.parseInt(input[1]);
					//If custID = 0, don't search based on it
					if(custID == 0)
					{
						output = branch.search(input[2]);
					}
					else
					{
						output = branch.search(custID, input[2]);
					}
				}
				catch(NumberFormatException ex)
				{
					output = "Error - an argument is not a number when it should be a number.";
				}
			}
			else
			{
				output = "Error - wrong number of arguments.";
			}

		}
		else if(input[0].equals("F"))
		{

			int custID;
			int branchID;
			int acctID;
			int temp;
			String temp1 = "";
			String temp2 = "";

			//Check the number of arguments
			if(input.length == 3)
			{
				//Split input[2] by "-"
				temp = input[2].indexOf("-");
				if(temp >= 0)
				{
					temp1 = input[2].substring(0,temp);
					temp2 = input[2].substring(temp+1,input[2].length());
				}

				//Try to parse some into integers; if it fails, return error
				try
				{
					custID = Integer.parseInt(input[1]);
					branchID = Integer.parseInt(temp1);
					acctID = Integer.parseInt(temp2);
					//Call the find method
					output = this.find(custID, branchID, acctID);
				}
				catch(NumberFormatException ex)
				{
					output = "Error - an argument is not a number when it should be a number.";
				}

			}
			else
			{
				output = "Error - wrong number of arguments.";
			}

		}
		else
		{
			output = "Error - that operation is not recognized.";
		}

	return output;

	}

//------------------------------------------------------
// withdrawal
//
// PURPOSE: Withdraws an amount from a particular Customer's Account
// PARAMETERS: int custID - the ID of the Customer
//			   int branchID - the ID of the branch the customer belongs to
//			   int acctID - the ID of the Account to withdraw from
//			   int amount - the amount to withdraw
// EXTERNAL REFERENCES: References to Customer, CustList
//------------------------------------------------------

	private String withdrawal(int custID, int branchID, int acctID, int amount)
	{
		String output;

		//Ensure the branch ID is correct
		if(branch.branchID == branchID)
			output = branch.withdrawal(custID, acctID, amount);
		else
			output = "Error - incorrect branch number.";

		return output;
	}

//------------------------------------------------------
// deposit
//
// PURPOSE: Deposits an amount to a particular Customer's Account
// PARAMETERS: int custID - the ID of the Customer
//			   int branchID - the ID of the branch that the customer belongs to
//			   int acctID - the ID of the Account to deposit to
//			   int amount - the amount to deposit
// EXTERNAL REFERENCES: References to Customer, CustList
//------------------------------------------------------

	private String deposit(int custID, int branchID, int acctID, int amount)
	{
		String output;

		//Ensure the branch ID is correct
		if(branch.branchID == branchID)
			output = branch.deposit(custID, acctID, amount);
		else
			output = "Error - incorrect branch number.";

		return output;

	}

//------------------------------------------------------
// find
//
// PURPOSE: Finds an Account with a specific ID
// PARAMETERS: int custID - the ID of the customer who the account belongs to
//			   int branchID - the ID of the branch to search in
//			   int acctID - the ID of the Account to find
// EXTERNAL REFERENCES: References to BankBranch
//------------------------------------------------------

	private String find(int custID, int branchID, int acctID)
	{
		String output;

		//Ensure the branch ID is correct
		if(branch.branchID == branchID)
			output = branch.find(custID, acctID);
		else
			output = "Error - incorrect branch number.";

		return output;

	}

}