import java.util.LinkedList;

public class BinomQueue
{
  private long num_operations;
  private long min_operations;
  private long max_operations;
  private LinkedList binom_q;

  public BinomQueue(int[] num_array)
  {
    LinkedList merge_q = new LinkedList();
    LinkedList new_q = null;
    int i;

    num_operations = 0;
    min_operations = 0;
    max_operations = 0;

    for (i = 0; i < num_array.length; i++)
    {
      new_q = new LinkedList();
      new_q.add(new BinomNode(num_array[i]));
      merge_q.addLast(new_q);
    }

    while (merge_q.size() > 1)
    {
      new_q = binomQMerge( (LinkedList)(merge_q.removeFirst()),
        (LinkedList)(merge_q.removeFirst()) );

      merge_q.addLast(new_q);
    }

    binom_q = (LinkedList)(merge_q.removeFirst());
  }

  public int popMin()
  {
    LinkedList descendents = new LinkedList();
    BinomNode min = (BinomNode)(binom_q.getFirst());
    BinomNode curr = null;
    int i, to_return;

    num_operations += 1;
    min_operations += 1;
    max_operations += 1;

    for (i = 1; i < binom_q.size(); i++)
    {
      curr = (BinomNode)(binom_q.get(i));
      if (curr.key < min.key)
      {
        min = curr;
      }

      num_operations += 3;
      max_operations += 3;
    }
    num_operations += 1;
    min_operations += 1;
    max_operations += 1;

    to_return = min.key;
    binom_q.remove(min);

    num_operations += 2;
    min_operations += 2;
    max_operations += 2;

    if (min.first_child != null)
    {
      descendents.addLast(min.first_child);
      min = min.first_child;

      num_operations += 4;
      max_operations += 4;

      while(min.next_sibling != null)
      {
        descendents.addLast(min.next_sibling);
        min = min.next_sibling;

        num_operations += 4;
        max_operations += 4;
      }

      binom_q = binomQMerge(binom_q, descendents);
    }

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

  private BinomNode popFirstOrNull(LinkedList a)
  {
    BinomNode to_return = null;

    if (a.size() > 0)
    {
      to_return = (BinomNode)(a.removeFirst());
    }

    num_operations += 2;
    min_operations += 2;
    max_operations += 2;

    return to_return;
  }

  // Merge together two binomial queues.
  private LinkedList binomQMerge(LinkedList a, LinkedList b)
  {
    LinkedList result = new LinkedList();
    BinomNode carry = null;
    BinomNode a_node = popFirstOrNull(a);
    BinomNode b_node = popFirstOrNull(b);

    while (a_node != null && b_node != null)
    {
      if (carry != null) // We have a carry from a previous iteration
      {

        if (carry.rank == a_node.rank && carry.rank == b_node.rank)
        {
          result.addLast(carry);
          num_operations += 5;

          carry = binomTreeMerge(a_node, b_node);
          a_node = popFirstOrNull(a);
          b_node = popFirstOrNull(b);
        }
        else if (carry.rank == a_node.rank)
        {
          num_operations += 2;

          carry = binomTreeMerge(carry, a_node);
          a_node = popFirstOrNull(a);
        }
        else if (carry.rank == b_node.rank)
        {
          num_operations += 2;

          carry = binomTreeMerge(carry, b_node);
          b_node = popFirstOrNull(b);
        }
        else
        {
          result.addLast(carry);
          num_operations += 1;

          carry = null;
        }

      }
      else //carry == null
      {

        if (a_node.rank < b_node.rank)
        {
          result.addLast(a_node);
          num_operations += 3;

          a_node = popFirstOrNull(a);
        }
        else if (a_node.rank > b_node.rank)
        {
          result.addLast(b_node);
          num_operations += 3;

          b_node = popFirstOrNull(b);
        }
        else // a_node.rank == b_node.rank
        {
          carry = binomTreeMerge(a_node, b_node);
          a_node = popFirstOrNull(a);
          b_node = popFirstOrNull(b);
        }
      }

      min_operations += 1;
      max_operations += 5;
    } //while (a_node != null && b_node != null)

    while (a_node != null)
    {
      if (carry != null)
      {
        if (a_node.rank < carry.rank)
        {
          result.addLast(a_node);
          num_operations += 3;

          a_node = popFirstOrNull(a);
        }
        else if (a_node.rank > carry.rank)
        {
          result.addLast(carry);
          num_operations += 3;

          carry = null;
        }
        else //a_node.rank == carry.rank
        {
          carry = binomTreeMerge(a_node, carry);
          a_node = popFirstOrNull(a);
        }
      }
      else // carry == null
      {
        result.addLast(a_node);
        num_operations += 1;

        a_node = popFirstOrNull(a);
      }

      min_operations += 1;
      max_operations += 3;
    }

    while (b_node != null)
    {
      if (carry != null)
      {
        if (b_node.rank < carry.rank)
        {
          result.addLast(b_node);
          num_operations += 3;

          b_node = popFirstOrNull(b);
        }
        else if (b_node.rank > carry.rank)
        {
          result.addLast(carry);
          num_operations += 3;

          carry = null;
        }
        else //a_node.rank == carry.rank
        {
          carry = binomTreeMerge(b_node, carry);
          b_node = popFirstOrNull(b);
        }
      }
      else // carry == null
      {
        result.addLast(b_node);
        num_operations += 1;

        b_node = popFirstOrNull(b);
      }

      min_operations += 1;
      max_operations += 3;
    }

    if (carry != null)
    {
      result.addLast(carry);
      num_operations += 1;
      min_operations += 1;
      max_operations += 1;

      carry = null;
    }

    return result;

  }

  // Merge together two trees of equal rank.
  private BinomNode binomTreeMerge(BinomNode a_node, BinomNode b_node)
  {
    BinomNode to_return = null;

    if (a_node.next_sibling != null)
    {
      a_node.next_sibling = null;
      num_operations += 1;
      min_operations += 1;
      max_operations += 1;
    }
    num_operations += 1;

    if (b_node.next_sibling != null)
    {
      b_node.next_sibling = null;
      num_operations += 1;
      min_operations += 1;
      max_operations += 1;
    }
    num_operations += 1;
    min_operations += 1;
    max_operations += 1;

    if (a_node.key <= b_node.key)
    {
      if (a_node.first_child == null)
      {
        a_node.first_child = b_node;
        num_operations += 2;
        min_operations += 2;
        max_operations += 2;
      }
      else
      {
        a_node.last_child.next_sibling = b_node;
        num_operations += 2;
        min_operations += 2;
        max_operations += 2;
      }

      a_node.rank += 1;
      a_node.last_child = b_node;
      num_operations += 2;
      min_operations += 2;
      max_operations += 2;

      to_return = a_node;
    }
    else
    {
      if (b_node.first_child == null)
      {
        b_node.first_child = a_node;
        num_operations += 2;
        min_operations += 2;
        max_operations += 2;
      }
      else
      {
        b_node.last_child.next_sibling = a_node;
        num_operations += 2;
        min_operations += 2;
        max_operations += 2;
      }

      b_node.rank += 1;
      b_node.last_child = a_node;
      num_operations += 2;
      min_operations += 2;
      max_operations += 2;

      to_return = b_node;
    }

    return to_return;
  }

  private class BinomNode
  {
    public int key;
    public int rank;
    public BinomNode first_child;
    public BinomNode last_child;
    public BinomNode next_sibling;

    public BinomNode(int key)
    {
      this.key = key;
      rank = 0;
      first_child = null;
      last_child = null;
      next_sibling = null;
    }
  }
}