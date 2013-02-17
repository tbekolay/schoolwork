// Derived from the Queue class, this class lets us track the line numbers for a
// word.
// We use the Integer object to allow for the generic Object held in the Queue
// class.
class LineQueue extends Queue
{
  /*****************************************************************************
   * Public Methods
   ****************************************************************************/

  // Our constructor just uses the superclass.
  LineQueue()
  {
    super();
  }

  // Enter uses the superclass but stores the value in an Integer object.
  public void enter(int item)
  {
    Integer the_value = new Integer(item);

    super.enter_queue((Object) the_value);
  }

  // Leave uses the superclass but extracts the int from the Integer object.
  public int leave()
  {
    Integer the_value = (Integer) super.leave_queue();

    return (the_value.intValue());
  }
} // end class LineQueue
