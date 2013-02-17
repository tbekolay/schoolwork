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
!QUESTION	   3
!---------------------------------------------------------
!This program reads in an integer representing the amount
!of change in pennies one should receive.  It determines
!the smallest amount of total coins the cashier should
!give, and in what denominations.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Prompt for and read one integer.
! Register use: %L0 - The read in integer
!		%L1 - Temporary register
!		%L2 - Total coins

	set 	prompt,%o0	!Prompt for input
	call	strout
	nop

	call	decin		!Reads in the total change
	nop
	call	decout		!Echo total change
	nop	
	mov	%o0,%L0
	
	call	newline
	nop

	mov	0,%y		!Required for proper division
	udiv	%L0,100,%L1	!Divide by 100 for number of loonies

	set	loonies,%o0	!Output loonies
	call	strout
	nop

	mov	%L1,%L2		!Set the number of loonies as the inital total coins
	mov	%L1,%o0		!Output that number
	call	decout
	nop

	call	newline
	nop

	umul	%L1,100,%L1	!Multiply # of loonies by 100
	sub	%L0,%L1,%L0	!And subtract it from total change

	udiv	%L0,25,%L1	!Divide remanining change by 25

	set	quarters,%o0	!Output quarters
	call	strout
	nop

	add	%L1,%L2,%L2	!Add # of quarters to total coins
	mov	%L1,%o0		!Output number of quarters
	call	decout
	nop

	call	newline
	nop

	umul	%L1,25,%L1	!Mutliply # of quarters by 100
	sub	%L0,%L1,%L0	!And subtract it from remaining change

	udiv	%L0,10,%L1	!Divide remaning change by 10
	
	set	dimes,%o0	!Output dimes
	call	strout
	nop

	add	%L1,%L2,%L2	!Add # of dimes to total coins
	mov	%L1,%o0		!Output number of dimes
	call	decout
	nop

	call	newline
	nop

	umul	%L1,10,%L1	!Multiply # of dimes by 10
	sub	%L0,%L1,%L0	!And subtract it from remaining change

	udiv	%L0,5,%L1	!Divide remaning change by 5

	set	nickels,%o0	!Output nickels
	call	strout
	nop

	add	%L1,%L2,%L2	!Add # of nickels to total coins
	mov	%L1,%o0		!Output number of nickels
	call	decout
	nop

	call	newline
	nop

	umul	%L1,5,%L1	!Mutliply # of nickels by 5
	sub	%L0,%L1,%L0	!And subtract it from remaning change

	set	pennies,%o0	!Output pennies
	call	strout
	nop

	add	%L0,%L2,%L2	!Add number of pennies to total coins
	mov	%L0,%o0		!Output number of pennies
	call	decout
	nop

	call	newline
	nop

	set	total,%o0	!Output total number of coins
	call	strout
	nop

	mov	%L2,%o0
	call	decout
	nop

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
         .ascii     "INSTRUCTOR       John van Rees\n"
	 .ascii	    "ASSIGNMENT	      2\n"
	 .asciz	    "QUESTION	      3\n"
prompt:  .asciz     "Please enter amount in pennies to return:"
loonies: .asciz	    "Loonies: "
quarters:.asciz	    "Quarters: "
dimes:   .asciz	    "Dimes: "
nickels: .asciz	    "Nickels: "
pennies: .asciz	    "Pennies: "
total:   .asciz	    "Total Coins: "
goodbye: .asciz     "\nEnd of Processing\n\n"
