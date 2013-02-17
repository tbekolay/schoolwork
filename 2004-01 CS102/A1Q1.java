/**
 * A1Q1 - Parses a text file for individual words, which are outputted to
 * another text file.
 * 74.102 SECTION L02
 * INSTRUCTOR Prof. Terry Andres
 * ASSIGNMENT Assignment 1, question 1
 * @author Trevor Bekolay, 6796723
 * @version January 29th, 2004
 */


import java.io.*;
import java.util.*;


public class A1Q1
{

  /**
   * PURPOSE: This main method is a skeleton. Most of the processing is done by
   * methods.
   * @param String[] args, which is not used in this program.
   * @return VOID
   */


  public static void main(String[] args)
  {

    String[] words;
    FileReader inFile;
    BufferedReader ins;

    // The try statement is needed in case an input/output error is thrown.
    try
    {

      // Assign1.txt is prepped for input.
      inFile = new FileReader("Assign1.txt");
      ins = new BufferedReader(inFile);

      // The words array is given a value of the number of words in the text
      // file. In this way, if there are no repetitions of words, the array will
      // be full.
      words = new String[countWords(ins)];

      // The stream is closed as we have gotten to the end.
      ins.close();

      // Assign1.txt is prepped for input again.
      inFile = new FileReader("Assign1.txt");
      ins = new BufferedReader(inFile);

      // The fillArray method is called to fill the words array with words.
      fillArray(ins, words);
      // The stream is closed
      ins.close();

      // The writeFile method is called. It handles the output.
      writeFile(words);

    }// Close try

    // The catch block catches any exceptions thrown by the try block.
    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }// Close catch


  }// Close main


  /**
   * PURPOSE: This method fills the words array with words from the text file.
   * @param BufferedReader ins: this enables this method to read from the file.
   * @param String[] words: The words array is the one being modified.
   * @return VOID
   */

  private static void fillArray(BufferedReader ins, String[] words)
  {

    String line;
    String input;
    int wordCount = 0;
    StringTokenizer strToken;

    try
    {

      // Read in one line first, so that we can check if the line is null every
      // time.
      line = ins.readLine();

      // This while loop continues until the end of the file. For each line, the
      // number of tokens are counted. Then each token is checked to see if it
      // is already in the array. If it is not in the array, it gets added.
      while (line != null)
      {

        strToken = new StringTokenizer(line, " ,\":;.()");

        for (int count = 0; count < strToken.countTokens(); count++)
        {
          input = strToken.nextToken();
          if (!hasMatch(words, input)) words[wordCount++] = input;
        }// for

        line = ins.readLine();

      }// while
    }// try

    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }// catch

  }// fillArray

  /**
   * PURPOSE: The countWords method counts the total number of words in the text
   * file.
   * @param BufferedReader ins: This allows the method to read from the file.
   * @return int: The number of words in the file.
   */

  private static int countWords(BufferedReader ins)
  {

    StringTokenizer strToken;
    String line;
    int numWords = 0;

    try
    {

      line = ins.readLine();

      // This loop simply counts the number of tokens in each line using the
      // .countTokens() method. The loop continues until there are no more
      // lines.
      while (line != null)
      {
        strToken = new StringTokenizer(line, " ,\":;.()");
        numWords += strToken.countTokens();
        line = ins.readLine();
      }// while

    }// try

    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }// catch

    return numWords;

  }// countWords

  /**
   * PURPOSE: The hasMatch method checks the existing words array to see if the
   * word in question is already in the array.
   * @param String[] array: The array in question. Named "array" for possible
   *          universality.
   * @param String input: The word being tested
   * @return boolean: False if the word is not found, true if it is.
   */

  private static boolean hasMatch(String[] array, String input)
  {

    // This for loop looks through each entry to find a match for the input,
    // ignoring the case.
    for (int count = 0; count < array.length; count++)
    {
      // This if condition is for the first string being checked; if this was
      // not present, the na NullPointerException may occur.
      if (array[count] == null) return false;
      else if (array[count].equalsIgnoreCase(input)) return true;
    }// for

    return false;

  }// hasMatch

  /**
   * PURPOSE: This method writes the array to a file.
   * @param String[] array: The words array, named array again for possible
   *          universality.
   * @return VOID
   */

  private static void writeFile(String[] array)
  {

    FileWriter outFile;
    PrintWriter outs;

    try
    {
      outFile = new FileWriter("A1Q1.txt");
      outs = new PrintWriter(outFile);

      // This for loop prints a single word on a single line. It will not write
      // anything for a null value, which most of the array may be, due to large
      // amounts of repetiton in the text file.
      for (int count = 0; count < array.length; count++)
      {
        if (array[count] != null) outs.println(array[count]);
      }// for
      outs.close();
    }// try

    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }// catch

  }// writeFile

}// A1Q1
