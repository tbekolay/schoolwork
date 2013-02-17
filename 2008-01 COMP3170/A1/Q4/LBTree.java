public class LBTree
{
  private long num_operations;
  private long min_operations;
  private long max_operations;
  private LBNode top;

  public LBTree(int[] num_array)
  {
    int i;
    int length = num_array.length;
    LBNode[] node_queue = new LBNode[length];
    LBNode a, b, p;

    num_operations = 0;
    min_operations = 0;
    max_operations = 0;

    for (i = 0; i < length; i++)
    {
      node_queue[i] = new LBNode(num_array[i]);
    }

    while (length > 1)
    {
      a = node_queue[0];
      b = node_queue[1];
      p = lbMerge(a, b);

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

    top = lbMerge(top.l_tree, top.r_tree);
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

  private LBNode lbMerge(LBNode a, LBNode b)
  {
    LBNode temp, p;

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

    p = lbMerge(a.r_tree, b);
    num_operations += 1;
    min_operations += 1;
    max_operations += 1;

    if (a.l_tree != null && p.r_path < a.l_tree.r_path)
    {
      a.r_tree = p;
      num_operations += 1;
    }
    else
    {
      a.r_tree = a.l_tree;
      a.l_tree = p;
      num_operations += 3;
    }
    num_operations += 4;
    min_operations += 4;
    max_operations += 9;

    if (a.r_tree != null)
    {
      a.r_path = 1 + a.r_tree.r_path;
      num_operations += 3;
    }
    num_operations += 1;
    min_operations += 1;
    max_operations += 4;

    return a;
  }

  private class LBNode
  {
    public int key;
    public int r_path;
    public LBNode l_tree;
    public LBNode r_tree;

    public LBNode(int key)
    {
      this.key = key;
      r_path = 0;
      l_tree = null;
      r_tree = null;
    }
  }
}