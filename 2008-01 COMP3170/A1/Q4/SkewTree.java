public class SkewTree
{
  private long num_operations;
  private long min_operations;
  private long max_operations;
  private SkewNode top;

  public SkewTree(int[] num_array)
  {
    int i;
    int length = num_array.length;
    SkewNode[] node_queue = new SkewNode[length];
    SkewNode a, b, p;

    num_operations = 0;
    min_operations = 0;
    max_operations = 0;

    for (i = 0; i < length; i++)
    {
      node_queue[i] = new SkewNode(num_array[i]);
    }

    while (length > 1)
    {
      a = node_queue[0];
      b = node_queue[1];
      p = skewMerge(a, b);

      length = length - 1;

      for (i = 0; i < length-1; i++)
      {
        node_queue[i] = node_queue[i+2];
      }
      node_queue[length-1] = p;
    }

    top = node_queue[0];
  }

  public int popMin()
  {
    int to_return = top.key;
    num_operations += 1;
    min_operations += 1;
    max_operations += 1;

    top = skewMerge(top.l_tree, top.r_tree);
    num_operations += 2;
    min_operations += 2;
    max_operations += 2;

    return to_return;
  }

  public long getNumOperations()
  {
    return num_operations;
  }

  public long getMinOperations()
  {
    return min_operations;
  }

  public long getMaxOperations()
  {
    return max_operations;
  }
  private SkewNode skewMerge(SkewNode a, SkewNode b)
  {
    SkewNode temp, p;

    if (a == null) return b;
    if (b == null) return a;

    if (a.key > b.key)
    {
      temp = a;
      a = b;
      b = temp;
    }
    num_operations += 2;
    min_operations += 2;
    max_operations += 2;

    p = skewMerge(a.r_tree, b);
    num_operations += 1;
    min_operations += 1;
    max_operations += 1;

    a.r_tree = a.l_tree;
    a.l_tree = p;
    num_operations += 3;
    min_operations += 3;
    max_operations += 3;

    return a;
  }

  private class SkewNode
  {
    public int key;
    public SkewNode l_tree;
    public SkewNode r_tree;

    public SkewNode(int key)
    {
      this.key = key;
      l_tree = null;
      r_tree = null;
    }
  }
}