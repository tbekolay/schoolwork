//-----------------------------------------
// NAME: 				Trevor Bekolay, umbekol0
// STUDENT NUMBER: 		6796723
// COURSE: Comp 4690 SECTION: A01
// INSTRUCTOR: 			Peter Graham
// ASSIGNMENT: 2 		QUESTION: 2
//
// REMARKS: This program simulates a branch
//  predictor.  It accepts a tracefile
//  of addresses and whether or not each
//  branch address was taken.
//-----------------------------------------

#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#define NOT_TAKEN 0
#define TAKEN 1

// The 3 variables we want to change for each run of the program.
#define INITIAL_TAKEN TAKEN
#define NUM_BITS 4
#define NUM_ENTRIES 32

// The value that a history register must have to change our prediction to TAKEN.
#define TAKEN_HISTORY (int)(pow(2,NUM_BITS)-1)

// A branch history table entry; keeps track of the current prediction, and
// the history of the past NUM_BITS branches.
typedef struct bhe
{
  unsigned predict : 1;
  unsigned history : NUM_BITS;
} bhe;

// The branch history table.
class BranchHistoryTable
{
  private:
    bhe entries[NUM_ENTRIES]; // The table entries
    int correct;              // How many correct predictions we've made
    int wrong;                // How many incorrect predictions we've made

  public:
  //------------------------------------------------------
  // BranchHistoryTable constructor
  //
  // PURPOSE: Initializes the BHT
  //------------------------------------------------------
    BranchHistoryTable()
    : correct(0), wrong(0)
    {
      int i;

      for (i=0; i < NUM_ENTRIES; i++)
      {
        entries[i].predict = INITIAL_TAKEN;
        entries[i].history = 0;
      }
    } // bht constructor

    //------------------------------------------------------
    // predict
    //
    // PURPOSE: Makes a prediction on the branch instruction
    //   at 'address.' Updates the current prediction based
    //   on if the branch was actually taken.
    // INPUT PARAMETERS:
    //   address - The address of the branch instruction.
    //   actual  - Was the branch actually taken?
    //------------------------------------------------------
    void predict(unsigned long address, unsigned short int actual)
    {
      unsigned int entry = address % NUM_ENTRIES;

      // Determine if our prediction would have been correct.
      if (entries[entry].predict == actual)
      {
        correct++;
      }
      else
      {
        wrong++;
      }

      // Update our history and current prediction.
      entries[entry].history <<= 1;
      entries[entry].history |= actual;
      if (entries[entry].history == TAKEN_HISTORY)
      {
        entries[entry].predict = TAKEN;
      }
      else if (entries[entry].history == 0)
      {
        entries[entry].predict = NOT_TAKEN;
      }


    } // void predict

    //------------------------------------------------------
    // print_results
    //
    // PURPOSE: Prints out statistics on how successful
    //   our branch prediction was.
    //------------------------------------------------------
    void print_results()
    {
      // Print final results.
      printf("%s%d%s%d%s%s%s%d%s%d%s%f%s",
        "\nSimulation complete.\nNumber of bits per entry: ", NUM_BITS,
        "\tNumber of entries: ", NUM_ENTRIES,
        "\n\tInitialized to: ", (INITIAL_TAKEN == TAKEN ? "taken" : "not taken"),
        "\nCorrect predictions: ", correct,
        "\tIncorrect predictions: ", wrong,
        "\nPrediction rate: ", ((double)correct / (double)(correct + wrong)) * 100,
        "%\n\n");
    }
}; // class BranchHistoryTable

//------------------------------------------------------
// main
//
// PURPOSE: Simulates a branch predictor by reading
//   in a trace file to simulate the execution of an
//   actual program.
// INPUT PARAMETERS:
// argc - # of command line arguments
// argv[] - array of strings containing the passed args.
//  argv[1] is the file name of the trace file
//------------------------------------------------------
int main (int argc, const char * argv[])
{
  BranchHistoryTable bht;              // Our dynamic branch prediction object
  unsigned long address, dest_address; // The addresses given by the trace file
  unsigned short int taken;            // Was the branch at 'address' taken?
  FILE * trace_file;                   // The trace file to simulate

  // Ensure we get passed a file
  if (argc != 2)
  {

    printf("Please pass the filename as a command line argument.\n\te.g., branch_predict branchTrace.txt");
    exit(0);
  }

  trace_file = fopen(argv[1], "r");

  // Ensure the file opens successfully
  if (!trace_file)
  {
    printf("Error loading %s.  Please ensure the file exists and is readable by the current user.", argv[1]);
  }
  else
  {
    // Start simulating the cache
    printf("Simulating ...\n");

    // Read in each line of our tracefile and do prediction
    while (fscanf(trace_file, "%lx %lx %hu", &address, &dest_address, &taken) == 3)
    {
      bht.predict(address, taken);
    }

    fclose(trace_file);

    // How did we do?
    bht.print_results();
  }

  exit(0);
}
