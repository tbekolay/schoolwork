import java.io.*;
import java.text.*;
import java.util.*;

public class testSparseMatrix
{
  // For a non-random small matrix.

  private static final int SNUMROWS = 20; // # rows
  private static final int SNUMCOLS = 15; // # columns

  // For a random LARGE matrix.

  private static final int LNUMROWS = 100000; // Max # rows
  private static final int LNUMCOLS = 100000; // Max # columns
  private static Random generator = new Random(); // Generate random entries
  private static final int NUMENTRIES = 1000; // # of non-zero entries


  public static void main(String[] args)
  {
    // small non-random matrix

    SparseMatrix sm = new SparseMatrix(SNUMROWS, SNUMCOLS);

    // large random matrix

    int lNumRows = 1 + generator.nextInt(LNUMROWS); // random # of rows
    int lNumCols = 1 + generator.nextInt(LNUMCOLS); // random # of columns
    SparseMatrix SM = new SparseMatrix(lNumRows, lNumCols);

    // Other local variables.

    int i;
    int row;
    int col;
    int entry;

    // First, work with the small, non-random matrix.

    System.out.println("Small, Non-random Matrix");
    System.out.println();
    System.out.println("The matrix is " + SNUMROWS + " x " + SNUMCOLS + ".");
    System.out.println();

    System.out.println("Adding value 80 to position [2,5].");
    sm.setValue(2, 5, 80);
    System.out.println("Adding value 81 to position [13,5].");
    sm.setValue(13, 5, 81);
    System.out.println("Adding value 82 to position [2,15].");
    sm.setValue(2, 15, 82);
    System.out.println("Adding value 83 to position [8,8].");
    sm.setValue(8, 8, 83);
    System.out.println("Adding value 84 to position [20,15].");
    sm.setValue(20, 15, 84);
    System.out.println("Adding value 85 to position [12,13].");
    sm.setValue(12, 13, 85);
    System.out.println("Adding value 86 to position [1,6].");
    sm.setValue(1, 6, 86);
    System.out.println("Adding value 87 to position [3,3].");
    sm.setValue(3, 3, 87);
    System.out.println("Adding value 88 to position [1,15].");
    sm.setValue(1, 15, 88);
    System.out.println("Adding value 89 to position [12,6].");
    sm.setValue(12, 6, 89);
    System.out.println("Adding value 90 to position [6,4].");
    sm.setValue(6, 4, 90);

    System.out.println();

    System.out.println("The entire matrix:");
    System.out.println();


    for (row = 1; row <= SNUMROWS; row++)
    {
      for (col = 1; col <= SNUMCOLS; col++)
      {
        entry = sm.getValue(row, col);
        if (entry == 0) System.out.print("  0");
        else
          System.out.print(" " + entry);
      } // end for col
      System.out.println();
    } // end for row

    System.out.println();

    System.out.println("Changing position [20,15] from 84 to 91.");
    sm.setValue(20, 15, 91);
    System.out.println("Position [20,15] contains " + sm.getValue(20, 15));
    System.out.println("Changing position [1,6] from 86 to 92.");
    sm.setValue(1, 6, 92);
    System.out.println("Position [1,6] contains " + sm.getValue(1, 6));
    System.out.println("Changing position [2,5] from 80 to 93.");
    sm.setValue(2, 5, 93);
    System.out.println("Position [2,5] contains " + sm.getValue(2, 5));
    System.out.println("Changing position [1,15] from 88 to 0.");
    sm.setValue(1, 15, 0);
    System.out.println("Position [1,15] contains " + sm.getValue(1, 15));
    System.out.println("Changing position [6,4] from 90 to 0.");
    sm.setValue(6, 4, 0);
    System.out.println("Position [6,4] contains " + sm.getValue(6, 4));

    System.out.println();

    System.out.println("The entire matrix:");
    System.out.println();

    for (row = 1; row <= SNUMROWS; row++)
    {
      for (col = 1; col <= SNUMCOLS; col++)
      {
        entry = sm.getValue(row, col);
        if (entry == 0) System.out.print("  0");
        else
          System.out.print(" " + entry);
      } // end for col
      System.out.println();
    } // end for row


    // Now, work with the large, random matrix.

    System.out.println();
    System.out.println("Large, Random Matrix of size " + lNumRows + " x "
        + lNumCols);
    System.out.println();

    // Add some random non-zero entries

    for (i = 0; i < NUMENTRIES; i++)
    {
      row = generator.nextInt(lNumRows) + 1;
      col = generator.nextInt(lNumCols) + 1;
      entry = generator.nextInt();

      SM.setValue(row, col, entry);

      if (SM.getValue(row, col) != entry)
      {
        System.out.println("Error: Position [" + row + "," + col
            + "] now contains " + SM.getValue(row, col)
            + " even though we just set it to " + entry);
      }
    } // for i

    System.out.println("Added " + NUMENTRIES + " non-zero entries.");
    System.out.println();

    // Access some random positions (should mostly be 0)

    System.out.println("Accessing " + NUMENTRIES + " random positions.");

    for (i = 0; i < 1000; i++)
    {
      row = generator.nextInt(lNumRows) + 1;
      col = generator.nextInt(lNumCols) + 1;
      entry = SM.getValue(row, col);
      if (entry != 0)
      {
        System.out.println("Position [" + row + "," + col + "] contains "
            + entry);
      }
    } // for i


    System.out.println();
    System.out.println("Test program completed successfully.");
  } // end method main

} // end class testSparseMatrix
