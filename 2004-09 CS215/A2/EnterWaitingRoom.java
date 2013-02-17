//-----------------------------------------
// CLASS: EnterWaitingRoom
//
// REMARKS: An event that occurs after the assessment,
//			or after arrival for emergency patients.
//			Begins another assessment if someone is waiting.
//			Enters the patient into the treatment queue.
//
// INPUT: Patient and time
//
// OUTPUT: Possibly a new Assement and/or StartTreatment object.
//
//-----------------------------------------

class EnterWaitingRoom extends Event
{

	public EnterWaitingRoom(Patient newPatient, int newTime)
	{
		super(newPatient, newTime);
	}

//------------------------------------------------------
// process
//
// PURPOSE: Perform tasks related to a patient entering the waiting room
// EXTERNAL REFERENCES: References to Patient, PatientList, EventList,
//						and PatientQueue.
//------------------------------------------------------

	public void process()
	{

		//Walk-in patients come to the waiting room from the assessment,
		//so there is now room for another patient to be assessed.
		if(patient.getUrgency() == 'W')
		{
			assessment.delete();
			if(!assessment.isEmpty())
			{
				events.enter(new Assessment(assessment.front(), time));
			}
		}

		//Enter the line for treatment and mark the time
		patient.mark(time);
		treatment.enter(patient);

		//If a room is free, start treatment
		if(freeRooms > 0)
		{
			treatment.next();
			events.enter(new StartTreatment(patient, time));
		}

		System.out.println(this.toString());


	}

	public String toString()
	{
		return (super.toString() + patient.getNumber() + " Priority " + patient.getPriority() + " enters the waiting room.");
	}

}