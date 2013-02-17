! a5q3.s
!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
!NAME              Stew Dent
!STUDENT NUMBER    1234567
!COURSE            74.222
!INSTRUCTOR        I. M. Bohring
!ASSIGNMENT        5
!QUESTION          3
!---------------------------------------------------------
! This program performs operations on a linked list of 
! 'student' nodes. The node stucture is outlined below. 
! The list is ordered; the key value is student number.
! The supported operations are:
! (C)reate: create a new list of student nodes.
! (P)rint: print the list
! (A)dd: a new node to the list.
! (D)elete: delete a node.
! (M)odify: change the data in a node.
! (Q)uit the program.
!---------------------------------------------------------
! Node structure
stno	=	0	! offset for student number (4 bytes)
name	=	4	! offset for name (31 bytes)
faculty	=	35	! offset for faculty (1 byte)
link	=	36	! offset for link (4 bytes)
linksz	=	4	! size of link
nodesz	=	link+linksz	! node size (40 bytes)
null	=	0x0		! null pointer
!---------------------------------------------------------
! Register use:
!	%L0	pointer to the linked list (head)
!	%L1	holds current operation
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

	mov	%g0,%L0		! head = null

	ba	mEnter		! enter main loop at bottom
	nop
mLoop:
chk1:	subcc	%L1,'C',%g0	! check for Create
	bne	chk2
	nop
	call	create		! create a list
	nop
	tst	%o0		! see if create successful
	be	mallocfail	! if not, end program
	nop
	mov	%o0,%L0		! save head pointer
	ba	mEnter		! enter main loop at bottom
	nop

chk2:	subcc	%L1,'P',%g0	! check for Print
	bne	chk3
	nop
	mov	%L0,%o0		! parm 1 (head)
	call	print
	nop
	ba	mEnter		! enter main loop at bottom
	nop
	
chk3:	subcc	%L1,'A',%g0	! check for Add
	bne	chk4
	nop
	mov	%L0,%o0		! parm 1 (head)
	call	add
	nop
	tst	%o0		! see if add successful
	be	mallocfail	! if not, end program
	nop
	ba	mEnter
	nop
	
chk4:	subcc	%L1,'D',%g0	! check for Delete
	bne	chk5
	nop
	call	decin		! read student number
	nop			! delay slot
	mov	%o0,%o1		! parm 2 (Key)
	call	decout		! echo output
	nop			! delay slot
	call	newline
	nop			! delay slot
	mov	%L0,%o0		! parm 1 (head)
	call	delete
	nop
	tst	%o0		! see if delete successful
	bne	mEnter		! it was 
	nop
	set	mmsg03,%o0
	call	strout
	nop
	ba	mEnter
	nop
	
chk5:	subcc	%L1,'M',%g0	! check for Modify
	bne	mEnter		! try again
	nop
	mov	%L0,%o0		! parm 1 (head)
	call	modify
	nop
	tst	%o0		! see if modify successful
	bne	mEnter		! it was 
	nop
	set	mmsg04,%o0
	call	strout
	nop

mEnter:
	set	mmsg00,%o0	! prompt for operation
	call	strout
	nop
	set	buffer,%o0
	call	strin		! get the task	
	nop
	ldub	[%o0],%o0	! get the operation char.
	mov	%o0,%L1		! save this operation char.
	call	charout		! echo operation 
	nop
	call	newline
	nop
	subcc	%o0,'Q',%g0	! check for Quit operation
	bne	mLoop		! if not, do the task
	nop
	ba	done
	nop			! delay slot
	
mallocfail:
        set     mmsg02,%o0
        call    strout
        nop                     !the "delay slot"


!Normal termination sequence
done:
       set      goodbye,%o0
       call     strout
       nop
                
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section

         .section   ".data"
banner:  .ascii     "NAME             Stew Dent\n"
         .ascii     "STUDENT NUMBER   1234567\n"
         .ascii     "COURSE           74.222\n"
         .ascii     "INSTRUCTOR       I. M. Bohring\n"
         .ascii     "ASSIGNMENT       5\n"
         .asciz     "QUESTION         3\n\n"    
goodbye: .asciz     "\n\nEnd of Processing\n\n"
mmsg00:	.asciz	"Enter operation: "
mmsg01:	.asciz	"This operation is currently unimplemented.\n"
mmsg02:	.asciz	"SERIOUS ERROR - malloc failed\n"
mmsg03:	.asciz	"Delete failed - Student Number not found.\n"
mmsg04:	.ascii	"Modify failed - Student Number not found\n"
	.asciz	"                or malloc failed.\n"
buffer:	.skip	2	! kludge to avoid a problem with charin...

!=========================================================
! subroutine create
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
create:	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! This subroutine creates an ordered linked list of
! student nodes. The list has a dummy node at the head
! of the list.
! The malloc function is used to obtain dynamic memory.
! If malloc fails, print an error message and return
! null.
!=========================================================
!	Input parameters
!	none
!	Output parameters
!	%i0	pointer to linked list or null if malloc
!		failure
!=========================================================
!	Register Usage
!	%L0	number of nodes to be read
!	%L1	pointer to head of list created
!	%L2	temp. pointer (newnode)
!	%L3	temp. pointer for list manipulation
!=========================================================

! Create a dummy node at the front of the list
	mov	nodesz,%o0	! number of bytes needed
	call	malloc		! Try to get a node
	nop
	mov	%o0,%L1		! initialize list pointer
	subcc	%o0,%g0,%g0	! See if malloc was successful
	be	crFail		! exit if not successful
	nop			! (delay slot)
	st	%g0,[%L1+link]	! store null into dummy link

! read and echo the number of nodes to be read
	set 	cmsg00,%o0
	call	strout
	nop
	call	decin
	nop
	call	decout
	nop
	call	newline
	nop
	mov	%o0,%L0		! save the node count

! Reading loop
	ba	crEnter		! enter loop at bottom
	nop
! Serious error with malloc goes here
crFail:
	clr	%i0		! set return value to null
	ba	crEnd		! all done
	nop			! (delay slot)

crLoop:
	call	getstudent	! read in the next student
	nop			! (delay slot)
        tst     %o0             ! check for malloc failure
        be      crFail          ! it did fail
        nop                     ! delay slot
	mov	%o0,%L2		! save newnode pointer

! Link this node into the list
	mov	%L1,%o0		! put head into param 1
	ld	[%L2+stno],%o1	! put student no. into param 2
	call	find		! find spot to insert this node
	nop			! (delay slot)
	st	%L2,[%o0+link]	! prev->link = newnode
	st	%o1,[%L2+link]	! newnode->link = curr

crEnter:
	subcc	%L0,1,%L0	! decrement and test counter
	bge	crLoop
	nop			! (delay slot)
	mov	%L1,%i0		! return pointer to the list
crEnd:
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section

         .section   ".data"
cmsg00:  .asciz	"Number of nodes to read: "

!=========================================================
! subroutine getstudent
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
getstudent:
	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! This subroutine reads data for one student and builds
! a Student node, which is returned
!=========================================================
!	Input parameters
!	none
!	Output parameters
!	%i0	pointer to Student node or null
!=========================================================
!	Register Usage
!	%L2	temp. pointer (newnode)
!=========================================================

	mov	nodesz,%o0	! number of bytes needed
	call	malloc		! Try to get a node
	nop
	mov	%o0,%L2		! save pointer to this node
	subcc	%o0,%g0,%g0	! See if malloc was successful
	bne	gtRead		! read Student data
	nop
! Serious error with malloc goes here
gtFail:
	clr	%i0		! set return value to null
	ba	gtEnd		! all done
	nop			! (delay slot)
	
gtRead:	
! Start reading data for this node
	set	gtmsg0,%o0
	call	strout
	nop
	call	decin		! read student number
	nop			! (delay slot)
	st	%o0,[%L2+stno]	! store student number in node
	call	decout		! echo student number
	nop			! (delay slot)
	call	newline		! 
	nop			! (delay slot)
	add	%L2,name,%o0	! read name directly into node's string
	call	strin		! get name
	nop			! (delay slot)
	add	%L2,name,%o0	! prepare to echo string
	call	strout		! echo name
	nop			! (delay slot)
	call	newline
	nop			! (delay slot)
	call	decin		! read faculty code
	nop			! (delay slot)
	stb	%o0,[%L2+faculty]	! store faculty code
	call	decout		! echo faculty code
	nop			! (delay slot)
	call	newline		! 
	nop			! (delay slot)
	call	newline		! 
	nop			! (delay slot)
	mov	%L2,%i0		! set return value
	
gtEnd:
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================
! the data section
	.section ".data"
gtmsg0:	.asciz	"Reading student data...\n"

!=========================================================
! subroutine find
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
find:	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! This subroutine finds the node containing a specified
! student number or the correct position where it should
! be inserted.
!=========================================================
!	Input parameters
!	%i0	pointer to head of list
!	%i1	student number
!	Output parameters
!	%i0	pointer to node prior (prev)
!	%i1	pointer to Key node (curr)
!=========================================================
!	Register Usage
!	%L0	prev pointer
!	%L1	curr pointer
!	%L2	Key value (student number)
!	%L3	curr student number
!=========================================================

	mov	%i0,%L0		! copy head into prev
	ld	[%i0+link],%L1	! get curr pointer
	mov	%i1,%L2		! copy student number
	ba	fiEnter		! enter traversal loop
	nop
fiLoop:
	mov	%L1,%L0		! prev = curr
	ld	[%L1+link],%L1	! curr = curr->link
fiEnter:
	subcc	%L1,%g0,%g0	! test for curr == null
	be	fiEnd		! we've reached end of list
	nop			! (delay slot)
	ld	[%L1+stno],%L3	! get curr student number
	subcc	%L2,%L3,%g0	! compare Key to curr key
	bg	fiLoop		! keep looking
	nop			! (delay slot)

fiEnd:
	mov	%L0,%i0		! return prev
	mov	%L1,%i1		! return curr
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================


! subroutine print
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
print:	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! Print the list. Print the full name of the faculty.
!=========================================================
!	Input parameters
!	%i0	pointer to linked list (head)
!	Output parameters
!	none
!=========================================================
!	Register Usage
!	%L0	pointer to current node (curr)
!=========================================================


! Point curr to first actual node in list
	ld	[%i0+link],%L0	! curr = head->link

! Enter printing loop at bottom
	ba	prEnter
	nop			! (delay slot)
prLoop:
	mov	%L0,%o0		! put curr into param 1
	call	print1
	nop			! (delay slot)
	ld	[%L0+link],%L0	! curr = curr->link
prEnter:
	tst	%L0		! check for end of list
	bne	prLoop
	nop			! (delay slot)

prEnd:
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================


!=========================================================
! subroutine print1
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
print1:	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! Print one Student record.
!=========================================================
!	Input parameters
!	%i0	pointer to Student node
!	Output parameters
!	none
!=========================================================
!	Register Usage
!	%L0	pointer to Student node
!=========================================================

	mov	%i0,%L0		! copy node pointer
	set	pmsg00,%o0	! prepare to print student number
	call	strout
	nop			! (delay slot)
	ld	[%L0+stno],%o0	! get student no. from node...
	call	decout		! and print it
	nop			! (delay slot)
	call	newline		
	nop			! (delay slot)
	set	pmsg01,%o0	! prepare to print student name
	call	strout
	nop			! (delay slot)
	add	%L0,name,%o0	! point to name in node...
	call	strout		! and print it
	nop			! (delay slot)
	call	newline		
	nop			! (delay slot)
	set	pmsg02,%o0	! prepare to print faculty
	call	strout
	nop			! (delay slot)
	ldub	[%L0+faculty],%g1	! get faculty code from node...
	set	jumptable,%L1	! get address of jump table
	ldub	[%L1+%g1],%g1	! get offset into faculty names
	set	facnames,%L1	! get address of faculty names
	add	%L1,%g1,%o0	! get address of actual faculty
	call	strout		! and print it
	nop			! (delay slot)
	call	newline		
	nop			! (delay slot)

!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section

         .section   ".data"
pmsg00:	.asciz	"Student Number:  "
pmsg01:	.asciz	"Student Name:    "
pmsg02:	.asciz	"Student Faculty: "
jumptable:
	.byte	0,6,23,36,51,60
facnames:
	.asciz	"Arts\n"
	.asciz	"Business Admin.\n"
	.asciz	"Engineering\n"
	.asciz	"Human Ecology\n"
	.asciz	"Science\n"
	.asciz	"University I\n"
	
!=========================================================
! subroutine add
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
add:	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! Add a new node to the list.
!=========================================================
!	Input parameters
!	%i0	pointer to head of list (head)
!	Output parameters
!	%i0	pointer to added node if successful or null
!		if not 
!=========================================================
!	Register Usage
!	%L0	copy of head
!	%L1	student number of new node (Key)
!	%o0	pointer to previous node (prev)
!	%o1	pointer to current node (curr)
!	%i0	pointer to new node (newnode)

	mov	%i0,%L0		! copy head
	call	getstudent	! get a new student
	nop			! delay slot
	mov	%o0,%i0		! save newnode
	tst	%i0		! check for malloc failure
	be	adEnd		! quit if failure
	nop			! delay slot

	ld	[%i0+stno],%L1	! get student number
	mov	%L0,%o0		! parm 1 for find
	mov	%L1,%o1		! parm 2 for find
	call	find
	nop			! delay slot
	st	%o1,[%i0+link]	! newnode->link = curr
	st	%i0,[%o0+link]	! prev->link = newnode

adEnd:
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================
	
!=========================================================
! subroutine delete
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
delete:	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! Delete a node from the list.
!=========================================================
!	Input parameters
!	%i0	pointer to head of list (head).
!	%i1	Student number of node to be deleted (Key).
!	Output parameters
!	%i0	pointer to deleted node if found or null
!		if not found
!=========================================================
!	Register Usage
!	%L0	temp copy of pointer
!	%L1	found node's student number (Key)
!	%o0	pointer to previous node (prev)
!	%o1	pointer to current node (curr)
 
	mov	%i0,%o0		! parm 1 (head) for find
	mov	%i1,%o1		! parm 2 (Key) for find
	call	find
	nop			! delay slot
	clr	%i0		! set return value to null
dchk0:	tst	%o1		! test (curr == null) 
	bne	dchk1		! check if keys match
	nop			! delay slot
	ba	deEnd		! didn't find the Key
	nop			! delay slot
dchk1:	ld	[%o1+stno],%L1	! get curr->studentno
	cmp	%L1,%i1		! compare key values
	bne	deEnd		! didn't find the key
	nop			! delay slot
dmatch:	mov	%o1,%i0		! return pointer to node
	ld	[%o1+link],%L0	! temp = curr->link
	st	%L0,[%o0+link]	! prev->link = temp
	mov	%o1,%o0		! parm 1 for free
	call 	free
	nop			! delay slot

deEnd:
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================
	
!=========================================================
! subroutine modify
!========== Standard prologue for code ===================
        .section    ".text"
        .align      4
modify:	save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
! Modify the data in a node in the list.
!=========================================================
!	Input parameters
!	%i0	pointer to head of list (head).
!	Output parameters
!	%i0	pointer to modified node if found or null
!		if not found
!=========================================================
!	Register Usage
!	%L0	pointer to modified node (newnode)
!	%L1	modified node's student number (Key)
!	%L2	curr node's student number (Key)
!	%L3	temp pointer
!	%o0	pointer to previous node (prev)
!	%o1	pointer to current node (curr)

	call	getstudent	! read the modified data
	nop			! delay slot
	mov	%o0,%L0		! copy pointer
	tst	%o0		! check for malloc failure
	bne	mfind		! try to find this Key
	nop			! delay slot
	clr	%i0		! set return value to null...
	ba	moEnd		! and return
	nop			! delay slot
 
mfind:	ld	[%L0+stno],%L1	! get Key from modified node
	mov	%i0,%o0		! parm 1 (head) for find
	mov	%L1,%o1		! parm 2 (Key) for find
	call	find
	nop			! delay slot
	clr	%i0		! set return value to null
mchk0:	tst	%o1		! test (curr == null) 
	bne	mchk1		! check if keys match
	nop			! delay slot
	ba	moEnd		! didn't find the Key
	nop			! delay slot
mchk1:	ld	[%o1+stno],%L2	! get curr->studentno
	cmp	%L1,%L2		! compare key values
	bne	moEnd		! didn't find the key
	nop			! delay slot
mmatch:	mov	%L0,%i0		! return pointer to node
	ld	[%o1+link],%L3	! temp = curr->link
	st	%L0,[%o0+link]	! prev->link = newnode
	st	%L3,[%L0+link]	! newnode->link = temp
	mov	%o1,%o0		! parm 1 for free
	call 	free
	nop			! delay slot

moEnd:
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

