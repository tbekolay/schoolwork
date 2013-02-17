/**
 * A1Q2 - Changes a text file to one that has the closest nubmer of characters
 * on a line to a particular value.
 * 74.102 SECTION L02
 * INSTRUCTOR Prof. Terry Andres
 * ASSIGNMENT Assignment 1, question 2
 * @author Trevor Bekolay, 6796723
 * @version January 29th, 2004
 */

import java.io.*;
import java.util.*;

public class A1Q2
{

  /**
   * PURPOSE: This main method is all of the code. The program is too short to
   * warrant the use of multiple methods.
   * @param String[] args, which is not used in this program.
   * @return VOID
   */

  public static void main(String[] args)
  {

    final int LINE_LIMIT = 80;
    StringTokenizer strToken;
    String output = "";
    String tempWord;
    String line;
    FileReader inFile;
    BufferedReader ins;
    FileWriter outFile;
    PrintWriter outs;

    // The try block is used again in case of possible thrown exceptions.
    try
    {

      // Both input and output are prepared.
      inFile = new FileReader("Assign1.txt");
      ins = new BufferedReader(inFile);
      outFile = new FileWriter("A1Q2.txt");
      outs = new PrintWriter(outFile);

      line = ins.readLine();

      // This while loop ensures that all lines of the text file will be
      // examined.
      while (line != null)
      {

        // If the line currently being examined is a blank line, then the
        // current output string is outputted, another line is printed, and the
        // output string is reset to "".
        if (line.equals(""))
        {
          outs.println(output);
          outs.println();
          output = "";
        }// if
        // If the line current being examined is not a blank line, then it is
        // split into tokens.
        else
        {
          strToken = new StringTokenizer(line, " ");
          while (strToken.hasMoreTokens())
          {
            tempWord = strToken.nextToken();
            // If the next token will put it over the previously defined limit
            // per line, then the output line will be outputted, then reset to
            // the word that would have put it over the limit.
            if (output.length() + tempWord.length() >= LINE_LIMIT)
            {
              outs.println(output);
              output = tempWord + " ";
            }// if
            else
            {
              output = output + tempWord + " ";
            }// else
          }// while
        }

        line = ins.readLine();

      }// while

      // If there is anything left over in the output string, it will be output
      // here.
      outs.println(output);


      // The streams are closed so that the program will operate normally.
      ins.close();
      outs.close();
    }// try

    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }// catch

  }// main

}// A1Q2
