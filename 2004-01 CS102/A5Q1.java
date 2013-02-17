/**
 * A5Q1 - Prompts for a word or phrase and returns whether or not it is a
 * palindrome.
 * 74.101 SECTION L02
 * INSTRUCTOR T. Andres
 * ASSIGNMENT Assignment 5, question 1
 * @author Trevor Bekolay, 6796723
 * @version April 3, 2004
 */

import javax.swing.*;

public class A5Q1
{

  /**
   * PURPOSE: main is the skeleton of the program; it prompts for a string, and
   * asks if the user would like to continue.
   * @param String[] args; not used in this program.
   * @return VOID
   */

  public static void main(String[] args)
  {

    boolean cont = true;
    String input;

    // This while loop continues to prompt the user for phrases until the user
    // presses "No" or "Cancel" on the confirm dialog box.
    while (cont)
    {

      input = JOptionPane
          .showInputDialog("Please enter the word or phrase you would like to be tested.");

      // This calls the recursive palindrome method, and outputs the answer
      // accordingly.
      if (palindrome(input)) System.out.println("I am of the opinion that \""
          + input + "\" is a palindrome!");
      else
        System.out.println("Methinks that \"" + input
            + "\" is not a palindrome.");

      if (JOptionPane.showConfirmDialog(null,
          "Would you like to choose another word?") != 0)
      {
        cont = false;
        System.out.println("Program ended normally.");
        System.exit(0);
      }
    }
  }

  /**
   * PURPOSE: palindrome determines if the inputted String is indeed a
   * palindrome.
   * @param String input: This is the string that will be tested to see if it is
   *          a palindrome.
   * @return boolean: true if it is a palindrome, false if it is not.
   */

  public static boolean palindrome(String input)
  {

    char first;
    char last;
    int length = input.length();

    // If we determine that the length of the String is either 0 or 1, then we
    // know that it is a palindrome, as it has successfully been reduced to
    // either 0 or 1 characters, and any one character is a palindrome.
    if (length == 0 || length == 1) return true;

    // If we must do further operations, we first pull the first and last
    // characters from the String. We cast both characters to lower case to
    // avoid problems with upper and lower case letters.
    first = Character.toLowerCase(input.charAt(0));
    last = Character.toLowerCase(input.charAt(length - 1));

    // NOTE: BONUS SECTION
    // If one of the characters that is pulled from the string is not a letter,
    // we take the substring of the rest of the string, so everything except for
    // the non-letter chracter, and then test that substring to see if it is a
    // palindrome.
    if (!Character.isLetter(first))
    {
      input = input.substring(1, length);
      return palindrome(input);
    }
    if (!Character.isLetter(last))
    {
      input = input.substring(0, (length - 1));
      return palindrome(input);
    }
    // END BONUS SECTION

    // If the first character equals the last character, then we can remove
    // those characters from the string by taking the substring of what is left.
    // We can then test that substring to see if it is a palindrome. Eventually,
    // either the first and last characters will not equal each other, or the
    // string will be reduced to either one or zero characters.
    if (first == last)
    {
      input = input.substring(1, (length - 1));
      return palindrome(input);
    }
    else
      return false;
  }
}
