#include "hash.h"

struct TABLE;
typedef struct TABLE table;

table *createtable(int);
void addtable(table*, int, char*, char);
int searchtable(table*, int, char*, char);