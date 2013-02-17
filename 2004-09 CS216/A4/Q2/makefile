CFLAGS = -Wall
CC = gcc

PROG = printTree
OBJS = tree.o

$(PROG) : $(OBJS)
	$(CC) $(CFLAGS) printTree.c $(OBJS) -o $@

%.o : %.c
	$(CC) $(CFLAGS) -c $<
	
clean:
	rm -f *.o printTree