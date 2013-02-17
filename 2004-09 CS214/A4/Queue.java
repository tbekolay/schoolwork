// A generic queue class that we can use to build our word and line number
// queues from.
class Queue
{
  /*****************************************************************************
   * Inner class
   ****************************************************************************/

  protected class Node
  {
    public Object element;
    public Node link;

    Node(Object new_element, Node new_link)
    {
      element = new_element;
      link = new_link;
    } // end Node constructor
  } // end class Node

  /*****************************************************************************
   * Attributes
   ****************************************************************************/

  // We'll be using a circular linked list with a single pointer to the last
  // element in the queue.
  protected Node rear; // Pointer to the tail of the queue.

  /*****************************************************************************
   * Public Methods
   ****************************************************************************/

  // Standard constructor.
  Queue()
  {
    rear = null;
  }

  // Adds the passed item to the end of the queue.
  public void enter_queue(Object item)
  {
    Node new_node = new Node(item, null);

    // We have to have special processing if the queue is empty.
    if (rear != null)
    {
      // Our new node points to the front of the queue.
      new_node.link = rear.link;

      // The current rear node has to point to our new node.
      rear.link = new_node;
    }

    else
    {
      // Since it's circular the element should point to itself.
      // This makes getting the front node easier...
      new_node.link = new_node;
    }

    // Our rear pointer always points to the new node.
    rear = new_node;
  } // end method enter_queue

  // Pulls off the front queue element and updates our rear node's link.
  public Object leave_queue()
  {
    Object front = null;

    // Only process if there's something in the queue.
    if (rear != null)
    {
      front = rear.link.element;

      // If there's only 1 element, then we have to set rear to null.
      if (rear.link == rear) rear = null;
      else
        rear.link = rear.link.link;
    }

    return (front);
  } // end method leave_queue

  public boolean isEmpty()
  {
    return (rear == null);
  }
} // end class Queue
