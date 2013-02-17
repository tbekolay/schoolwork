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
!QUESTION	   5
!---------------------------------------------------------
!This program reads in a string of 0's and 1's and
!converts it to its hexadecimal and integer equivalents.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Prompt for and read one integer.
! Register use: %L0 - Pointer to the start of the string
!		%L1 - The final number
!		%g1 - The current byte read in

	clr	%L1		!Clears %L1
	set 	prompt,%o0	!Prompt for input
	call	strout
	nop

	set	input,%L0	!Get string input from stdin
	mov	%L0,%o0
	call	strin
	nop

	call	strout		!Echo input
	nop
	call	newline
	nop

repeat:
	ldub	[%L0],%g1	!put char in temporary register
	cmp	%g1,0		!Sets flag if char is null terminator
	inc	%L0		!Increment %L0

	be	endloop		!End if flag true
	nop

	sub	%g1,'0',%g1	!Subtracts '0' from %g1 to get the number 0 or 1
	sll	%L1,1,%L1	!Shift the output number left
	add	%L1,%g1,%L1	!Add the lowest order bit of the input string

	ba	repeat		!Continue
	nop

endloop:
	mov	%L1,%o0		!Outputs the compiled number
	call	hexout		!In hex
	nop
	call	newline
	nop
	call	decout		!And integer
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
         .ascii     "INSTRUCTOR       John van Rees\n"
	 .ascii	    "ASSIGNMENT	      2\n"
	 .asciz	    "QUESTION	      5\n"
prompt:  .asciz     "Please enter a string of bits (up to 31): "
input:   .skip	    32
goodbye: .asciz     "\n\nEnd of Processing\n\n"
