//-----------------------------------------
// CLASS: TreatmentCompleted
//
// REMARKS: An event that takes place after StartTreatment.
//			A patient either leaves (begins Departure event)
//			or is admitted to the hopsital (begins AdmittingToHospital
//			event.)
//
// INPUT: Patient and time
//
// OUTPUT: The time spend in the treatment queue and a
//		   new event.
//
//-----------------------------------------

class TreatmentCompleted extends Event
{

	private final int DURATION = 1;


	public TreatmentCompleted(Patient newPatient, int newTime)
	{
		super(newPatient, newTime);
	}


//------------------------------------------------------
// process
//
// PURPOSE: Perform tasks related to the completion of treatment of a patient
// EXTERNAL REFERENCES:  References to Patient, EventList and PatientList
//------------------------------------------------------

	public void process()
	{

		boolean empty;

		//Priority 1 patients must be admited to the hospital
		if(patient.getPriority() == 1)
		{
			empty = admit.isEmpty();
			patient.mark(time);
			admit.enter(patient);

			if(empty)
			{
				events.enter(new AdmittingToHospital(patient, time + DURATION));
			}
		}

		//Other patients depart
		else
		{
			events.enter(new Departure(patient, time + DURATION));
		}


		System.out.println(this.toString());

	}

	public String toString()
	{
		return (super.toString() + patient.getNumber() + " Priority " + patient.getPriority() + " treatment completed.");
	}

}