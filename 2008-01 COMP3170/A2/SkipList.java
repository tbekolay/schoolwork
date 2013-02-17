public class SkipList
{
  private static final int MAXLEVELS = (int)(Math.log(1024) / Math.log(2));
  private static LCRandom rng = new LCRandom();
  private SkipNode sentinel;
  
  private class SkipNode
  {
    public int value;
    public SkipNode[] next;
    
    public SkipNode(int value, int num_levels)
    {
      this.value = value;
      next = new SkipNode[num_levels];
      // Probably not necessary, but why not.
      for (int i=0; i < num_levels; i++)
      {
        next[i] = null;
      }
    }
  }
  
  public SkipList()
  {
    sentinel = new SkipNode(Integer.MIN_VALUE, MAXLEVELS);
    
    for (int i=0; i < MAXLEVELS; i++)
    {
      sentinel.next[i] = sentinel;
    }
  }
  
  public void insert(int value)
  {
    SkipNode new_node = null;
    SkipNode prev = null;
    int num_levels = 1; // Always have at least one level
    
    // Determine how many links this node should have.
    while (rng.nextRandom() < 0.5 && num_levels < MAXLEVELS)
    {
      num_levels++;
    }
    
    new_node = new SkipNode(value, num_levels);
    
    // Insert the node into the list by updating links.
    for (int i=num_levels-1; i >= 0; i--)
    {
      prev = sentinel;
      
      while (prev.next[i] != sentinel && prev.next[i].value < value)
      {
        prev = prev.next[i];
      }
      
      // Now at the correct position.
      new_node.next[i] = prev.next[i];
      prev.next[i] = new_node;
    }
  }
  
  public int search(int value)
  {
    final int NOT_FOUND = -1;
    SkipNode curr = null;
    int to_return = NOT_FOUND;
    
    // Search for the node, starting from the highest level and working down.
    for (int i=MAXLEVELS-1; i >= 0 && to_return == NOT_FOUND; i--)
    {
      curr = sentinel.next[i];
      
      while (curr != sentinel && to_return == NOT_FOUND)
      {
        if (curr.value == value)
        {
          to_return = i+1; // Return the level that we found the value on.
        }
        curr = curr.next[i];
      }
    }
    
    return to_return;
  }
  
  public void print()
  {
    SkipNode curr = null;
    
    for (int i=MAXLEVELS-1; i >= 0; i--)
    {
      // Print out the sentinel as the first in the list.
      System.out.print("Sentinel");
      
      curr = sentinel.next[i];
      
      while (curr != sentinel)
      {
        System.out.print("->" + curr.value);
        curr = curr.next[i];
      }
      
      System.out.println("->Sentinel");
    }
  }
  
  
  public static void main (String[] args)
  {
    final int[] TEST_VALUES = {9074, 1024, 59, 771, 12, 101, 72, 883, 10221,
        314, 159, 265, 358, 2718, 602, 485, 599, 3023, 5000, 128};
    SkipList list = new SkipList();
    int rand, rc;
    
    // Insert our test values.
    for (int i=0; i < TEST_VALUES.length; i++)
    {
      list.insert(TEST_VALUES[i]);
    }
    
    // Search for a few values, to make sure they inserted correctly and
    // that the search method works.
    for (int i=0; i < 5; i++)
    {
      rand = (int)(rng.nextRandom() * TEST_VALUES.length);
      rc = list.search(TEST_VALUES[rand]);
      if (rc != -1)
      {
        System.out.println(TEST_VALUES[rand] + " found at level " + rc + "!");
      }
      
    }
    
    // Print out the list.
    System.out.println();
    list.print();
    
    System.out.println("**Program completed successfully.**");
    System.exit(0);
  }
}