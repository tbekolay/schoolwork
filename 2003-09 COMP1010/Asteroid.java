/**
 * Class Asteroid. Creates random asteroids and gives them unique identifiers,
 * and has a number of methods to manipulate data.
 * 
 * 74.101 SECTION L03
 * INSTRUCTOR Ryan Wegner
 * ASSIGNMENT Assignment 4, Question 2
 * @author Trevor Bekolay, 6796723
 * @version 1.0, 19-Nov-2003
 */

class Asteroid
{

  private int centreX;
  private int centreY;
  private int radius;
  private int ident;
  private static int asteroidsCreated;

  /**
   * A constructor. Defines an asteroid with a random center and radius.
   */

  public Asteroid()
  {
    centreX = randomCoordinate();
    centreY = randomCoordinate();
    radius = (int) ((randomCoordinate() / 10) + 1); // The radius is calculated
                                                    // by manipulating the
                                                    // randomCoordinate method.
    asteroidsCreated++;
    ident = asteroidsCreated;
  }

  /**
   * hit determines if the points passed to it are within the circle's bounds.
   * 
   * @param x - the x-coordinate of the point to be tested
   * @param y - the y-coordinate of the poitn to be tested
   * @return boolean - whether or not the point is in the cirle.
   */

  public boolean hit(int x, int y)
  {
    double distance;

    // From lab 3, we know the formula for distance
    distance = Math.sqrt(((x - centreX) ^ 2) + ((y - centreY) ^ 2));

    // If the distance to the center of the circle is less than or equal to the
    // radius, the point is in the circle, and is thus a hit.
    if (distance <= radius) return true;
    else
      return false;
  }

  /**
   * print prints out the information of a specific asteroid.
   * 
   * @return void
   */

  public void print()
  {
    System.out.println("Asteroid " + ident + " at (" + centreX + "," + centreY
        + ") with radius " + radius);
  }

  /**
   * randomCoordinate creates a random interger between 0 and 99.
   * 
   * @return int - the random number between 0 and 99
   */

  static int randomCoordinate()
  {
    return (int) (Math.random() * (100));
  }

}
