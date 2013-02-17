import java.io.*;
import java.util.*;

public class Table
{

  TableNode array[] = new TableNode[5437];

  public Table(String filename)
  {

    FileReader theFile; // The file to be read (character file).
    BufferedReader in; // Read text from a character-input stream.
    StringTokenizer tokens;
    String inLine; // A line of input from BufferedReader in.
    String word;
    int hash;

    try
    {

      theFile = new FileReader(filename);
      in = new BufferedReader(theFile);

      inLine = in.readLine();

      while (inLine != null)
      {

        tokens = new StringTokenizer(inLine);

        while (tokens.hasMoreTokens())
        {

          word = tokens.nextToken().toLowerCase();
          hash = hash(word);
          array[hash] = new TableNode(word, array[hash]);

        }

        inLine = in.readLine();

      }

      in.close();

    }

    catch (IOException ex)
    {
      System.out.println("I/O error: " + ex.getMessage());
    }

  }

  public static int hash(String input)
  {

    int i;
    final int NUM = 3;
    int product = 0;

    for (i = 0; i < input.length(); i++)
    {

      product += (int) input.charAt(i) * Math.pow(NUM, i);

    }

    return (product % 5437);

  }

  public boolean search(String input)
  {

    int hash;
    TableNode curr;
    boolean found = false;

    hash = hash(input);

    curr = array[hash];

    while (curr != null && !found)
    {
      if (curr.word.equals(input)) found = true;
      curr = curr.link;
    }

    return found;

  }

}

class TableNode
{

  public String word;
  public TableNode link;

  TableNode(String newWord, TableNode newLink)
  {
    word = newWord;
    link = newLink;
  }

}
