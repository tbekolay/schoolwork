! a4q2.s
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
!ASSIGNMENT        0
!QUESTION          0
!---------------------------------------------------------
! This program implements multiplication and division
! algorithms and tests them.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Register use:
!       %L0     task (M)ultiply, (D)ivide or (Q)uit
!	%L1	first number
!	%L2	second number
!	%L3	answer (product or quotient)
!	%L4	holds sign of result
!	%L5	work register
!	%L6	work register
!	%L7	loop index

! Main loop: Read task 
!	M - multiply
!	D - divide
!	Q - quit

	ba	Menter		! enter at bottom
	nop			! delay slot
Mloop:	
! Read the two values
	set	num1,%o0	! prepare to print prompt
	call	strout
	nop			! delay slot
	call	decin		! read the value
	nop			! delay slot
	call	decout		! echo the value
	nop			! delay slot
	call	newline
	nop			! delay slot
	or	%o0,%g0,%L1	! store the first value
	set	num2,%o0	! prepare to print prompt
	call	strout
	nop			! delay slot
	call	decin		! read the value
	nop			! delay slot
	call	decout		! echo the value
	nop			! delay slot
	call	newline
	nop			! delay slot
	or	%o0,%g0,%L2	! store the second value

! Determine the sign of the result 
! Also copy absolute value of numbers to work registers
	or	%g0,1,%L4	! assume sign is positive
	srl	%L1,31,%L5	! get sign bit of first number
	srl	%L2,31,%L6	! get sign bit of second number
	subcc	%L5,%L6,%g0	! compare the two bits
	be	copy		! sign is positive
	nop			! delay slot
	or	%g0,-1,%L4	! sign is negative
copy:	or	%L1,%g0,%L5	! copy first number
	subcc	%L5,0,%g0	! see if the first number is negative
	bge	copy2
	nop			! delay slot
	sub	%g0,%L5,%L5	! absolute value of first number
copy2:	or	%L2,%g0,%L6	! copy second number
	subcc	%L6,0,%g0	! see if the second number is negative
	bge	task
	nop			! delay slot
	sub	%g0,%L6,%L6	! absolute value of second number
	
! Check if Multiply or Divide was specified
task:	subcc	%L0,'M',%g0	! check for Multiply response
	bne	Divide		! go to Divide code
	nop			! delay slot

! The Multiplication section
Multiply:
	or	%g0,%g0,%L3	! initialize product
	or	%g0,%g0,%L7	! initialize loop counter
	ba	Penter		! enter product loop at bottom
	nop			! delay slot
Ploop:
	andcc	%L5,1,%g0	! check for a 1 bit in multiplier
	be	Pnext		! found a 0 bit here
	nop			! delay slot
	add	%L6,%L3,%L3	! add multiplicand to partial product
Pnext:
	sll	%L6,1,%L6	! shift multiplicand
	srl	%L5,1,%L5	! shift multiplier
	add	%L7,1,%L7	! increment loop index
Penter:
	subcc	%L7,16,%g0	! compare loop index to stop value
	bl	Ploop		! do loop body
	nop			! delay slot
	ba	Output		! all done, go to output section
	nop			! delay slot

! The Division section
Divide:	
	or	%g0,%g0,%L3	! initialize quotient

! Do some checking on the values first
! If the divisor == 0, print an error message
chk1:	subcc	%L6,%g0,%g0	! test divisor
	bne	chk2		! it is OK
	nop			! delay slot
	set	divzero,%o0	! prepare to print error message
	call	strout
	nop			! delay slot
	ba	Menter		! get next task
	nop			! delay slot
! If divisor > dividend, the answer is zero and we are done
chk2:
	subcc	%L6,%L5,%g0	! compare divisor and dividend
	bg	Output		! no need to do the division
	nop			! delay slot

! Left shift until divisor > dividend, then shift right once
	or	%g0,%g0,%L7	! initialize loop counter
	ba	Qrenter		! enter loop at bottom
	nop			! delay slot
Qrloop:
	sll	%L6,1,%L6	! shift the divisor
	add	%L7,1,%L7	! count the shift
Qrenter:
	subcc	%L6,%L5,%g0	! compare divisor and dividend
	ble	Qrloop		! do a shift
	nop			! delay slot
	srl	%L6,1,%L6	! shift right one bit
	sub	%L7,1,%L7	! subtract one shift

	ba	Qenter		! enter quotient loop at bottom
	nop			! delay slot
Qloop:
	sll	%L3,1,%L3	! shift quotient to the left
	subcc	%L5,%L6,%g0	! compare dividend and divisor
	bl	Qnext		! dividend < divisor
	nop			! delay slot
	sub	%L5,%L6,%L5	! find new remainder
	or	%L3,1,%L3	! Put a 1 bit into quotient

Qnext:	srl	%L6,1,%L6	! shift divisor to the right
	sub	%L7,1,%L7	! decrement loop index
Qenter:
	subcc	%L7,%g0,%g0	! compare loop index to stop value
	bge	Qloop		! do loop body
	nop			! delay slot

! The Output section
Output:
! First check if we have the correct sign on the answer
	subcc	%L4,1,%g0	! is the sign positive?
	be	Ostart		! it is positive
	nop			! delay slot
Oneg:	sub	%g0,%L3,%L3	! change answer to negative

Ostart:	or	%L1,%g0,%o0	! prepare to print first value
	call	decout
	nop			! delay slot
	subcc	%L0,'M',%g0	! was this a multiplication?
	bne	Odiv		! it was a division 
	nop			! delay slot
Omult:	set	mult,%o0	! prepare to print mult symbol
	ba	Osymb		! print the mult symbol
	nop			! delay slot
Odiv:	set	div,%o0		! prepare to print divide symbol
Osymb:	call	strout
	nop			! delay slot
	or	%L2,%g0,%o0	! prepare to print second value
	call	decout
	nop			! delay slot
	set	equals,%o0
	call	strout
	nop			! delay slot
Oans:	or	%L3,%g0,%o0	! prepare to print answer
	call	decout
	nop			! delay slot
	call	newline	
	nop			! delay slot

Menter:
	or	%g0,%g0,%L0	! clear L0
	set	prompt,%o0	! prepare to print prompt
	call	strout
	nop			! delay slot
	call	charin		! read task from keyboard
	nop			! delay slot
	call	charout		! echo input
	nop			! delay slot
	call	newline
	nop			! delay slot
	or	%o0,%g0,%L0	! store response to L0
	subcc	%L0,'Q',%g0	! check for Quit response
	bne	Mloop		! enter main loop
	nop			! delay slot

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
banner:  .ascii     "NAME             Stew Dent\n"
         .ascii     "STUDENT NUMBER   1234567\n"
         .ascii     "COURSE           74.222\n"
         .ascii     "INSTRUCTOR       I. M. Bohring\n"
         .ascii     "ASSIGNMENT       0\n"
         .asciz     "QUESTION         0\n\n"
prompt:	.asciz	"Enter choice: (M)ult (D)ivide or (Q)uit "
num1:	.asciz	"Enter the first value: "
num2:	.asciz	"Enter the second value: "
mult:	.asciz	" x "
div:	.asciz	" / "
equals:	.asciz	" = "
divzero:.asciz	"Error - attempt to divide by zero.\n"
goodbye: .asciz     "\n\nEnd of Processing\n\n"

