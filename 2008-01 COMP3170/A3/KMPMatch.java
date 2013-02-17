/* For Questions 5 - 7, I wrote actual Java code to make the concepts clear
   to myself. All of the attached code works; .java files can be provided
   for proof if necessary. In some places, there are subtle differences
   between my code and the explanations give in class. Usually these differences
   are due to the class conventions of using arrays beginning with 1; I use
   0-based arrays, as is Java's convention. I will attempt to point out where
   the difference is important.

   Where appropriate, I will include both general explanations
   and code. Explanations will precede the code.

   Question 5:

   In my implementation of KMP, the next[] array for aabbaabababbaabbaabb is:

   p    = a a b b a a b a b a b b a a b b a a b b
   next = 0 1 0 0 1 2 3 1 0 1 0 0 1 2 3 4 5 6 7 4
   
   Note that this likely differs from the next[] array one would obtain
   using the algorithm as described in class. Using the algorithm in class,
   one should get:
   
   p    = a a b b a a b a b a b b a a b b a a b b
   next = 0 1 1 1 2 3 4 2 1 2 1 1 2 3 4 5 6 7 8 5
   
   The version that I obtain works with my code because all of the arrays
   (including the strings) start at index 0 rather than index 1, as is the
   convention in class.
   
   --------------------------------------------------------------------------
   
   The output for the following code is:
   
   Match found at 10
   0 1 0 0 1 2 3 1 0 1 0 0 1 2 3 4 5 6 7 4 
*/

public class KMPMatch
{
  private static final int NOT_FOUND = -1;

  public static int findMatch(String source, String pattern)
  {
    int match = NOT_FOUND;
    int source_ix = 1, pattern_ix = 1;
    int source_len = source.length(), pattern_len = pattern.length();
    int[] next = getNext(pattern);

    while (source_ix < source_len && pattern_ix < pattern_len)
    {
      if (pattern_ix == 0 || source.charAt(source_ix) == pattern.charAt(pattern_ix))
      {
        source_ix++; pattern_ix++;
      }
      else
      {
        pattern_ix = next[pattern_ix-1];
      }
    }

    if (pattern_ix == pattern_len)
    {
      match = source_ix - pattern_len;
    }

    return match;
  }

  public static int[] getNext(String pattern)
  {
    int[] next = new int[pattern.length()];
    int i = 2, j = 0;

    next[0] = 0;

    while (i-1 < next.length)
    {
      if (pattern.charAt(i-1) == pattern.charAt(j))
      {
        next[i-1] = ++j;
        i++;
      }
      else if (j == 0)
      {
        next[i-1] = j;
        i++;
      }
      else
      {
        j = next[j-1];
      }
    }

    return next;
  }


  public static void main(String[] args)
  {
    // Testing
    String a = "00123467891234566";
    String p = "12345";

    int match = findMatch(a, p);

    if (match == NOT_FOUND)
    {
      System.out.println("Match not found!");
    }
    else
    {
      System.out.println("Match found at " + match);
    }

    // Question # 5
    String test = "aabbaabababbaabbaabb";
    int[] next = getNext(test);
    for (int i=0; i < next.length; i++)
      System.out.print(next[i] + " ");
  }
}