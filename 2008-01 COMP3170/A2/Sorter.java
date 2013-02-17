public class Sorter
{
  public static int insertionSort(int[] a, int left, int right, int increment)
  {
    int num_accesses = 0;
    int temp;
    int i,j;
    
    for (i = left; i <= right; i += increment)
    {
      temp = a[i];
      num_accesses += 1;

      for(j = i-increment; j >= 0 && a[j] > temp; j -= increment)
      {
        a[j+increment] = a[j];
        num_accesses += 3;
      }
      num_accesses += 1;
      
      a[j+increment] = temp;
      num_accesses += 1;
    }
  
    return num_accesses;
  }
  
  public static int quickSort(int[] a, int cut_off)
  {
    int num_accesses = 0;
    
    num_accesses += qSort(a, 0, a.length-1, cut_off);
    num_accesses += insertionSort(a, 0, a.length-1, 1);
    
    return num_accesses;
  }
  
  private static int qSort(int[] a, int left, int right, int cut_off)
  {
    int mid, pivot, i, j;
    
    int num_accesses = 0;
    
    if (right - left <= cut_off)
    {
      return num_accesses;
    }
    
    mid = (left + right) / 2;
    
    // Do median-of-three swapping
    if (a[left] > a[mid])
    {
      num_accesses += swap(a, left, mid);
    }
    num_accesses += 2;

    if(a[left] > a[right])
    {
      num_accesses += swap(a, left, right);
    }
    num_accesses += 2;
    
    if (a[mid] > a[right])
    {
      num_accesses += swap(a, mid, right);
    }
    num_accesses += 2;
    
    pivot = a[mid];
    num_accesses += 1;
    num_accesses += swap(a, mid, right);
    
    i = left; j = right-1;
    
    while (i < j)
    {
      for (;i < right && a[i] <= pivot;i++) num_accesses += 1; num_accesses += 1;
      for (;j >= left && a[j] >= pivot;j--) num_accesses += 1; num_accesses += 1;
      num_accesses += swap(a, i, j);
    }
    
    num_accesses += swap(a, i, j); //Why undo the last swap...?
    num_accesses += swap(a, i, right); // Replace pivot
    
    num_accesses += qSort(a, left, i-1, cut_off);
    num_accesses += qSort(a, i+1, right, cut_off);
    
    return num_accesses;
  }
  
  public static int shellSort(int[] a, int[] incr)
  {
    int num_accesses = 0;
    
    for(int k=incr.length-1; k >=0; k--)
    {
      for (int j = 0; j < incr[k]; j++)
      {
        num_accesses += insertionSort(a, j, a.length-1, incr[k]);
      }
    }
    
    return num_accesses;
  }
  
  public static int[] calcHibbard(int arraylength)
  {
    int[] incr;
    int length; // We will always have at least one increment
    int test = 1;
    
    // Simulate filling the array to determine how large it has to be.
    for (length = 0; test <= arraylength; ++length)
    {
      test = (2*test) + 1;
    }
    
    incr = new int[length];
    incr[0] = 1;
    
    for (int i=1; i < length; i++)
    {
      incr[i] = (2*incr[i-1]) + 1;
    }
    
    return incr;
  }
  
  public static int[] calcKnuth(int arraylength)
  {
    int[] incr;
    int length; // We will always have at least one increment
    int test = 1;
    
    // Simulate filling the array to determine how large it has to be.
    for (length = 0; test <= arraylength; ++length)
    {
      test = (3*test) + 1;
    }
    
    incr = new int[length];
    incr[0] = 1;
    
    for (int i=1; i < length; i++)
    {
      incr[i] = (3*incr[i-1]) + 1;
    }
    
    return incr;
  }
  
  public static int[] calcSedgewick(int arraylength)
  {
    int[] incr;
    int length; // We will always have at least one increment
    int test = 1, i = 0;
    
    // Simulate filling the array to determine how large it has to be.
    for (length = -1; test <= arraylength; ++length)
    {
      if (i%2 == 0)
      {
        test = 9 * (int)Math.pow(2, i/2) * ((int)Math.pow(2, i/2)-1) + 1;        
      }
      else
      {
        test = (int)Math.pow(2, i/2+2) * ((int)Math.pow(2, i/2+2)-3) + 1;
      }
      i++;
    }
    
    incr = new int[length];
    
    for (i = 0; i < length; i++)
    {
      if (i%2 == 0)
      {
        incr[i] = 9 * (int)Math.pow(2, i/2) * ((int)Math.pow(2, i/2)-1) + 1;        
      }
      else
      {
        incr[i] = (int)Math.pow(2, i/2+2) * ((int)Math.pow(2, i/2+2)-3) + 1;
      }
    }
    
    return incr;
  }
  
  public static int heapSort(int[] a, int nary)
  {
    int num_accesses = 0;
    
    num_accesses += heapify(a, nary);
    
    for(int last=a.length-1; last > 0; last--)
    {
      num_accesses += swap(a, 0, last);
      num_accesses += floatDown(a, 0, last, nary);
    }
    
    return num_accesses;
  }
  
  private static int heapify(int[] a, int nary)
  {
    int num_accesses = 0;

    for (int k = (a.length - 1) / nary; k >= 0; k--)
    {
      num_accesses += floatDown(a, k, a.length-1, nary);
    }
    
    return num_accesses;
  }
  
  private static int floatDown(int[] a, int start, int end, int nary)
  {
    int num_accesses = 0;
    int[] j = new int[nary];
    int max, temp = a[start]; // Entry a[start] has been increased
    num_accesses += 1;

    for (int i = 0; i < nary; i++)
    {
      j[i] = (nary * start) + i + 1;
    }

    while (j[0] < end)
    {
      max = j[0];

      for (int i = 1; i < nary; i++)
      {
        if (j[i] < end)
        {
          if (a[j[i]] > a[max])
          {
            max = j[i];
          }
          num_accesses += 2;
        }
      }

      if (temp < a[max])
      {
        a[start] = a[max];
        num_accesses += 3;

        start = max;
        for (int i = 0; i < nary; i++)
        {
          j[i] = (nary * start) + i + 1;
        }
      }
      else
      {
        break;
      }
    }

    a[start] = temp;
    num_accesses += 1;
    
    return num_accesses;
  }
  
  public static boolean isSorted(int[] a)
  {
    boolean sorted = true;
    
    for (int i=1; i < a.length && sorted; i++)
    {
      if (a[i-1] > a[i])
      {
        sorted = false;
      }
    }
    
    return sorted;
  }
  
  public static int swap(int[] a, int ix1, int ix2)
  {
    int temp = a[ix1];
    a[ix1] = a[ix2];
    a[ix2] = temp;
    
    return 4; // 4 array accesses per swap
  }
  
  public static void main(String[] args)
  {
    final int MIN_RAND = 1;
    final int MAX_RAND = 100000;
    final int RANGE = MAX_RAND - MIN_RAND;
    final int N = 10000;
    LCRandom rng = new LCRandom();
    int[] a = new int[N];
    int[] copy = new int[N];
    // Keep track of how many accesses for each algorithm.
    int quick32,quick64,shellHibbard,shellKnuth,shellSegdewick,heap2,heap3;
    
    for (int i=0; i < N; i++)
    {
      a[i] = (int)(MIN_RAND + ( rng.nextRandom() * (RANGE) ));
    }
    
    // Quicksort, cutoff 32
    System.arraycopy(a, 0, copy, 0, N);
    quick32 = quickSort(copy, 32);
    
    if (!isSorted(copy))
    {
      System.out.println("Error! Not sorted after QuickSort with cut-off 32.");
      System.exit(0);
    }
    
    // Quicksort, cutoff 64
    System.arraycopy(a, 0, copy, 0, N);
    quick64 = quickSort(copy, 64);
    
    if (!isSorted(copy))
    {
      System.out.println("Error! Not sorted after QuickSort with cut-off 64.");
      System.exit(0);
    }
    
    // Shellsort, Hibbard's increments
    System.arraycopy(a, 0, copy, 0, N);
    shellHibbard = shellSort(copy, calcHibbard(N));
    
    if (!isSorted(copy))
    {
      System.out.println("Error! Not sorted after ShellSort using Hibbard's increments.");
      System.exit(0);
    }
    
    // Shellsort, Knuth's increments
    System.arraycopy(a, 0, copy, 0, N);
    shellKnuth = shellSort(copy, calcKnuth(N));
    
    if (!isSorted(copy))
    {
      System.out.println("Error! Not sorted after ShellSort using Knuth's increments.");
      System.exit(0);
    }
    
    // Shellsort, Sedgewick's increments
    System.arraycopy(a, 0, copy, 0, N);
    shellSegdewick = shellSort(copy, calcSedgewick(N));
    
    if (!isSorted(copy))
    {
      System.out.println("Error! Not sorted after ShellSort using Sedgewick's increments.");
      System.exit(0);
    }
    
    // Heapsort, binary heap
    System.arraycopy(a, 0, copy, 0, N);
    heap2 = heapSort(copy, 2);
    
    if (!isSorted(copy))
    {
      System.out.println("Error! Not sorted after binary HeapSort.");
      System.exit(0);
    }
    
    // Heapsort, ternary heap
    System.arraycopy(a, 0, copy, 0, N);
    heap3 = heapSort(copy, 3);
    
    if (!isSorted(copy))
    {
      System.out.println("Error! Not sorted after ternary HeapSort.");
      System.exit(0);
    }
    
    // Print out a table with results.
    
    System.out.println(" For N = " + N);
    System.out.println();
    System.out.println(" Sorting Algorithm      | Number of Array Accesses");
    System.out.println(" -----------------------|-------------------------");
    System.out.println(" QuickSort, cut-off 32  | " + quick32);
    System.out.println(" QuickSort, cut-off 64  | " + quick64);
    System.out.println(" ShellSort, Hibbard's   | " + shellHibbard);
    System.out.println(" QuickSort, Knuth's     | " + shellKnuth);
    System.out.println(" QuickSort, Sedgewick's | " + shellSegdewick);
    System.out.println(" HeapSort, binary       | " + heap2);
    System.out.println(" HeapSort, ternary      | " + heap3);
    
    System.out.println("**Program completed successfully.**");
    System.exit(0);
  }
}