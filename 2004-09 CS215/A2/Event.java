//-----------------------------------------
// CLASS: Event
//
// REMARKS: Abstract class containing some generalities
//			about the other subclass events.  Also contains
//			most of the static methods each event will need.
//
// INPUT: Patient and time
//
// OUTPUT: The time of the event.
//
//-----------------------------------------

import java.util.Random;

abstract class Event
{

	//These are all needed by one or more specific event, thus
	//it is easiest to define them as static
	protected static PatientList patients = new PatientList();
	protected static Random x = new Random(1000);
	protected static PatientList assessment = new PatientList();
	protected static PatientQueue treatment = new PatientQueue();
	protected static int freeRooms = 3;
	protected static PatientQueue admit = new PatientQueue();
	protected static EventList events = new EventList();

	protected Patient patient;
	protected int time;

	protected Event(Patient newPatient, int newTime)
	{
		patient = newPatient;
		time = newTime;
	}

	public abstract void process();

//------------------------------------------------------
// firstArrival
//
// PURPOSE: A static method that handles the first arrival event
//			initiated by the main method.
// PARAMETERS: Patient patient - the first patient
// EXTERNAL REFERENCES:  References to EventList and Patient.
//------------------------------------------------------

	protected static void firstArrival(Patient patient)
	{
		events.enter(new Arrival(patient, patient.getArrival()));
	}

//------------------------------------------------------
// getTime
//
// PURPOSE: Returns the current time of the event.
//------------------------------------------------------

	public int getTime()
	{
		return time;
	}

	public String toString()
	{
		return ("Time " + time + ": ");
	}

}