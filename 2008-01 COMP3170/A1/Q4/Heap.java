public class Heap
{
  private long num_operations;
  private long min_operations;
  private long max_operations;
  private int[] h;
  private int nary;
  private int length;

  public Heap(int nary, int[] num_array)
  {
    num_operations = 0;
    min_operations = 0;
    max_operations = 0;

    this.nary = nary;
    length = num_array.length;
    h = new int[length];
    System.arraycopy(num_array, 0, h, 0, length);
    buildHeap();
  }

  public int popMin()
  {
    int to_return = h[0];
    num_operations += 1;
    min_operations += 1;
    max_operations += 1;

    h[0] = h[length-1];
    num_operations += 2;
    min_operations += 2;
    max_operations += 2;

    length -= 1;

    floatDown(0);

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

  private void buildHeap()
  {
    int k;

    for (k = (length - 1) / nary; k >= 0; k--)
    {
      floatDown(k);
    }
  }

  public void floatDown(int k)
  {
    int[] j = new int[nary];
    int i, min;
    // Entry h[k] has been increased
    int temp = h[k];
    num_operations += 1;

    for (i = 0; i < nary; i++)
    {
      j[i] = (nary * k) + i + 1;
    }

    while (j[0] < length)
    {
      min = j[0];

      for (i = 1; i < nary; i++)
      {
        if (j[i] < length)
        {
          if (h[j[i]] < h[min])
          {
            min = j[i];
          }
          num_operations += 2;
          min_operations += 2;
          max_operations += 2;
        }
      }

      if (temp > h[min])
      {
        h[k] = h[min];
        num_operations += 3;

        k = min;
        for (i = 0; i < nary; i++)
        {
          j[i] = (nary * k) + i + 1;
        }
      }
      else
      {
        break;
      }
    }
    max_operations += ( 3 * (Math.log( (double)(length) ) / Math.log( (double)(nary) ) ) );

    h[k] = temp;
    num_operations += 1;
    min_operations += 1;
    max_operations += 1;
  }

  public void floatUp(int k)
  {
    // (Algorithm from in class)

    int j;
    // Entry h[k] has been reduced
    int temp = h[k];
    num_operations += 1;

    j = k / nary; // Parent of h[k]
    while (j > 0 && temp < h[j])
    {
      h[k] = h[j];
      num_operations += 3;

      k = j;
      j = j / nary;
    }

    h[k] = temp;
    num_operations += 1;
  }

}