public class NQueens
{
  private static final int N = 9;
  private static final int AVAILABLE = 0;
  private static final int QUEEN = -1;
  private static int[][] space = new int[N][N];
  private static int dead_ends = 0;
  private static int solutions = 0;

  public static void addQueen()
  {
    for (int i = 0; i < N; i++)
    {
      for (int j = 0; j < N; j++)
      {
        space[i][j] = AVAILABLE;
      }
    }

    addQueen(0);
  }

  private static void addQueen(int row)
  {
    boolean dead_end = true;
    int col, i;

    // Try to add a queen in row
    for (col = 0; col < N; col++)
    {
      if (space[row][col] == AVAILABLE)
      {
        // Try a queen here
        space[row][col] = QUEEN;
        // Mark column with value row
        for (i = row + 1; i < N; i++)
        {
          if (space[i][col] == AVAILABLE)
          {
            space[i][col] = row + 1;
          }
        }
        // Mark two diagonals
        for (i = 1; i + row < N; i++)
        {
          // Left diagonal
          if (row + i < N && col - i >= 0
              && space[row + i][col - i] == AVAILABLE)
          {
            space[row + i][col - i] = row + 1;
          }
          // Right diagonal
          if (row + i < N && col + i < N
              && space[row + i][col + i] == AVAILABLE)
          {
            space[row + i][col + i] = row + 1;
          }
        }

        if (row == N - 1)
        {
          // We have a solution!
          solutions++;
          dead_end = false;
        }
        else
        {
          addQueen(row + 1);
          // If we do another iteration, then we're not at a dead end.
          dead_end = false;
        }

        // Restore column
        for (i = row + 1; i < N; i++)
        {
          if (space[i][col] == row + 1)
          {
            space[i][col] = AVAILABLE;
          }
        }
        // Restore two diagonals
        for (i = 1; i + row < N; i++)
        {
          // Left diagonal
          if (row + i < N && col - i >= 0 && space[row + i][col - i] == row + 1)
          {
            space[row + i][col - i] = AVAILABLE;
          }
          // Right diagonal
          if (row + i < N && col + i < N && space[row + i][col + i] == row + 1)
          {
            space[row + i][col + i] = AVAILABLE;
          }
        }

        space[row][col] = AVAILABLE;
      }
    }

    // At this point, dead_end is true if we were not a successful leaf node
    // and we didn't recurse any further (i.e. the row has no open position,
    // and we're not at the last row)
    if (dead_end) dead_ends++;
  }

  public static void main(String[] args)
  {
    // Try the n queens problem
    addQueen();

    // Print out the counts
    System.out.println(N + " Queens problem resulted in " + solutions
        + " solutions.");
    System.out.println(N + " Queens problem resulted in " + dead_ends
        + " dead ends.");
    System.out.println("End of processing.");

  }
}
