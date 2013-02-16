/**
 * ShapeDraw - Draws customizable rectangles and triangles.
 * 
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 3, question 1
 * @author Trevor Bekolay, 6796723
 * @version November 1, 2003
 */

import javax.swing.*;

public class ShapeDraw
{

  /**
   * PURPOSE: Draws a rectangle of user defined dimensions.
   * @return VOID
   */

  public static void drawRectangle()
  {

    String strHeight, strWidth;
    int height, width, num1, num2;

    // Get user input for dimensions of rectangle
    strHeight = JOptionPane
        .showInputDialog("Please input the height of desired rectangle.");
    strWidth = JOptionPane
        .showInputDialog("Please input the width of desired rectangle.");

    // Parse into integer
    height = Integer.parseInt(strHeight);
    width = Integer.parseInt(strWidth);

    System.out.println("Rectangle: Height " + height + ", Width " + width
        + "\n");

    // These nested for loops print *'s in a rectangle shape. The width defines
    // how many times the second loop executes, which adds a * to a line, and
    // the height defines how many times the first loop executes, which starts a
    // new line and executes the second loop.

    for (num1 = 0; num1 < height; num1++)
    {
      for (num2 = 0; num2 < width; num2++)
      {
        System.out.print("*");
      }
      System.out.println();
    }
    System.out.println();

  }

  /**
   * PURPOSE: Draws a triangle of user defined height.
   * @return VOID
   */

  public static void drawTriangle()
  {

    String strHeight;
    int height, num1, num2;

    // Get user input for height of triangle.
    strHeight = JOptionPane
        .showInputDialog("Please input the height of desired triangle.");

    height = Integer.parseInt(strHeight);

    System.out.println("Triangle: Height " + height + "\n");

    // These nested for loops both execute for as many times as the height. The
    // second loop prints a * when the second counter, or the column number, is
    // greater than or equal to the height minus the first counter, or the
    // height minus the row number. The first loop starts a new line and
    // executes the second loop.

    for (num1 = 1; num1 <= height; num1++)
    {
      for (num2 = 0; num2 < height; num2++)
      {
        if (num2 >= height - num1) System.out.print("*");
        else
          System.out.print(" ");
      }
      System.out.println();
    }
    System.out.println();

  }


  public static void main(String[] args)
  {

    String input;
    boolean keepGoing = true;

    // This while loop executes as long as keepGoing does not turn to false,
    // which it does when "Q" is entered. This skeleton calls the supporting
    // methods depending on user input.
    while (keepGoing)
    {

      // Get user input.
      input = JOptionPane
          .showInputDialog("Would you like to draw a rectangle or a triangle?  Enter q to quit.");

      // If the user enters "rectangle," it calls the drawRectangle method, and
      // the same for triangles.
      if (input.compareToIgnoreCase("rectangle") == 0) drawRectangle();
      else if (input.compareToIgnoreCase("triangle") == 0) drawTriangle();
      else
      // If q is entered, the while loop condition is not met.
      if (input.compareToIgnoreCase("q") == 0) keepGoing = false;
      else
        System.out.println("Please enter rectangle, triangle, or q.");
    }

    System.out.println("Thank you for using ShapeDraw.\n\nEnd of processing.");

    System.exit(0);

  }
}
