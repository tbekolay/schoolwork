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
!ASSIGNMENT        2
!QUESTION          1
!---------------------------------------------------------
!This program outputs the first ten fibonacci numbers;
!a fibonacci number is defined as F(n) = F(n-2)+F(n-1)
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Prompt for and read two integers.
! Register use: %L0 - F(n - 2) (Initially F(0))
!               %L1 - F(n - 1) (Initially F(1))
!		%L2 - The sum (F(n))
!		%L3 - Counter


	set 	header,%o0
	call	strout
	nop

	clr	%L3		!Clear L3; the counter
	mov	0,%L0		!Place 0 in L0; F(0)

	mov	%L3,%o0		!Output L3, the counter (0 at this point)
	call	decout
	nop

	mov	',',%o0		!For proper looking output
	call	charout		!Add ,
	nop

	mov	%L0,%o0		!Output L0, F(0)
	call	decout
	nop

        call    newline         !finish off the line
        nop

	mov	1,%L1		!Place 1 in L1; F(1)
	inc	%L3		!Increment counter
		 
	mov	%L3, %o0	!Output L3, the counter (1 at this point)
	call	decout
	nop

	mov 	',',%o0		!For proper looking output
	call	charout		!Add ,
	nop

	mov	%L1,%o0		!Output L1, F(1)
	call	decout
	nop

        call    newline         !finish off the line
        nop

loop:   add     %L0,%L1,%L2     !Place sum( F(n-2) + F(n-1) ) in L2
	inc	%L3		!Increment counter

	mov	%L3,%o0		!Output counter
	call	decout
	nop

	mov	',',%o0		!For proper looking output
	call	charout		!Add ,
	nop

	mov	%L2,%o0		!Output F(n)
	call	decout
	nop

        call    newline         !finish off the line
        nop

	mov	%L1,%L0		!F(n-1) becomes F(n-2)
	mov	%L2,%L1		!F(n) becomes F(n-1)

	cmp	%L3,10		!If we have not done this 10 times
	blt	loop		!Loop
	nop

        call    newline         !spacing
        nop

!Normal termination sequence

	set	goodbye,%o0
	call    strout
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
	 .asciz	    "QUESTION         1\n"
header:  .ascii     "n ,f(n) \n"
	 .asciz     "-----\n"
goodbye: .asciz     "\nEnd of Processing\n\n"
