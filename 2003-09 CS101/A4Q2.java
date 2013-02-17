/**
 * class A4Q2. Creates asteroids and takes shots at them until done.
 * 
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 4
 * @author Trevor Bekolay, 6796723
 * @version 1.0, 27-Oct-2003
 */
public class A4Q2
{
  // tracks how many asteroids we still have, outside of main since it makes
  // tracking in
  // multiple methods straightforward (due to pass-by=value restrictions)
  private static int numAsteroids = 5;

  /**
   * Our application. It will create and maintain 5 asteroids. The application
   * will take random shots at the asteroids until they're all destroyed.
   * 
   * @param args -- an array of command line options
   * @return void
   */
  public static void main(String args[])
  {
    // our asteroid objects
    Asteroid asteroid1 = new Asteroid();
    Asteroid asteroid2 = new Asteroid();
    Asteroid asteroid3 = new Asteroid();
    Asteroid asteroid4 = new Asteroid();
    Asteroid asteroid5 = new Asteroid();

    // the randomly selected bullet coordinates
    int shotX;
    int shotY;

    // we need to track which bullet is fired for our output
    int bullet_count = 0;

    System.out.println("Beginning asteroid simulation");
    System.out.println();

    // shoot at the asteroids as along as we have some to shoot at
    while (numAsteroids > 0)
    {
      bullet_count++;

      // generate a random shot
      shotX = Asteroid.randomCoordinate();
      shotY = Asteroid.randomCoordinate();

      // check for hits (allowing for multiple hits/bullet)
      asteroid1 = takeAShot(asteroid1, shotX, shotY, bullet_count);
      asteroid2 = takeAShot(asteroid2, shotX, shotY, bullet_count);
      asteroid3 = takeAShot(asteroid3, shotX, shotY, bullet_count);
      asteroid4 = takeAShot(asteroid4, shotX, shotY, bullet_count);
      asteroid5 = takeAShot(asteroid5, shotX, shotY, bullet_count);
    }

    System.out.println("All asteroids have been eliminated");

    System.exit(0);
  }

  /**
   * Determines if a bullet hits the given asteroid and prints results.
   * 
   * @param asteroid -- the asteroid object
   * @param shotX -- the shot's x-coordinate
   * @param shotY -- the shot's y-coordinate
   * @param bullet_count -- the current bullet being fired; required for
   *          printing
   * @return Asteroid -- the current asteroid on a miss and null if it's
   *         destroyed
   */
  private static Asteroid takeAShot(Asteroid asteroid, int shotX, int shotY,
      int bullet_count)
  {
    // preset the return value to be the original
    Asteroid newAsteroid = asteroid;

    // make sure an asteroid still exists before shooting at it
    // we short-circuit the if to do this...
    if (asteroid != null && asteroid.hit(shotX, shotY))
    {
      asteroid.print();
      System.out.println("Destroyed by bullet " + bullet_count + " at ("
          + shotX + "," + shotY + ")");
      System.out.println();

      newAsteroid = null;
      numAsteroids--;
    }

    return newAsteroid;
  }
}
