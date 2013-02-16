/**
 * QuickHelp - Displays a variety of poems or exits depending on user input.
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 2, question 3
 * @author Trevor Bekolay, 6796723
 * @version October 13, 2003
 */

import javax.swing.*;

public class QuickHelp
{
  public static void main(String[] args)
  {

    boolean keepGoing = true; // It is assumed that the user will keep going,
    // unless they enter a 5.
    String input;

    // This do-while loop will continue as long as keepGoing is true, which it
    // always is unless the user enters a 5.

    do
    {

      // Gets user input
      input = JOptionPane
          .showInputDialog("Poems For A Term\nPlease pick a topic:\n1. Study Time\n2. Exam Period\n3. After Class\n4. The Future\n5. exit");

      // Displays a different poem depending on the number entered. The nested
      // if else construct is used so that if statements are not unnecessarily
      // computed.

      if (input == "1") JOptionPane
          .showMessageDialog(
              null,
              "Study Time:\nYou work that you might keep pace with the earth and the soul of the earth... Work is love made visible.\n-Kahill Gibran, The Prophet");
      else if (inputNum == "2") JOptionPane
          .showMessageDialog(null,
              "Exam Period:\nApril is the cruellest month\n-T.S. Eliot, \"The Waste Land\"");
      else if (inputNum == "3") JOptionPane
          .showMessageDialog(
              null,
              "After Class:\nWine comes in at the mouth\nAnd love comes in at the eye;\nThat's all we shall know for truth\nBefore we grow old and die.\nI lift the glass to my mouth,\nI look at you, and I sigh.\n-William Butler Yeats, \"A Drinking Song\"");
      else if (inputNum == "4") JOptionPane
          .showMessageDialog(
              null,
              "The Future:\nTake the dream you’ve been having since you were a child, the one with open fields and the wind sounding.\n-Lorna Crozier, \"PACKING FOR THE FUTURE: INSTRUCTIONS\"");
      else

      // Entering a 5 sets keepGoing to false, causing the do-while loop to end.
      if (inputNum == "5")
      {
        JOptionPane.showMessageDialog(null,
            "Thank you for using Quick Help!\n\n*** End of program ***");
        keepGoing = false;
      }

      // If the user enters something that is not a number from 1 to 5, an
      // error-catching message will appear.
      else
        JOptionPane.showMessageDialog(null,
            "Please enter a number from 1 to 5.");
    } while (keepGoing);

    System.exit(0);
  }
}
