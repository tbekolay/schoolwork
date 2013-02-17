!Example program   D. Meek 21 Jan 2002
!
!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
!NAME              JANE SMITH
!STUDENT NUMBER    1234567
!COURSE            74.222
!INSTRUCTOR        Dereck Meek
!ASSIGNMENT        1
!QUESTION          4
!---------------------------------------------------------
!This program reads two 32-bit integers from input and sums and prints
!them out with appropriate headings
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Prompt for and read two integers.
! Register use: %L0 - the first integer
!               %L1 - the second integer
        
! Print the first prompt
        set     prompt1,%o0
        call    strout
        nop                     !The "delay slot".
    
! Read the first value
        call    decin           !Call decin library routine
        nop
        mov     %o0,%L0         !Place the result into %L0
            
! Print the second prompt
        set     prompt2,%o0
        call    strout
        nop                     !The "delay slot".
    
! Read the second value
        call    decin           !Call decin library routine
        nop
        mov     %o0,%L1         !Place the result into %L1

! Calculate the sum and print a line of output.
! Register use: %L0 - the first integer
!               %L1 - the second integer
!               %L2 - their sum

        add     %L0,%L1,%L2     !Place sum in local reg 2.

! Print a message, and the answer

        set     outline,%o0     !string address
        call    strout
        nop

        mov     %L2,%o0         !put the sum into %o0
        call    decout          !and write it out
        nop
        
        call    newline         !finish off the line
        nop
        call    newline         !spacing
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
banner:  .ascii     "NAME             JANE SMITH\n"
         .ascii     "STUDENT NUMBER   1234567\n"
         .ascii     "COURSE           74.222\n"
         .ascii     "INSTRUCTOR       Dereck Meek\n"
         .ascii     "ASSIGNMENT       1\n"
         .asciz     "QUESTION         4\n\n"    
prompt1: .asciz     "Enter a number:"
prompt2: .asciz     "Enter a second number:"
outline: .asciz     "A+B is "
goodbye: .asciz     "\n\nEnd of Processing\n\n"


