public class Sorting
{

  public static void main(String[] parms)
  {

    int[] list = { 500, 300, 100, 700, 900, 10, 200, 600, 5 };

    printList(list);
    sortList3(list);
    printList(list);

    stringSort();

    System.out.println("\nProgram completed normally.");
  }

  public static void sortList1(int[] list)
  {
    int counter1;
    int counter2;
    int temp;

    // Use the following statements if the original array
    // must not be modified:
    // int[] list2 = new int[list.length];
    // System.arraycopy(list, 0, list2, 0, list.length);

    // Bubble sort
    for (counter1 = 0; counter1 < list.length; counter1++)
    {
      for (counter2 = 0; counter2 < list.length; counter2++)
      {
        if (list[counter2] > list[counter2 + 1])
        {
          temp = list[counter2];
          list[counter2] = list[counter2 + 1];
          list[counter2 + 1] = temp;
        }
      }
    }
  }

  public static void sortList2(int[] list)
  {
    int counter1;
    int counter2;
    int temp;

    // Bubble sort
    for (counter1 = 0; counter1 < list.length - 1; counter1++)
    {
      for (counter2 = 0; counter2 < list.length - counter1 - 1; counter2++)
      {
        if (list[counter2] > list[counter2 + 1])
        {
          temp = list[counter2];
          list[counter2] = list[counter2 + 1];
          list[counter2 + 1] = temp;
        }
      }
    }
  }

  public static void sortList3(int[] list)
  {
    int counter1;
    int counter2;
    int smallest;
    int temp;

    // Selection sort
    for (counter1 = 0; counter1 < list.length - 1; counter1++)
    {
      smallest = 0;
      for (counter2 = smallest + 1; counter2 < list.length; counter2++)
      {
        if (list[smallest] > list[counter2])
        {
          smallest = counter2;
        }
      }
      temp = list[counter1];
      list[counter1] = list[smallest];
      list[smallest] = temp;
    }
  }

  public static void sortList4(int[] list)
  {
    int counter1;
    int counter2;
    int smallest;
    int temp;

    // Selection sort
    for (counter1 = 0; counter1 < list.length - 1; counter1++)
    {
      smallest = counter1;
      for (counter2 = smallest + 1; counter2 < list.length; counter2++)
      {
        if (list[smallest] > list[counter2])
        {
          smallest = counter2;
        }
      }
      temp = list[counter1];
      list[counter1] = list[smallest];
      list[smallest] = temp;
    }
  }

  public static void sortList5(int[] list1)
  {
    int counter1;
    int counter2;
    int element;
    int[] list2;

    // Insertion sort
    if (list1.length >= 1)
    {
      list2 = new int[list1.length];
      list2[0] = list1[0];
    }
    else
    {
      list2 = null;
    }
    for (counter1 = 1; counter1 < list1.length; counter1++)
    {
      element = list1[counter1];
      for (counter2 = counter1; counter2 > 0 && list2[counter2 - 1] > element; counter2--)
      {
        list2[counter2] = list2[counter2 - 1];
      }
      list2[counter2] = element;
      // printList(list2);
    }
    printList(list2); // don't bother returning the list, just print it
  }

  public static void printList(int[] list)
  {
    int counter;

    System.out.println();
    for (counter = 0; list != null && counter < list.length; counter++)
    {
      System.out.println(list[counter]);
    }
  }

  public static void stringSort()
  {
    String[] stringList = { "Xena", "Lara", "Sean", "Justin", "Brittany" };
    printStrings(stringList);
    sortList6(stringList);
    printStrings(stringList);
  }

  public static void sortList6(String[] list)
  {
    int counter1;
    int counter2;
    int smallest;
    String temp;

    // Selection sort
    for (counter1 = 0; counter1 < list.length - 1; counter1++)
    {
      smallest = counter1;
      for (counter2 = smallest + 1; counter2 < list.length; counter2++)
      {
        if (list[smallest].compareTo(list[counter2]) > 0)
        {
          smallest = counter2;
        }
      }
      temp = list[counter1];
      list[counter1] = list[smallest];
      list[smallest] = temp;
    }
  }

  public static void printStrings(String[] list)
  {
    int counter;

    System.out.println();
    for (counter = 0; counter < list.length; counter++)
    {
      System.out.println(list[counter]);
    }
  }
}
