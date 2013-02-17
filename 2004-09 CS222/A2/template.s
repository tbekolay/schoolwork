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
!QUESTION	   1
!---------------------------------------------------------
!This program reads two 32-bit integers from input and sums and prints
!them out with appropriate headings
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Prompt for and read one integer.
! Register use: %L0 - The read in integer
!               %L1 - The counter
!		%L2 - The sum

	set 	prompt,%o0
	call	strout
	nop

	call	decin
	nop
	mov	%o0,%L0
	
	call	newline
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
	 .ascii	    "ASSIGNMENT	      2\n"
	 .asciz	    "QUESTION	      3\n"
prompt:  .asciz     "Please enter amount in pennies to return:"
loonies: .asciz	    "Loonies: "
quarters:.asciz	    "Quarters: "
dimes:   .asciz	    "Dimes: "
nickels: .asciz	    "Nickels: "
pennies: .asciz	    "Pennies: "
total:   .asciz	    "Total Coins: "
goodbye: .asciz     "\n\nEnd of Processing\n\n"
