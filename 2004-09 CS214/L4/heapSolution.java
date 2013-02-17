import java.io.*;
import java.util.*;

public class heapSolution
{

  int[] heapArray; // The array containing the heap.
  int heapSize; // The number of elements currently stored in the heap.


  /*******************************************************************
  * CONSTRUCTOR                                                      *
  *                                                                  *
  * Purpose: Find out how many elements to put into the array        *
  *          (first number in the input file), create a heapArray    *
  *          of that size, and then read the elements into the       *
  *          heapArray.  The input file is "heapSortData.txt".       *
  *******************************************************************/
  public heapSolution( )
  {
    FileReader theFile; // The file to be read (character file).
    BufferedReader in;  // Read text from a character-input stream.
    String inLine;   // A line of input from BufferedReader in.
    String[] tokens; // the elements from a line of input.
    
    // Read the data to be inserted into the heap.
    
    try
    {
      theFile = new FileReader( "heapSortData.txt" );
      in = new BufferedReader( theFile );
      
      // Get the number of elements to read in and allocate
      // a heapArray of that size.
      
      inLine = in.readLine();
      tokens = inLine.split( "\\s+" );
      heapArray = new int[ Integer.parseInt( tokens[0] ) ];
      heapSize = 0;
      
      // Read the elements into the heapArray.
      
      inLine = in.readLine();
      
      while (inLine != null)
      {
        
        // Parse the current line of input.
        // Each line can contain more than one element.
        
        tokens = inLine.split( "\\s+" );;
        
        for (int i=0 ; i<tokens.length ; i++ )
        {
          // Get the next element from the current line of input.
          
          heapArray[ heapSize ] =  Integer.parseInt( tokens[i] );
          heapSize++;
          
        } 
        
        inLine = in.readLine();
        
      } // end while (inLine != null)
      
      in.close();
      
    } 
    
    catch (IOException ex)
    {
      System.out.println( "I/O error: " + ex.getMessage() );
    }
  }  

  /**************************************************************
  * retrieveMax                                                 *
  *                                                             *
  * Purpose: Remove and return the maximum value from the heap. *
  *                                                             *
  * Returns: The maximum value stored in the heap.              *
  **************************************************************/
  public int retrieveMax( )
  {
    int result = heapArray[0];

    heapArray[0] = heapArray[heapSize-1];
    heapSize--;
    siftDown( 0 );

    return result;
  
  }



  /***************************************************************
  * siftDown                                                     *
  *                                                              *
  * Purpose: The value in heapArray[i] may no longer be heap-    *
  *          ordered --- it may be smaller than one or both of   *
  *          its children.  Heapify the heap --- restore heap-   *
  *          heap order.                                         *
  * Param i: Input parameter                                     *
  *          The index of the element that may have messed up    *
  *          the heap ordering.                                  *
  ***************************************************************/
  private void siftDown( int i )
  {
    int j = i;
    int rcIndex; // index of the right child of heapArray[j]
    int lcIndex; // index of the left child of heapArray[j]
    boolean done = false;
    int temp;
    
    while (!done)
    {
      rcIndex = rightChild( j );
      lcIndex = leftChild( j );
      if (rcIndex < heapSize)
      {
        // heapArray[j] has two children
        
        if (heapArray[lcIndex] > heapArray[rcIndex])
        {
          // heapArray[j]'s left is larger than its right child.
          
          if (heapArray[j] > heapArray[lcIndex])
          {
            // heapArray[j] is larger than both its children.
            
            done = true;
          }
          else
          {
            // heapArray[j] is NOT larger than both its children.
            // Exchange heapArray[j] with its largest child,
            // its left child.
            
            temp = heapArray[j];
            heapArray[j] = heapArray[lcIndex];
            heapArray[lcIndex] = temp;
            j = lcIndex;
          }
        }
        else
        {
          // heapArray[j]'s left child is NOT larger than its right child
          
          if (heapArray[j] > heapArray[rcIndex])
          {
            // heapArray[j] is larger than both its children.
            
            done = true;
          }
          else
          {
            // heapArray[j] is NOT larger than both its children.
            // Exchange heapArray[j] with the larger of its two children,
            // its right child.
            
            temp = heapArray[j];
            heapArray[j] = heapArray[rcIndex];
            heapArray[rcIndex] = temp;
            j = rcIndex;
          }
        }
      }
      else if (lcIndex < heapSize)
      {
        // heapArray[j] has only one child --- a left child.
        
        if (heapArray[j] > heapArray[lcIndex])
        {
          // Already heap-ordered!
          
          done = true;
        }
        else
        {
          // Exchange with its larger child.
          
          temp = heapArray[j];
          heapArray[j] = heapArray[lcIndex];
          heapArray[lcIndex] = temp;
          j = lcIndex;
        }
      }
      else
      {
        // No children --- it's a leaf!
        
        done = true;
      }
    } // end while
  } // end method siftDown
  
  /**************************************************************
  * rightChild                                                  *
  *                                                             *
  * Purpose: Compute the index of the right child of            *
  *          heapArray[i].                                      *
  * Param i: Input parameter.                                   *
  *          The index in heapArray of the node for which       *
  *          we want to find the right child.                   *
  * Returns: The index of the right child of heapArray[i].      *
  *          (Does not check if the right child exists.)        *
  **************************************************************/
  private int rightChild( int i )
  {
    return 2*i + 2;
  }

  /**************************************************************
  * leftChild                                                   *
  *                                                             *
  * Purpose: Compute the index of the left child of             *
  *          heapArray[i].                                      *
  * Param i: Input parameter.                                   *
  *          The index in heapArray of the node for which       *
  *          we want to find the left child.                    *
  * Returns: The index of the left child of heapArray[i].       *
  *          (Does not check if the left child exists.)         *
  **************************************************************/
  private int leftChild( int i )
  {
    return 2*i + 1;
  } // end method leftChild

  /***************************************************************
  * printHeap                                                    *
  *                                                              *
  * Purpose: Print out the values stored in heapArray.           *
  ***************************************************************/
  public void printHeap( )
  {
    int i;
    int numPrintedOnLine = 0;

    for (i = 0; i < heapSize; i++)
    {
      System.out.print( heapArray[i] + " " );
      numPrintedOnLine++;
      if (numPrintedOnLine == 20)
      {
        System.out.println();
        numPrintedOnLine = 0;
      }
    }
    System.out.println();
  } // end method printHeap

  /*****************************************************************
  * heapSort                                                       *
  *                                                                *
  * Purpose: To sort the array heapArray using a heap sort.        *
  *****************************************************************/
  public void heapSort()
  {
    int i;
    int result;
    int tempSize = heapSize;
    
    // Heapify the unsorted array.

    for (i = heapSize/2; i >= 0; i--)
    {
      siftDown( i );
    }

    // Now sort the array by repeatedly doing retrieveMax.

    for (i = heapSize - 1; i > 0; i--)
    {
      result = retrieveMax();
      heapArray[i] = result;
    }
    heapSize = tempSize;
  } // end method heapSort

  /*******************************************************************
  * main                                                             *
  *******************************************************************/
  public static void main( String[] args )
  {
    heapSolution H = new heapSolution();

    // Print out unsorted array.

    System.out.println( "Unsorted array:" );
    H.printHeap();

    // Sort array.
    
    H.heapSort();

    // Print sorted array.

    System.out.println( "Sorted array:" );
    H.printHeap();
  } // end method main

} // end class heap
