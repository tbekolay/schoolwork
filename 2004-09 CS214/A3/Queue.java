public class Queue
{

  Node rear;

  Queue()
  {
    rear = null;
  }

  Queue(int newData)
  {
    rear = new Node(newData, null);
    rear.link = rear;
  }

  public void enter(int newData)
  {

    if (rear == null)
    {
      rear = new Node(newData, null);
      rear.link = rear;
    }
    else
    {
      rear.link = new Node(newData, rear.link);
      rear = rear.link;
    }

  }

  public int leave()
  {

    int temp = -1;

    if (rear != null)
    {
      if (rear.link == rear)
      {
        temp = rear.data;
        rear = null;
      }
      else
      {
        temp = rear.link.data;
        rear.link = rear.link.link;
      }
    }

    return temp;
  }

  public int front()
  {

    int temp = -1;

    if (rear != null && rear.link != null) temp = rear.link.data;

    return temp;

  }

  public boolean isEmpty()
  {

    return (rear == null);

  }

}

class Node
{

  int data;
  Node link;

  Node(int newData, Node newLink)
  {
    data = newData;
    link = newLink;
  }

}
