//-----------------------------------------
// CLASS: EventList
//
// REMARKS: A specific OrderedQueue that handles
//			the list of events.
//
// INPUT: Events
//
// OUTPUT: Events in order based on time, then patient number.
//
//-----------------------------------------

class EventList extends OrderedQueue
{

	public EventList()
	{

		//Creates a dummy node
		top = new Node(new Arrival(new Patient(-1,'W',-1), -1), null); //As event is abstract

	}

//------------------------------------------------------
// enter
//
// PURPOSE: Inserts an event into the EventList, based on
//			the time and then the PatientNumber
// PARAMETERS: Event - the event to be added.
// EXTERNAL REFERENCES: References to Node and Event
//------------------------------------------------------

	public void enter(Event event)
	{

		Node next = top.link;
		Event curr = null;
		Node prev = top;

		if(next != null)
			curr = (Event)next.getData();

		// Traverse the list looking for the correct position
		while( next != null && curr.getTime() < event.getTime() )
		{
			prev = next;
			next = next.link;
			if(next != null)
				curr = (Event)next.getData();

		}

		// Within those of the same time, find the correct position
		while( next != null && curr.getTime() == event.getTime() && curr.patient.getNumber() < event.patient.getNumber() )
		{
			prev = next;
			next = next.link;
			if(next != null)
				curr = (Event)next.getData();
		}

		// Insert the data at that position
		prev.link = new Node(event, next);

	}

//------------------------------------------------------
// next
//
// PURPOSE: Returns the next event in the list, and deletes it.
// EXTERNAL REFERENCES:  References to Event.
//------------------------------------------------------

	public Event next()
	{
		return (Event)super.leave();
	}

}