//-----------------------------------------
// CLASS: Patient
//
// REMARKS: Patient contains the structure to hold
//			information about a patient.
//
// INPUT: String input from a file
//
// OUTPUT: Patient objects
//
//-----------------------------------------

class Patient
{

	private static int nextNumber = 28064212;

	private int patientNumber;
	private int arrival;
	private char urgency;
	private int treatment;
	private int priority;
	private int temp;

	private int waitTime = 0;

	public Patient(String input)
	{

		patientNumber = nextNumber++;

		String[] pieces = new String[3];

		pieces = input.split(" ");

		arrival = Integer.parseInt(pieces[0]);
		urgency = pieces[1].charAt(0);
		treatment = Integer.parseInt(pieces[2]);

	}

	protected Patient(int newArrival, char newUrgency, int newTreat)
	{
		arrival = newArrival;
		urgency = newUrgency;
		treatment = newTreat;
	}

//------------------------------------------------------
// mark
//
// PURPOSE: Marks a point in time which will be compared with
//			another point in time to determine wait times
// PARAMETERS: int time - the time to mark
//------------------------------------------------------


	public void mark(int time)
	{
		//marks a time to calculate the last wait off of
		temp = time;
	}

//------------------------------------------------------
// addWait
//
// PURPOSE: Takes in a time value and subtracts the marked time
//			to give a wait time.
// PARAMETERS:int time - the end of the waiting
//------------------------------------------------------

	public void addWait(int time)
	{
		waitTime = waitTime + time - temp;
	}

//------------------------------------------------------
// getWait
//
// PURPOSE: Returns the total wait time
//------------------------------------------------------

	public int getWait()
	{
		return waitTime;
	}

//------------------------------------------------------
// setPriority
//
// PURPOSE: sets the priority to a passed number
// PARAMETERS: int newPriority - the new Priority
//------------------------------------------------------

	protected void setPriority(int newPriority)
	{
		priority = newPriority;
	}

//------------------------------------------------------
// getPriority
//
// PURPOSE: returns the priority value
//------------------------------------------------------

	protected int getPriority()
	{
		return priority;
	}

//------------------------------------------------------
// getNumber
//
// PURPOSE: returns the Patient Number
//------------------------------------------------------

	protected int getNumber()
	{
		return patientNumber;
	}

//------------------------------------------------------
// getArrival
//
// PURPOSE: returns the arrival time
//------------------------------------------------------

	protected int getArrival()
	{
		return arrival;
	}

//------------------------------------------------------
// getUrgency
//
// PURPOSE: returns the urgency code
//------------------------------------------------------

	protected char getUrgency()
	{
		return urgency;
	}

//------------------------------------------------------
// getTreatment
//
// PURPOSE: returns the length of treatment time
//------------------------------------------------------

	protected int getTreatment()
	{
		return treatment;
	}

	public String toString()
	{

		return("Patient: " + patientNumber + ", Arrived " + arrival + ", " + urgency + ", Treatment time: " + treatment + "\n");

	}

}