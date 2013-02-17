/**
 * TaxCalc - Calculates final sale prices of goods in three provinces
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 1, question 3
 * @author Trevor Bekolay, 6796723
 * @version September 29, 2003
 */

import javax.swing.*; // Needed for JOptionPane
public class TaxCalc
{
  public static void main(String[] args)
  {

    // Declare variables
    String name, studentNumber, input;
    double purchasePrice, gstTotal, abPST, abTotal, mbTotal, peiTotal, mbPST, peiPST;
    // Declare constants
    final double GST = 0.07, AB_PST = 0.0, MB_PST = 0.07, PEI_PST = 0.1;

    // Get user input
    name = JOptionPane.showInputDialog("Please enter your name.");
    studentNumber = JOptionPane
        .showInputDialog("Please enter your student number.");
    input = JOptionPane.showInputDialog("Please enter a purchase price.");

    purchasePrice = Double.parseDouble(input); // Convert string to double

    // Calculate GST
    gstTotal = purchasePrice * GST;
    gstTotal = Math.round(gstTotal * 100) / 100.0; // Round gstTotal

    // Calculate Alberta's total
    abPST = purchasePrice * AB_PST;
    abPST = Math.round(abPST * 100) / 100.0; // Round abPST
    abTotal = purchasePrice + abPST + gstTotal;
    abTotal = Math.round(abTotal * 100 + 5) / 100.0; // Round abTotal

    // Calculate Manitoba's total
    mbPST = purchasePrice * MB_PST;
    mbPST = Math.round(mbPST * 100) / 100.0; // Round mbPST
    mbTotal = purchasePrice + mbPST + gstTotal;
    mbTotal = Math.round(mbTotal * 100) / 100.0; // Round mbTotal


    // Calculate PEI's total
    peiPST = (purchasePrice + gstTotal) * PEI_PST;
    peiPST = Math.round(peiPST * 100) / 100.0; // Round peiPST
    peiTotal = purchasePrice + gstTotal + peiPST;
    peiTotal = Math.round(peiTotal * 100) / 100.0; // Round peiTotal


    // Display results in console
    System.out.println("Name: " + name + "\nNumber: " + studentNumber
        + "\n\nPurchase Price: " + purchasePrice + "\n\nGST: " + gstTotal
        + "\n\nAB PST: " + abPST + "\nAB Total: " + abTotal + "\n\nMB PST: "
        + mbPST + "\nMB Total: " + mbTotal + "\n\nPEI PST: " + peiPST
        + "\nPEI Total: " + peiTotal + "\n\n*** End of program ***");

    // Make sure program exits correctly
    System.exit(0);
  }
}
