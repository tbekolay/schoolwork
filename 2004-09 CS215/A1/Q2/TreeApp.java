//-----------------------------------------
// NAME			: Trevor Bekolay
// STUDENT NUMBER	: 6796723
// COURSE		: 074.215
// SECTION		: L02
// INSTRUCTOR	: Ryan Wegner
// ASSIGNMENT	: Assignment #1
// QUESTION		: Question #2
//
// REMARKS: Creates a B+ tree and fills it with student
//			records from a file, Student.txt, and can find
// 			records within a range of keys or based on an address.
//
// INPUT: Both from Student.txt and Stdin.
//
// OUTPUT: Based on input from the user, various student records.
//
//-----------------------------------------

import java.io.*;

public class TreeApp
{
	public static void main(String[] args)
	{

		Tree students;
		String inLine;
		int choice = 0;
		int lower;
		int upper;
		String[] input = new String[5];
		Student student;
		IntegerKey studNo;
		InputStreamReader standardIn;
		BufferedReader inStd;
		FileReader theFile;
		BufferedReader inFile;

		try
		{
			//Define input methods
			standardIn = new InputStreamReader( System.in );
			inStd = new BufferedReader( standardIn );
		    theFile = new FileReader( "Students.txt" );
		    inFile = new BufferedReader( theFile );

			//Create tree
			students = new Tree();

			inLine = inFile.readLine();

			System.out.println("Assignment 1, Question 2. 74.215 L02.  Trevor Bekolay, 6796723.\n");
			System.out.println("Creating student database.\n");

			//Reads in all lines of Students.txt and inserts them into the BPlus tree
			while( inLine != null )
			{

				input = inLine.split(", ");
				student = new Student(Integer.parseInt(input[0]), input[1], input[2], input[3], input[4]);
				studNo = new IntegerKey(Integer.parseInt(input[0]));
				students.insert( studNo, student );

				inLine = inFile.readLine();

			}

			//Loops continually until user chooses to quit.
			//Prompts with 3 choices
			while( choice != 3 )
			{
				System.out.println("What would you like to do?\n1. Search for students in a range of Student Numbers.\n2. Search for students who live on a certain street.\n3. Quit");

				choice = Integer.parseInt(inStd.readLine());

				//Choice 1: output students between 2 student numbers
				if( choice == 1 )
				{
					System.out.println("What is the lower bound of Student Numbers to search for?");
					lower = Integer.parseInt(inStd.readLine());
					System.out.println("What is the upper bound of Student Numbers to search for?");
					upper = Integer.parseInt(inStd.readLine());

					System.out.println(students.search(new IntegerKey(lower), new IntegerKey(upper)));
				}
				//Choice 2: output students that live on a particular street
				else if( choice == 2 )
				{
					System.out.println("What is the name of the street you would like to search for?");
					inLine = inStd.readLine();

					System.out.println(students.search(inLine));
				}
			}

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