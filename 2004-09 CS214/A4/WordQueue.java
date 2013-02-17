// This class is used to queue the line numbers for a given word.
class WordQueue extends Queue
{
  /*****************************************************************************
   * Inner class
   ****************************************************************************/

  // The objects stored in this queue consist of the word and a pointer to a
  // queue.
  private class Word
  {
    public String word;
    public LineQueue lines;

    // The constructor takes a line number since we have one if we're adding the
    // word.
    Word(String new_word, int line)
    {
      word = new_word;

      // create the queue and insert the first line number
      lines = new LineQueue();
      lines.enter(line);
    } // end Word constructor
  } // end private class Word

  /*****************************************************************************
   * Public Methods
   ****************************************************************************/

  // standard constructor
  WordQueue()
  {
    super();
  }

  // We have an insert routine which will look for the word and add it if not
  // found.
  // We queue the words so that they will come out in the order they appeared.
  // If the word is found it will simply enqueue the line number.
  public void enter(String word, int line)
  {
    Word the_word = null;
    boolean found = false;
    Node next = null;

    // Make sure the queue isn't empty.
    if (!isEmpty())
    {

      the_word = (Word) rear.element;
      next = rear.link;

      // We have a special case with rear so check it first.
      if (the_word.word.equals(word)) found = true;

      // First, search the queue for the word.
      while (next != rear && !found)
      {
        the_word = (Word) next.element;

        if (the_word.word.equals(word)) found = true;
        else
          next = next.link;
      }
    } // end if (!isEmpty())

    // If it's not there, add it to the queue.
    if (!found)
    {
      the_word = new Word(word, line);
      super.enter_queue((Object) the_word);
    }

    // Otherwise, add the line to the word's queue.
    else
    {
      the_word.lines.enter(line);
    }
  } // end method enter

  // We can print all of their words and the lines they appear on.
  public void print()
  {
    Word next_word;

    // Iterate through the queue and print the word and all its line numbers.
    while (!isEmpty())
    {
      next_word = leave();

      System.out.print("Invalid word \"" + next_word.word
          + "\" found on lines ");

      // Get all the line numbers for the word and print them.
      while (!next_word.lines.isEmpty())
        System.out.print(next_word.lines.leave() + " ");

      System.out.println();
    } // end while
  } // end method print

  /*****************************************************************************
   * Private Methods
   ****************************************************************************/

  // We don't need leave as public methods since we have a more abstract
  // interface.
  private Word leave()
  {
    return ((Word) super.leave_queue());
  }

} // end class WordQueue
