! A2Q1.s
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
!QUESTION          1
!---------------------------------------------------------
! Print a table of the numbers 1..9 and their cubes.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Register use:
!	%L0	x
!	%o0	x^3 

!Print table heading
        set     head,%o0
        call    strout
	nop

! Initialize the loop variable and enter loop
	or	%g0,1,%L0	! x = 1
	ba	test
	nop
loop:
!Print x
        or	%L0,%g0,%o0	! move x into %o0
        call    decout
	nop
!Print spaces
        set     spaces,%o0
        call    strout
	nop
! Calculate x^3 in %o0 so it's ready for printing
	smul	%L0,%L0,%o0	! x^2
	smul	%o0,%L0,%o0	! x^3
        call    decout
	nop
	call	newline
	nop


	add	%L0,1,%L0	! increment x
test:	subcc	%L0,9,%g0	! compare x to 9
	ble	loop
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
banner:  .ascii     "NAME             Stew Dent\n"
         .ascii     "STUDENT NUMBER   1234567\n"
         .ascii     "COURSE           74.222\n"
         .ascii     "INSTRUCTOR       I. M. Bohring\n"
         .ascii     "ASSIGNMENT       2\n"
         .asciz     "QUESTION         1\n\n"    
head:	.ascii	"x    x^3\n"
	.asciz	"_________\n"
spaces:	.asciz	"    "
goodbye: .asciz     "\n\nEnd of Processing\n\n"
