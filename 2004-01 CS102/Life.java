/**
 * Life: Stores life board configurations and performs actions on them.
 * 74.101 SECTION L02
 * INSTRUCTOR Terry Andres
 * ASSIGNMENT Assignment 4, question 2
 * @author Trevor Bekolay, 6796723
 * @version March 20, 2004
 */

public class Life
{
  private int[][] lifeBoard;
  private int rows;
  private int columns;

  /**
   * PURPOSE: Life is the constructor for Life. It fills the lifeBoard array
   * with values.
   * @param int[][] lifeCells: The constructor accepts a two-dimensional array
   *          of values. These values are the coordinates of where there are
   *          live cells.
   */

  public Life(int[][] lifeCells)
  {
    int row;
    int column;
    int count;

    // By default, the lifeBoard array is 20x20. This can be changed at design
    // time.
    rows = 20;
    columns = 20;

    lifeBoard = new int[rows][columns];

    // This for loop sets each value in the lifeBoard array to 0, initially.
    for (row = 0; row < (rows - 1); row++)
    {
      for (column = 0; column < (columns - 1); column++)
      {
        lifeBoard[row][column] = 0;
      }
    }

    // This for loop changes the cells defined in lifeCell to 1.
    for (count = 0; count < lifeCells.length; count++)
    {
      lifeBoard[lifeCells[count][0]][lifeCells[count][1]] = 1;
    }

  }

  /**
   * PURPOSE: printLife outputs the current array configuration and returns the
   * same.
   * @return int[][]: The 2-dimensional array that contains the current board
   *         configuration.
   */

  public int[][] printLife()
  {
    int row;
    int column;

    // This for loop outputs the array information using System.out.print.
    for (row = 0; row < (rows - 1); row++)
    {
      for (column = 0; column < (columns - 1); column++)
      {
        System.out.print(lifeBoard[row][column] + " ");
      }
      System.out.println();
    }

    System.out.println();

    // This return statement is used by the GUI part of the A4Q2 class.
    return lifeBoard;

  }

  /**
   * PURPOSE: runLife changes the board configuration based on predefined rules
   * and the amount of time that passes.
   * @param int nunLife is the length of time that passes, or how many
   *          generations we wish to skip ahead.
   * @return int[][]: The current board configuration.
   */

  public int[][] runLife(int numLife)
  {

    int count;
    int row;
    int column;
    // We must define a new int[][] newBoard that will contain the new board
    // configuration, as we wish to change all of the elements inside the
    // lifeBoard array at the same time.
    int[][] newBoard = new int[rows][columns];

    // This master for loop will repeat the operations for as many generations
    // as defined in the classes that call runLife.
    for (count = 0; count < numLife; count++)
    {

      // First, set the first and last rows to be equal to those of lifeBoard.
      newBoard[0] = lifeBoard[0];
      newBoard[rows - 1] = lifeBoard[rows - 1];

      // This for loop sets the columns to be the same as those of lifeBoard.
      for (row = 0; row < (rows - 1); row++)
      {
        newBoard[row][0] = lifeBoard[row][0];
        newBoard[row][columns - 1] = lifeBoard[row][columns - 1];
      }

      // This for loop uses the isAlive method to determine if each cell is
      // alive.
      for (row = 1; row < (rows - 1); row++)
      {
        for (column = 1; column < (columns - 1); column++)
          newBoard[row][column] = isAlive(row, column);
      }
      lifeBoard = newBoard;
    }
    return lifeBoard;
  }

  /**
   * PURPOSE: The isAlive method determines if a certain cell in lifeBoard will
   * stay alive in the next generation.
   * @param int row and
   * @param int column are the row and column value of the cell to be checked.
   * @return int, 0 for dead cell and 1 for an alive cell.
   */

  private int isAlive(int row, int column)
  {

    int count;
    int rowCount;
    int colCount;
    int total = 0;
    int currentCell = 0;

    // This for loop runs through the 8 elements around a certain cell (it will
    // not go out of bounds since we do not check the first and last rows or
    // columns).
    for (rowCount = -1; rowCount < 2; rowCount++)
    {
      for (colCount = -1; colCount < 2; colCount++)
      {
        // If we are at the current cell, we wish to remember if it is currently
        // alive or dead, and not add it to the running total.
        if (rowCount == 0 && colCount == 0) currentCell = lifeBoard[row][column];
        // If we are not at the current cell, we keep a running total of the
        // number of live cells around the current cell.
        else
          total += lifeBoard[row + rowCount][column + colCount];
      }
    }

    // Based on the rules given, we either change the living status of that cell
    // or keep it the same.
    if (currentCell == 0 && total == 3) return 1;
    else if (currentCell == 1 && (total < 2 || total > 3)) return 0;
    else
      return currentCell;
  }
}
