//-----------------------------------------
// CLASS: Assessment
//
// REMARKS: An event that takes place after arrival
//			for non-emergency patients.  Basic filling
//			out of information, takes 4 time units.
//
// INPUT: Patient and time
//
// OUTPUT: Time spent in line to be assessed and an
//		   EnterWaitingRoom object
//
//-----------------------------------------

class Assessment extends Event
{

	private final int DURATION = 4;

	public Assessment(Patient newPatient, int newTime)
	{
		super(newPatient, newTime);
	}


//------------------------------------------------------
// process
//
// PURPOSE: Perform tasks related to the assessment of a patient
// EXTERNAL REFERENCES:  References to Patient, and EventList.
//------------------------------------------------------

	public void process()
	{
		//Randomly set the priority
		patient.setPriority(x.nextInt(5)+1);

		patient.addWait(time); //Record the time, as the patient will be leaving the queue
		events.enter(new EnterWaitingRoom(patient, time + DURATION));

		System.out.println(this.toString());
	}

	public String toString()
	{
		return (super.toString() + patient.getNumber() + " Priority " + patient.getPriority() + " begins assessment.");
	}

}