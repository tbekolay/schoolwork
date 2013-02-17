/* Question 6:

   The modifications that I made to the KMPMatch class were:
    - In the findMatch function, whenever we enter the "else" condition,
      we check to see how many characters we have matched. If that number is
      a new max, then we remember that result (the length of the prefix and
      where it is found in the source string).
    - After the while loop, the final check (that used to be used to find if
      a complete match is found) is instead another check to see if the current
      number of matched characters exceeds the running maximum.
    - The findMatch function returns both the index where the prefix was found
      and the lenght of that prefix. This information is encapsulated in the
      Match private class, which in a nice (i.e. C) world would just be a simple
      struct.
      
    The results of those modifications can be seen below. As is shown in my
    test case, the algorithm works as expected.
    
    --------------------------------------------------------------------------
    
    The output for the following code is:
    
    Largest prefix is 3 characters at index 6
*/

public class KMPPrefixMatch
{
  private static class Match
  {
    public int index;
    public int length;
    
    public Match() { index = NOT_FOUND; length = NOT_FOUND; }
  }
  
  private static final int NOT_FOUND = -1;
  
  public static Match findMatch(String source, String pattern)
  {
    Match match = new Match();
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
        if (pattern_ix > match.length)
        {
          match.length = pattern_ix;
          match.index = source_ix - pattern_ix;
        }
        
        pattern_ix = next[pattern_ix-1];
      }
    }

    if (pattern_ix > match.length)
    {
      match.length = pattern_ix;
      match.index = source_ix - pattern_ix;
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
    String a = "010120123567";
    String p = "12345";
    
    Match match = findMatch(a, p);
    
    System.out.println("Largest prefix is " + match.length + " characters at index " + match.index);
    
  }
}