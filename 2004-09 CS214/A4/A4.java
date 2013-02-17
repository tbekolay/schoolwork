// This program provides a spell checker using hashing.
// It reads in the dictionary file (unsortedWords.txt)
// and then loads a document file (photoshopPrimer.txt) and
// performs a spell check on all the words in the document.

import java.util.*;
import java.io.*;

public class A4
{
  public static void main(String args[])
  {
    // Create (and read in) our dictionary.
    Dictionary dictionary = new Dictionary();
    // Hold the invalid words and their line numbers.
    WordQueue bad_words = new WordQueue();
    int line = 0;
    int invalid_words = 0;
    String read_line;
    FileReader theFile;
    BufferedReader in;
    // Used to extract the words in a line of text.
    String token;
    StringTokenizer tokens;

    // Read the document 1 line at a time.
    try
    {
      theFile = new FileReader("photoshopPrimer.txt");
      in = new BufferedReader(theFile);

      System.out.println("Checking photoshopPrimer.txt");

      // Process each line in turn.
      read_line = in.readLine();
      while (read_line != null)
      {
        line++;

        // Use the string tokenizer to split the line into words
        // --- using punctuation.
        tokens = new StringTokenizer(read_line, " / , . ! ? ; : \" ) ( * - ");

        while (tokens.hasMoreElements())
        {
          token = tokens.nextToken();

          // See if the word is in the dictionary and issue an error if not.
          if (!dictionary.lookup(token))
          {
            bad_words.enter(token, line);
            invalid_words++;
          }
        } // end while (tokens.hasMoreElements() )

        // Get the next word to process.
        read_line = in.readLine();
      } // end while (read_line != null)

      in.close();
    } // end try

    catch (IOException ex)
    {
      System.out.println("I/O error: " + ex.getMessage());
    }

    System.out.println("There are a total of " + invalid_words
        + " invalid words:");

    bad_words.print();

    System.out.println("\nEnd of processing");
  } // end main method
} // end class a3q2
