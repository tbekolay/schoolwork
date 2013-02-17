! File: Palindrome.s
!
! This program reads several lines from a file.
! It terminates on an empty line so each line in the
! file must have an \n character.
! For each line, it determines whether or not it is
! a palindrome (reads the same forward and
! backward), and prints an appropriate message
! Note the \n character is not stored.

!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
        
! Register usage:
!       %o0,%o1 - parameters/results of i/o routines
!       %L1 - subscript into buffer
!       %L6 - address of buffer
!       %L2 - end of char. in buffer (note null count as characters )
!----------------------------------------------
! Read in a line of input, and echo it.

TopLoop:

        set     buffer,%L6      !address of input buffer
	mov	%L6,%o0
        call    strin           !read the characters
        nop		       
        

        ldub    [%L6],%g1       !check the last char.  
	cmp	%g1,0		!see if null        
        beq     AllDone         !if null, then all done
	nop
        
        mov	%L6,%o0		!prepare echo
        call    strout          !write out the characters
        nop
	call	newline		!spacing and flush buffer immediately for debug
	nop

!Get pointer %L2 to point to null in buffer
	set 	buffer, %L1
repeat:
	ldub	[%L1],%g1	!put char in temp. reg.
	cmp	%g1,0		!see if null
        inc     %L1    		!increment pointer

	bne	repeat		!if not keep looking
	nop		
	add	%L1,-2,%L2	!%L2 points to last printable char in buffer

!----------------------------------------------
! Determine whether or not the line in the
! buffer (containing %L0 characters) is a
! palindrome and write a message and an
! empty line.
! Register usage:
!   %L1 - address: starts at beginning of buffer and increments
!   %L2 - address: starts at end of the line and decrements
!   %L3 - the character %L1 points to
!   %L4 - the character %L2 point to


	mov	%L6,%L1
        ba	CmpEnter	!ENter the loop, but first....
        nop

CmpLoop:
        ldub    [%L1],%L3       !get the next char from front
        ldub    [%L2],%L4       !get the next char from end
	dec	%L2		!move back pointer
        inc     %L1   		!increment front pointer
        cmp     %L3,%L4         !compare the characters
        bne     NotPalin        !if different, not a palindrome
        nop	       
CmpEnter:
        cmp     %L1,%L2         !compare the two pointers
        bl      CmpLoop         !if they have not met or crossed, continue
        nop

Palindrome:
        set     YesMsg,%o0      !address of message
        call    strout          !write message
        nop
        ba      EndCmp          !done this section
        nop
        
NotPalin:
        set     NoMsg,%o0       !address of message
        call    strout          !write message
        nop

EndCmp:

!----------------------------------------------
! Close off the main loop

        ba      TopLoop
        nop
        
!----------------------------------------------
! Finish off the program

AllDone:
        set     FinalMsg,%o0    !address of final message
        call    strout          !write final message
        nop
                
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

        .section ".data"
        
buffer: .skip    80	!the input buffer
        .asciz   ''       !one extra null char here, just in case!

FinalMsg:       .asciz  "-----End Of File-----\n"
YesMsg:         .asciz  "The above line IS a palindrome.\n\n"
NoMsg:          .asciz  "The above line IS NOT a palindrome.\n\n"
