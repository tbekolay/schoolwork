//-----------------------------------------
// NAME		: Trevor Bekolay
// STUDENT NUMBER	: 6796723
// COURSE		: course #074.215
// SECTION		: L02
// INSTRUCTOR	: Ryan Wegner
// ASSIGNMENT	: assignment #2
//
// REMARKS: This program is an event-based simulation
//			that simulates a somewhat realistic hospital
//			setting, and determines wait times for patients.
//
// INPUT: File-based input that contains arrival times
//		  and treatment times for different patients.
//
// OUTPUT: The events that occur in the simulation
//		   and a list of wait times per patient
//		   as well as the average wait per patient.
//
//-----------------------------------------

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Hospital
{

	//This is used by an event, so it must be public
	public static BufferedReader inFile;

//------------------------------------------------------
// main
//
// PURPOSE: Main function of the program, reads in the information
//			from the input file and starts the simulation.
// PARAMETERS: String[] args - Not used in this program
// EXTERNAL REFERENCES:  References to Input methods, EventList and Event.
//------------------------------------------------------

	public static void main(String[] args)
	{

		//Declare input objects
		FileReader theFile;
		InputStreamReader standardIn;
		BufferedReader inStd;
		String filename;
		EventList events;

		try
		{

			//Define input methods
			standardIn = new InputStreamReader( System.in );
			inStd = new BufferedReader( standardIn );
			events = Event.events;

			System.out.println("Assignment 2. 74.215 L02.  Trevor Bekolay, 6796723.\n");
			System.out.println("Please enter input file name.");

			//Prompt for filename
			filename = inStd.readLine();

			//Load that file
		    theFile = new FileReader( filename );
		    inFile = new BufferedReader( theFile );

			System.out.println("Beginning simulation...\n\n");

			//Set the initial arrival event
			Event.firstArrival(new Patient(inFile.readLine()));

			//The rest of the events occur because of the way
			//the simulation is set up.  Continue through the
			//EventList until there are no more events.
			while(!events.isEmpty())
			{
				events.next().process();
			}

			//Output the wait times
			System.out.println(Event.patients.printWaits());

			inStd.close();
			inFile.close();

		}

		catch( IOException ex )
		{
			System.out.println( "I/O error: " + ex.getMessage() );
		}

		System.out.println("\nProgram completed normally!");
		System.exit(0);

	}

}