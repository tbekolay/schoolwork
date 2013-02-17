//-----------------------------------------
// CLASS: IntegerKey
//
// REMARKS: IntegerKeys are part of the name-value pair entries.
//			Stores an int which is used to identify entries.
//
// INPUT: int newValue - the key value
//
// OUTPUT: The key value
//
//-----------------------------------------

class IntegerKey extends Key
{

	private int value;

//------------------------------------------------------
// IntegerKey
//
// PURPOSE: IntegerKey constructor
// PARAMETERS:  int newValue - the key value
//------------------------------------------------------

	public IntegerKey(int newValue)
	{
		value = newValue;
	}

//------------------------------------------------------
// compareTo
//
// PURPOSE: Compares the current IntegerKey to one passed to it.
//			Outputs 1 if it is bigger than the passed value,
//			0 if it is the same,
//			-1 if it is smaller.
// PARAMETERS:  Object obj - the IntegerKey to test against
//------------------------------------------------------

	public int compareTo(Object obj)
	{
		IntegerKey input = (IntegerKey)obj;
		int compare = 0;

		if(value < input.value)
			compare = 1;
		else if(value > input.value)
			compare = -1;

		return( compare );
	}

//------------------------------------------------------
// toString
//
// PURPOSE: Outputs the string representation of the key
//------------------------------------------------------

	public String toString()
	{
		return( "" + value );
	}

//------------------------------------------------------
// getValue
//
// PURPOSE: returns the key value as an integer
//------------------------------------------------------

	public int getValue()
	{
		return( value );
	}


}