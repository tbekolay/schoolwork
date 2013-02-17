! A2Q2.s
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
!ASSIGNMENT        2
!QUESTION          2
!---------------------------------------------------------
! Read a list of n integers, counting the number of positive,
! negative and zero values seen.
! This loop is a counted loop. (n is read from input).
!---------------------------------------------------------

!Print identification banner
	set	banner,%o0
	call	strout
	nop			! (delay slot)

! Register use:
!	%L0	loop index
!	%L1	n
!	%L2	positive count
!	%L3	negative count
!	%L4	zero count

! Initialize counters and loop index
	or	%g0,%g0,%L0	! index = 0
	or	%g0,%g0,%L2	! pos. count = 0
	or	%g0,%g0,%L3	! neg. count = 0
	or	%g0,%g0,%L4	! zero count = 0

! Prompt for n and echo it.
	set	msg1,%o0
	call	strout
	nop			! (delay slot)
	call	decin		! read the value...
	nop			! (delay slot)
	call	decout		! and echo it.
	nop			! (delay slot)
	call	newline
	nop			! (delay slot)
	or	%o0,%g0,%L1	! save n in %L1

! if n is negative, print msg and quit, else enter loop
	subcc	%L1,%g0,%g0	! compare n to zero
	bge	test		! n >= 0, so enter loop
	nop			! (delay slot)
	set	msg7,%o0
	call	strout
	nop			! (delay slot)
	ba	done
	nop			! (delay slot)

! Main loop starts here
loop:
! Get the next value from the user and echo it
	set	msg3,%o0	! prompt for the value
	call	strout
	nop			! (delay slot)
	call	decin
	nop			! (delay slot)
	call	decout
	nop			! (delay slot)
	call	newline
	nop			! (delay slot)
! determine if positive, negative or zero and count.
	subcc	%o0,%g0,%g0	! compare to zero
	bl	neg		! found a negative
	nop			! (delay slot)
	bg	pos		! found a positive
	nop			! (delay slot)
zero:	add	%L4,1,%L4	! count a zero value
	ba	next		! get next value
	nop			! (delay slot)
neg:	add	%L3,1,%L3	! count a negative value
	ba	next		! get next value
	nop			! (delay slot)
pos:	add	%L2,1,%L2	! count a positive value
next:
	add	%L0,1,%L0	! increment loop index
test:	
	subcc	%L0,%L1,%g0	! test if index < n
	bl	loop
	nop			! (delay slot)

! Print out results
	set	msg4,%o0	! positive values
	call	strout
	nop			! (delay slot)
	or	%L2,%g0,%o0	! get positive count in o0
	call	decout		! print positive count
	nop			! (delay slot)
	set	msg5,%o0	! negative values
	call	strout
	nop			! (delay slot)
	or	%L3,%g0,%o0	! get negative count in o0
	call	decout		! print negative count
	nop			! (delay slot)
	set	msg6,%o0	! zero values
	call	strout
	nop			! (delay slot)
	or	%L4,%g0,%o0	! get zero count in o0
	call	decout		! print zero count
	nop			! (delay slot)

!Normal termination sequence
done:	set	eop,%o0
	call	strout
	nop			! (delay slot)
                
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section

	.section	".data"
banner:	.ascii	"NAME             Stew Dent\n"
	.ascii	"STUDENT NUMBER   1234567\n"
	.ascii	"COURSE           74.222\n"
	.ascii	"INSTRUCTOR       I. M. Bohring\n"
	.ascii	"ASSIGNMENT       2\n"
	.asciz	"QUESTION         2\n\n"    
msg1:	.asciz	"Enter number of values: "
msg3:	.asciz	"Enter a value: "
msg4:	.asciz	"Positive values: "
msg5:	.asciz	"\nNegative values: "
msg6:	.asciz	"\nZero values: "
msg7:	.asciz	"Negative value not allowed for n.\n"
eop:	.asciz	"\n\nEnd of Processing\n\n"
