public class Stack
{

  Node top;

  Stack()
  {
    top = null;
  }

  public void push(Cell element)
  {
    top = new Node(element, top);
  }

  public Cell pop()
  {
    Cell element = null;

    if (top != null)
    {
      element = top.data;
      top = top.link;
    }

    return (element);
  }

  public Cell top()
  {
    Cell element = null;

    if (top != null) element = top.data;

    return (element);
  }

  public boolean isEmpty()
  {
    return (top == null);
  }

}

class Node
{
  Node link;
  Cell data;

  Node(Cell newdata, Node newlink)
  {
    data = newdata;
    link = newlink;
  }

}
