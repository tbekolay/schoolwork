//-----------------------------------------
// NAME: 				Trevor Bekolay, umbekol0
// STUDENT NUMBER: 		6796723
// COURSE: Comp 4690 SECTION: A01
// INSTRUCTOR: 			Peter Graham
// ASSIGNMENT: 1 		QUESTION: 4
//
// REMARKS: This program simulates a direct
//   mapped cache with parameters passed
//   to it in the command line to determine
//   the hit rate.
//-----------------------------------------

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <math.h>

#define INVALID -1

//------------------------------------------------------
// main
//
// PURPOSE: Simulates a cache by creating an array to
//   store tags, then reads in a trace file to simulate
//   the execution of an actual program.
// INPUT PARAMETERS:
// argc - # of command line arguments
// argv[] - array of strings containing the passed args.
//  argv[1] is the size (in bytes) of each cache line
//  argv[2] is the number of cache lines
//  argv[3] is the
//------------------------------------------------------
int main (int argc, const char * argv[])
{
  int i;                                   // Loop counter
  unsigned long hits = 0;                  // Keep track of how many hits
  unsigned long misses = 0;                // Keep track of how many misses
  int line_size = atoi(argv[1]);           // Size (in bytes) of each cache line
  int num_lines = atoi(argv[2]);           // Number of cache lines
  int di_bits = (int)log2(line_size);      // Number of bits for line_size
  int bi_bits = (int)log2(num_lines);      // Number of bits for num_lines
  int cache_lines[num_lines];              // Array to simulate the cache directory
  FILE * trace_file = fopen(argv[3], "r"); // The trace file to simulate
  unsigned long address, line, tag;        // The address and its derivatives
  char dontcare;                           // R or W; in this case, we don't care

  // Set all of the starting tags to invalid.
  for ( i=0 ; i<num_lines ; i++ )
  {
    cache_lines[i] = INVALID;
  }

  // Start simulating the cache
  printf("Simulating ...\n");

  // Read in each line of our tracefile
  while (fscanf(trace_file, "%lu %c", &address, &dontcare) == 2)
  {
    // Split into line number of tag.
    line = (address / line_size) % num_lines;
    tag = address >> (bi_bits + di_bits);

    // If tag matches, we have a hit!
    if (cache_lines[line] == tag)
    {
      hits++;
    }
    // Otherwise, we miss and store the new tag.
    else
    {
      cache_lines[line] = tag;
      misses++;
    }
  }

  fclose(trace_file);

  // Print final results.
  printf("%s%d%s%d%s%d%s%d%s%f%s",
    "\nSimulation complete.\nLine size: ", line_size,
    "\tNumber of lines: ", num_lines,
    "\nHits: ", hits,
    "\tMisses: ", misses,
    "\nHit rate: ", ((double)hits / (double)(hits + misses)) * 100,
    "%\n\n");

  exit(0);
}
