#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tree.h"
#define NULL 0
#define WORDLEN 20

/**
 * PURPOSE: insert adds the specified input to the Tree in
 *			a particular order.
 * PARAM    char *input: the string to be added
 *			Tree tree: the tree to add the string to
 * RETURN  Tree: a pointer back to the new tree
 **/

Tree insert(Tree tree, char *input)
{
	Node *prev = NULL;
	Node *curr = tree;
	Node *newnode = (Node*)malloc(sizeof(Node)); /*Set space aside for the new node*/

	/*Make links initially NULL*/
	newnode->left = NULL;

	/*Set space for an copy the input into the node's data member*/
	newnode->data = (char*)malloc(strlen(input));
	strcpy(newnode->data, input);

	newnode->right = NULL;

	/*Traverse the tree until you come to NULL*/
	while(curr != NULL)
	{
		prev = curr;

		if(strcmp(input, curr->data) < 0)
			curr = curr->left;
		else
			curr = curr->right;
	}

	/*If prev is null, then we haven't traversed at all, so
	we must create a new root*/
	if(prev == NULL)
		tree = newnode;

	/*Otherwise, put it in previous's left or right link, depending
	on how it compares to prev->data*/
	else
	{
		if(strcmp(input, prev->data) < 0)
			prev->left = newnode;
		else
			prev->right = newnode;
	}

	return tree;
}

/**
 * PURPOSE: fillArray is a recursive method that fills a char* array
 *			with strings, taking them from a BST
 * PARAM    Tree tree: the tree to take the strings from
 *			char *array[]: the array to be filled
 *			int next: How far the current iteration must fill the array
 * RETURN  int: the index of the first unused component of the subarray
 **/

static int fillArray(Tree tree, char *array[], int next)
{
	int j;

	/*If the current tree is empty, this is the base case*/
	if(tree == NULL)
		return next;

	/*Otherwise*/
	else
	{
		/*Fill the array with elements to the left of the current Node*/
		j = fillArray(tree->left, array, next);
		/*Fill the current array element with the current node's data*/
		array[j] = tree->data;
		/*Then fill the array with elements to the right*/
		return fillArray(tree->right, array, j+1);
	}
}

/**
 * PURPOSE: insertAll is a recursive method that adds all of the elements
 *			in the passed array into the passed tree
 * PARAM    Tree tree - the tree to be created
 *			char *array[] - an array of char*'s that will be the data in the tree
 *			int left - the left bound of the subtree we are working with
 *			int right - the right bound of the subtree we are working with
 * RETURN  Tree - a pointer to the newly created tree
 **/

static Tree insertAll(Tree tree, char *array[], int left, int right)
{

	int mid;

	/*If the left and right pointers haven't crossed*/
	if(left <= right)
	{
		/*Find the middle element*/
		mid = (left + right)/2;
		/*Insert it into the tree*/
		tree = insert(tree, array[mid]);
		/*Insert into the tree the middle element from the left half recursively*/
		tree = insertAll(tree, array, left, mid - 1);
		/*Insert into the tree the middle element from the right half recursively*/
		tree = insertAll(tree, array, mid + 1, right);
	}

	return tree;

}

/**
 * PURPOSE: buildBalancedTree creates a balanced free from a presumably
 *			unbalanced one.
 * PARAM    Tree tree: unbalanced tree to be balanced
 *			int count: number of elements in that tree
 * RETURN  Tree: pointer to the newly balanced tree
 **/

Tree buildBalancedTree(Tree tree, int count)
{
	char *array[count];

	/*Move all of the elements of the unbalanced (but sorted)
	tree to an array*/
	fillArray(tree, array, 0);

	/*Initialize a new tree*/
	tree = NULL;

	/*Insert the elements of the array to a new tree, in such
	a way that it will be balanced.*/
	tree = insertAll(tree, array, 0, count - 1);

	return tree;
}

/**
 * PURPOSE: print_Tree recursively prints a tree horizontally, with the rightmost
 *			leaves at the top and the root and the farthest left.
 * PARAM    Tree tree: the tree (or subtree) to be printed
 *			int level: the level that the tree or subtree is on
 * RETURN  VOID
 **/

static void print_Tree(Tree tree, int level)
{

	int i; /*Loop counter*/

	/*If the tree has a right child, print it recursively*/
	if(tree->right != NULL)
		print_Tree(tree->right, level + 1);

	/*Tab over level times*/
	for(i = 0; i < level; i++)
	{
		printf("\t");
	}
	/*Print the tree's root's data*/
	printf("%s",tree->data);

	/*If the tree has a left child, print it recursively*/
	if(tree->left != NULL)
		print_Tree(tree->left, level +1);

}

/**
 * PURPOSE: Wrapper method for print_Tree, as the initial level of 0
 *			is a little odd to pass from the mainline.
 * PARAM    Tree tree: The tree to be printed
 * RETURN  VOID
 **/

void printTree(Tree tree)
{
	/*The root is on level 0*/
	print_Tree(tree, 0);
}
