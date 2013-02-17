public class WordQueue
{

  WordNode rear;
  int size;

  WordQueue()
  {
    rear = null;
    size = 0;
  }

  public void enter(String newWord, int lineNumber)
  {

    WordNode curr = rear;
    boolean found = false;

    if (curr != null)
    {
      do
      {
        if (curr.word.equalsIgnoreCase(newWord))
        {
          curr.numbers.enter(lineNumber);
          found = true;
        }

        curr = curr.link;
      } while (curr != rear && !found);

    }

    if (!found)
    {

      size++;
      if (rear == null)
      {
        rear = new WordNode(newWord, lineNumber, null);
        rear.link = rear;
      }
      else
      {
        rear.link = new WordNode(newWord, lineNumber, rear.link);
        rear = rear.link;
      }
    }

  }

  public String leave()
  {

    String temp = null;

    if (rear != null)
    {
      if (rear.link == rear)
      {
        temp = rear.word;
        rear = null;
      }
      else
      {
        temp = rear.link.word;
        rear.link = rear.link.link;
      }
    }

    return temp;
  }

  public String front()
  {

    String temp = null;

    if (rear != null && rear.link != null) temp = rear.link.word;

    return temp;

  }

  public int size()
  {
    return size;
  }

  public boolean isEmpty()
  {

    return (rear == null);

  }

  public String getLines()
  {

    String output = "";
    WordNode front = rear.link;

    while (!front.numbers.isEmpty())
    {
      output = output + front.numbers.leave() + " ";
    }

    return output;

  }

}

class WordNode
{

  String word;
  Queue numbers;
  WordNode link;

  WordNode(String newWord, int lineNumber, WordNode newLink)
  {

    word = newWord;
    numbers = new Queue(lineNumber);
    link = newLink;

  }

}
