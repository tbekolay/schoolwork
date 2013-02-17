// ASCII Snakes and Ladders Game
//
// Uses class SparseMatrix (constructor, setValue, getValue)
// Needs file gameBoard.txt (see Documents section).
//
// Plays two-player Snakes and Ladders game:
// A board game in which players move pieces according
// to the roll of a die. If a player reaches a ladder
// the player climbs the ladder to a higher position.
// If a player reaches a snake, the player slides down
// the snake to a lower position. The object is to
// reach the top square first.
//


import java.io.*;
import java.util.*;

public class SnakesAndLadders
{
  private SparseMatrix board;
  // Game board implemented as a sparse matrix.
  // At the square corresponding to the head
  // of a snake, store the position of the
  // tip of the snake's tail.
  // At a square corresponding to the foot
  // of a ladder, store the position of the
  // top of the ladder.
  private int[] boardSize = new int[2];
  // boardSize[0] = # rows in game board,
  // boardSize[1] = # columns in game board.
  private Random die; // The players roll a die to move on the board.
  private int[] player; // The players.

  private final int NUMPLAYERS = 2; // The number of players.

  public SnakesAndLadders()
  {
    int currPlayer;

    // Initiallize game state.

    initBoard("gameBoard.txt");
    die = new Random(System.currentTimeMillis());
    player = new int[NUMPLAYERS];
    for (currPlayer = 0; currPlayer < NUMPLAYERS; currPlayer++)
    {
      player[currPlayer] = 1;
    }
  } // end SnakesAndLadders constructor

  private void initBoard(String fileName)
  {
    FileReader theFile; // The file to be read (character file).
    BufferedReader in; // Read text from a character-input stream.
    String inLine; // A line of input from BufferedReader in.
    String token; // The next element from a line of input.
    StringTokenizer tokens; // To get the elements from a line of input.
    int i; // index of for-loops
    int startRow; // row of start of ladder or snake
    int startCol; // column of start of ladder or snake
    int endRow; // row of end of ladder or snake
    int endCol; // column of end of ladder or snake

    try
    {
      theFile = new FileReader(fileName);
      in = new BufferedReader(theFile);

      // The first line is the size of the board:
      // number of rows number of columns

      inLine = in.readLine();

      if (inLine == null)
      {
        System.out.println("Error: File " + fileName + " is empty.");
      }
      else
      {
        // First line contains # rows and # cols in board.

        tokens = new StringTokenizer(inLine);
        for (i = 0; (i < 2) && tokens.hasMoreElements(); i++)
        {
          token = tokens.nextToken();
          boardSize[i] = Integer.parseInt(token);
        } // end for

        // Allocate the board.

        board = new SparseMatrix(boardSize[0], boardSize[1]);

        // Read the snakes and ladders into the board.
        // Each line of the file contains 4 integers:
        // The row and column of the foot of a ladder
        // or the head of a snake.
        // The row and column of the top of the ladder
        // or the tip of the tail of the snake
        // (i.e., where does the player end up, if
        // he/she climbs the ladder or slips down
        // the snake?)

        inLine = in.readLine();

        while (inLine != null)
        {
          // Parse the current line of input.
          // Each line contains one snake or one ladder (four integers).

          tokens = new StringTokenizer(inLine);

          // Get row number of ladder foot or snake head.

          token = tokens.nextToken();
          startRow = Integer.parseInt(token);

          // Get column number of ladder foot or snake head.

          token = tokens.nextToken();
          startCol = Integer.parseInt(token);

          // Get row number of ladder top or snake tail-tip.

          token = tokens.nextToken();
          endRow = Integer.parseInt(token);

          // Get column number of ladder top or snake tail-tip.

          token = tokens.nextToken();
          endCol = Integer.parseInt(token);

          // Add new snake or ladder to board.

          board.setValue(startRow, startCol, toPlayerPosn(endRow, endCol,
              boardSize));

          // Get next snake or ladder.

          inLine = in.readLine();

        } // end while (inLine != null)

      } // end else (file contained board size)

      in.close();

    } // end try

    catch (IOException ex)
    {
      System.out.println("I/O error: " + ex.getMessage());
    }

  } // end method initBoard


  // ***********************************************************
  // *
  // * The board has two coordinate systems:
  // *
  // * A. The (row, column) coordinate system:
  // * Example: A 4x5 board has the following (row, column)
  // * coordinate system:
  // * (4,1) (4,2) (4,3) (4,4) (4,5)
  // * (3,1) (3,2) (3,3) (3,4) (3,5)
  // * (2,1) (2,2) (2,3) (2,4) (2,5)
  // * (1,1) (1,2) (1,3) (1,4) (1,5)
  // * The sparse matrix implementation uses this
  // * representation.
  // *
  // * B. The player-position coordinate system, which
  // * mirrors how players move.
  // * Example: A 4x5 board has the following
  // * player-position coordinate system:
  // * 20 19 18 17 16
  // * 11 12 13 14 15
  // * 10 9 8 7 6
  // * 1 2 3 4 5
  // * Note that odd rows go right to left
  // * and even rows go left to right.
  // *
  // ************************************************************

  // ***********************************************************
  // * toPlayerPosn
  // *
  // * PURPOSE Convert from the (row, column) coordinate system
  // * to the player-position coordinate system.
  // * PARAM row
  // * INPUT parameter
  // * player is in this row.
  // * PARAM col
  // * INPUT parameter
  // * player is in column col.
  // * PARAM boardSize
  // * INPUT parameter
  // * boardSize[0] = # of rows in the board
  // * boardSize[1] = # of columns in the board
  // *
  // * RETURNS player position for (row, col)
  // ***********************************************************

  private int toPlayerPosn(int row, int col, int[] boardSize)
  {
    int playerPosn;

    // Row row contains positions (row-1)*(# columns)+1 to
    // (row)*(# columns).
    // Odd rows increase in position from left to right.
    // Even rows increase in position from right to left.

    if ((row % 2) != 0)
    {
      // Odd row (player position increases left to right).

      playerPosn = ((row - 1) * boardSize[1]) + col;
    }
    else
    {
      // Even row (player position increases right to left).

      playerPosn = ((row - 1) * boardSize[1]) + 1 + boardSize[1] - col;
    }

    return playerPosn;

  } // end method toPlayerPosn


  // *******************************************************
  // * toRow
  // *
  // * PURPOSE To help convert from the player-position
  // * coordinate system to the (row, column) system,
  // * by computing what row a player position is in.
  // * Row R contains player position
  // * (R-1)*(# of columns)+1 to player position
  // * R*(# of columns)
  // *
  // * PARAM playerPosn
  // * INPUT parameter
  // * The player's position in the player-position
  // * coordinate system.
  // * PARAM boardSize
  // * INPUT parameter
  // * boardSize[0] = # of rows in the board
  // * boardSize[1] = # of columns in the board
  // *
  // * RETURNS row containing player position.
  // ***********************************************************

  private int toRow(int playerPosn, int[] boardSize)
  {
    int row;

    if (playerPosn % boardSize[1] == 0) row = playerPosn / boardSize[1];
    else
      row = playerPosn / boardSize[1] + 1;

    return row;

  } // end method toRow



  // *******************************************************
  // * toCol
  // *
  // * PURPOSE To help convert from the player-position
  // * coordinate system to the (row, column) system,
  // * by computing what column a player position is in.
  // *
  // * PARAM playerPosn
  // * INPUT parameter
  // * The player's position in the player-position
  // * coordinate system.
  // * PARAM boardSize
  // * INPUT parameter
  // * boardSize[0] = # of rows in the board
  // * boardSize[1] = # of columns in the board
  // *
  // * RETURNS column containing player position.
  // ***********************************************************

  private int toCol(int playerPosn, int[] boardSize)
  {
    int row = toRow(playerPosn, boardSize);
    int col;

    if ((row % 2) != 0)
    {
      // Odd rows go left-to-right in player-position coordinate system.

      col = (playerPosn - (row - 1) * boardSize[1]);
    }
    else
    {
      // Even rows go right-to-left.

      col = boardSize[1] - (playerPosn - (row - 1) * boardSize[1]) + 1;
    }


    return col;

  } // end method toCol



  public static void main(String[] args)
  {
    SnakesAndLadders game = new SnakesAndLadders();
    boolean gameOver = false; // Game's over when someone reaches
    // the last board square.
    int currPlayer; // Current player: i.e., it is
    // currPlayer's turn to move.
    int rollDie; // The result of rolling the die.
    int lastPosn; // Last position on the board.
    int updatePosn; // New position if player reached
    // a snake or a ladder.

    lastPosn = game.boardSize[0] * game.boardSize[1];

    // Players alternate rolling the die and moving their pieces.

    currPlayer = 0;
    while (!gameOver)
    {
      // Player currPlayer's turn

      System.out.println("Player " + currPlayer + "'s turn:");
      System.out.println("    Player " + currPlayer + " is on square "
          + game.player[currPlayer]);

      // Roll the die.

      rollDie = game.die.nextInt(6) + 1;

      System.out.println("    Player " + currPlayer + " rolled a " + rollDie);


      // Move the player's piece.

      if ((game.player[currPlayer] + rollDie) >= lastPosn)
      {
        // Player landed on last square (don't need exact roll).

        game.player[currPlayer] = lastPosn;

        System.out.println();
        System.out.println("Player " + currPlayer + " reached the "
            + "last square and won the game!");

        gameOver = true;
      }
      else
      {
        // Rolling the die did not land the player on the last square.

        game.player[currPlayer] = game.player[currPlayer] + rollDie;
        System.out.println("    Player " + currPlayer + " moved to "
            + "square " + game.player[currPlayer]);

        // Check if the player landed on a snake or a ladder.

        updatePosn = game.board.getValue(game.toRow(game.player[currPlayer],
            game.boardSize), game
            .toCol(game.player[currPlayer], game.boardSize));

        if (updatePosn != 0)
        {
          if (updatePosn < game.player[currPlayer]) System.out
              .println("    Player " + currPlayer
                  + " slid down a snake to square " + updatePosn);
          else
            System.out.println("    Player " + currPlayer
                + " climbed a ladder to square " + updatePosn);

          game.player[currPlayer] = updatePosn;

          if (game.player[currPlayer] >= lastPosn)
          {

            System.out.println();
            System.out.println("Player " + currPlayer + " reached the "
                + "last square and won the game!");

            gameOver = true;
          }
        }
      } // end else (player hasn't reached last position)

      currPlayer = (currPlayer + 1) % game.NUMPLAYERS;
    } // end while (!gameOver)

  } // end method main


} // end class SnakesAndLadders
