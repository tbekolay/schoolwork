//-----------------------------------------
// CLASS: Arrival
//
// REMARKS: An event that occurs for all patients
//			when they first walk in the door or are
//			brought to the door by ambulance.
//
// INPUT: Patient and time
//
// OUTPUT: An assessment or enterWaitingRoom object,
//		   depending on patient urgency
//
//-----------------------------------------

import java.io.IOException;

class Arrival extends Event
{

	public Arrival(Patient newPatient, int newTime)
	{
		super(newPatient, newTime);
	}

//------------------------------------------------------
// process
//
// PURPOSE: Perform the tasks required when one arrives at a hospital
// EXTERNAL REFERENCES: References to Patient, PatientList, PatientQueue,
//						EventList, and BufferedReader.
//------------------------------------------------------

	public void process()
	{

		boolean empty;
		Patient newPatient;
		String next;

		//Adds the patient to a PatientList, as all patients
		//must go through this method
		patients.insert(patient);

		//Walk-in patients are assessed or waiting to be assessed
		if(patient.getUrgency() == 'W')
		{
			empty = assessment.isEmpty();
			patient.mark(time); //mark the time entering the assessment PatientQueue
			assessment.insert(patient);

			if(empty)
			{
				events.enter(new Assessment(patient, time));
			}
		}

		//Emergency patients skip the assessment
		else //patient.getUrgency() == 'E'
		{
			patient.setPriority(1);
			events.enter(new EnterWaitingRoom(patient, time));
		}

		//Read in the next arrival and process it.
		try
		{
			next = Hospital.inFile.readLine();

			if(next != null)
			{
				newPatient = new Patient(next);
				events.enter(new Arrival(newPatient, newPatient.getArrival()));
			}
		}
		catch( IOException ex )
		{
			System.out.println( "I/O error: " + ex.getMessage() );
		}


		System.out.println(this.toString());

	}

	public String toString()
	{
		String urgent;

		if(patient.getUrgency() == 'E')
			urgent = "(emergency) Priority 1";
		else
			urgent = "(walk-in)";

		return (super.toString() + patient.getNumber() + " " + urgent + " arrives.");
	}

}