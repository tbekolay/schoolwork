/**
 * TempConverter - Converts Celsius temperatures to Kelvin and Fahrenheit
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 1, question 1
 * @author Trevor Bekolay, 6796723
 * @version September 29, 2003
 */

import javax.swing.*; // Needed for JOptionPane
public class TempConverter
{

  public static void main(String[] args)
  {

    // Declare variables
    String name, studentNumber, input;
    double celsius, fahrenheit, kelvin;
    // Declare constants
    final double FAHR_MULT = (9.0 / 5.0), FAHR_ADD = 32, KELV_ADD = 273.15;

    // Get user input
    name = JOptionPane.showInputDialog("Please enter your name.");
    studentNumber = JOptionPane
        .showInputDialog("Please enter your student number.");
    input = JOptionPane
        .showInputDialog("Please enter a temperature in Celsius.");

    celsius = Double.parseDouble(input); // Convert string to double

    // Convert celsius to others
    fahrenheit = celsius * FAHR_MULT + FAHR_ADD;
    kelvin = celsius + KELV_ADD;

    // Display results
    JOptionPane.showMessageDialog(null, "Name: " + name + "\nStudent Number: "
        + studentNumber + "\n\nC = " + celsius + "\nF = " + fahrenheit
        + "\nK = " + kelvin + "\n\n*** End of program ***");

    // Make sure program exits correctly
    System.exit(0);
  }
}

