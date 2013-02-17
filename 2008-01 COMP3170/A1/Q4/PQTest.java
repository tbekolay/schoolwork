public class PQTest
{
  public static void main (String[] args)
  {
    // Constants
    final int MAX_NUM = 100000;
    final int ARRAY_SIZE = 10000;
    final int NUM_ACCESSES = 1000;

    // Priority Queues
    Heap binaryHeap;
    Heap ternaryHeap;
    Heap quaternaryHeap;
    Heap quinaryHeap;
    Heap senaryHeap;
    Heap septenaryHeap;
    LBTree lbTree;
    SkewTree skewTree;
    BinomQueue binomQueue;

    // Other variables
    int min;

    // Make an array and fill it with random numbers
    int[] num_array = new int[ARRAY_SIZE];

    for (int i=0; i < ARRAY_SIZE; i++)
    {
      num_array[i] = (int)(Math.random()*MAX_NUM) + 1;
    }

    // Binary Heap
    binaryHeap = new Heap(2, num_array);
    System.out.println("Building Binary Heap took " + binaryHeap.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = binaryHeap.popMin();
      // Testing
      if (i%100 == 0) System.out.println("binaryHeap: i=" + i + ", min=" + min);
    }

    System.out.println("Binary Heap performed " + binaryHeap.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + binaryHeap.getMinOperations() + "\tMaximum operations: " + binaryHeap.getMaxOperations());
    System.out.println();

    // Ternary Heap
    ternaryHeap = new Heap(3, num_array);
    System.out.println("Building Ternary Heap took " + ternaryHeap.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = ternaryHeap.popMin();
      // Testing
      if (i%100 == 0) System.out.println("ternaryHeap: i=" + i + ", min=" + min);
    }

    System.out.println("Ternary Heap performed " + ternaryHeap.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + ternaryHeap.getMinOperations() + "\tMaximum operations: " + ternaryHeap.getMaxOperations());
    System.out.println();

    // Quaternary Heap
    quaternaryHeap = new Heap(4, num_array);
    System.out.println("Building Quaternary Heap took " + quaternaryHeap.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = quaternaryHeap.popMin();
      // Testing
      if (i%100 == 0) System.out.println("quaternaryHeap: i=" + i + ", min=" + min);
    }

    System.out.println("Quaternary Heap performed " + quaternaryHeap.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + quaternaryHeap.getMinOperations() + "\tMaximum operations: " + quaternaryHeap.getMaxOperations());
    System.out.println();

    // Quinary Heap
    quinaryHeap = new Heap(5, num_array);
    System.out.println("Building Quinary Heap took " + quinaryHeap.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = quinaryHeap.popMin();
      // Testing
      if (i%100 == 0) System.out.println("quinaryHeap: i=" + i + ", min=" + min);
    }

    System.out.println("Quinary Heap performed " + quinaryHeap.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + quinaryHeap.getMinOperations() + "\tMaximum operations: " + quinaryHeap.getMaxOperations());
    System.out.println();

    // Senary Heap
    senaryHeap = new Heap(6, num_array);
    System.out.println("Building Senary Heap took " + senaryHeap.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = senaryHeap.popMin();
      // Testing
      if (i%100 == 0) System.out.println("senaryHeap: i=" + i + ", min=" + min);
    }

    System.out.println("Senary Heap performed " + senaryHeap.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + senaryHeap.getMinOperations() + "\tMaximum operations: " + senaryHeap.getMaxOperations());
    System.out.println();

    // Septenary Heap
    septenaryHeap = new Heap(7, num_array);
    System.out.println("Building Septenary Heap took " + septenaryHeap.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = septenaryHeap.popMin();
      // Testing
      if (i%100 == 0) System.out.println("septenaryHeap: i=" + i + ", min=" + min);
    }

    System.out.println("Septenary Heap performed " + septenaryHeap.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + septenaryHeap.getMinOperations() + "\tMaximum operations: " + septenaryHeap.getMaxOperations());
    System.out.println();

    // LB Tree
    lbTree = new LBTree(num_array);
    System.out.println("Building LB Tree took " + lbTree.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = lbTree.popMin();
      // Testing
      if (i%100 == 0) System.out.println("lbTree: i=" + i + ", min=" + min);
    }

    System.out.println("LB Tree performed " + lbTree.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + lbTree.getMinOperations() + "\tMaximum operations: " + lbTree.getMaxOperations());
    System.out.println();

    // Skew Tree
    skewTree = new SkewTree(num_array);
    System.out.println("Building Skew Tree took " + skewTree.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = skewTree.popMin();
      // Testing
      if (i%100 == 0) System.out.println("skewTree: i=" + i + ", min=" + min);
    }

    System.out.println("Skew Tree performed " + skewTree.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + skewTree.getMinOperations() + "\tMaximum operations: " + skewTree.getMaxOperations());
    System.out.println();

    // Binomial Queue
    binomQueue = new BinomQueue(num_array);
    System.out.println("Building Binomial Queue took " + binomQueue.getNumOperations() + " operations.");

    for (int i=0; i < NUM_ACCESSES; i++)
    {
      min = binomQueue.popMin();
      // Testing
      if (i%100 == 0) System.out.println("binomQueue: i=" + i + ", min=" + min);
    }

    System.out.println("Binomial Queue performed " + binomQueue.getNumOperations() + " operations.");
    System.out.println("Minimum operations: " + binomQueue.getMinOperations() + "\tMaximum operations: " + binomQueue.getMaxOperations());
    System.out.println();

    // Finished
    System.out.println("*** End of processing - written by Trevor Bekolay ***");
  }
}
