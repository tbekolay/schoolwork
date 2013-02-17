! File: char_optimized.s
!
! This example will repeatedly prompt the user to enter
! a character, and it will print the ascii code for
! that character (in hexadecimal). The letter 'Q' will
! cause the program to quit.  This version has optimized 
! the delay slot, even splitting a set command into its 
! two parts.
!
! This file needs the library routines defined in "SparcIO.c"

!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
        
! Register dictionary:
!   %o0 - used for parameters/results when calling I/O routines.
!   %L0 - The characte typed by the user
	sethi	%hi(TheChar),%L1	!The hi order bits of TheChar
        ba      EnterLoop
  	or	%L1,%lo(TheChar),%L1   	!The low order bits of TheChar
					!Note delay slot filled
MainLoop:
	set     OutLine,%o0     !the address of the whole line
        call    strout          !print the line
        stb     %L0,[%L1]       !but first, put the character in it.

        call    hexout          !call hexout
        mov     %L0,%o0         !passing the char as the parameter
        
        call    newline         !end this line
        nop
        call    newline         !and leave a blank one, too
        nop
        
EnterLoop:
        set     prompt,%o0      !address of prompt message
        call    strout          !print the prompt
        nop                     !delay slot   
GetNew:
        call    getchar         !get the character typed
        nop
        cmp     %o0,'\n'        !ignore the \n characters
        be      GetNew
        mov     %o0,%L0         !Put the new character in %L0      
        
        cmp     %L0,'Q'         !was it a 'Q'?
        bne     MainLoop        !if not, keep going
        nop

!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

        .section ".data"
        
prompt:  .asciz "Type a character (Q to quit):"

OutLine: .ascii 'The ascii code for "'
TheChar: .skip  1
         .asciz '" is '
        
