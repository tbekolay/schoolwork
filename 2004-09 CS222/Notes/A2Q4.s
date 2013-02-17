! A2Q4.s
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
!QUESTION          4
!---------------------------------------------------------
! Read a string of character input and perform some 
! conversions on the characters.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Register use:
!	%L0	pointer to input buffer
!	%L1	holds one character from buffer
!	%g1	holds converted character
       
! Read input string into buffer
	set	prompt,%o0
	call	strout
	nop			! delay slot
	set	buffer,%o0
	call	strin
	nop			! delay slot
       
! Echo input string 
	set	echo,%o0
	call	strout
	nop			! delay slot
	set	buffer,%o0
	call	strout
	nop			! delay slot
	call	newline
	nop			! delay slot

! Processing loop. Process characters until null byte reached.
	set	buffer,%L0	! get pointer to buffer
	ba	looptest	! enter loop at bottom
	nop			! delay slot
loop:
! Start checking for characters that need to be converted.
	subcc	%L1,' ',%g0	! check for blank
	be	blank		! process a blank
	nop			! delay slot
	subcc	%L1,'0',%g0	! start checking for digits
	bl	next		! ignore this one
	nop			! delay slot
	subcc	%L1,'9',%g0	! is it a digit?
	ble	digit		! process a digit
	nop			! delay slot
	subcc	%L1,'a',%g0	! start checking for lowercase
	bl	next		! ignore this one
	nop			! delay slot
	subcc	%L1,'z',%g0	! is it a lowercase?
	ble	lower		! process a lowercase
	nop			! delay slot
	ba	next		! this is an 'other' char
	nop			! delay slot
blank:
	or	%g0,'_',%g1	! put underscore in %g1
	stb	%g1,[%L0]	! store in buffer
	ba	next
	nop			! delay slot
digit:
	or	%g0,'*',%g1	! put asterisk in %g1
	stb	%g1,[%L0]	! store in buffer
	ba	next
	nop			! delay slot
lower:	! Note the two different ways to change to uppercase
!	sub	%L1,'a'-'A',%g1	! put uppercase in %g1
	and	%L1,-33,%g1	! put uppercase in %g1
	stb	%g1,[%L0]	! store in buffer

next:
	add	%L0,1,%L0	! increment buffer pointer
looptest:
	ldub	[%L0],%L1	! get a byte from buffer
	subcc	%L1,%g0,%g0	! test for null byte
	bg	loop
	nop			! delay slot

! Print converted string 
	set	convert,%o0
	call	strout
	nop			! delay slot
	set	buffer,%o0
	call	strout
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
         .ascii     "ASSIGNMENT       2\n"
         .asciz     "QUESTION         4\n\n"    
prompt:	.asciz	"Enter the input string\n"
echo:	.asciz	"The original string\n"
convert:	.asciz	"The converted string\n"
goodbye: .asciz     "\n\nEnd of Processing\n\n"
buffer:	.skip	82	! input buffer
