/**
 * DigitAdder - Adds the digits of seven digit numbers
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 1, question 2
 * @author Trevor Bekolay, 6796723
 * @version September 29, 2003
 */

import javax.swing.*; // Needed for JOptionPane
public class DigitAdder
{

  public static void main(String[] args)
  {

    // Declare variables
    String name, studentNumber, input;
    int number, sum, digit;

    // Get user input
    name = JOptionPane.showInputDialog("Please enter your name.");
    studentNumber = JOptionPane
        .showInputDialog("Please enter your student number.");
    input = JOptionPane.showInputDialog("Please enter a seven digit number.");

    number = Integer.parseInt(input); // Convert string to integer

    // Sum the digits
    sum = 0;
    digit = number / 1000000; // Find first digit
    sum = sum + digit; // Add digit to sum
    digit = (number % 1000000) / 100000; // Find second digit
    sum = sum + digit;
    digit = (number % 100000) / 10000; // Find third digit
    sum = sum + digit;
    digit = (number % 10000) / 1000; // Find fourth digit
    sum = sum + digit;
    digit = (number % 1000) / 100; // Find fifth digit
    sum = sum + digit;
    digit = (number % 100) / 10; // Find sixth digit
    sum = sum + digit;
    digit = (number % 10); // Find seventh digit
    sum = sum + digit;

    // Display results
    JOptionPane.showMessageDialog(null, "Name: " + name + "\nStudent Number: "
        + studentNumber + "\n\nValue = " + number + "\nSum = " + sum
        + "\n\n*** End of program ***");

    // Make sure program exits correctly
    System.exit(0);
  }
}
