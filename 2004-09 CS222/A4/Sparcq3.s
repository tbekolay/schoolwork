!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
!NAME              Trevor Bekolay
!STUDENT NUMBER    6796723
!COURSE            74.222
!INSTRUCTOR        Prof. Van Rees
!ASSIGNMENT	   4
!QUESTION	   3
!---------------------------------------------------------
!This program reads in between 1 and 100 integers, determines the
!smallest, and outputs it.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

	call	doWork
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
	 .ascii	    "ASSIGNMENT	      4\n"
	 .asciz	    "QUESTION	      3\n"
head1:   .asciz     "Reading in "
head2:   .asciz     " values . . .\n"
comma:   .asciz     ", "
minprmpt:.asciz     "Minimum value is "
errprmpt:.asciz     "Error!  Must read in between 1 and 100 entries!\n"
goodbye: .asciz     "\n\nEnd of Processing\n\n"

!=========================================================
!        doWork subroutine
!doWork builds an array on the stack based on input from stdin.
!It then passes a pointer to the first data member and the length
!of the array to the computeMin routine.
!=========================================================
! Register usage:
!	%L1 - Address of A[0]
!	%L2 - Address of current array member being looked at
!	%L3 - The number of integers to be read in
!	%L4 - Loop counter

	.section	".text"
	.global		doWork
	.align		4
doWork:
	save	%sp,-496,%sp

	!use our formula for computing address of A[0]	
	mov     100,%L1		!100-0 = 10
	sll     %L1,2,%L1	!4*100
	sub	%fp,%L1,%L1	!%L1 contains address of A[0]
	mov     %L1,%L2		!make a copy of %L1 

	set	head1,%o0	!Print out part of a heading
	call	strout
	nop
	
	call	decin		!Read in an integer
	nop
	mov	%o0,%L3		!Move it to %L3
	call	decout		!Echo the integer
	nop

	set	head2,%o0	!Print out the rest of the heading
	call	strout
	nop

	cmp	%L3,0		!Make sure 'n' > 0
	ble	error
	nop
	
	cmp	%L3,100		!Make sure 'n' <= 100
	bgt	error
	nop

	clr	%L4		!Clear counter
	ba	test		!Bottom tested loop
	nop

loop:	call    decin		!Read data
	nop
	call	decout		!Echo data
	nop
	st      %o0,[%L2]	!Store on the stack
	set	comma,%o0	!Beautify output
	call	strout
	nop

	add	%L2,4,%L2	!Increment array pointer
	add	%L4,1,%L4	!Increment counter
	
test:	cmp	%L3,%L4		
	bne	loop		!Loop while we haven't hit %L3
	nop

	call	newline
	nop

	mov	%L1,%o0		!Address of A[0]
	mov	%L3,%o1		!Number of elements
	call	computeMin	!Computes min value
	nop
	
	mov	%o0,%i0		!Takes output from computeMin

	set	minprmpt,%o0	!Output heading
	call	strout
	nop
	
	mov	%i0,%o0		!Outputs min value
	call	decout
	nop
	call	newline
	nop
	
	ba	done		!Finished
	nop
	
error:	set	errprmpt,%o0	!If 'n' < 1, > 100
	call	strout		!Print error message
	nop

done:
	ret
	restore

!=========================================================
!        computeMin subroutine
!=========================================================
! Register usage:
!    %i1 - input parameter n
!    %i0 - output parameter - value of fibon(n)
!    %l1 - holds fibon(n-1)
!
	.section	".text"
	.global		computeMin
	.align		4
computeMin:
	save	%sp,-96,%sp
	
	mov	%i0,%L0		!Address of A[0]
	mov	%i1,%L1		!Number of elements
	clr	%L2		!Counter

	ld	[%L0],%L3	!Min

minloop:
	ld      [%L0],%L4	!Load value at %L0
	cmp	%L4,%L3		!If less than min
	bge	notmin
	nop
	mov	%L4,%L3		!Becomes new min

notmin:
	add	%L2,1,%L2	!Increment counter
	add     %L0,4,%L0	!Increment array pointer
	cmp 	%L2,%L1		!If we've checked every member
	bl  	minloop		!of the arrary, output current min
	nop

	mov	%L3,%i0
	
	ret
	restore
