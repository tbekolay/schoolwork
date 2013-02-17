import java.io.*;

public class Board
{
  // simple definitions for the board evaluator results
  private final static int LOSS = -1;
  private final static int DRAW = 0;
  private final static int WIN  = 1;

  // definitions to track the current state of a board cell
  // also used to indicate who wins
  private final static char EMPTY = 'b';
  private final static char X = 'X';
  private final static char O = 'O';

  // our board is a linear array (easier to manage)
  private final static int NUM_LOCATIONS = 9;
  private char board[];

  // potential square locations
  private final int TOP_LEFT = 0;
  private final int TOP_CENTRE = 1;
  private final int TOP_RIGHT = 2;
  private final int MIDDLE_LEFT = 3;
  private final int MIDDLE_CENTRE = 4;
  private final int MIDDLE_RIGHT = 5;
  private final int BOTTOM_LEFT = 6;
  private final int BOTTOM_CENTRE = 7;
  private final int BOTTOM_RIGHT = 8;

  private final int WINS = 8;
  // winning states are always read top to bottom and then left to right
  // done this way since I want easy initialization and it never changes
  private final int winningStates[][] = { 
		{ TOP_LEFT, TOP_CENTRE, TOP_RIGHT },
		{ MIDDLE_LEFT, MIDDLE_CENTRE, MIDDLE_RIGHT },
		{ BOTTOM_LEFT, BOTTOM_CENTRE, BOTTOM_RIGHT },
		{ TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT },
		{ TOP_CENTRE, MIDDLE_CENTRE, BOTTOM_CENTRE },
		{ TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT },
		{ TOP_LEFT, MIDDLE_CENTRE, BOTTOM_RIGHT },
		{ TOP_RIGHT, MIDDLE_CENTRE, BOTTOM_LEFT },
  };

  // tracks how many comparisons we're actually performing (should be
  // less with pruning...)
  private int comparisons = 0;

  // this is ugly. I create an object so I can do an int pass be reference
  // in my recursive calls. Needed so I can see how well/badly a move went.
  private class Outcome
  {
    public int value;
  }

  // allocates and initializes our board to be emtpy
  public Board()
  {
    board = new char[NUM_LOCATIONS];

    reset();
  }

  // sets the board to be completely empty
  public void reset()
  {
    int i;

    for ( i=0 ; i<NUM_LOCATIONS ; i++ )
      board[i] = EMPTY;
  }

  // returns the current board state for the given board location
  public char currentState( int position )
  {
    return( board[position] );
  }

  // returns true if the game's over
  public boolean gameOver()
  {
    boolean gameOver = true;
    int i;

    // only need to check for a winner if it isn't a draw
    if ( !full() )
    {
      // if the winner isn't empty then the game's over
      gameOver = (winner() != EMPTY);
    }
      
    return( gameOver );
  }

  // returns the player that won, EMPTY if neither
  private char winner()
  {
    char winner = EMPTY;
    int i;

    // check each winning state one by one to see if we're done
    for ( i=0 ; (i < WINS) && (winner == EMPTY) ; i++ )
    {
      // the winning states let us look at specific board locations

      if ( (board[winningStates[i][0]] == X) &&
           (board[winningStates[i][1]] == X) &&
           (board[winningStates[i][2]] == X) )
        winner = X;

      else if ( (board[winningStates[i][0]] == O) &&
                (board[winningStates[i][1]] == O) &&
                (board[winningStates[i][2]] == O) )
        winner = O;
    }
    
    return( winner );
  }

  // returns true if there are no spaces left to put a piece
  private boolean full()
  {
    boolean result = true;
    int i;

    for ( i=0 ; i<NUM_LOCATIONS && result ; i++ )
      if ( board[i] == EMPTY )
        result = false;

    return( result );
  }

  // puts a player's piece (X) at the specified location
  // returns true if the placement was successful (i.e. not occupied)
  public boolean playerMove( int position )
  {
    boolean result = false;

    if ( position >= 0 && position < NUM_LOCATIONS )
    {
      // only want to allow the place if the current location isn't occupied
      if ( board[position] == EMPTY )
      {
        board[position] = X;
        result = true;
      }
    }
      
    return( result );
  }

  // puts a computer's piece (O) on the board
  public void computerMove()
  {
    // none of these parameters used here but needed for the recursion
    Outcome outcome = new Outcome();
    int alpha = LOSS;   // computer adjusts alpha
    int beta = WIN;     // opponent adjusts beta

    board[findComputerMove( outcome, alpha, beta )] = O;
  }

  // Recursively find the best move the opponent will make.
  // The parameters are required since they will oscillate through
  // the recursion and we need to keep each levels values.
  // Even though not required, the parameters and result are the same
  // as findComputerMove() to make it easier to understand.
  private int findOpponentMove( Outcome outcome, int alpha, int beta )
  {
    int i;
    Outcome response = new Outcome();
    int result = 0;
    char victor = winner();

    // must test all end cases since we recurse to a complete board
    // instead of one level prior
    // we also have to full board last since now it can be full and
    // a win so we want to know about the win first
    if ( victor == X )
      outcome.value = LOSS;
    else if ( victor == O )
      outcome.value = WIN;
    else if ( full() )
      outcome.value = DRAW;
    else
    {
      // worst case for the opponent, want to find something better
      outcome.value = WIN;

      // prune if the computer can't produce a value lower than our 
      // current outcome (lower would replace ours and it's maximizing)
      for ( i=0 ; (i < NUM_LOCATIONS) && (outcome.value > alpha) ; i++ )
      {
        // try all positions not currently in use
        if ( board[i] == EMPTY )
        {
          board[i] = X;
          // beta is our best so far, so tell the computer maximizer
          // where it can prune
          findComputerMove( response, alpha, outcome.value );
          board[i] = EMPTY;

          comparisons++;

          // if this move puts the computer in a worse position, use it
          if ( response.value < outcome.value )
          {
            outcome.value = response.value;
            result = i;
          }
        }		
      }
    }

    return( result );
  }


  // Recursively find the best move for the computer to make.
  // The parameters are required since they will oscillate through
  // the recursion and we need to keep each levels values.
  // Returns the location for the computer's move.
  private int findComputerMove( Outcome outcome, int alpha, int beta )
  {
    int i;
    Outcome response = new Outcome();
    int result = 0;
    char victor = winner();

    // must test all end cases since we recurse to a complete board
    // instead of one level prior
    // we also have to full board last since now it can be full and
    // a win so we want to know about the win first
    if ( victor == O )
      outcome.value = WIN;
    else if ( victor == X )
      outcome.value = LOSS;
    else if ( full() )
      outcome.value = DRAW;
    else
    {
      // worst case for the computer, want to find something better
      outcome.value = LOSS;

      // prune if the opponent can't produce a value higher than our 
      // current outcome (higher would replace ours and it's minimizing)
      for ( i=0 ; (i < NUM_LOCATIONS) && (outcome.value < beta) ; i++ )
      {
        // try all positions not currently in use
        if ( board[i] == EMPTY )
        {
          board[i] = O;
          // alpha is our best so far, so tell the opponent minimizer
          // where it can prune
          findOpponentMove( response, outcome.value, beta );
          board[i] = EMPTY;

          comparisons++;

          // if this move puts the computer in a better position, use it
          if ( response.value > outcome.value )
          {
            outcome.value = response.value;
            result = i;
          }
        }		
      }
    }

    return( result );
  }

  // prints out the current board state
  public void print()
  {
    int i;

    for ( i=0 ; i<NUM_LOCATIONS ; i++ )
    {
      System.out.print( board[i] + " " );
      if ( (i+1)%3 == 0 )
        System.out.println();
    }

    System.out.println();
  }

  // prints the current performance statistic
  public void performance()
  {
    System.out.println( "We performed " + comparisons + " comparisons." );
  }

  public static void main( String args[] )
  {
    // command line is true for pruning, false if not
    Board board = new Board();
    InputStreamReader standardIn;
    BufferedReader    in;
    int position = 0;

    // read in player moves until the game's over
    try
    {
      standardIn = new InputStreamReader( System.in );
      in         = new BufferedReader( standardIn );

      board.print();

      while ( !board.gameOver() )
      {
        do
        {
          System.out.print( "Enter your choice (0-8): " );
          position = Integer.parseInt( in.readLine() );
        } while ( !board.playerMove(position) );

        if ( !board.gameOver() )
          board.computerMove();

        board.print();
      }

      board.performance();
    }

    catch( IOException ex )
    {
      System.out.println( "I/O error: " + ex.getMessage() );
    }

  }
}