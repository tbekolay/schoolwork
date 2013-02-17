! CountCaps.s
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
! Read a string into a buffer and count uppercase letters.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     ! (delay slot)

! Register use:
!	%L0	pointer into buffer
!	%L1	c (a char)
!	%L2	count


! read in a string and echo it
	set	prompt,%o0
	call	strout
        nop                     ! (delay slot)
	set	buffer,%o0
	call	strin
        nop                     ! (delay slot)
	set	echo,%o0
	call	strout
        nop                     ! (delay slot)
	call	newline
        nop                     ! (delay slot)

! Do loop initialization
	or	%g0,%g0,%L2	! count = 0
	set	buffer,%L0	!set up pointer

! enter loop
	ba	next		!enter at bottom
        nop                     ! (delay slot)

! The loop body starts here
!See if the character is uppercase
loop:	subcc	%L1,'A',%g0	! compare char to A
	bl	update		! go to next char
        nop                     ! (delay slot)
	subcc	%L1,'Z',%g0	! compare char to Z
	bg	update		! go to next char
        nop                     ! (delay slot)
count:	add	%L2,1,%L2	! count++

update:	add	%L0,1,%L0	!move to next char
next:	ldub	[%L0],%L1	!get a char
test:	subcc	%L1,%g0,%g0	!check for null byte
	bne	loop
        nop                     ! (delay slot)
! end of loop

! Print the count
	set	msg1,%o0
	call	strout
        nop                     ! (delay slot)
	or	%L2,%g0,%o0	! get count into %o0
	call	decout
        nop                     ! (delay slot)
	call	newline
        nop                     ! (delay slot)
        
!Normal termination sequence

       set      goodbye,%o0
       call     strout
        nop                     ! (delay slot)
                
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
prompt:	.asciz	"enter string "
echo:	.ascii	"The entered string is "
buffer:	.skip	82
msg1:	.asciz	"Number of Uppercase: "
goodbye: .asciz     "\n\nEnd of Processing\n\n"
