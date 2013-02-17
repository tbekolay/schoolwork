//-----------------------------------------
// CLASS: StartTreatment
//
// REMARKS: StartTreatment occurs when a treatment room
//			opens and a patient enters it.
//
// INPUT: Patient and time
//
// OUTPUT: A future TreatmentCompleted event.
//
//-----------------------------------------

class StartTreatment extends Event
{

	public StartTreatment(Patient newPatient, int newTime)
	{
		super(newPatient, newTime);
	}

//------------------------------------------------------
// process
//
// PURPOSE: Perform tasks related to beginning treatment of a patient
// EXTERNAL REFERENCES:  References to Patient and EventList
//------------------------------------------------------

	public void process()
	{

		//No longer in the treatment queue, so calculate time spent in it.
		patient.addWait(time);
		freeRooms--;
		events.enter(new TreatmentCompleted(patient, time + patient.getTreatment()));
		System.out.println(this.toString());

	}

	public String toString()
	{
		return (super.toString() + patient.getNumber() + " Priority " + patient.getPriority() + " enters treatment room. (" + freeRooms + " rooms still available)");
	}

}