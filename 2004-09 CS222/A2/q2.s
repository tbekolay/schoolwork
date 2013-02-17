!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
!NAME              Trevor Bekolay
!STUDENT NUMBER    6796723
!COURSE            74.222
!INSTRUCTOR        John van Rees
!ASSIGNMENT	   2
!QUESTION	   2
!---------------------------------------------------------
!This program reads in an integer and outputs the sum from
!1 to the read in integer.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Prompt for and read one integer.
! Register use: %L0 - The read in integer
!               %L1 - The counter
!		%L2 - The sum

input:
	set 	prompt,%o0	!Prompt for input
	call	strout
	nop

	call	decin		!Get input
	nop
	call	decout		!Echo input
	nop
	mov	%o0,%L0		!Move input to %L0

	call	newline		!Spacing
	nop

	mov	1,%L1		!Start the counter at 1
	clr	%L2		!Make sure the sum is currently 0
	
	ba	check		!Check the condition first
	nop

loop:	
	add	%L1,%L2,%L2	!In the loop, the counter is added to the sum
	inc	%L1		!The counter is incremented

check:	
	cmp	%L0,-1		!If the input is -1, goto endloop
	be	endloop
	nop

	cmp	%L0,%L1		!If the counter is less than the read in integer
	bne	loop		!Continue looping
	nop

	set	outline1,%o0	!Output
	call	strout
	nop

	mov	%L0,%o0		!Outputs the max number
	call	decout
	nop

	set	outline2,%o0	!Output
	call	strout
	nop

	mov	%L2,%o0		!Outputs the sum
	call	decout
	nop

	call	newline
	nop

	call	newline
	nop

	ba	input		!Continues operation until -1 is entered
	nop

endloop:

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
         .ascii     "INSTRUCTOR       John van Rees\n"
	 .ascii	    "ASSIGNMENT	      2\n"
	 .asciz	    "QUESTION	      1\n"
prompt:  .asciz     "Please enter a number: "
outline1:.asciz	    "The sum from 1 to "
outline2:.asciz	    " is "
goodbye: .asciz     "\n\nEnd of Processing\n\n"
