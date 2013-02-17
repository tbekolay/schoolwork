/**
 * LongPalindrome - Takes in a phrase and determines if it is a palindrome
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 2, question 2
 * @author Trevor Bekolay, 6796723
 * @version October 12, 2003
 */

import javax.swing.*;

public class LongPalindrome
{
  public static void main(String[] args)
  {

    String input;
    boolean palindrome = true; // Palindrome is assumed to be true unless
    // proven otherwise.
    int inputLength, counter1, counter2;
    char char1, char2;

    // Get user input and find the length of it.
    input = JOptionPane.showInputDialog("Please enter a phrase.");
    inputLength = input.length();

    // Set counter1 to be the index at the start of the string, and counter2 the
    // index at the end.
    counter1 = 0;
    counter2 = inputLength - 1;
    // Sets char1 as the first digit of the string, char2 as the last.
    char1 = input.charAt(counter1);
    char2 = input.charAt(counter2);

    // The parent loop continues as long as we are still comparing chars on the
    // left with those on the right. If we reach the midpoint, or beyond, and
    // palindrome hasn't been proven false, then we know that it is a
    // palindrome.

    while (counter1 < counter2 && palindrome)
    {

      // This loop find the next char that is a digit or letter. If the char in
      // question is not a digit or letter, it moves to the next digit.

      while (!Character.isLetterOrDigit(char1))
      {
        counter1++;
        char1 = input.charAt(counter1);
      }


      // This loop is similar to the previous one, except it starts at the end
      // of the string and moves left if it finds a char that is not a letter or
      // digit.

      while (!Character.isLetterOrDigit(char2))
      {
        counter2--;
        char2 = input.charAt(counter2);
      }


      // Compares the two chars

      if (!(Character.toLowerCase(char1) == Character.toLowerCase(char2))) palindrome = false;

      // Moves the chars to be considered one to the right and left
      // respectively.

      counter1++;
      char1 = input.charAt(counter1);
      counter2--;
      char2 = input.charAt(counter2);

    }


    // Displays results.

    if (palindrome) System.out.println(input
        + " is a palindrome!\n\n*** End of program ***");
    else
      System.out.println(input
          + " is not a palindrome.\n\n*** End of program ***");

    System.exit(0);

  }
}
