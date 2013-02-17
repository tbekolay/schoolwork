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
!QUESTION	   4
!---------------------------------------------------------
!This program reads in an integer and sets even parity.
!That is, if the number of 1 bits is odd, the top bit
!will be set to 1.  In this way, the number of 1's in
!an integer will always be even.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Prompt for and read one integer.
! Register use: %L0 - The read in integer
!		%L1 - A copy of the read in integer to check parity
!		%L2 - The sum of the 1 bits
!		%L3 - Temporary

	set 	prompt,%o0	!Prompt for input
	call	strout
	nop

	call	decin		!Get input
	nop
	call	decout		!Echo input
	nop
	mov	%o0,%L0
	
	call	newline
	nop

	mov	%L0,%L1		!Copy the read in int to %L1
	clr	%L2		!Set L2 to 0 intially

	ba	check		!First check the loop condition
	nop

loop:
	and	%L1,1,%L3	!In the loop, and %L1 with 1 to 
				!see if the lowest order bit is 1
	add	%L2,%L3,%L2	!Add that to %L3
	srl	%L1,1,%L1	!Shift %L1 to the right
	
check:
	orcc	%L1,0,%g0	!If %L1 is not 0, continue looping
	bne	loop		
	nop

	and	%L2,1,%L3	!Determines %L2 mod 2
	
	set	parity,%o0	!Output
	call	strout
	nop

	mov	%L3,%o0		!Outputs the parity bit
	call	decout
	nop
	call	newline
	nop

	sll	%L3,31,%L3	!Makes parity bit the highest order bit
	or	%L0,%L3,%L0	!Merges parity bit and read in integer
	
	set	new,%o0		!Outputs the new integer
	call	strout
	nop

	mov	%L0,%o0
	call	decout
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
	 .asciz	    "QUESTION	      4\n"
prompt:  .asciz     "Please enter a positive number: "
parity:	 .asciz	    "Parity bit is: "
new:     .asciz     "New value is: "
goodbye: .asciz     "\nEnd of Processing\n\n"
