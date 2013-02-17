/**
 * Tree23 - 2-3 Tree Data Structure. Includes methods to insert values and
 * search for inserted values.
 * @74.214 SECTION L02
 * @INSTRUCTOR Michael Zapp
 * @ASSIGNMENT Assignment 5
 * @AUTHOR Trevor Bekolay, 6796723
 * @VERSION December 5, 2004
 */

public class Tree23
{

  // The value that a TreeNode gets in 'high' if it holds only one element.
  final int EMPTY = Integer.MIN_VALUE;

  /**
   * TreeNode - A class created for 2-3 trees to hold 2 elements and 3 pointers
   */

  private class TreeNode
  {

    int low;
    int high;
    TreeNode left;
    TreeNode middle;
    TreeNode right;

    /**
     * PURPOSE: The TreeNode constructor. Takes in the parameters of the new
     * TreeNode to be constructed and constructs it.
     * @PARAM int NewLow - the new lower value in the TreeNode
     * @PARAM int newHigh - the new higher value in the TreeNode
     * @PARAM TreeNode newLeft - the new left pointer in the TreeNode
     * @PARAM TreeNode newMiddle - the new middle pointer in the TreeNode
     * @PARAM TreeNode newRight - the new right pointer in the TreeNode
     * @RETURN VOID
     */

    TreeNode(int newLow, int newHigh, TreeNode newLeft, TreeNode newMiddle,
        TreeNode newRight)
    {
      low = newLow;
      high = newHigh;
      left = newLeft;
      middle = newMiddle;
      right = newRight;
    }

    /**
     * PURPOSE: elements returns the number of elements stored in a TreeNode. A
     * value is not an element if it is equal to EMPTY.
     * @RETURN int - the number of elements in the TreeNode.
     */

    public int elements()
    {
      int count = 0; // Counter

      // A value is not counted if it is equal to EMPTY.
      if (low != EMPTY) count++;
      if (high != EMPTY) count++;

      return count;
    }

    /**
     * PURPOSE: children returns the number of children stored in a TreeNode. A
     * pointer is not a child if it is null.
     * @RETURN int - the number of children in the TreeNode.
     */

    public int children()
    {
      int count = 0; // Counter

      // A pointer is not a child if it is null.
      if (left != null) count++;
      if (middle != null) count++;
      if (right != null) count++;

      return count;
    }

    /**
     * PURPOSE: findMiddle rearranges the values in a TreeNode such that the
     * lowest and highest are stored in the TreeNode, and the middle value is
     * returned.
     * @PARAM int key - the value which, in addition to the current TreeNode's
     *          low and high, will be ordered
     * @RETURN int - the middle value
     */

    public int findMiddle(int key)
    {

      int middle = EMPTY;

      // If the key is the highest value
      if (key > low && key > high)
      {
        // Return high and replace it with the key
        middle = high;
        high = key;
      }

      // If the key is the middle value
      else if (key > low && key < high)
      {
        // Simply return it
        middle = key;
      }

      // If the key is the lowest value
      else
      // if(key < low)
      {
        // Return low and replace it with the key
        middle = low;
        low = key;
      }

      return middle;
    }

    /**
     * PURPOSE: add inserts a key value into a TreeNode that currently has one
     * element.
     * @PARAM int key - the key value to be inserted
     * @RETURN VOID
     */

    public void add(int key)
    {

      // If key is larger than the value, simply make high the key value
      if (key > low)
      {
        high = key;
      }

      // If key is lower than the value, set high to low and put key in low
      else
      // if(key < low)
      {
        high = low;
        low = key;
      }

    }

  }

  /**
   * ParentStack - a type of stack that holds TreeNodes
   */

  private class ParentStack
  {

    Node top;

    /**
     * PURPOSE: ParentStack constructor. Creates an empty stack.
     * @RETURN VOID
     */

    ParentStack()
    {
      top = null;
    }

    /**
     * PURPOSE: push adds a TreeNode element to the top of the ParentStack
     * @PARAM TreeNode element - the TreeNode to be added
     * @RETURN VOID
     */

    public void push(TreeNode element)
    {
      top = new Node(element, top);
    }

    /**
     * PURPOSE: pop returns the top element of the ParentStack, then removes it
     * from the stack
     * @RETURN TreeNode - the top element of the ParentStack
     */

    public TreeNode pop()
    {
      TreeNode element = null;

      if (top != null)
      {
        element = top.element;
        top = top.link;
      }

      return (element);
    }

    /**
     * PURPOSE: isEmpty determines is the stack has any elements in it.
     * @RETURN boolean - whether or not the stack has elements
     */

    public boolean isEmpty()
    {
      return (top == null);
    }

    /**
     * Node - a class used by ParentStack that holds a TreeNode element and a
     * link to the next node.
     */

    private class Node

    {

      TreeNode element;
      Node link;

      /**
       * PURPOSE: Node constructor. Accepts a TreeNode and Node, constructs a
       * Node based on that input
       * @PARAM TreeNode newdata - The new TreeNode to added to the stack.
       * @PARAM Node newlink - A link to the next node in the stack
       * @RETURN VOID
       */

      Node(TreeNode newdata, Node newlink)
      {
        element = newdata;
        link = newlink;
      }
    }
  }

  TreeNode root;
  int height;

  /**
   * PURPOSE: The Tree23 constructor creates a new empty 2-3 tree, with height
   * 0.
   * @RETURN VOID
   */

  public Tree23()
  {

    root = null;
    height = 0;

  }

  /**
   * PURPOSE: insert takes in a value and places it in its appropriate place in
   * the 2-3 tree. Employs the split method if needed.
   * @PARAM int key - the value to be inserted
   * @RETURN VOID
   */

  public void insert(int key)
  {

    int middle = EMPTY;
    TreeNode curr = root; // Begin at the root
    TreeNode currParent = null;

    boolean found = false;
    ParentStack parents = new ParentStack(); // A stack of parent TreeNodes

    // Searches through the 2-3 tree to find the correct place for the new
    // element to be inserted, if it is not already in the 2-3 tree.
    while ((curr != null) && !found)
    {

      // Makes sure that the value is not already in the tree
      if (curr.low == key) found = true;
      else if (curr.high == key) found = true;

      // If it is not in the tree
      else
      {

        // Add the current node to the parents stack
        parents.push(curr);

        // Then move to the next node until you reach a null.
        if (curr.children() == 2)
        {
          if (key < curr.low) curr = curr.left;
          else
            curr = curr.right;
        }
        else
        {
          if (key < curr.low) curr = curr.left;
          else if (key > curr.low && key < curr.high) curr = curr.middle;
          else
            curr = curr.right;
        }

      }
    }

    // If the value to be inserted is not already in the tree, insert it
    if (!found)
    {

      curr = parents.pop();

      // If the current node is null, then there is nothing in our tree
      // So we must create a new root that holds the element to be inserted
      if (curr == null)
      {
        root = new TreeNode(key, EMPTY, null, null, null);
        height++; // Height is incremented as we have created a new root
      }

      // If we have a root node
      else
      {

        // If the current TreeNode only has one element, then we simply add the
        // value
        // to be inserted into the current non-full TreeNode
        if (curr.elements() == 1)
        {
          curr.add(key);
        }

        // If the current TreeNode has 2 elements, we must split
        else if (curr.elements() == 2)
        {

          // Find which value is the middle
          middle = curr.findMiddle(key);

          currParent = parents.pop();

          // Since splitting can involve recursion, we call a separate split
          // method
          split(curr, currParent, null, parents, middle);

        }
      }
    }
    else
    {
      // If the item has been found, output an error message
      System.out.println(key + " is already in the two-thee tree!");
    }

  }

  /**
   * PURPOSE: split is used by insert. It splits a TreeNode with 3 elements
   * recursively, if needed.
   * @PARAM TreeNode curr - the current TreeNode that must be split
   * @PARAM TreeNode currParent - the parent of the TreeNode to be split
   * @PARAM TreeNode temp - a temporary TreeNode that is the largest child of a
   *          node that temporarily has four children
   * @PARAM ParentStack parents - the stack of parents build from 'insert'
   * @PARAM int middle - the middle value that will be pushed up in the split
   * @RETURN VOID
   */

  private void split(TreeNode curr, TreeNode currParent, TreeNode temp,
      ParentStack parents, int middle)
  {

    TreeNode rightest = null; // The TreeNode with the largest value when
    // there are 4 children of one node temporarily

    // If the current node has no parent, it must be the root node.
    if (currParent == null)
    {
      // So we split the root node
      root = new TreeNode(middle, EMPTY, new TreeNode(curr.low, EMPTY,
          curr.left, null, curr.middle), null, new TreeNode(curr.high, EMPTY,
          curr.right, null, temp));

      height++; // Since we have created a new root, we must increment height

    }

    // Otherwise, there are numerous different cases
    else
    {

      // If our parent node has two children
      if (currParent.children() == 2)
      {

        // Add the middle value to that parent (as it will have one element
        // currently)
        currParent.add(middle);

        // Then split the current node, based on if it is the right or left
        // child.
        if (curr == currParent.left)
        {
          currParent.left = new TreeNode(curr.low, EMPTY, curr.left, null,
              curr.middle);
          currParent.middle = new TreeNode(curr.high, EMPTY, curr.right, null,
              temp);
        }
        else
        // if(curr == currParent.right)
        {
          currParent.middle = new TreeNode(curr.low, EMPTY, curr.left, null,
              curr.middle);
          currParent.right = new TreeNode(curr.high, EMPTY, curr.right, null,
              temp);
        }

      }

      // If our parent node has three children

      else
      // if(currParent.children() == 3)
      {

        // Split the children into 4 nodes, storing the largest value in a
        // temporary TreeNode called rightest.
        // How it is split depends on where the current node is in relation to
        // its parent.
        if (curr == currParent.left)
        {
          rightest = currParent.right;
          currParent.right = currParent.middle;
          currParent.middle = new TreeNode(curr.high, EMPTY, curr.right, null,
              temp);
          currParent.left = new TreeNode(curr.low, EMPTY, curr.left, null,
              curr.middle);
        }

        else if (curr == currParent.middle)
        {
          rightest = currParent.right;
          currParent.right = new TreeNode(curr.high, EMPTY, curr.right, null,
              temp);
          currParent.middle = new TreeNode(curr.low, EMPTY, curr.left, null,
              curr.middle);
        }

        else
        // if(curr == currParent.right)
        {
          rightest = new TreeNode(curr.high, EMPTY, curr.right, null, temp);
          currParent.right = new TreeNode(curr.low, EMPTY, curr.left, null,
              curr.middle);
        }

        curr = currParent; // Since we must split again, set curr to be its
        // parent
        middle = curr.findMiddle(middle); // Find what will be the middle value
        // when the parent splits
        currParent = parents.pop(); // Set the new parent
        split(curr, currParent, rightest, parents, middle); // Recurse
      }
    }
  }

  /**
   * PURPOSE: search attempts to find a key value in the 2-3 tree and outputs
   * whether or not it was found.
   * @PARAM int key - the key value to be searched for
   * @RETURN boolean - whether or not the key is in the 2-3 tree
   */

  public boolean search(int key)
  {

    TreeNode curr = root; // Start at the root
    boolean found = false;

    // We will search each TreeNode until we have found the element
    // or we have exhausted the 2-3 tree
    while ((curr != null) && !found)
    {

      // If the current TreeNode contains the key, we are done
      if (curr.low == key) found = true;
      else if (curr.high == key) found = true;

      // Otherwise, we go down a level depending on the number of
      // children and how key compares to the values in the current
      // TreeNode.
      else
      {
        if (curr.children() == 2)
        {
          if (key < curr.low) curr = curr.left;
          else
            curr = curr.right;
        }
        else
        {
          if (key < curr.low) curr = curr.left;
          else if (key > curr.low && key < curr.high) curr = curr.middle;
          else
            curr = curr.right;
        }

      }
    }

    return found;

  }

}
