/**
 * Sorts - uses a variety of sorts to sort an array of data, testing each for
 * speed
 * @74.214 SECTION L02
 * @INSTRUCTOR Michael Zapp
 * @ASSIGNMENT Assignment #1
 * @AUTHOR Trevor Bekolay, 6796723
 * @VERSION October 11, 2004
 */

import java.util.*;
import java.io.*;

public class Sorts
{

  /**
   * PURPOSE: Main calls the various sort methods and times them.
   * @PARAM INPUT args: Not used in this program.
   * @RETURN VOID
   */

  public static void main(String[] args)
  {

    final String FILENAME = "A1Data.txt"; // filename of file containing array
    // elements
    int[] array; // array to be sorted
    long timestamp;

    System.out
        .println("74.214 Assignment 1.  September 2004.\nSorting with insertion sort, merge sort and quick sort.\n(Student number 6796723, Section L02)\n");

    // Calls and times the insertion sort and outputs the time and whether or
    // not it is sorted.
    array = fillArray(FILENAME);

    timestamp = new Date().getTime();
    array = insertionSort(array, 0, array.length - 1);
    timestamp = new Date().getTime() - timestamp;

    System.out.println("Time for insertion sort: " + timestamp
        + " milliseconds.");

    if (checkSort(array, 0, array.length - 1)) System.out
        .println("Insertion sort successfully sorted " + array.length
            + " elements.\n");
    else
      System.out.println("Insertion sort unsuccessfully sorted " + array.length
          + " elements.\n");

    // Calls and times the merge sort and outputs the time and whether or not it
    // is sorted.
    array = fillArray(FILENAME);

    timestamp = new Date().getTime();
    array = mergeSort(array, 0, array.length - 1);
    timestamp = new Date().getTime() - timestamp;

    System.out.println("Time for merge sort: " + timestamp + " milliseconds.");

    if (checkSort(array, 0, array.length - 1)) System.out
        .println("Merge sort successfully sorted " + array.length
            + " elements.\n");
    else
      System.out.println("Merge sort unsuccessfully sorted " + array.length
          + " elements.\n");

    // Calls and times the quick sort and outputs the time and whether or not it
    // is sorted.
    array = fillArray(FILENAME);

    timestamp = new Date().getTime();
    array = quickSort(array, 0, array.length - 1);
    timestamp = new Date().getTime() - timestamp;

    System.out.println("Time for quick sort: " + timestamp + " milliseconds.");

    if (checkSort(array, 0, array.length - 1)) System.out
        .println("Quick sort successfully sorted " + array.length
            + " elements.\n");
    else
      System.out.println("Quick sort unsuccessfully sorted " + array.length
          + " elements.\n");

    System.out.println("Program completed normally.");
    System.exit(0);

  }

  /**
   * PURPOSE: Main calls the various sort methods and times them.
   * @PARAM INPUT args: Not used in this program.
   * @RETURN VOID
   */

  public static int[] fillArray(String fileName)
  {

    int i = 0; // counter
    int k; // for loop variable
    int[] array = new int[10000]; // array created to intially accept values
    // from the input file
    int[] finalArray = null; // final array to be returned
    FileReader inFile;
    BufferedReader ins;
    StringTokenizer tokenizer;
    String nextLine;
    String temp;

    try
    {
      // prepared the file for input
      inFile = new FileReader(fileName);
      ins = new BufferedReader(inFile);

      nextLine = ins.readLine();

      // iterates through the entire input file
      while (nextLine != null)
      {

        // tokenizes the current line
        tokenizer = new StringTokenizer(nextLine);

        // changes every token in the line to an int and places it in the
        // unsorted array
        while (tokenizer.hasMoreTokens())
        {
          array[i] = Integer.parseInt(tokenizer.nextToken());
          i++; // counter for the number of elements added
        }

        nextLine = ins.readLine();

      }

      // defines the final array for the number of items that was actually added
      // to the intial array.
      finalArray = new int[i];

      // fills the final array with the elements in the initial array.
      for (k = 0; k < i; k++)
      {
        finalArray[k] = array[k];
      }

      ins.close();
    }

    catch (IOException ex)
    {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }

    return finalArray;

  }

  /**
   * PURPOSE: insertionSort iteratively sorts the passed array in ascending
   * order.
   * @PARAM INPUT array: The array of items to be sorted
   * @INPUT left: The left bound of the section of 'array' to be sorted.
   * @INPUT right: The right bound of the section of 'array' to be sorted.
   * @RETURN the sorted array
   */

  public static int[] insertionSort(int[] array, int left, int right)
  {

    int i, j; // for loop variables
    int temp;

    // interates from the second element to the last, placing current element in
    // the correct place
    for (i = left + 1; i <= right; i++)
    {

      temp = array[i]; // pulls the current element out of the array
      j = i;

      // moves elements before the current element that are larger than it
      // forward
      // stops when a smaller element is found or if at the start of the array
      while (j > left && temp < array[j - 1])
      {
        array[j] = array[j - 1];
        j--;
      }

      array[j] = temp;

    }

    return array;

  }

  /**
   * PURPOSE: mergeSort recursively sorts the passed array in ascending order by
   * splitting up the array into smaller subarrays and merging them.
   * @PARAM INPUT array: The array of items to be sorted
   * @INPUT left: The left bound of the section of 'array' to be sorted.
   * @INPUT right: The right bound of the section of 'array' to be sorted.
   * @RETURN the sorted array
   */

  public static int[] mergeSort(int[] array, int left, int right)
  {

    int mid; // the point at which the array is split into two subarrays

    // if there are less than 1 elements, nothing happens
    // if there is more than 1 element, the array is split in the middle
    // each subarray is recursively sorted then merged together
    if (right - left > 0)
    {
      mid = (right + left) / 2;

      mergeSort(array, left, mid);
      mergeSort(array, mid + 1, right);

      merge(array, left, mid, right); // calls the merge method


      return array;
    }

    else
    {
      return array;
    }

  }

  /**
   * PURPOSE: merge is a process used by mergeSort that merges two subarrays of
   * a passed array based on 3 indices.
   * @PARAM INPUT array: The array of items to be sorted
   * @INPUT left: The left bound of the first subarray to be merged.
   * @INPUT mid: The index that separates the left subarray from the right.
   * @INPUT right: The right bound of the second subarray to be merged.
   * @RETURN the array with the subarrays merged
   */

  public static int[] merge(int[] array, int left, int mid, int right)
  {

    int i = left; // counter, begins at left because it is an index
    int leftCount = 0;
    int midCount = 1; // initialized at 1 because array[mid] is part of the
    // first subarray
    int[] newArray = new int[array.length]; // a new temporary array

    // this while loop fills newArray with sorted ints between left and right
    while (i <= right)
    {
      // if the number current being pointed to in the first subarray is greater
      // than
      // the one pointed to in the second subarray
      if (array[left + leftCount] > array[mid + midCount])
      {
        // the value in the second subarray is added to newArray
        newArray[i] = array[mid + midCount];
        midCount++;

        // if the end of the second subarray has been reached:
        if (mid + midCount > right)
        {

          // fill the rest of newArray with the elements from the first
          // subarray.
          while (left + leftCount <= mid)
          {
            i++;
            newArray[i] = array[left + leftCount];
            leftCount++;
          }
        }
      }

      // in the other case
      else
      {
        // the value in the first subarray is added to newArray
        newArray[i] = array[left + leftCount];
        leftCount++;

        // if the end of the first subarray has been reached:
        if (left + leftCount == mid + 2)
        {

          // fill the rest of newArray with the elements from the second
          // subarray.
          while (mid + midCount <= right)
          {
            newArray[i] = array[mid + midCount];
            i++;
            midCount++;
          }
        }
      }

      i++;
    }

    // since we no longer have to keep the elements of array in the same order
    // we can replace the elements from left to right in array with the now
    // sorted
    // contents of newArray
    for (i = left; i <= right; i++)
    {
      array[i] = newArray[i];
    }

    return array;

  }

  /**
   * PURPOSE: quickSort recursively sorts the passed array in ascending order by
   * dividing the array and later subarrays into bigs and smalls based on the
   * value of pivot and sorts the bigs and smalls.
   * @PARAM INPUT array: The array of items to be sorted
   * @INPUT left: The left bound of the section of 'array' to be sorted.
   * @INPUT right: The right bound of the section of 'array' to be sorted.
   * @RETURN the sorted array
   */

  public static int[] quickSort(int[] array, int left, int right)
  {

    int i; // for loop variable
    int temp;
    int pivot;
    int endSmalls = left; // the end of the smalls, which begins at left

    // if there is more than one element in the subarray
    if (right - left > 0)
    {
      // then we define the pivot as the first element in that subarray
      pivot = left;

      // this for loop separates the subarray into the smalls and the bigs based
      // on how they compare to the pivot
      for (i = left + 1; i <= right; i++)
      {
        if (array[i] < array[pivot])
        {
          endSmalls++;
          temp = array[i];
          array[i] = array[endSmalls];
          array[endSmalls] = temp;
        }
      }

      // finally it switches the pivot with the end of the smalls so that the
      // pivot
      // is in between the bigs and smalls
      temp = array[pivot];
      array[pivot] = array[endSmalls];
      array[endSmalls] = temp;

      // we then recursively quickSort the bigs and the smalls
      quickSort(array, left, endSmalls);
      quickSort(array, endSmalls + 1, right);
    }

    return array;

  }

  /**
   * PURPOSE: checkSort verifies that the passed array is in ascending order
   * from the 'left' to the 'right' position in the array.
   * @PARAM INPUT array: The array of items to be checked.
   * @INPUT left: The left bound of the section of 'array' to be checked.
   * @INPUT right: The right bound of the section of 'array' to be checked.
   * @RETURN VOID
   */

  public static boolean checkSort(int[] array, int left, int right)
  {

    boolean sorted = true; // we assume it is sorted until we find an unsorted
    // element
    int i; // for loop variable

    // this for loop looks at every element after the first to ensure it is
    // bigger
    // than the element before it.
    for (i = left + 1; i <= right; i++)
    {
      // if an element is smaller than the one before it, we return false
      if (array[i] < array[i - 1]) sorted = false;
    }

    return sorted;

  }

}
