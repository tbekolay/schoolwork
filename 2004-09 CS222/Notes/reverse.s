! File: Reverse.s
!
! This program reads in a list of up to 100
! word (4 byte) values, until the
! sentinel -1 is read. It then lists the
! values in reverse order.  Many delay slots optimized
!

!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
        
!Initialize. Write a prompt. Point %L0 to the array.
! Register usage:
!     L0 - a pointer which advances through the array
!     L1 - a pointer to the front of the array

        set     prompt,%o0      !address of prompt string
        call    strout          !write the prompt
        nop                     !to avoid SET in delay slot
        
        set	theList,%L0     !point %L0 to the list
        ba	ReadEnter	!Enter the loop
        nop			!delay slot
        
! Read integers until -1 is read

ReadLoop:
	st	%o0,[%L0]	!Store it in memory
        add     %L0,4,%L0       !advance L0 to next memory location
ReadEnter:
	call    decin 	        !read another integer
        nop			!delay slot - nothing to do        
        cmp     %o0,-1          !check for the sentinel
        bne     ReadLoop        !if not sentinel, loop
        nop

! Echo the integers in reverse order.

        set     header,%o0      !address of header string
        call    strout          !write the header string
        nop
        
        set     theList,%L1     !this is where we want to stop
        ba	EchoEnter	!Enter the echoing loop	
        nop			!delay slot
EchoLoop:
        call    decout		!and print it...
        ld      [%L0],%o0       !...after putting it in %o0  !delay slot  
        call	newline
	nop
EchoEnter:
	cmp     %L1,%L0         !check to see if %L0 back to theList(%L1)
        bne     EchoLoop        !if not there yet, keep writing
        sub     %L0,4,%L0       !move back to the previous word first

! Terminate gracefully

	set	endMsg,%o0	!Print a terminating message
	call	strout
	nop
		
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================
        
        .section ".data"
        
prompt:  .asciz "Enter integers terminated by -1:\n"
header:  .asciz "\nThe integers in reverse order:\n"
endMsg:  .asciz	"\n\nProgram ended.\n"

         .align 4
theList: .skip  400     !100 words = 400 bytes
        

