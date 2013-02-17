// the type definition for a linked list node
typedef struct ln {
  int key;
  int data;
  struct ln *next;
} listNode, *listNodePtr;

// prototype definitions for the list operations
void listInsert(listNodePtr *list,  int key, int value);

void listDelete(listNodePtr *list, int key);

int listFind(listNodePtr list, int key);

void traverse(listNodePtr list);


