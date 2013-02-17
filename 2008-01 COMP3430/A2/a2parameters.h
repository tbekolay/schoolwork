/*
  a2parameters.h
  COMP 3430 Winter 2008
  
  Parameters for concurrent programming problem.
*/
#define TRUE 1
#define FALSE 0
#define NOT_FOUND -1
#define NUMREADS 20
#define NUMWRITES 20
#define READERS 10
#define KEYMIN 0
#define KEYMAX 20
#define MICROSLEEP 100000

#define randInt(L,H) (L)+(int)(((H)-(L)+1.0)*(rand()/(RAND_MAX+1.0)))
