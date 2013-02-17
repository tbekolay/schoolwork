import java.util.*;
import java.io.*;

class Dictionary
{

  private class Element
  {
    public String word;
    public Element link;

    // Standard linked list constructor.
    Element(String new_word, Element new_link)
    {
      word = new_word;
      link = new_link;
    }
  } // end private class Element

  private final int TABLE_SIZE = 2000;
  // Our table. It consists of a list of pointers to the individual hash chains.
  // It's essentially a list of top pointers.
  private Element table[];

  // Our constructor will read in and hash the dictionary file,
  // so once the dictionary is created, we're ready to start the spell check.
  Dictionary()
  {
    int i;
    int hash_value;
    String read_word;
    FileReader theFile;
    BufferedReader in;

    // Start by initializing the table to a list of null pointers.
    table = new Element[TABLE_SIZE];
    for (i = 0; i < TABLE_SIZE; i++)
      table[i] = null;

    // Read the words into the table.
    try
    {
      theFile = new FileReader("words.txt");
      in = new BufferedReader(theFile);

      System.out.print("Loading dictionary");

      // Each line contains 1 word...
      read_word = in.readLine();
      while (read_word != null)
      {
        System.out.print(".");

        // Always work in lower case.
        read_word = read_word.toLowerCase();

        // The hash value gives us the index into the table,
        // which tells us the list to use for creating the new node.
        hash_value = hash(read_word);

        // Create a new node at the top of the appropriate list
        // (so this is our insert).
        table[hash_value] = new Element(read_word, table[hash_value]);

        // Get the next word to process.
        read_word = in.readLine();
      } // end while

      System.out.println();

      in.close();
    } // end try

    catch (IOException ex)
    {
      System.out.println("I/O error: " + ex.getMessage());
    }
  } // end Dictionary constructor

  // Try to find the given word in the dictionary.
  // Returns true if the word is present, false otherwise.
  public boolean lookup(String the_word)
  {
    boolean found = false;
    // Make sure we only work in lower case.
    String hash_word = the_word.toLowerCase();
    // We start our search by hashing to the appropriate list.
    int table_index = hash(hash_word);
    Element next = table[table_index];

    // Search for the word in the list.
    while (!found && next != null)
    {
      if (hash_word.equals(next.word)) found = true;
      else
        next = next.link;
    }

    return (found);
  } // end method lookup

  // Test method to see how the dictionary is working...
  public void print_table()
  {
    int i;
    Element next;

    for (i = 0; i < TABLE_SIZE; i++)
    {
      System.out.print("Chain " + i + " contains: ");

      next = table[i];
      while (next != null)
      {
        System.out.print(next.word + " ");
        next = next.link;
      }

      System.out.println("\n");
    } // end for
  } // end method print_table

  /*****************************************************************************
   * Private Methods
   ****************************************************************************/

  // we have a private hash function since using hashing is an implementation
  // detail
  // it returns the hash value for the given string
  private int hash(String the_word)
  {
    final int HASHING_CONSTANT = 13; // pick a prime!
    int i;
    int hash = (int) the_word.charAt(0);

    // use Horner's method to compute the hash efficiently
    // x^k-1 + a(x^k-2 + a(x^k-3 + ... + a(x2 + a(x1 + ax0))...))
    for (i = 1; i < the_word.length() - 1; i++)
    {
      hash += (int) the_word.charAt(i);
      hash *= HASHING_CONSTANT;
    }
    hash += (int) the_word.charAt(the_word.length() - 1);

    return (Math.abs(hash) % TABLE_SIZE);
  } // end method hash

} // end Dictionary class
