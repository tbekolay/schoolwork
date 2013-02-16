import javax.swing.*;

/**
 * Class A4Q1. Generates predictions from a set of pre-defined words.
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 4, Question 1
 * @author Trevor Bekolay, 6796723
 * @version 1.0, 18-Nov-2003
 */
public class A4Q1
{
  /**
   * Our application. It will prompt the user for their name and make up
   * predictions until done.
   * @param args -- an array of command line options
   * @return void
   */
  public static void main(String args[])
  {
    // the text used to build our predictions
    final String firstWord = "sees wants   hears desires fears loathes needs";
    final String secondWord = "death love wealth poverty sickness danger travel  sadness  ";
    final String thirdWord = "coming";
    final String fourthWord = "tomorrow never  soon next  immediately";
    final String fifthWord = ". ! ? ...";
    final String sixthWord = "";
    final String company = "Madame Trewlaney's Crystal Gazing Corp";

    String name;
    String prediction;
    int response;

    name = JOptionPane.showInputDialog(null,
        "What's you're name? (Enter Trelawney to quit)", company,
        JOptionPane.QUESTION_MESSAGE);
    while (!name.equals("Trelawney"))
    {
      do
      {
        // build the prediction by randomly selecting words from our lists
        prediction = name;
        prediction += " " + extractWord(firstWord, randomIndex(1, 7));
        prediction += " " + extractWord(secondWord, randomIndex(1, 8));
        prediction += " " + extractWord(thirdWord, randomIndex(1, 1));
        prediction += " " + extractWord(fourthWord, randomIndex(1, 5));
        prediction += extractWord(fifthWord, randomIndex(1, 4));
        prediction += " " + extractWord(sixthWord, randomIndex(1, 2));

        JOptionPane.showMessageDialog(null, prediction, company,
            JOptionPane.INFORMATION_MESSAGE);

        response = JOptionPane.showConfirmDialog(null,
            "Would you like another prediction for " + name + "?", company,
            JOptionPane.YES_NO_OPTION);
      } while (response == JOptionPane.YES_OPTION);

      name = JOptionPane.showInputDialog(null,
          "What's you're name? (Enter Trelawney to quit)", company,
          JOptionPane.QUESTION_MESSAGE);
    }

    JOptionPane.showMessageDialog(null, "end of predictions", company,
        JOptionPane.INFORMATION_MESSAGE);
    System.exit(0);
  }

  /**
   * Generates random numbers given the passed range.
   * @param low -- the minimum value to generate for our random number
   * @param high -- the maximum value to generate for our random number
   * @return int -- a random number between low and high
   */
  static int randomIndex(int low, int high)
  {
    return (int) (Math.random() * (high - low + 1)) + low;
  }

  /**
   * Pulls a word from the passed list of words.
   * @param wordList -- the words to choose from, separated by spaces
   * @param wordIndex -- the word to extract, the first word is considered word 1
   * @return word -- the extracted word
   */
  static String extractWord(String wordList, int wordIndex)
  {
    String word = "";
    int count = 0, wordCount = 0;

    // This while loop looks at each character. The mother while loop looks for
    // the first non-whitespace character.
    while (count < wordList.length())
    {
      if (wordList.charAt(count) != ' ')
      {
        wordCount++; // When a non-whitespace character is found, the wordcount
                      // goes up.
        if (wordCount == wordIndex)
        {
          // If the wordcount is the same as the word we are trying to find, we
          // extract the word with this while loop.
          while (count < wordList.length())
          {
            if (wordList.charAt(count) != ' ')
            {
              word += wordList.charAt(count);
              count++;
            }
            else
              count = wordList.length();
          }
        }
        // If the word is not the one we are looking for, we pass over it,
        // continuing the mother loop once another whitespace character is
        // found.
        else
        {
          while (wordList.charAt(count) != ' ')
          {
            count++;
          }
        }
      }
      count++;
    }

    return word;
  }
}
