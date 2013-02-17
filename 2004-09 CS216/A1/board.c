#include <stdio.h>

/* simple definitions for the board evaluator results*/
#define LOSS -1
#define DRAW 0
#define WIN 1

/* definitions to track the current state of a board cell
   also used to indicate who wins */
#define EMPTY 'b'
#define X 'X'
#define O 'O'

#define NUM_LOCATIONS 9

/* potential square locations */
#define TOP_LEFT 0
#define TOP_CENTRE 1
#define TOP_RIGHT 2
#define MIDDLE_LEFT 3
#define MIDDLE_CENTRE 4
#define MIDDLE_RIGHT 5
#define BOTTOM_LEFT 6
#define BOTTOM_CENTRE 7
#define BOTTOM_RIGHT 8

#define WINS = 8
/* winning states are always read top to bottom and then left to right
   done this way since I want easy initialization and it never changes */
int winningStates[8][3] = {
  { TOP_LEFT, TOP_CENTRE, TOP_RIGHT },
  { MIDDLE_LEFT, MIDDLE_CENTRE, MIDDLE_RIGHT },
  { BOTTOM_LEFT, BOTTOM_CENTRE, BOTTOM_RIGHT },
  { TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT },
  { TOP_CENTRE, MIDDLE_CENTRE, BOTTOM_CENTRE },
  { TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT },
  { TOP_LEFT, MIDDLE_CENTRE, BOTTOM_RIGHT },
  { TOP_RIGHT, MIDDLE_CENTRE, BOTTOM_LEFT },
};

/* tracks how many comparisons we're actually performing (should be
   less with pruning...) */
static int comparisons = 0;

/* allocates and initializes our board to be emtpy
   our board is a linear array (easier to manage) */
char board[NUM_LOCATIONS];

/* this is ugly. I create an object so I can do an int pass by reference
   in my recursive calls. Needed so I can see how well/badly a move went. */
struct Outcome
{
  int value;
};

/* sets the board to be completely empty */
void reset()
{
  int i;

  for ( i=0 ; i<NUM_LOCATIONS ; i++ )
    board[i] = EMPTY;

};

/* returns the current board state for the given board location */
char currentState( int position )
{
  return( board[position] );
};

char winner();
int full();

/* returns 1 if the game's over */
int gameOver()
{
  int gameOver = 1;

  /* only need to check for a winner if it isn't a draw */
  if ( !full() )
  {
    /* if the winner isn't empty then the game's over */
	if (winner() == 0)
		gameOver = EMPTY;

  }
  return( gameOver );
};

/* returns the player that won, EMPTY if neither */
char winner()
{
  char winner = EMPTY;
  int i;

  /* check each winning state one by one to see if we're done */
  for( i=0 ; (i < WINS) && (winner == EMPTY) ; i++ )
  {
    /* the winning states let us look at specific board locations */
    if ( (board[winningStates[i][0]] == X) && (board[winningStates[i][1]] == X) && (board[winningStates[i][2]] == X) )
      winner = X;

    else if ( (board[winningStates[i][0]] == O) && (board[winningStates[i][1]] == O) && (board[winningStates[i][2]] == O) )
      winner = O;
  }
   
  return (winner);

};

/* returns 1 if there are no spaces left to put a piece */
int full()
{
  int result = 1;
  int i;

  for ( i=0 ; i<NUM_LOCATIONS && result ; i++ )
    if ( board[i] == EMPTY )
      result = 0;

  return( result );
};

/* puts a player's piece (X) at the specified location
   returns 1 if the placement was successful (i.e. not occupied) */
int playerMove( int position )
{
  int result = 0;

  if ( position >= 0 && position < NUM_LOCATIONS )
  {
    /* only want to allow the place if the current location isn't occupied */
    if ( board[position] == EMPTY )
    {
      board[position] = X;
      result = 1;
    }
  }

  return( result );
};

int findComputerMove( struct Outcome outcome, int alpha, int beta );

/* puts a computer's piece (O) on the board */
void computerMove()
{
  /* none of these parameters used here but needed for the recursion */
  struct Outcome outcome;
  int alpha = LOSS;   // computer adjusts alpha
  int beta = WIN;     // opponent adjusts beta

  board[findComputerMove( outcome, alpha, beta )] = O;
};

/* Recursively find the best move the opponent will make.
   The parameters are required since they will oscillate through
   the recursion and we need to keep each levels values.
   Even though not required, the parameters and result are the same
   as findComputerMove() to make it easier to understand. */
int findOpponentMove( struct Outcome outcome, int alpha, int beta )
{
  int i;
  struct Outcome response;
  int result = 0;
  char victor = winner();

  /* must test all end cases since we recurse to a complete board
     instead of one level prior
     we also have to full board last since now it can be full and
     a win so we want to know about the win first */
  if ( victor == X )
    outcome.value = LOSS;
  else if ( victor == O )
    outcome.value = WIN;
  else if ( full() )
    outcome.value = DRAW;
  else
  {
    /* worst case for the opponent, want to find something better */
    outcome.value = WIN;

    /* prune if the computer can't produce a value lower than our
       current outcome (lower would replace ours and it's maximizing) */
    for ( i=0 ; (i < NUM_LOCATIONS) && (outcome.value > alpha) ; i++ )
    {
      /* try all positions not currently in use */
      if ( board[i] == EMPTY )
      {
        board[i] = X;
        /* beta is our best so far, so tell the computer maximizer
           where it can prune */
        findComputerMove( response, alpha, outcome.value );
        board[i] = EMPTY;

        comparisons++;

        /* if this move puts the computer in a worse position, use it */
        if ( response.value < outcome.value )
        {
          outcome.value = response.value;
          result = i;
        }
      }
    }
  }

  return( result );
};


/* Recursively find the best move for the computer to make.
   The parameters are required since they will oscillate through
   the recursion and we need to keep each levels values.
   Returns the location for the computer's move. */
int findComputerMove( struct Outcome outcome, int alpha, int beta )
{
  int i;
  struct Outcome response;
  int result = 0;
  char victor = winner();

  /* must test all end cases since we recurse to a complete board
     instead of one level prior
     we also have to full board last since now it can be full and
     a win so we want to know about the win first */
  if ( victor == O )
    outcome.value = WIN;
  else if ( victor == X )
    outcome.value = LOSS;
  else if ( full() )
    outcome.value = DRAW;
  else
  {
    /* worst case for the computer, want to find something better */
    outcome.value = LOSS;

    /* prune if the opponent can't produce a value higher than our
       current outcome (higher would replace ours and it's minimizing) */
    for ( i=0 ; (i < NUM_LOCATIONS) && (outcome.value < beta) ; i++ )
    {
      /* try all positions not currently in use */
      if ( board[i] == EMPTY )
      {
        board[i] = O;
        /* alpha is our best so far, so tell the opponent minimizer
           where it can prune */
        findOpponentMove( response, outcome.value, beta );
        board[i] = EMPTY;

        comparisons++;

        /* if this move puts the computer in a better position, use it */
        if ( response.value > outcome.value )
        {
          outcome.value = response.value;
          result = i;
        }
      }
    }
  }

  return( result );
};

/* prints out the current board state */
void print()
{
  int i;

  for ( i=0 ; i<NUM_LOCATIONS ; i++ )
  {
    printf("%d ",board[i]);
    if ( (i+1)%3 == 0 )
      printf("\n");
  }

  printf("\n");
};

/* prints the current performance statistic */
void performance()
{
  printf( "We performed %d comparisons.",comparisons );
};

int main()
{

  int position = 0;

  reset();

  /* read in player moves until the game's over */
  print();

  while ( !gameOver() )
  {
    do
    {
      printf("Enter your choice (0-8): " );
	  position = (int)(getchar());
    } while ( playerMove(position) == 0 );

    if ( gameOver() == 0 )
      computerMove();

    print();
  }

  performance();

  return 0;
}
