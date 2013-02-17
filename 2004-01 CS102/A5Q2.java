/**
 * A5Q2 - Shoots bullets at asteroids. Uses linked lists to store said
 * asteroids.
 * 74.101 SECTION L02
 * INSTRUCTOR T. Andres
 * ASSIGNMENT Assignment 5, question 2
 * @author Trevor Bekolay, 6796723
 * @version April 3, 2004
 */

public class A5Q2
{

  public static void main(String args[])
  {
    // Create the asteroids and take shots at them until they're all gone.
    Asteroids asteroids;
    int bullet_count = 0; // track which bullet is fired

    asteroids = new Asteroids();

    System.out.println("\nBeginning asteroid simulation");
    // shoot at the asteroids as along as we have some to shoot at
    while (asteroids.anyLeft())
    {
      bullet_count++;
      asteroids.takeAShot(bullet_count);
    }

    System.out.println("\nAll asteroids have been eliminated");
  }
}

class Asteroids
{
  // constant to control how many asteroids we're playing with
  private static final int NUM_ASTEROIDS = 20;
  // constants used to control the random generation of asteroids
  private static final int MIN_POSITION = 0;
  private static final int MAX_POSITION = 99;

  // The first asteroid needs to be recorded.
  Node firstAsteroid;

  int activeAsteroids;

  public Asteroids()
  {
    initializeAsteroids();
  }

  /**
   * PURPOSE: intializeAsteroids fills a linked list with NUM_ASTEROIDS
   * asteroids objects.
   * @return VOID
   */

  private void initializeAsteroids()
  {

    Node currentAsteroid;
    Node lastAsteroid;
    int i;

    // Creates the first Asteroid object in the firstAsteroid node.
    firstAsteroid = new Node(new Asteroid(), firstAsteroid);
    lastAsteroid = firstAsteroid;
    // Creates the remaining Asteroids using a linked list structure.
    for (i = 1; i < NUM_ASTEROIDS; i++)
    {
      currentAsteroid = new Node(new Asteroid(), null);
      lastAsteroid.nextAsteroid = currentAsteroid;
      lastAsteroid = currentAsteroid;
    }
    activeAsteroids = NUM_ASTEROIDS;
  }

  /**
   * PURPOSE: takeAShot determines if a randomly fired bullet will destroy one
   * of the asteroids.
   * @param bullet_count is a running total of how many bullets have been fired
   *          thus far.
   * @return VOID
   */

  public void takeAShot(int bullet_count)
  {
    // Randomly generate a shot and search the array of asteroids to see
    // which ones (if any) were hit. On a hit it prints the fact and gets
    // rid of the reference to that asteroid.
    int i;
    int shotX;
    int shotY;
    // We must start with the first asteroid.
    Node currentAsteroid = firstAsteroid;
    Node lastAsteroid = null;

    // generate a random shot
    shotX = randomCoordinate();
    shotY = randomCoordinate();

    // NOTE: BONUS SECTION
    // This for loop runs through each asteroid object in the linked list to
    // determine if the bullet destroyed it. A destroyed asteroid is taken out
    // of the linked list entirely.
    // We run from 0 to the number of activeAsteroids, as that number will
    // decrease every time an asteroid is hit.
    for (i = 0; i < activeAsteroids; i++)
    {
      if (currentAsteroid.asteroid.hit(shotX, shotY))
      {

        currentAsteroid.asteroid.print();
        System.out.println("Destroyed by bullet " + bullet_count + " at ("
            + shotX + "," + shotY + ")");

        // If the asteroid hit was the first asteroid, we make the second
        // asteroid in the linked list the first asteroid.
        if (currentAsteroid == firstAsteroid)
        {
          firstAsteroid = firstAsteroid.nextAsteroid;
        }
        // If the asteroid hit was not the first asteroid, we change the pointer
        // of the asteroid before the current one to where the currentAsteroid
        // points to, effectively cutting the currrentAsteroid out of the linked
        // list.
        else
        {
          lastAsteroid.nextAsteroid = currentAsteroid.nextAsteroid;
        }
        activeAsteroids--;
      }
      lastAsteroid = currentAsteroid;
      currentAsteroid = currentAsteroid.nextAsteroid;
    }
  }

  public static int randomCoordinate()
  {
    // A service routine used to randomly generate a coordinate.
    return ((int) (Math.random() * (MAX_POSITION - MIN_POSITION + 1)) + MIN_POSITION);
  }

  public boolean anyLeft()
  {
    boolean result;
    if (activeAsteroids > 0)
    {
      result = true;
    }
    else
    {
      result = false;
    }
    return result;
  }

  private class Node
  {

    // We have two references: one to the next asteroid and one to the asteroid
    // object, not the values that are in the asteroid object.
    Node nextAsteroid;
    Asteroid asteroid;

    // A typical constructor for a linked list.
    public Node(Asteroid asteroid, Node nextAsteroid)
    {
      this.asteroid = asteroid;
      this.nextAsteroid = nextAsteroid;
    }

  }

}


class Asteroid
{
  private static final int MIN_RADIUS = 1;
  private static final int MAX_RADIUS = 10;

  // the current state of an asteroid -- it's position and size
  private int x;
  private int y;
  private int radius;

  // uniquely identifies the asteroid based on when it was created
  private int id;

  // tracks the number of asteroids we've worked with
  private static int asteroid_count = 0;

  /**
   * Constructs an asteroid object by randomly setting its size and position.
   * Increments the number of asteroids that currently exist.
   */
  public Asteroid()
  {
    // generate the asteroid's position and size
    x = Asteroids.randomCoordinate();
    y = Asteroids.randomCoordinate();
    radius = (int) (Math.random() * (MAX_RADIUS - MIN_RADIUS + 1)) + MIN_RADIUS;

    asteroid_count++;

    // unique identifier is just a matter of the number of asteroids we create
    id = asteroid_count;
  }

  /**
   * Determine if the passed bullet hit the asteroid. It's a hit if the distance
   * between the centre of the asteroid and the bullet is less than the radius
   * of the asteroid.
   */
  public boolean hit(int x, int y)
  {
    boolean hit = false;
    int distance;

    // determine how far the bullet is from the asteroid's centre
    distance = (int) Math.sqrt(Math.pow((this.x - x), 2)
        + Math.pow((this.y - y), 2));

    // if the bullet is within the asteroid's cirlce, it's a hit
    if (distance <= radius) hit = true;

    return hit;
  }

  public void print()
  {
    // Print the current state of the asteroid
    System.out.println("\nAsteroid " + id + " at (" + x + "," + y
        + ") with radius " + radius);
  }
}
