import java.io.*;
import java.util.*;

public class BSTsolution
{
  
  private Node root; // The root of the tree.
  
  /*********************************************************************
  *  Private Class:  A binary search tree node
  *********************************************************************/
  public class Node
  {
    public int value;  // value stored in node
    public Node left;  // left child of node
    public Node right; // right child of node
    
    /******************************************************
      * Constructor
      *
      * Purpose: creates a new node with a given value
      *          and given left and right children.
      * Input: the value, the left child, and the right
      *        child for the new node.
      ******************************************************/
    public Node( int newValue, Node newLeft, Node newRight )
    {
      value = newValue;
      left = newLeft;
      right = newRight;
    } // end Node constructor
    
  } // end class Node
  
  
  /*********************************************************
    * BST Constructor
    *
    * Purpose: creates an empty tree.
    *********************************************************/
  public BSTsolution()
  {
    root = null;
  }
  
  
  
  
  /*********************************************************
    * delete
    *
    * Purpose: delete a given key from a binary search tree.
    * Input: the key to be deleted.
    *********************************************************/
  public void delete( int key )
  {
    // First, find the node containing the key.
    
    Node keyParent = null;
    Node keyNode = root;
    boolean found = false;
    
    while ((keyNode != null) && !found)
    {
      if (keyNode.value == key)
        found = true;
      else
      {
        keyParent = keyNode;
        if (key < keyNode.value)
          keyNode = keyNode.left;
        else
          keyNode = keyNode.right;
      }
    } // end while
    
    
    if (found)
    {
      // Now delete the key.
      
      if ((keyNode.left != null) && (keyNode.right != null))
        
        // The node to delete has two children.
        
        deleteTwoChildren( keyNode );
      
      else if (keyParent != null)
        
        // The node we're going to delete is not the root
        // and it has zero children or one child.
        
        deleteEasyCase( keyNode, keyParent );
      
      else
        
        // The key we're going to delete is in the root
        // and it has zero children or one child.
        
        deleteEasyCaseRoot();
    }
    else
    {
      System.out.println( "Delete: cannot delete " + key
                          + " because it is not in the tree." );
    }
    
  } // end method delete
  
  /*********************************************************
    * Private Methods for delete
    *********************************************************/
  
  /********************************************************
    * deleteEasyCase
    *
    * Purpose: delete a node with 0 children or 1 child,
    *          where the node is not the root.
    * Input: keyNode is the node to be deleted, and
    *        keyParent is the parent of keyNode.
    ********************************************************/
  private void deleteEasyCase( Node keyNode, Node keyParent )
  {
    if (keyNode.left == null)
    {
      // Replace keyNode with its right child.
      // (Also handles case when both children are null.)
      
      if (keyNode == keyParent.left)
        keyParent.left = keyNode.right;
      else
        keyParent.right = keyNode.right;
    }
    else
    {
      // Replace keyNode with its left child.
      
      if (keyNode == keyParent.left)
        keyParent.left = keyNode.left;
      else
        keyParent.right = keyNode.left;
    }
  } // end deleteEasyCase
  
  
  /********************************************************
    * deleteEasyCaseRoot
    *
    * Purpose: delete the root when it has 0 children or 1 child.
    ********************************************************/
  private void deleteEasyCaseRoot( )
  {
    if (root.left == null)
    {
      // Replace root with its right child.
      // (Also handles case when both children are null.)
      
      root = root.right;
    }
    else
    {
      // Replace root with its left child.
      
      root = root.left;
    }
  } // end deleteEasyCaseRoot
  
  
  /********************************************************
    * deleteTwoChildren
    *
    * Purpose: delete a node with 2 children.
    * Input: keyNode is the node to be deleted.
    ********************************************************/
  private void deleteTwoChildren( Node keyNode )
  {
    Node isParent; // Parent of the inorder successor.
    Node is;       // The inorder successor of keyNode.
    
    // First, find the inorder successor of keyNode.
    // Inorder successor is the leftmost descendant
    // of the right child of keyNode.
    
    isParent = keyNode;
    is = keyNode.right;
    while (is.left != null)
    {
      isParent = is;
      is = is.left;
    } // end while
    
    // Copy the inorder successor's value into keyNode.
    
    keyNode.value = is.value;
    
    // Now delete the inorder successor.
    // (The inorder successor is guaranteed to have
    // at most one child and the inorder successor
    // is also not the root, so its deletion is
    // an easy case.)
    
    deleteEasyCase( is, isParent );
    
  } // end method deleteTwoChildren
  
  
  /*********************************************************
    * constructHardCodedTree
    *
    * Purpose: construct a particular tree without the use
    *          of an insert method --- simply by directly
    *          linking nodes together into the desired shape.
    **********************************************************/
  public void constructHardCodedTree( )
  {
    Node a, b; // Used for constructing the tree.
    Node llc, lrc, rlc, rrc; // The four grandchildren of the root.
                             // Also used for constructing the tree.
    
    // Construct the left child of the left child of the root.
    a = new Node( 4,
                  new Node( 3, null, null ),
                  null );
    a = new Node( 2,
                  new Node( 1, null, null ),
                  a );
    b = new Node( 6,
                  null,
                  new Node( 7, null, null ) );
    b = new Node( 8,
                  b,
                  new Node( 9, null, null ) );
    b = new Node( 10,  b,  null );
    llc = new Node( 5, a, b );
    
    // Construct the right child of the left child of the root.
    a = new Node( 13,
                  new Node( 12, null, null ),
                  new Node( 14, null, null ) );
    b = new Node( 17,
                  null,
                  new Node( 18, null, null ) );
    b = new Node( 16, null, b );
    lrc = new Node( 15, a, b );
    
    // Construct the left child of the right child of the root.
    a = new Node( 20,
                  null,
                  new Node( 21, null, null ) );
    a = new Node( 22, a, null );
    b = new Node( 25,
                  new Node( 24, null, null ),
                  null );
    b = new Node( 26, b, null );
    rlc = new Node( 23, a, b );
    
    // Construct the right child of the right child of the root.
    a = new Node( 29,
                  null,
                  new Node( 30, null, null ) );
    a = new Node( 28, null, a );
    b = new Node( 34,
                  new Node( 33, null, null ),
                  null );
    b = new Node( 32, null, b );
    rrc = new Node( 31, a, b );
    
    // Put the pieces together into one tree.
    
    root = new Node( 19,
                     new Node( 11, llc, lrc ),
                     new Node( 27, rlc, rrc ) );
    
  } // end method constructHardCodedTree
  
  
  
  /************************************************************
    * printInorder
    *
    * Purpose: print out the tree using an inorder traversal.
    *          (If the tree is OK, this should give a sorted
                *          ordering of the values stored in the tree.)
    ************************************************************/
  private static void printInorder( Node curr )
  {
    if (curr != null)
    {
      printInorder( curr.left );
      System.out.print( curr.value + " " );
      printInorder( curr.right );
    }
  } // end method printInorder
  
  
  
  /*************************************************************
    * Main
    *
    * Purpose: Create a binary search tree and then perform
    *          deletions in the tree.
    *          Note: The tree is NOT constructed using an insertion
    *          algorithm.  Instead, the tree is "hard-coded" ---
    *          nodes are simply connected together.
    **************************************************************/
  public static void main( String[] args )
  {
    BSTsolution Tree = new BSTsolution();
    
    // First, construct the tree.
    
    Tree.constructHardCodedTree();
    
    
    // Perform deletions.
    
    // Print original tree.
    System.out.println( "Original tree:" );
    System.out.println();
    printInorder( Tree.root );
    System.out.println();
    System.out.println();
    
    // Delete a node with no children.
    Tree.delete( 7 );
    System.out.println( "Deleted 7 (no children):" );
    System.out.println();
    printInorder( Tree.root );
    System.out.println();
    System.out.println();
    
    // Delete nodes with one children.
    Tree.delete( 20 );
    Tree.delete( 25 );
    Tree.delete( 29 );
    Tree.delete( 34 );
    System.out.println( "Deleted 20, 25, 29, 34 (one child each):" );
    System.out.println();
    printInorder( Tree.root );
    System.out.println();
    System.out.println();
    
    // Delete node with 2 children, where successor is right child,
    // and successor has no children.
    Tree.delete( 13 );
    System.out.println( "Deleted 13 (Inorder successor is right child"
                        + " and successor has no children):" );
    System.out.println();
    printInorder( Tree.root );
    System.out.println();
    System.out.println();
    
    // Delete node with 2 children, where successor is right child,
    // and successor has a right child.
    Tree.delete( 16 );
    System.out.println( "Deleted 16 (Inorder successor is right child"
                        + " and successor has a right child):" );
    System.out.println();
    printInorder( Tree.root );
    System.out.println();
    System.out.println();
    
    // Delete nodes with 2 children whose successor is NOT the right child.
    Tree.delete( 19 );
    Tree.delete( 11 );
    Tree.delete( 5 );
    Tree.delete( 27 );
    System.out.println( "Deleted 19, 11, 5, 27 "
                        + "(Inorder successor is not the right child):" );
    System.out.println();
    printInorder( Tree.root );
    System.out.println();
    System.out.println();
    
    System.out.println( "BST program completed normally." );
    
  } // end main
  
  
  
} // end class BST
