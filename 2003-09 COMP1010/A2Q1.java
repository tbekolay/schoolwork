/**
 * ShortPalindrome - Takes in a word or string of digits and determines if it is
 * a palindrome
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 2, question 1
 * @author Trevor Bekolay, 6796723
 * @version October 12, 2003
 */

import javax.swing.*;

public class ShortPalindrome
{
  public static void main(String[] args)
  {

    String input;
    boolean palindrome = true; // Palindrome is assumed to be true unless
    // proven false
    int inputLength, counter;

    // Get user input and find length of it
    input = JOptionPane
        .showInputDialog("Please enter a single word or string of digits.");
    inputLength = input.length();

    /*
     * This while loop works such that, while a counter is less than the length
     * of the string, and a boolean is true, the characters in the string that
     * are at the counter - 1 (since index of 0 is still valid) are compared
     * with those at length - counter, so the first and last digit will be
     * compared, then the second and second last digit and so on. Both digits
     * are made into lowercase so that the chars will be equal no matter what
     * case they are originally in.
     */

    counter = 1;

    while (counter <= inputLength && palindrome)
    {

      if (!(Character.toLowerCase(input.charAt(counter - 1)) == Character
          .toLowerCase(input.charAt(inputLength - counter)))) palindrome = false;

      counter++;
    }


    // Displays the results of the previous loops. If two digits didn't match,
    // the boolean is false.
    if (palindrome) System.out.println(input
        + " is a palindrome!\n\n*** End of program ***");
    else
      System.out.println(input
          + " is not a palindrome.\n\n*** End of program ***");

    System.exit(0);

  }
}
