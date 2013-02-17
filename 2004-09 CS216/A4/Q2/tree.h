typedef struct node {
    struct node *left;
    char *data;
    struct node *right;
} Node;

typedef Node *Tree;

Tree insert(Tree tree, char *input);
Tree buildBalancedTree(Tree input, int count);
void printTree(Tree input);
