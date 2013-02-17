import java.io.*;
import java.util.*;

public class Spelling
{

  public static void main(String[] args)
  {

    String filename;
    FileReader theFile; // The file to be read (character file).
    BufferedReader in; // Read text from a character-input stream.
    StringTokenizer tokens;
    String inLine; // A line of input from BufferedReader in.
    String word;
    int hash;
    int lineNumber = 0;
    WordQueue misspelled = new WordQueue();

    System.out.println("74.214 Assignment 3, November 2004");
    System.out.println("Question 2: Spell checking.");
    System.out.println("(Student number 6796723, Section L02)");
    System.out.println();

    if (args.length < 2)
    {
      System.out
          .println("Please supply the name of the dictionary and input file as command line arguments.");
      return;
    }

    Table dictionary = new Table(args[0]);

    filename = args[1];

    try
    {

      theFile = new FileReader(filename);
      in = new BufferedReader(theFile);

      inLine = in.readLine();
      lineNumber++;

      while (inLine != null)
      {

        tokens = new StringTokenizer(inLine, " ,./:?!()-;+\"_");

        while (tokens.hasMoreTokens())
        {

          word = tokens.nextToken();
          if (!dictionary.search(word.toLowerCase()))
          {
            misspelled.enter(word, lineNumber);
          }

        }

        inLine = in.readLine();
        lineNumber++;

      }

      in.close();

    }

    catch (IOException ex)
    {
      System.out.println("I/O error: " + ex.getMessage());
    }

    System.out.println("There are a total of " + misspelled.size()
        + " invalid words: ");

    while (!misspelled.isEmpty())
    {
      System.out.println("Invalid word \"" + misspelled.front()
          + "\" found on lines " + misspelled.getLines());

      misspelled.leave();
    }

    System.out.println("Program completed succesfully.");
    System.exit(0);

  }

}
