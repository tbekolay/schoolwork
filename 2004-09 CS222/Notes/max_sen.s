!Example Program :	finding maximum with sentinel
!By Ben Li
		
!========== Standard prologue for Sparc code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
	
!=========================================================
!NAME              John Doe
!STUDENT NUMBER    1234567
!COURSE            74.222
!INSTRUCTOR        Ben Li
!ASSIGNMENT        0
!QUESTION          0
!---------------------------------------------------------
!This program will read positive numbers (one at a time) 
! and and compute the max number read
!It will read numbers until a non-negative number is read
!The maximum positive number will then be displayed
!___________________________________________________________________	

!	register dictionary
!	%l0 - the max number
!	%l1 - # of positive numbers entered

	mov 0,%l0		!initialize max number and numbers read
	mov 0,%l1

loop:		
	set prompt,%o0		!prompt for number
	call strout
	nop

	call decin		!read number
	nop
	
	cmp %o0,0
	ble done		!jump to done if %o0 <=0
	nop

	add %l1,1,%l1		!increment # of numbers read

				!check if %o0 is > %l0
	cmp %o0,%l0		! if %o0 <= %l0 then just go read next number
	ble loop
	nop

	mov %o0,%l0		!%o0> %l0, so store %o0 in %l0
	ba loop
	nop
done:

	set cntstr,%o0
	call strout
	nop
	
	mov %l1,%o0		!move count into %o0
	call decout
	nop

	call newline
	nop
	
	set maxstr,%o0
	call strout
	nop
	
	mov %l0,%o0		! move max into %o0
	call decout
	nop
	
	call newline
	nop
		
!========== Standard epilogue for Sparc code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section
	
	.section ".data"	!data section
prompt:	.asciz 	"Enter a positive number ( < 0 to quit):"
cntstr:	.asciz	"Number of positive numbers read:"
maxstr:	.asciz	"The biggest number read:"

