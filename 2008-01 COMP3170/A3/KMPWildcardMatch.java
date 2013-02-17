/* Question 7:
  
   Allow the single character wildcard (?) is actually quite a simple change
   from the regular KMP algorithm. We simply have to add one check to if
   statements in both the findMatch and getNext functions.
   
   Note that I have assumed in this example that wildcards are only used in
   the pattern string that we are searching FOR, not in the source string that
   we are searching. If we wish to have wildcards accessible for both, it is
   an easy one line change to add one more check in the if statement in
   findMatch.
   
   I have provided pseudocode below because the question asks for it;
   however, I believe that the actual code is arguably more clear.
   
   Note that this pseudocode differs slightly from the code given in class;
   this is due to Java using 0-based arrays, whereas the class notes uses
   1-based arrays.
   
   findMatch(source, pattern):
     while we haven't found a match and still have things to search:
       if (pattern_ix is 0 or source[source_ix] equals pattern[pattern_ix]
       or pattern[pattern_ix] is the wildcare:
         source_ix++, pattern_ix++
       else:
         pattern_ix = next[pattern_ix-1]
     
     if pattern_ix equals pattern_len:
       match is (source_ix - pattern_len)
   
   getNext(pattern):
     i = 2, j = 0, next[0] = 0
     
     while i <= next.length:
       if pattern[i-1] == pattern[j] or pattern[i-1] == WILDCARD
       or pattern[j] == WILDCARD:
         j++, next[i-1] = j, i++
       else if j == 0:
         next[i-1] = j, i++
       else:
         j = next[j-1];

   In my implementation of KMP, the next[] array for abca?c?b is:

   p    = a b c a ? c ? b
   next = 0 0 0 1 2 3 4 5
   
   Note that this likely differs from the next[] array one would obtain
   using the algorithm as described in class. Using the algorithm in class,
   one should get:
   
   p    = a b c a ? c ? b
   next = 0 1 1 2 3 4 5 6
   
   The version that I obtain works with my code because all of the arrays
   (including the strings) start at index 0 rather than index 1, as is the
   convention in class.

   --------------------------------------------------------------------------
   
   The output for the following code is:
   
   Match found at 2
   0 0 0 1 2 3 4 5 
*/

public class KMPWildcardMatch
{
  private static final int NOT_FOUND = -1;
  private static char WILDCARD = '?';
  
  public static int findMatch(String source, String pattern)
  {
    int match = NOT_FOUND;
    int source_ix = 1, pattern_ix = 1;
    int source_len = source.length(), pattern_len = pattern.length();
    int[] next = getNext(pattern);
    
    while (source_ix < source_len && pattern_ix < pattern_len)
    {
      if (pattern_ix == 0 || source.charAt(source_ix) == pattern.charAt(pattern_ix)
       || pattern.charAt(pattern_ix) == WILDCARD)
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
    
    while (i <= next.length)
    {
      if (pattern.charAt(i-1) == pattern.charAt(j)
       || pattern.charAt(i-1) == WILDCARD || pattern.charAt(j) == WILDCARD)
      {
        next[i-1] = ++j;
        i++;
      }
      else if (j == 0)
      {
        next[i-1] = 0;
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
    String a = "xxabcaQcQbxx";
    String p = "abca?c?b";
    
    int match = findMatch(a, p);
    
    if (match == NOT_FOUND)
    {
      System.out.println("Match not found!");
    }
    else
    {
      System.out.println("Match found at " + match);
    }
    
    // Question # 7
    String test = "abca?c?b";
    int[] next = getNext(test);
    for (int i=0; i < next.length; i++)
      System.out.print(next[i] + " ");
  }
}