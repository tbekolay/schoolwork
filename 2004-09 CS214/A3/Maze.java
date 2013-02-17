import java.io.*;
import java.util.*;

public class Maze
{

  public static void main(String[] args)
  {

    char[][] maze;
    Stack stack = new Stack();
    Cell currentCell;
    Cell startCell;
    Cell goalCell;
    boolean trapped = false;

    System.out.println("74.214 Assignment 3, November 2004");
    System.out.println("Question 1: Maze traversal.");
    System.out.println("(Student number 6796723, Section L02)");
    System.out.println();

    if (args.length < 1)
    {
      System.out
          .println("Please supply the name of the input file as a command line argument.");
      return;
    }

    maze = createMaze(args[0]);

    startCell = findStart(maze);

    goalCell = findGoal(maze);

    currentCell = startCell;

    print(maze);

    while (!currentCell.equals(goalCell) && !trapped)
    {

      maze[currentCell.row][currentCell.col] = '.';

      print(maze);

      if (maze[currentCell.row - 1][currentCell.col] == '0'
          || maze[currentCell.row - 1][currentCell.col] == 'e') stack
          .push(new Cell(currentCell.row - 1, currentCell.col));

      if (maze[currentCell.row + 1][currentCell.col] == '0'
          || maze[currentCell.row + 1][currentCell.col] == 'e') stack
          .push(new Cell(currentCell.row + 1, currentCell.col));

      if (maze[currentCell.row][currentCell.col - 1] == '0'
          || maze[currentCell.row][currentCell.col - 1] == 'e') stack
          .push(new Cell(currentCell.row, currentCell.col - 1));

      if (maze[currentCell.row][currentCell.col + 1] == '0'
          || maze[currentCell.row][currentCell.col + 1] == 'e') stack
          .push(new Cell(currentCell.row, currentCell.col + 1));

      if (stack.isEmpty())
      {
        trapped = true;
        System.out
            .println("Unable to find the exit.  Mouse has died a terrible death.");
      }

      else
      {
        currentCell = stack.pop();
      }
    }

    if (!trapped)
    {
      System.out
          .println("Mouse has successfully reached the end of the maze!  And has died a terrible death.");
    }

    System.exit(0);

  }

  public static char[][] createMaze(String filename)
  {
    FileReader theFile; // The file to be read (character file).
    BufferedReader in; // Read text from a character-input stream.
    StringTokenizer tokens;
    String inLine; // A line of input from BufferedReader in.
    String token; // The next element from a line of input.
    int rows;
    int cols;
    char[][] maze = null;
    int i, j;

    try
    {

      theFile = new FileReader(filename);
      in = new BufferedReader(theFile);

      inLine = in.readLine();

      tokens = new StringTokenizer(inLine);

      rows = Integer.parseInt(tokens.nextToken());

      cols = Integer.parseInt(tokens.nextToken());

      maze = new char[rows][cols];

      for (i = 0; i < rows; i++)
      {
        inLine = in.readLine();

        tokens = new StringTokenizer(inLine);

        for (j = 0; j < cols; j++)
        {
          maze[i][j] = tokens.nextToken().charAt(0);

        }

      }

      in.close();

    }

    catch (IOException ex)
    {
      System.out.println("I/O error: " + ex.getMessage());
    }

    return maze;

  }

  public static Cell findStart(char[][] maze)
  {

    int i, j;
    int rows = maze.length;
    int cols = maze[0].length;
    Cell start = null;

    for (i = 0; i < rows; i++)
    {

      for (j = 0; j < cols; j++)
      {

        if (maze[i][j] == 'm') start = new Cell(i, j);

      }

    }

    return start;

  }

  public static Cell findGoal(char[][] maze)
  {

    int i, j;
    int rows = maze.length;
    int cols = maze[0].length;
    Cell goal = null;

    for (i = 0; i < rows; i++)
    {

      for (j = 0; j < cols; j++)
      {

        if (maze[i][j] == 'e') goal = new Cell(i, j);

      }

    }

    return goal;

  }

  public static void print(char[][] maze)
  {

    int i, j;
    int rows = maze.length;
    int cols = maze[0].length;
    Cell goal = null;

    for (i = 0; i < rows; i++)
    {

      for (j = 0; j < cols; j++)
      {

        System.out.print(maze[i][j]);

      }

      System.out.println();

    }

    System.out.println();

  }

}

class Cell
{
  public int row;
  public int col;

  Cell(int newRow, int newCol)
  {

    row = newRow;
    col = newCol;

  }

  public boolean equals(Cell test)
  {
    return (this.row == test.row && this.col == test.col);
  }

}
