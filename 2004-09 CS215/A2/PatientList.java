//-----------------------------------------
// CLASS: PatientList
//
// REMARKS: PatientList is an extension of LinkedList
//			that mimics a first come first serve queue.
//
// INPUT: Patient objects
//
// OUTPUT: Patient objects in a FIFO queue
//
//-----------------------------------------

class PatientList extends LinkedList
{

	public PatientList()
	{
		// Creates a dummy entry
		top = new Node(new Patient(-1,'W',-1), null);
	}

//------------------------------------------------------
// insert
//
// PURPOSE: inserts a patient into the PatientList, at the front
// PARAMETERS: Patient patient - the patient to add
// EXTERNAL REFERENCES:  References to Patient
//------------------------------------------------------

	public void insert(Patient patient)
	{

		super.insertFirst(patient);

	}

//------------------------------------------------------
// front
//
// PURPOSE: returns the last element in the PatientList.
// EXTERNAL REFERENCES:  References to Node, and Patient
//------------------------------------------------------

	public Patient front()
	{

		Node curr = top;
		Node prev = null;
		Patient next = null;

		while(curr.link != null)
		{
			prev = curr;
			curr = curr.link;
		}

		if(curr != top)
		{
			next = (Patient)curr.getData();
		}

		return next;

	}

//------------------------------------------------------
// delete
//
// PURPOSE: Deletes a Patient from the end of the queue.
// EXTERNAL REFERENCES:  Refernces to patient class.
//------------------------------------------------------

	public Patient delete()
	{
		return (Patient)super.deleteLast();
	}

//------------------------------------------------------
// printWaits
//
// PURPOSE: Prints out a table to patients and their wait times.
// EXTERNAL REFERENCES:  References to Node, and Patient.
//------------------------------------------------------

	public String printWaits()
	{

		int numPatients = 0;
		int totalWait = 0;
		Node curr = top.link;
		Patient next = null;
		String output = "\n";

		//Loop throught the PatientList, printing out and adding
		//the wait times.
		while(curr != null)
		{
			next = (Patient)curr.getData();
			numPatients++;
			totalWait += next.getWait();
			output += next.getNumber() + "\tWaited: " + next.getWait() + "\n";
			curr = curr.link;
		}

		//Output numPatients and the average wait time per patient.
		output += "\nNumber of Patients: " + numPatients + "\tAverage wait per patient: " + ((double)totalWait / (double)numPatients);

		return output;

	}

}