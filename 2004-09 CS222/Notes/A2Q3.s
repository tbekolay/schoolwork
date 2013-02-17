! A2Q3.s
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
!QUESTION          3
!---------------------------------------------------------
! read a list of integer values until sentinel value of
! -99999 is read. Find count, sum, min, max and average.
! The average should be correctly rounded to nearest integer.
!---------------------------------------------------------

!Print identification banner
	set	banner,%o0
	call	strout
	nop			! (delay slot)

! Register use:
!	%L0	count
!	%L1	sum
!	%L2	min
!	%L3	max
!	%L4	average
!	%L7	sentinel

! Initialize count, sum and sentinel
	or	%g0,%g0,%L0	! count = 0
	or	%g0,%g0,%L1	! count = 0
	set	-99999,%L7	! sentinel = -99999

! prompt for a number and echo it.
	set	prompt,%o0
	call	strout
	nop			! (delay slot)
	call	decin
	nop			! (delay slot)
	call	decout
	nop			! (delay slot)
	call	newline
	nop			! (delay slot)

! Check for sentinel value, issue error msg if found and exit.
	subcc	%o0,%L7,%g0	! compare number to sentinel
	bne	init		! this number is OK
	nop			! (delay slot)
error:	set	errmsg,%o0
	call	strout
	nop			! (delay slot)
	ba	done
	nop			! (delay slot)

! Initialize min and max with the first number
init:	or	%o0,%g0,%L2	! init min
	or	%o0,%g0,%L3	! init max
! Enter the main loop (skip the min and max update code)
	ba	tally
	nop			! (delay slot)

! main loop starts here.
loop:
! check for new min or max
min:	subcc	%o0,%L2,%g0	! compare against min
	bge	max	
	nop			! (delay slot)
	or	%o0,%g0,%L2	! update min
	ba	tally
	nop			! (delay slot)

max:	subcc	%o0,%L3,%g0	! compare against max
	ble	tally	
	nop			! (delay slot)
	or	%o0,%g0,%L3	! update max

tally:	add	%o0,%L1,%L1	! sum += value
	add	%L0,1,%L0	! count++
read:
! prompt for a number and echo it.
	set	prompt,%o0
	call	strout
	nop			! (delay slot)
	call	decin
	nop			! (delay slot)
	call	decout
	nop			! (delay slot)
	call	newline
	nop			! (delay slot)
! Check for sentinel value
	subcc	%o0,%L7,%g0	! compare number to sentinel
	bne	loop		! this number is OK
	nop			! (delay slot)

! Print results.
	set	msg1,%o0	! count
	call	strout
	nop			! (delay slot)
	or	%L0,%g0,%o0
	call	decout
	nop			! (delay slot)

	set	msg2,%o0	! sum
	call	strout
	nop			! (delay slot)
	or	%L1,%g0,%o0
	call	decout
	nop			! (delay slot)

	set	msg3,%o0	! min
	call	strout
	nop			! (delay slot)
	or	%L2,%g0,%o0
	call	decout
	nop			! (delay slot)

	set	msg4,%o0	! max
	call	strout
	nop			! (delay slot)
	or	%L3,%g0,%o0
	call	decout
	nop			! (delay slot)

! Calculate the average rounded to the nearest integer.
! The formula is (2*sum + count) / (2*count)
! Work with positive values and add the sign later

	or	%L1,%g0,%L4	! put sum into average
	subcc	%L1,%g0,%g0	! check for a negative sum
	bge	calc		! begin calculation
	nop			! (delay slot)
neg:	sub	%g0,%L1,%L4	! change sum to positive
calc:	sll	%L4,1,%L4	! 2*sum
	add	%L4,%L0,%L4	! add count
	sll	%L0,1,%L0	! 2*count
	sra	%L4,31,%g1	! prepare y-register for division
	mov	%g1,%y		! ready to divide
	sdiv	%L4,%L0,%L4	! all done

! If the sum was negative, negate the average.
	subcc	%L1,%g0,%g0	! test sum
	bge	prtavg
	nop			! (delay slot)
	sub	%g0,%L4,%L4	! so negate already

prtavg:	set	msg5,%o0	! average
	call	strout
	nop			! (delay slot)
	or	%L4,%g0,%o0
	call	decout
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
	.asciz	"QUESTION         3\n\n"    
prompt:	.asciz	"Enter a number: "
errmsg:	.asciz	"First value read was the sentinel - ending program.\n"
msg1:	.asciz	"Number of values: "
msg2:	.asciz	"\nSum of values: "
msg3:	.asciz	"\nMinimum value: "
msg4:	.asciz	"\nMaximum value: "
msg5:	.asciz	"\nAverage value: "
eop:	.asciz	"\n\nEnd of Processing\n\n"
