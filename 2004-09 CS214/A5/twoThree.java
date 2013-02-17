/**
 * twoThree - declares and fills a two-three tree based on input from a file.
 * Then searches for certain values, also inputted from a file.
 * @74.214 SECTION L02
 * @INSTRUCTOR Michael Zapp
 * @ASSIGNMENT Assignment 5
 * @AUTHOR Trevor Bekolay, 6796723
 * @VERSION December 5, 2004
 */

import java.io.*;

public class twoThree
{

  /**
   * PURPOSE: main reads in a file, TwoThreeInsert.txt and inserts all numbers
   * found in it into a 2-3 tree. It then searches for all numbers found in
   * TwoThreeSearch.txt.
   * @PARAM String[] args - Command line arguments, not used in this program.
   * @RETURN VOID
   */

  public static void main(String[] args)
  {

    Tree23 twoThree = new Tree23(); // The new 2-3 tree

    // Used for file input
    String read_line;
    FileReader theFile;
    BufferedReader in;

    System.out.println("74.214 Assignment 5, December 2004");
    System.out.println("Inserting into and searching a 2-3 tree.");
    System.out.println("(Student number 6796723, Section L02)");
    System.out.println();

    try
    {

      // Opens TwoThreeInsert.txt for input
      theFile = new FileReader("TwoThreeInsert.txt");
      in = new BufferedReader(theFile);

      read_line = in.readLine();

      // Reads in every line, inserting the values into the 2-3 tree.
      while (read_line != null)
      {
        System.out
            .println("Inserting " + read_line + " into a two-three tree.");
        twoThree.insert(Integer.parseInt(read_line));

        read_line = in.readLine();
      }

      // Opens TwoThreeSearch.txt for input
      theFile = new FileReader("TwoThreeSearch.txt");
      in = new BufferedReader(theFile);

      System.out.println();
      read_line = in.readLine();

      // Reads in every line, searching for each value in the 2-3 tree.
      while (read_line != null)
      {
        System.out.println("Searching for " + read_line + "...");

        // Outputs the results of the search
        if (twoThree.search(Integer.parseInt(read_line))) System.out
            .println(read_line + " found!");
        else
          System.out.println(read_line + " could not be found.");

        read_line = in.readLine();
      }

      in.close();

    }

    catch (IOException ex)
    {
      System.out.println("I/O error: " + ex.getMessage());
    }

    System.out.println("\nEnd of processing");

  }

}
