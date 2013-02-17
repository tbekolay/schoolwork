CFLAGS = -Wall
CC = gcc

PROG = inToPost
OBJS = stack.o toPost.o

$(PROG) : $(OBJS)
	$(CC) $(CFLAGS) inToPost.c $(OBJS) -lm -o $@

%.o : %.c
	$(CC) $(CFLAGS) -c $<
	
clean:
	rm -f $(PROG) $(OBJS)
