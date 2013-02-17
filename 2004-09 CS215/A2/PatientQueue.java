//-----------------------------------------
// CLASS: PatientQueue
//
// REMARKS: PatientQueue holds patient objects like
//			PatientLits, but the enter method works
//			slightly different, being ordered by priority
//			instead of time.
//
// INPUT: Patient objects
//
// OUTPUT: Patient objects structured by priority.
//
//-----------------------------------------

class PatientQueue extends OrderedQueue
{

	public PatientQueue()
	{
		//Creates a dummy node
		top = new Node(new Patient(-1,'W',-1), null);
	}

//------------------------------------------------------
// enter
//
// PURPOSE: Inserts a patient in order in the queue
// PARAMETERS: Patient patient - the patient to be added
// EXTERNAL REFERENCES: Refences to Node and Patient
//------------------------------------------------------

	public void enter(Patient patient)
	{

		Node next = top.link;
		Patient curr = null;
		Node prev = top;

		if(next != null)
			curr = (Patient)next.getData();

		// Traverse the list looking for the correct position
		while( next != null && curr.getPriority() < patient.getPriority() )
		{
			prev = next;
			next = next.link;
			if(next != null)
				curr = (Patient)next.getData();

		}

		// Traverse within those with the same priority as patient looking
		// for the correct position.
		while( next != null && curr.getPriority() == patient.getPriority() && curr.getArrival() > patient.getArrival() )
		{
			prev = next;
			next = next.link;
			if(next != null)
				curr = (Patient)next.getData();
		}

		// Insert the data at that position
		prev.link = new Node(patient, next);

	}

//------------------------------------------------------
// front
//
// PURPOSE: returns the first element in the list
// EXTERNAL REFERENCES: Refernces Patient
//------------------------------------------------------

	public Patient front()
	{
		return (Patient)super.top();
	}

//------------------------------------------------------
// next
//
// PURPOSE: Returns the first element in the list and deletes it
// EXTERNAL REFERENCES: References Patient
//------------------------------------------------------

	public Patient next()
	{
		return (Patient)super.leave();
	}

}