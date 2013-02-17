import java.io.*;
import java.util.*;

public class A2Q1
{

  public static void main(String[] parms)
  {
    Flights flights;

    flights = createFlights();

    flights.sortFlights();

    flights.printFlights();

    processTrips(flights);

    System.out.println("\nProgram completed normally.");
  }


  public static Flights createFlights()
  {
    Flight newFlight;
    Flights flights;

    FileReader fileReaderIn;
    BufferedReader fileIn;

    String inputLine;

    flights = new Flights();

    try
    {
      fileReaderIn = new FileReader("flights.txt");
      fileIn = new BufferedReader(fileReaderIn);

      inputLine = fileIn.readLine(); // remove header line
      inputLine = fileIn.readLine();
      while (inputLine != null)
      {
        newFlight = createFlight(inputLine);
        flights.addFlight(newFlight);
        // flights.addFlight(inputLine);
        inputLine = fileIn.readLine();
      }
      fileIn.close();
    }
    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }
    catch (NumberFormatException nfe)
    {
      System.out.println("Invalid number entered.");
    }

    return flights;
  }

  public static Flight createFlight(String inputLine)
  {
    Flight newFlight;
    String[] result;

    int flight;
    String origin;
    String destination;
    int cost;

    result = inputLine.split("\\s+");
    flight = Integer.parseInt(result[0]);
    origin = result[1];
    destination = result[2];
    cost = Integer.parseInt(result[3]);
    newFlight = new Flight(flight, origin, destination, cost);
    return newFlight;
  }

  public static void processTrips(Flights flights)
  {
    FileReader fileReaderIn;
    BufferedReader fileIn;

    String inputLine = "";

    System.out.println("\nLooking up Destinations:");
    try
    {
      fileReaderIn = new FileReader("travel.txt");
      fileIn = new BufferedReader(fileReaderIn);

      inputLine = fileIn.readLine(); // remove header line
      inputLine = fileIn.readLine();
      while (inputLine != null)
      {
        processTrip(flights, inputLine);
        inputLine = fileIn.readLine();
      }
      fileIn.close();
    }
    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }
    return;
  }


  public static void processTrip(Flights flights, String inputLine)
  {
    String[] result;
    Flights returnFlights;

    String origin;
    String destination;
    int bestCost;

    result = inputLine.split("\\s+");
    origin = result[0];
    destination = result[1];
    System.out.println("\nLooking for a flight from " + origin + " to "
        + destination);
    returnFlights = flights.searchTrips(origin, destination);
    if (returnFlights != null)
    {
      returnFlights.printFlights();
      bestCost = returnFlights.searchCosts();
      System.out.println("Best cost is: " + bestCost);
    }
    else
    {
      System.out.println("A flight from " + origin + " to " + destination
          + " does not exist.");
    }
  }


}


/** ****************************************************************** */


class Flights
{

  private Vector flights;

  public Flights()
  {
    flights = new Vector();
  }


  public void addFlight(Flight flight)
  {
    flights.addElement(flight);
    return;
  }


  public void addFlight(String inputLine)
  { // same as createFlight
    Flight newFlight;
    String[] result;

    int flight;
    String origin;
    String destination;
    int cost;

    result = inputLine.split("\\s+");
    flight = Integer.parseInt(result[0]);
    origin = result[1];
    destination = result[2];
    cost = Integer.parseInt(result[3]);
    newFlight = new Flight(flight, origin, destination, cost);
    addFlight(newFlight);
    return;
  }


  public void printFlights()
  {
    int position;

    System.out.println("\nList of flights:");
    for (position = 0; position < flights.size(); position++)
    {
      System.out.println(flights.elementAt(position).toString());
    }
    System.out.println();
  }


  public Flights searchTrips(String origin, String destination)
  {
    Flight currentFlight;
    Flights flightList = new Flights();
    int counter;
    int found = 0;

    for (counter = 0; counter < flights.size(); counter++)
    {
      currentFlight = (Flight) flights.elementAt(counter);
      if (currentFlight.checkTrip(origin, destination))
      {
        flightList.addFlight(currentFlight);
        found++;
      }
    }
    if (found == 0)
    {
      flightList = null;
    }
    return flightList;
  }

  public int searchCosts()
  {
    Flight currentFlight;
    int counter;
    int found = 0;
    int bestCost = Integer.MAX_VALUE;
    for (counter = 0; counter < flights.size(); counter++)
    {
      currentFlight = (Flight) flights.elementAt(counter);
      bestCost = currentFlight.checkCost(bestCost);
    }
    return bestCost;
  }

  /**
   * PURPOSE: sortFlights sorts the fligths Vector in the current Flights
   * object.
   * @return VOID
   */

  public void sortFlights()
  {

    // Selection sort.

    int counter1;
    int counter2;
    int smallest;
    Flight temp;
    Flight temp2;

    // This for loop runs through all but the last Flight object, as it will
    // always be the highest.
    for (counter1 = 0; counter1 < flights.size() - 1; counter1++)
    {
      smallest = counter1;
      // This for loop compares the current lowest cost to the cost of the
      // remaining elements of the Vector.
      for (counter2 = smallest + 1; counter2 < flights.size(); counter2++)
      {
        temp = (Flight) flights.elementAt(smallest);
        temp2 = (Flight) flights.elementAt(counter2);
        // If temp has a smaller cost, we define it as the new smaller Flight.
        if (temp.compareTo(temp2) == -1)
        {
          smallest = counter2;
        }
      }
      // We then switch the current element with the smallest element.
      temp = (Flight) flights.elementAt(counter1);
      temp2 = (Flight) flights.elementAt(smallest);
      flights.setElementAt(temp2, counter1);
      flights.setElementAt(temp, smallest);
    }
  }
}


/** ****************************************************************** */


class Flight
{

  private int flightNumber;
  private String origin;
  private String destination;
  private int cost;


  public Flight(int flightNumber, String origin, String destination, int cost)
  {
    this.flightNumber = flightNumber;
    this.origin = origin;
    this.destination = destination;
    this.cost = cost;
  }


  public String toString()
  {
    String result;

    result = "Flight " + flightNumber + " travels from " + origin + " to "
        + destination + " and costs $" + cost;
    return result;
  }


  public boolean checkTrip(String origin, String destination)
  {
    boolean result = false;
    if (this.origin.equals(origin) && this.destination.equals(destination))
    {
      result = true;
    }
    return result;
  }


  public int checkCost(int cost)
  {
    int result = cost;
    if (this.cost < result)
    {
      result = this.cost;
    }
    return result;
  }

  /**
   * PURPOSE: getCost is merely an accessor so that the compareTo method can get
   * the cost information of other flights.
   * @return int cost, the cost of the other flight.
   */

  public int getCost()
  {
    return cost;
  }

  /**
   * PURPOSE: compareTo compares the cost values of 2 flights, and returns an
   * int based on which is larger.
   * @param Flight incFlight: The flight that we wish to compare against the
   *          current flight.
   * @return int, based on whether the incoming or the current flight is larger.
   */

  public int compareTo(Flight incFlight)
  {
    int incCost;

    incCost = incFlight.getCost();

    // If the incoming cost is larger than the current cost, we output 1.
    if (incCost > this.cost) return 1;
    // If they are the same, we output 0.
    else if (incCost == this.cost) return 0;
    // If the current cost is higher, we return -1.
    else
      return -1;
  }
}
