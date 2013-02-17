//-----------------------------------------
// CLASS: Departure
//
// REMARKS: Final event, lets another person into a
//			now free treatment room.
//
// INPUT: Patient, time
//
// OUTPUT: New StartTreatment event
//
//-----------------------------------------

class Departure extends Event
{

	public Departure(Patient newPatient, int newTime)
	{
		super(newPatient, newTime);
	}

//------------------------------------------------------
// process
//
// PURPOSE: Perform tasks related to the departure of a patient.
// EXTERNAL REFERENCES: Refernces to PatientQueue, and Event List
//------------------------------------------------------

	public void process()
	{

		//Patient has left a room, so one more is free
		freeRooms++;

		//If there is someone in line for the room, they may begin treatment.
		if(!treatment.isEmpty())
		{
			events.enter(new StartTreatment(treatment.next(), time));
		}

		System.out.println(this.toString());


	}

	public String toString()
	{
		return (super.toString() + patient.getNumber() + " Priority " + patient.getPriority() + " departs. (" + freeRooms + " rooms still available)");
	}

}