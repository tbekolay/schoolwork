//-----------------------------------------
// CLASS: Student
//
// REMARKS: Storage object, contains 5 data fields
//
// INPUT: int studentNo, String newLast, String newFirst
//		  String newAddress, String newDate
//		  - the five data members of Student objects
//
// OUTPUT: The 5 data members, or selective ones
//
//-----------------------------------------

class Student
{
	private int studentNumber;
	private String lastName;
	private String firstName;
	private String address;
	private String date;

//------------------------------------------------------
// Student
//
// PURPOSE: Student constructor
// PARAMETERS:  int studentNo, String newLast, String newFirst
//		        String newAddress, String newDate - the five data members of Student objects
//------------------------------------------------------

	public Student(int studentNo, String newLast, String newFirst, String newAddress, String newDate)
	{
		studentNumber = studentNo;
		lastName = newLast;
		firstName = newFirst;
		address = newAddress;
		date = newDate;
	}

//------------------------------------------------------
// toString
//
// PURPOSE: Returns the string representation
//------------------------------------------------------

	public String toString()
	{
		return( studentNumber + ", " + lastName + ", " + firstName + ", " + address + ", " + date );
	}

//------------------------------------------------------
// getNames
//
// PURPOSE: Returns a student's first and last name
//------------------------------------------------------


	public String getNames()
	{
		return( lastName + ", " + firstName );
	}

//------------------------------------------------------
// checkAddress
//
// PURPOSE: Checks if a string ends with the street name passed to it
// PARAMETERS:  String street - the street name
//------------------------------------------------------

public boolean checkAddress(String street)
	{
		return( address.endsWith(street) );
	}

}