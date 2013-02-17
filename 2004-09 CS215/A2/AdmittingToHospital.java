//-----------------------------------------
// CLASS: AdmittingToHospital
//
// REMARKS: An event that occurs after Treatment
//			is complete.  Occurs for priority 1
//			patients who must be admitted.
//
// INPUT: Patient and time
//
// OUTPUT: The time spent in line waiting to be
//		   admitted and a departure object.
//
//-----------------------------------------

class AdmittingToHospital extends Event
{

	private final int DURATION = 3;

	public AdmittingToHospital(Patient newPatient, int newTime)
	{
		super(newPatient, newTime);
	}

//------------------------------------------------------
// process
//
// PURPOSE: Perform tasks related to AdmittingToHospital
// EXTERNAL REFERENCES: References to the Patient, PatientQueue,
//						and EventList classes.
//------------------------------------------------------

	public void process()
	{
		patient.addWait(time - 1); //Adds the time spend waiting for a nurse
		admit.leave(); //Leaves the admit PatientQueue


		//If there are still people waiting for a nurse

		if(!admit.isEmpty())
		{
			//Create another AdmittingToHospital event.

			events.enter(new AdmittingToHospital(admit.front(), time));
		}

		//Add departure event to EventList
		events.enter(new Departure(patient, time + DURATION));


		System.out.println(this.toString());
	}

	public String toString()
	{
		return (super.toString() + patient.getNumber() + " Priority " + patient.getPriority() + " is admitted to the hospital.");
	}

}