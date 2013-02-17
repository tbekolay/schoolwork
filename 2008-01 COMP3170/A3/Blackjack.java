public class Blackjack
{
  // Constants
  private static LCRandom rng = new LCRandom();
  private static final int MINCARD = 1;
  private static final int SOFTACE = 1;
  private static final int HARDACE = 11;
  private static final int ACEDELTA = HARDACE-SOFTACE;
  private static final int JACK = 11;
  //private static final int QUEEN = 12;
  //private static final int KING = 13;
  private static final int MAXCARD = 13;
  private static final int BLACKJACK = 21;
  
  private class Player
  {
    public int total;
    public int showing; // The first card drawn (shown face up in blackjack)
    public int hard_aces;
    
    public Player()
    {
      int card;
      hard_aces = 0;
      
      // We initialize each player by drawing two cards.
      
      // The first card is a special case, as it is shown.
      // We'll use this card to initialize our instance variables.
      card = drawRawCard();
      if (card == HARDACE) hard_aces++;
      showing = card;
      total = card;
      
      // The second card is the general case.
      drawCard();
    }
    
    public void drawCard()
    {
      int card;
      
      card = drawRawCard();
      total += card;
      if (card == HARDACE) hard_aces++;
      
      while (total > BLACKJACK && hard_aces > 0)
      {
        total -= ACEDELTA;
        hard_aces--;
      }
    }
    
    private int drawRawCard()
    {
      int card;
      
      // draw the raw card
      card = (int)(MINCARD + ( rng.nextRandom() * (MAXCARD-MINCARD) ));
      
      // if it's a jack, queen or king, lower to 10
      if (card >= JACK) card = 10;
      // if it's an ace, return 11; the caller can deal with lowering the total
      if (card == SOFTACE) card = HARDACE;
      
      return card;
    }
  }

  // Returns the player's winnings.
  public double simulateHand(double bet, double q1, double q2, double q3)
  {
    Player player = new Player();
    Player dealer = new Player();
    boolean hit = true;
    double p;
    
    // Check for blackjack; if so, win 2.5 times our bet!
    if (player.total == BLACKJACK) return (bet * 2.5);

    // Loop while we are hitting and haven't bust.
    while (hit && player.total <= BLACKJACK)
    {
      // If we have a soft ace...
      if (player.hard_aces > 0)
      {
        if (player.total <= 11) hit = true;
        else if (player.total >= 17) hit = false;
        else if (dealer.showing >= 8) hit = true;
        else if (player.total >= 13 && dealer.showing <= 5) hit = false;
        else
        {
          p = rng.nextRandom();
          switch (dealer.showing)
          {
            case 2: hit = true; break;
            case 3: hit = (p <= q1); break;
            case 4: hit = (p >= q1); break;
            case 5: hit = (p >= q2); break;
            case 6: hit = (p >= q1); break;
            case 7: hit = (p <= q1); break;
          }
        }
      }
      // If we don't have a soft ace...
      else 
      {
        if (player.total <= 17) hit = true;
        else if (player.total >= 19) hit = false;
        else if (dealer.showing <= 7) hit = false;
        else if (dealer.showing >= 10) hit = true;
        else
        {
          p = rng.nextRandom();
          if (dealer.showing == 8) hit = (p >= q3);
          if (dealer.showing == 9) hit = (p <= q3);
        }
      }

      // Hit, if we've chosen to.
      if (hit)
      {
        player.drawCard();
      }
    } // while
    
    // If we've bust, then we lose our bet.
    if (player.total > 21) return (-1.0 * bet);
    
    // The dealer now takes cards
    while (dealer.total < 17)
    {
      dealer.drawCard();
    }

    // Has the dealer bust?
    if (dealer.total > BLACKJACK) return (2.0 * bet);
    
    // At this point, neither player has bust.
    if (player.total > dealer.total) return (2.0 * bet);
    else return (-1.0 * bet);
  }
  
  public void test()
  {
    Player pl = new Player();
    System.out.println("New player!");
    System.out.println("Total=" + pl.total + "\tShowing=" + pl.showing + "\tHard aces=" + pl.hard_aces);
    for (int j=0; j<4; j++)
    {
      pl.drawCard();
      System.out.println("Total=" + pl.total + "\tShowing=" + pl.showing + "\tHard aces=" + pl.hard_aces);
    }    
  }
  
  public static void main(String[] args)
  {
    final double Q[] = {0.75, 0.8333, 0.9167, 1.0};
    final int NUMHANDS = 10000;
    Blackjack blackjack = new Blackjack();
    double bet = 1.0, total;
    
    // Table header
    System.out.printf("%-6s | %-6s | %-6s | %-7s\n", " Q1", " Q2", " Q3", " Total");
    System.out.printf("---------------------------------\n");
    
    for (int i = 0; i < Q.length; i++)
    {
      for (int j = 0; j < Q.length; j++)
      {
        for (int k = 0; k < Q.length; k++)
        {
          // Simulate NUMHANDS hands
          total = 0.0;
          for (int l = 0; l < NUMHANDS; l++)
          {
            total += blackjack.simulateHand(bet, Q[i], Q[j], Q[k]);
          }
          // Print out results
          System.out.printf("%.4f | %.4f | %.4f | %.1f\n", Q[i], Q[j], Q[k], total);
        }
      }
    }
  }
}