/**
 * SparseMatrix - A method of representing a matrix containing mostly 0's using
 * linked lists
 * @74.214 SECTION L02
 * @INSTRUCTOR Professor Zapp
 * @ASSIGNMENT Assignment 2
 * @AUTHOR Trevor Bekolay, 6796723
 * @VERSION October 25, 2004
 */

public class SparseMatrix
{

  Node tlCorner; // points to the top left corner of linked list structure
  int maxRow; // number of rows
  int maxCol; // number of columns

  /**
   * PURPOSE: This constructor for the SparseMatrix accepts bounds for it and
   * creates a structure for the SparseMatrix to be used.
   * @PARAM int rowsize: number of rows
   * @PARAM int colsize: number of columns
   * @RETURN VOID
   */

  public SparseMatrix(int rowsize, int colsize)
  {

    maxRow = rowsize;
    maxCol = colsize;
    Node lastRow = new Node(rowsize + 1, 0, 0, null, null); // creates a dummy
    // node that
    // tlCorner will
    // point down to.
    Node lastCol = new Node(0, colsize + 1, 0, null, null); // creates a dummy
    // node that
    // tlCorner will
    // point right to.
    tlCorner = new Node(0, 0, 0, lastRow, lastCol); // creates the tlCorner node
    // that will be used
    // extensivley

  }

  /**
   * PURPOSE: getValue returns the integer value stored at the coordinates
   * passed to it.
   * @PARAM int row: the row coordinate of the value
   * @PARAM int col: the column coordinate of the value
   * @RETURN INT: The integer value at row, col
   */

  public int getValue(int row, int col)
  {

    Node curr; // the node that will traverse the sparse matrix
    int value = 0; // the value to be returned. If the node does not exist,
    // it will return 0.

    curr = findValue(row, col); // utilizes the findValue method to return the
    // node
    // at row, col if one exists.

    if (curr != null) // if a node was found
    value = curr.value; // the value is returned

    return value;

  }

  /**
   * PURPOSE: setValue sets a particular coordinate in the SparseMatrix to a
   * certain value.
   * @PARAM int row: the row coordinate to be set
   * @PARAM int col: the column coordinate to be set
   * @PARAM int newEntryValue: the new value that will be set.
   * @RETURN VOID
   */

  public void setValue(int row, int col, int newEntryValue)
  {

    Node above; // the node above the node that we wish to deal with
    Node prev = null; // previous node used to manage links
    Node curr; // current node
    int oldValue; // the old value at that coordinate

    curr = findValue(row, col); // makes curr the old node if one exists

    if (curr != null) // if one exists, it merely changes the value stored in
    // it.
    {
      curr.value = newEntryValue;
      if (curr.value == 0) // if it changes that value to 0, it removes that
      // node.
      {
        findDown(row, col).down = curr.down; // bypasses that node in its
        // column
        findRight(row, col).right = curr.right; // bypasses the node in its row
        if (isRowEmpty(row)) // if that row is now empty, it will remove it
        deleteRow(row);
        if (isColEmpty(col)) // if that column is now empty, it will remove it.
        deleteCol(col);
      }
    }

    else
    // if that node does not yet exist, it will create it
    {

      if (findValue(row, 0) == null) // if the row does not exist, it is
      // created
      createRow(row);
      if (findValue(0, col) == null) // if the column does not exist, it is
      // created
      createCol(col);

      curr = tlCorner;

      // traverses the linked list until curr is pointing to the node after the
      // node we
      // wish to create will be, and prev points to the node before it
      while (curr.row < row)
        curr = curr.down;

      while (curr.col < col)
      {
        prev = curr;
        curr = curr.right;
      }

      // makes the new node, pointed to by the node to the left of it, pointing
      // to the
      // node to the right and below it
      prev.right = new Node(row, col, newEntryValue, findDown(row, col).down,
          curr);

      // creates a link from the node above it to the node just created
      findDown(row, col).down = prev.right;

    }

  }

  /**
   * PURPOSE: findValue is a private method that returns the node at a specified
   * coordinate or null is there is no node at that coordinate
   * @PARAM int row: the row of the node we wish to find
   * @PARAM int col: the column of the node we wish to find
   * @RETURN NODE: the node specified or null if none found
   */

  private Node findValue(int row, int col)
  {

    Node curr = tlCorner;
    Node found = null; // the node to be returned. Remains null if not found.

    while (curr.row < row)
      // moves to the row we wish to find
      curr = curr.down;
    if (curr.row == row) // if that row is present, traverses to the column we
    // wish to find
    {
      while (curr.col < col)
        curr = curr.right;
      if (curr.col == col) // if that column exists, then we have found the
      // node and return it.
      found = curr;
    }

    return found;

  }


  /**
   * PURPOSE: findDown is used by setValue to determine the Node that would be
   * directly above a node at a specified coordinate
   * @PARAM int row: the row of the specified coordinate
   * @PARAM int col: the column of the specified coordinate
   * @RETURN NODE: The node that is directly above a node at that coordinate
   */

  private Node findDown(int row, int col)
  {

    Node curr = tlCorner;
    Node prev = null;

    // traverses right across the linked list to the correct column first
    while (curr.col < col)
      curr = curr.right;

    // then traverses down until curr is the node below the specified
    // coordinates
    // and prev is the node above the specified coordinates
    while (curr.row < row)
    {
      prev = curr;
      curr = curr.down;
    }

    return prev;

  }


  /**
   * PURPOSE: findRight is used by setValue to determine the Node that would be
   * directly left of a node at a specified coordinate
   * @PARAM int row: the row of the specified coordinate
   * @PARAM int col: the column of the specified coordinate
   * @RETURN NODE: The node that is directly left of a node at that coordinate
   */

  private Node findRight(int row, int col)
  {

    Node curr = tlCorner;
    Node prev = null;

    // traverses down across the linked list to the correct row first
    while (curr.row < row)
      curr = curr.down;

    // then traverses right until curr is the node right of the specified
    // coordinates
    // and prev is the node left of the specified coordinates
    while (curr.col < col)
    {
      prev = curr;
      curr = curr.right;
    }

    return prev;

  }


  /**
   * PURPOSE: deleteRow deletes a specific row by bypassing the first dummy
   * node. This is only used if we have confirmed that the row is indeed empty.
   * @PARAM int row: the row to be deleted.
   * @RETURN VOID
   */

  private void deleteRow(int row)
  {

    Node prev = null;
    Node curr = tlCorner;

    // traverses down until curr is the row header to be deleted and prev
    // is the row header after it.
    while (curr.row < row)
    {
      prev = curr;
      curr = curr.down;
    }

    // bypasses the empty row
    prev.down = curr.down;

  }

  /**
   * PURPOSE: deleteCol deletes a specific column by bypassing the first dummy
   * node. This is only used if we have confirmed that the column is indeed
   * empty.
   * @PARAM int col: the column to be deleted.
   * @RETURN VOID
   */

  private void deleteCol(int col)
  {

    Node prev = null;
    Node curr = tlCorner;

    // traverses right until curr is the column header to be deleted and prev
    // is the column header after it.
    while (curr.col < col)
    {
      prev = curr;
      curr = curr.right;
    }

    // bypasses the empty column
    prev.right = curr.right;

  }

  /**
   * PURPOSE: createRow creates a new empty row by creating the header and
   * trailer nodes for that row.
   * @PARAM int row: the row to be created
   * @RETURN VOID
   */

  private void createRow(int row)
  {

    Node prev = null;
    Node curr = tlCorner;

    // traverses down until curr is the row header to be deleted and prev
    // is the row header after it.
    while (curr.row < row)
    {
      prev = curr;
      curr = curr.down;
    }

    // creates a new header node that points to a trailer node which points to
    // nothing.
    prev.down = new Node(row, 0, 0, curr, new Node(row, maxCol + 1, 0, null,
        null));

  }

  /**
   * PURPOSE: createCol creates a new empty column by creating the header and
   * trailer nodes for that column.
   * @PARAM int col: the column to be created
   * @RETURN VOID
   */

  private void createCol(int col)
  {

    Node prev = null;
    Node curr = tlCorner;

    // traverses right until curr is the column header to be deleted and prev
    // is the column header after it.
    while (curr.col < col)
    {
      prev = curr;
      curr = curr.right;
    }

    // creates a new header node that points to a trailer node which points to
    // nothing.
    prev.right = new Node(0, col, 0, new Node(maxRow + 1, col, 0, null, null),
        curr);

  }

  /**
   * PURPOSE: isRowEmpty determines if the row is empty; that is, there are only
   * two nodes, a header and trailer
   * @PARAM int row: the row to be tested
   * @RETURN BOOLEAN: true if the row is empty, false if there are elements
   *         within
   */

  private boolean isRowEmpty(int row)
  {

    boolean rowEmpty = true;
    Node test = tlCorner;

    // traverses the row headers
    while (test.row < row)
      test = test.down;

    // if the correct row header is found, determine if there are only 2 nodes.
    if (test.row == row)

    // if so, then return false as there are elements.
    if (test.right.right != null) rowEmpty = false;

    return rowEmpty;

  }

  /**
   * PURPOSE: isColEmpty determines if the column is empty; that is, there are
   * only two nodes, a header and trailer
   * @PARAM int col: the column to be tested
   * @RETURN BOOLEAN: true if the column is empty, false if there are elements
   *         within
   */

  private boolean isColEmpty(int col)
  {

    boolean colEmpty = true;
    Node test = tlCorner;

    // traverses the column headers
    while (test.col < col)
      test = test.right;

    // if the correct column header is found, determine if there are only 2
    // nodes.
    if (test.col == col)

    // if so, then return false as there are elements.
    if (test.down.down != null) colEmpty = false;

    return colEmpty;

  }

}

/**
 * Node - an internal class used to implement the SparseMatrix using a linked
 * list.
 * @74.214 SECTION L02
 * @INSTRUCTOR Professor Zapp
 * @ASSIGNMENT Assignment 2
 * @AUTHOR Trevor Bekolay, 6796723
 * @VERSION October 25, 2004
 */

class Node
{

  int row; // the row this node is in.
  int col; // the column this node is in.
  int value; // the value to be stored
  Node down; // a pointer pointing to the next non-zero element below it or
  // a trailer dummy node.
  Node right; // a pointer pointing to the next non-zero element to the right of
  // it or a trailer dummy node.

  /**
   * PURPOSE: This constructor creates a new Node object based on the parameters
   * @PARAM int row: the row the node will be in.
   * @PARAM int col: the column the node will be in.
   * @PARAM int value: the value to be stored.
   * @PARAM Node down: the pointer to the node below it
   * @PARAM Node right: the pointer to the node to the right of it
   * @RETURN VOID
   */

  public Node(int row, int col, int value, Node down, Node right)
  {

    this.row = row;
    this.col = col;
    this.value = value;
    this.down = down;
    this.right = right;
  }

}
