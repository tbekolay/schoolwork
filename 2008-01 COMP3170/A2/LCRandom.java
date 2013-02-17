import java.util.Date;

public class LCRandom
{
  private int seed;

  private static final int P = 2147483647;
  private static final int A = 16807;
  private static final int Q = 127773;
  private static final int R = 2836;

  public LCRandom()
  {
    // If we don't get passed a seed, use the system time.
    seed = (int)new Date().getTime();
    
    // Make sure the seed is within the acceptable range.
    if (this.seed < 1)
    {
      this.seed += P-2;
    }
    else if (this.seed > P-1)
    {
      this.seed -= P-2;
    }
  }
  
  public LCRandom(int seed)
  {
    this.seed = seed;
    
    // Make sure the seed is within the acceptable range.
    if (this.seed < 1)
    {
      this.seed += P-2;
    }
    else if (this.seed > P-1)
    {
      this.seed -= P-2;
    }
  }

  public float nextRandom()
  {
    // Calculate the next seed (being sure to avoid overflow).
    // We don't have to do Math.floor(seed/Q) because seed and Q are both ints.
    seed = ((-1*R) * (seed / Q)) + (A * (seed % Q));
    
    if (seed < 1)
    {
      seed += P-2;
    }
    
    // We return a real number 0.0 < x < 1.0
    return (float)seed / (float)(P-1);
  }
  
  public static void main(String[] args)
  {
    // Create a new RNG with seed of 127775
    final int NUM_ITERATIONS = 10;
    final int SEED = 127775;
    LCRandom rng = new LCRandom(SEED);
    
    // Print out the first NUM_ITERATIONS random numbers
    for (int i=0; i < NUM_ITERATIONS; i++)
    {
      System.out.println("Next random number is: " + rng.nextRandom());
    }
    
    System.out.println("**Program completed successfully.**");
    System.exit(0);
  }
}