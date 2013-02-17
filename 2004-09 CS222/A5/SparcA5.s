!=========================================================
!NAME              Trevor Bekolay
!STUDENT NUMBER    6796723
!COURSE            74.222
!INSTRUCTOR        Prof. Van Rees
!ASSIGNMENT	   5
!QUESTION	   1
!=========================================================
! This program creates a linked list, prints it out, then deletes certain sections
! based on input from a file, printing out the current state of the linked list
! after each set of deletions.
!
! The structure of a node is simply:
!    record
!       Link: NodePtr; {4 bytes at offset 0}
!	Freq: short int; {2 bytes at offset 4}
!	Char: char;    {1 byte at offset 6}
!	1 byte of padding
!    end

	Link = 0
	Freq = 4
	Char = 6
	NodeSize = 8 !8 bytes is the size of one node

!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes

!=========================================================
! Register Usage
!
! %L0 - pointer to top of linked list
! %L1 - number of deletions to make
! %o1 - first char (for deleteLLSub subprogram)
! %o2 - second char (for deleteLLSub subprogram)

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

	set	head0,%o0	!Print out a header
	call	strout
	nop

	call	createLL	!Create the linked list
	nop

	mov	%o0,%L0		!L0 = pointer to top of newly created linked list

	call	newline		!Beautify
	nop

	set	prnthead,%o0	!Print header before printing
	call	strout
	nop

	mov	%L0,%o0		!Set parameter for printLL
	call	printLL		!call printLL	
	nop

	call	decin		!Get the number of deletions to make
	nop
	
	mov	%o0,%L1		!L1 = number of deletions to make		

	ba	delcheck	!Bottom tested
	nop

delloop:
	mov	%o0,%o1		!o1 = first char
	call	charin		!Pass over the space
	nop
	call	charin		!Get next character
	nop
	mov	%o0,%o2		!o2 = second char
	call	charin		!Pass over the newline character
	nop

	set	delhead1,%o0	!Output delhead1
	call	strout
	nop
	
	mov	%o1,%o0
	call	charout		!Output first char
	nop
	
	set	delhead2,%o0	!Output delhead2
	call	strout
	nop
	
	mov	%o2,%o0		!Output second char
	call	charout
	nop
	
	call	newline		!Beautify
	nop

	mov	%L0,%o0		!o0 = pointer to list
	call	deleteLLSub	!call deleteLLSub
	nop

	call	newline
	nop

	mov	%L0,%o0		!o0 = pointer to list
	call	printLL		!Print out the new list
	nop
	
delcheck:
	call 	charin		!Get the next character
	nop
	tst	%L1		!check if L1 <= 0
	dec	%L1		!decrease L1
	bne	delloop		
	nop

!Normal termination sequence

        set      goodbye,%o0
        call     strout
        nop

!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section
         .section   ".data"
banner:  .ascii     "NAME             Trevor Bekolay\n"
         .ascii     "STUDENT NUMBER   6796723\n"
         .ascii     "COURSE           74.222\n"
         .ascii     "INSTRUCTOR       Prof. Van Rees\n"
	 .ascii	    "ASSIGNMENT	      5\n"
	 .asciz	    "QUESTION	      1\n"
head0:   .asciz     "\nCreating a linked list . . .\n"
prnthead:.asciz     "Printing out linked list . . .\n"
delhead1:.asciz     "Deleting "
delhead2:.asciz     " to "
goodbye: .asciz     "\n\nEnd of Processing\n\n"

!=========================================================
!        createLL subroutine
! Creates a linked list, reading in numbers and characters
! from stdin.
!=========================================================
! Register Usage
! 
! %L0 - pointer to dummy node (top)
! %L1 - pointer to current node
! %L2 - new freqency
! %L3 - new character
! %L4 - the current node

	.section	".text"
	.global		createLL
	.align		4
createLL:
	save	%sp,-96,%sp !allocate min 96 bytes
	mov 	NodeSize,%o0    !Pass the # of bytes desired
        call    malloc          !NewNode = new Node()
	nop

	mov	%o0,%L0		!L0 contains a pointer to the dummy node (top)
	mov	%L0,%L1		!L1 contains a pointer to the current node

	ba	createcheck	!Bottom tested
	nop

createloop:	
	mov	%o0,%L2		!L2 contains the new frequency	
	call	decout		!Echo
	nop
	mov	' ',%o0		!Output space
	call	charout
	nop
	call	charin
	nop
	mov	%o0,%L3		!L3 contains the new char
	call	charout		!Echo
	nop
	call	newline		!Beautify
	nop
	mov 	NodeSize,%o0    !Pass the # of bytes desired
        call    malloc          !NewNode = new Node()	
	nop
	mov	%L1,%L4  	!L4 contains the current node
	mov	%o0,%L1		!curr = curr.link (malloced space)
	st	%L1,[%L4+Link]  !Sets prev.link	
	st	%g0,[%L1+Link]	!Sets curr.link to null
	sth	%L2,[%L1+Freq]  !Sets curr.freq
	stb	%L3,[%L1+Char]  !Sets curr.char

createcheck:
	call	decin		!Read in the next integer
	nop
	tst	%o0		!Check for the sentinel
	bne	createloop
	nop
		
createdone:
	mov	%L0,%i0		!Return a pointer to the dummy (top)
	
	ret
	restore

!=========================================================
!        printLL subroutine
! Prints out the linked list pointed to by the passed argument.
!=========================================================
! Register Usage
! 
! %L0 - pointer to current node

	.section	".text"
	.global		printLL
	.align		4
printLL:
	save	%sp,-96,%sp !allocate min 96 bytes
	mov 	%i0,%L0		!L0 = pointer to current node (top right now)
	ld 	[%L0+Link],%L0	!current = current.link (Get past dummy)

	ba printcheck		!Bottom tested
	nop	
	
printloop:
	mov	'(',%o0		!output (
	call	charout
	nop
	
	lduh	[%L0+Freq],%o0	!o0 = frequency
	call	decout		!output frequency
	nop
	
	mov	',',%o0		!output ,
	call	charout
	nop

	ldub	[%L0+Char],%o0	!o0 = char
	call	charout		!output char
	nop

	mov	')',%o0		!output )
	call	charout
	nop
	
	call	newline		!Beautify
	nop

	ld	[%L0+Link],%L0	!current = current.link

printcheck:
	tst	%L0		!Check is current == null
	bne	printloop	!If not, continue looping
	nop
	
	call	newline
	nop	

	ret
	restore

!=========================================================
!        deleteLLSub subroutine
! Deletes a section of nodes, beginning with the first passed char,
! ending with the second passed char.
!=========================================================
! Register Usage
! 
! %L0 - pointer to top of list
! %L1 - the first char
! %L2 - the second char
! %L3 - prev
! %L4 - curr (prev.link)
! %L5 - curr.char

	.section	".text"
	.global		deleteLLSub
	.align		4
deleteLLSub:
	save	%sp,-96,%sp !allocate min 96 bytes
	
	mov	%i0,%L0		!pointer to list
	mov	%i1,%L1		!Fist char
	mov	%i2,%L2		!Second char

	mov	%L0,%L3		!Prev pointer
	ld	[%L0+Link],%L4	!Current (prev.link)	

	ba	delete1check	!Bottom tested
	nop

delete1loop:
	mov	%L4,%L3		!prev = curr
	ld	[%L4+Link],%L4	!curr = curr.link

delete1check:
	ldub	[%L4+Char],%L5	!L5 = curr.char
	cmp	%L5,%L1		!check if curr.char == first char
	bne	delete1loop	!if not, keep looping
	nop

	ba	delete2check	!Bottom tested
	nop

delete2loop:
	ld	[%L4+Link],%L4	!curr = curr.link
	ld	[%L3+Link],%o0
	
	call	free		!free up prev.link
	nop
	
	st	%L4,[%L3+Link]	!set prev.link to curr, deleting the old curr

delete2check:
	ldub	[%L4+Char],%L5	!L5 = curr.char
	cmp	%L5,%L2		!check if curr.char == second char
	bne	delete2loop	!if not, keep looping
	nop

delete2done:
	ld	[%L4+Link],%L4	!curr = curr.link
	ld	[%L3+Link],%o0
	
	call	free		!free up prev.link
	nop
	
	st	%L4,[%L3+Link]	!set prev.link to curr

	ret
	restore
