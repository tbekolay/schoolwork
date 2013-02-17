//-----------------------------------------
// NAME			: Trevor Bekolay
// STUDENT NUMBER	: 6796723
// COURSE		: 074.215
// SECTION		: L02
// INSTRUCTOR	: Ryan Wegner
// ASSIGNMENT	: Assignment #1
// QUESTION		: Question #1
//
// REMARKS: A simple ordered queue, implemented with a linked list.
//			Performs a number of hard coded tasks.
// INPUT: Hard coded.
//
// OUTPUT: Displays results of tasks to Stdout
//
//-----------------------------------------

#include <iostream>
#include "orderedqueue.h"

using namespace std;

int main()
{

	OrderedQueue *Q1;
	OrderedQueue *Q2;
	int output;

	cout << "Assignment 1, Question 1.  074.215 LO2.  Trevor Bekolay,
6796723.  Aries.\n" << endl;

	//Creates and manipulates Q1
	Q1 = new OrderedQueue;
	cout << "Queue 1 created" << endl;
	Q1->enter(10);
	Q1->enter(5);
	Q1->enter(15);
	Q1->enter(20);
	output = Q1->leave();
	cout << "Leaving from Q1: " << output << endl;
	Q1->enter(15);
	output = Q1->leave();
	cout << "Leaving from Q1: " << output << endl;

	//Creates Q2 and manipulates both
	Q2 = new OrderedQueue;
	cout << "Queue 2 created" << endl;
	Q2->enter(100);
	Q2->enter(50);
	output = Q2->leave();
	cout << "Leaving from Q2: " << output << endl;
	output = Q2->leave();
	cout << "Leaving from Q2: " << output << endl;
	output = Q2->leave();
	cout << "Leaving from Q2: " << output << endl;
	Q2->enter(15);
	output = Q1->leave();
	cout << "Leaving from Q1: " << output << endl;
	output = Q1->leave();
	cout << "Leaving from Q1: " << output << endl;
	output = Q1->leave();
	cout << "Leaving from Q1: " << output << endl;

	//Free the memory used
	delete Q1;
	delete Q2;
	Q1 = NULL;
	Q2 = NULL;

	cout << "\nProgram completed successfully!" << endl;

	return( 0 );

}
