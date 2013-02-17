CFLAGS =
CC = CC

PROG = taz
OBJS = BinarySearchTree.o TreeNode.o HuffNode.o HuffmanTree.o ListNode.o LinkedList.o ListHNode.o PriorityQueue.o ListFNode.o Frequency.o FreqList.o FileHandler.o

$(PROG) : $(OBJS)
	$(CC) $(CFLAGS) taz.cpp $(OBJS) -o $@

%.o : %.cpp
	$(CC) $(CFLAGS) -c $<
	
clean:
	rm -f $(PROG) $(OBJS)
