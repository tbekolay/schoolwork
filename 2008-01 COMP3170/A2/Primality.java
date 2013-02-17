public class Primality
{
  public static final int COMPOSITE = 0;
  public static final int PROBABLY_PRIME = 1;
  private static LCRandom rng = new LCRandom();
  
  private static long power(long a, long m, long n)
  {
    // Calculates a^m (mod n)
    long b;
           
    if (m == 1) return a;
    
    b = power(a, m/2, n);
    b = (b * b) % n;
    
    if (m%2 == 1)
    {
      b = (b * a) % n;
    }

    return b;
  }
  
  public static int isPrime(long n)
  {
    final int NUM_ITERATIONS = 50;
    long result;
    long a;
    
    System.out.print("Testing " + n + "...\t");
    
    for (int i=0; i < NUM_ITERATIONS; i++)
    {
      // Choose a random number 2 < a < n-2.
      a = (long)(2 + ( rng.nextRandom() * (n-4) ));
      
      // Calculate a ^ n-1 (mod n).
      result = power(a, n-1, n);
      
      if (result != 1)
      {
        System.out.println("Finished in " + (i+1) + " iteration(s).");
        return COMPOSITE;
      }
    }
    
    // At this point, we're reasonably sure that n is prime.
    System.out.println("Finished in " + NUM_ITERATIONS + " iterations.");
    return PROBABLY_PRIME;
  }
  
  public static void main(String[] args)
  {
    // Test out a bunch of values to see if they're prime.
    final int[] TEST_NUMBERS = { 16807, 17389, 104729, 2147483647, 1223,
        127773, 2836, 1987, 4409, 1319592028, 1319592027, 561, 12553, 224737 };
    
    for (int i=0; i < TEST_NUMBERS.length; i++)
    {
      // Test for primality and print an appropriate message.
      if (Primality.isPrime(TEST_NUMBERS[i]) == Primality.PROBABLY_PRIME)
      {
        System.out.println("\t" + TEST_NUMBERS[i] + " is probably prime.");
      }
      else
      {
        System.out.println("\t" + TEST_NUMBERS[i] + " is definitely not prime.");
      }
    }
    
    System.out.println("**Program completed successfully.**");
    System.exit(0);
  }
}