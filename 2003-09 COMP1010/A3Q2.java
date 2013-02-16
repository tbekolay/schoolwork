/**
 * GradeCalc - Calculate GPA's based on letter grades.
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 3, question 2
 * @author Trevor Bekolay, 6796723
 * @version November 1, 2003
 */


import javax.swing.*;

public class GradeCalc
{

  public static void main(String[] args)
  {

    String strStudentNumber, grade, firstName, lastName;
    int studentNumber, numGrades;
    double gradeTotal;
    boolean keepGoing = true, gradeInput;

    // This while loop continues until the user inputs -1. It has the bulk of
    // the program inside it, but will skip that section if a -1 is inputted.

    while (keepGoing)
    {

      // Resets variables to give initial values and to initialize the loop for
      // each time it executes.
      gradeInput = true;
      numGrades = 0;
      gradeTotal = 0;

      // Get user input.
      strStudentNumber = JOptionPane
          .showInputDialog("Plese enter a student number, or -1 to quit.");

      // Check for -1.
      if (strStudentNumber.compareToIgnoreCase("-1") == 0) keepGoing = false;
      else
      {

        studentNumber = Integer.parseInt(strStudentNumber);

        // Get user input.
        firstName = JOptionPane
            .showInputDialog("Please enter the student's first name.");
        lastName = JOptionPane
            .showInputDialog("Please enter the student's last name.");

        // This while loop allows the user to add in however many grades they
        // wish to enter. When a -1 is entered, again the loop will end.

        while (gradeInput)
        {

          grade = JOptionPane
              .showInputDialog("Plese enter a letter grade, or -1 when finished.");

          if (grade.compareToIgnoreCase("-1") == 0) gradeInput = false;
          // Calls the marksFromGrade method to determine numerical grade point
          // value, and sums it.
          else
          {
            gradeTotal = gradeTotal + marksFromGrade(grade);
            numGrades++;
          }
        }

        // Prints out all data.
        printStudentInfo(firstName, lastName, studentNumber, gradeTotal,
            numGrades);
        System.out.println();
      }
    }

    System.out.println("Thank you for using GradeCalc.\n\nEnd of processing.");

    System.exit(0);

  }

  /**
   * PURPOSE: Determines the numerical value associated with a letter grade.
   * @param INPUT grade - the letter grade the user enters
   * @return DOUBLE - Returns the numerical value
   */

  public static double marksFromGrade(String grade)
  {

    // Final values are used because these variables will not change in the
    // course of the program.
    final double APLUS_GRADE = 4.5, A_GRADE = 4.0, BPLUS_GRADE = 3.5, B_GRADE = 3.0, C_GRADE = 2.0, D_GRADE = 1.0, F_GRADE = 0.0;

    // This set of if statements merely associates a number to each letter
    // grade. It is useful to have this outside of the main program, to avoid
    // clutter.
    if (grade.compareToIgnoreCase("A+") == 0) return APLUS_GRADE;
    else if (grade.compareToIgnoreCase("A") == 0) return A_GRADE;
    else if (grade.compareToIgnoreCase("B+") == 0) return BPLUS_GRADE;
    else if (grade.compareToIgnoreCase("B") == 0) return B_GRADE;
    else if (grade.compareToIgnoreCase("C") == 0) return C_GRADE;
    else if (grade.compareToIgnoreCase("D") == 0) return D_GRADE;
    else
      return F_GRADE;

  }

  /**
   * PURPOSE: Prints out a mini-transcript of the student.
   * @param IN/OUT firstName - the first name, user inputted.
   * @param IN/OUT lastName - the last name, user inputted.
   * @param IN/OUT studentNumber - the students number, user inputted.
   * @param INPUT gradeTotal - running total of grade points.
   * @param INPUT numGrades - running total of number of grades inputted.
   * @return VOID
   */

  public static void printStudentInfo(String firstName, String lastName,
      int studentNumber, double gradeTotal, int numGrades)
  {

    double gpa;

    // Calculate GPA
    gpa = gradeTotal / numGrades;

    // Output all data
    System.out.println("Student Name " + firstName + " " + lastName);
    System.out.println("Student Number " + studentNumber);
    System.out.println("GPA: " + gpa);
    // Display a message concerning performance
    if (gpa > 3.0) System.out
        .println("Congratulations, you're an Honours student!");
    else if (gpa < 2.0) System.out.println("Warning, you might be failing.");

  }
}
