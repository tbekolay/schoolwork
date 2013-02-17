import java.text.*;
import java.util.Random;

/**
 * A5Q1 - derive some statistics and a histogram from large arrays
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 5, question 1
 * @author Trevor Bekolay, 6796723
 * @version Dec.3/03
 */
public class A5Q1
{
  public static void main(String[] args)
  {
    final int SIZE = 100000;
    final int LOW = 1;
    final int HIGH = 100;
    int[] array1;
    int[] array2;
    int[] freq;

    array1 = buildRandomArray(SIZE, LOW, HIGH);
    array2 = buildRandomArray(SIZE, LOW, HIGH);

    printNumberMatchingPairs(array1, array2);

    System.out.println("histogram for array1");
    freq = freqDist(array1, LOW, HIGH);
    printFreqDist(freq);

    System.out.println("histogram for array2");
    freq = freqDist(array2, LOW, HIGH);
    printFreqDist(freq);

    System.out.println("\nEnd of Processing");
    System.exit(0);
  }

  /**
   * PURPOSE: Generate an array of random int values taken from a Gaussian
   * (normal) distribution.
   * @param int SIZE - the size of the array
   * @param int LOW - lowest value to be generated
   * @param int HIGH - highest value to be generated
   * @return int [] array - the array of values
   */
  public static int[] buildRandomArray(int size, int low, int high)
  {
    int[] array = new int[size];

    // Get random int values from a normal distribution
    // with mean of (HIGH-LOW)/2 and std. dev. of (HIGH-LOW)/10.
    // Truncate the distribution so all values are in range [LOW..HIGH]
    int mean = (high - low) / 2;
    int std = (high - low) / 10;
    Random rand = new Random();
    for (int i = 0; i < size; i++)
    {
      int theScore = (int) (rand.nextGaussian() * std) + mean;
      array[i] = Math.min(Math.max(low, theScore), high);
    }
    return array;
  }// buildRandomArray

  /**
   * PURPOSE: Prints the number of matched pairs between the two arrays; a
   * matched pair being any value that is at the same index in both arrays.
   * @param int [] array1 - the first randomly created array
   * @param int [] array2 - the second randomly created array
   * @return void
   */
  public static void printNumberMatchingPairs(int[] array1, int[] array2)
  {
    // int Pairs must be initialized first, as it appears on the right side of
    // an equation
    int pairs = 0;

    // This for loop runs from 0 to the length of the array minus 1 (since 0
    // counts)
    for (int i = 0; i < array1.length; i++)
    {
      // When the value at the same index of both arrays is the same, pairs
      // increases by one.
      if (array1[i] == array2[i]) pairs++;
    }

    // Outputs data
    System.out.println("Matching pairs: " + pairs);
    System.out.println();

  }// printNumberMatchingPairs

  /**
   * PURPOSE: Determines the frequency of each value over the range of [LOW ..
   * HIGH]
   * @param int [] array - the array from which data will be extracted
   * @param int [] low - the lower extreme of the values to be found
   * @param int [] high - the higher extreme of the values to be found
   * @return int [] frequency - the array of frequency values
   */
  public static int[] freqDist(int[] array, int low, int high)
  {
    // declare a new array, frequency, which has length that is equal to the
    // difference between the boundaries
    int[] frequency = new int[high - low];

    // for each value of the passed array, increase the frequency array at index
    // equal to the value in the current place in the passed array.
    for (int i = 0; i < array.length; i++)
    {
      frequency[array[i] - low]++;
    }

    return frequency;

  }// freqDist

  /**
   * PURPOSE: Prints out a properly formatted histogram based on the frequency
   * array
   * @param int [] freq - the array of frequencies, from freqDist
   * @return void
   */
  public static void printFreqDist(int[] freq)
  {
    DecimalFormat form3 = new DecimalFormat("000");
    DecimalFormat form5 = new DecimalFormat("00000");

    // header
    System.out.println("  bin   (count)");

    // PARAM is the size of any one bin, and since there are 10 bins, it is the
    // length of the array divided by 10. Note that this only works for arrays
    // of sizes that are multiples of 10.
    final int PARAM = (freq.length + 1) / 10;
    int count;
    int numBlocks;

    // this for loops runs 10 times, as there are 10 bins. It displays the
    // information for one bin each time the loop is run through.
    for (int i = 0; i < 10; i++)
    {
      // outputs the lower and upper bounds of the current bin, based on how
      // many times the loop has been gone through and the PARAM value
      System.out.print(form3.format((PARAM * i) + 1) + ":"
          + form3.format(PARAM * (i + 1)) + " ");

      // sets count to 0 so that it is reset each time the loop is gone through.
      count = 0;

      // this for loop adds to count the frequency of all of the values in the
      // current bin.
      for (int j = (PARAM * i); j < (PARAM * (i + 1) - 1); j++)
      {
        count += freq[j];
      }

      // outputs count in a specific form.
      System.out.print("(" + form5.format(count) + ")");

      // since a block will be drawn for any part of 750, numBlocks will be
      // equal to count / 750 only when there is no reminder; for example, when
      // count is 0, we do not want to draw a block, but we do when count is 1.
      if (count % 750 == 0) numBlocks = (count / 750);

      // if numBlocks has a remainder when divided by 750, then count / 750 must
      // have some decimals that are truncated. We will add one block to make up
      // for these truncated decimals.

      else
        numBlocks = (count / 750) + 1;

      // this for loop prints a block based on the numBlocks value.
      for (int k = 0; k < numBlocks; k++)
      {
        System.out.print("=");
      }

      System.out.println();
    }

    System.out.println();

  }// printFreqDist

} // public class A5Q1
