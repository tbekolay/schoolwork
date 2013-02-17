//-----------------------------------------
// CLASS: Key
//
// REMARKS: Abstract class that defines other Key classes
//
// INPUT: None, abstract
//
// OUTPUT: None
//
//-----------------------------------------

abstract class Key
{
	public Key() {}
	abstract public int compareTo(Object obj);
	abstract public String toString();
}