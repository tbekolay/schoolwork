!Example Program :	A very simple subroutine example
!By Ben Li
		
!========== Standard prologue for Sparc code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!----------------------------------------------------------------
!this is the mainline
	
	set prompt, %o0
	call strout
	nop

	call decin		!read number into %o0
	nop
	call mysub		!call our subroutine with 1 argument in %o0
	nop	
	ba done			!jump to end of program
	nop
	
!start of my subroutine	mysub
!this routine adds 1 to the argument and prints the new value
!notice it doesn't change the parameter, but makes a copy if it
!and works on the copy.
mysub:
	save	%sp,-96,%sp
	mov %i0, %o0		!make a copy of 1st parameter (%i0)
	add %o0, 1, %o0
	call decout
	nop
	call newline
	nop
		
	ret			!return to caller
	restore
	
!end of my subroutine mysub

done:	
			
!========== Standard epilogue for Sparc code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section
	
	.section ".data"	!data section
prompt:	.asciz 	"Enter a number:"

