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
!
!This program tests decin, decout, hexin, hexout, strin, strout
! It also demonstrates memdump by printing part of itself
!
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0      !initialize parameter for strout
        call    strout
        nop                     !the "delay slot"
        call    newline
        nop
        
! Test decin, decout
        set     prompt1,%o0     !initialize parameter for strout
        call    strout
        nop
        call    decin
        nop
        mov     %o0,%L0         !save the value
        set     prompt2,%o0     !initialize parameter for strout
        call    strout
        nop
        mov     %L0,%o0         !restore the value
        call    decout
        nop
        call    newline
        nop
! Test hexin, hexout
        set     prompt3,%o0     !initialize parameter for strout
        call    strout
        nop
        call    hexin
        nop
        mov     %o0,%L0         !save the value
        set     prompt4,%o0     !initialize parameter for strout
        call    strout
        nop
        mov     %L0,%o0         !restore the value
        call    hexout
        nop
        call    newline
        nop
! Test strin, strout
        set     prompt5,%o0     !initialize parameter for strout
        call    strout
        nop
        set     string,%o0
        call    strin
        nop
        call    newline
        nop
        set     prompt6,%o0     !initialize parameter for strout
        call    strout
        nop
        set     string,%o0
        call    strout
        nop
        call    newline
        nop
! Do a memory dump of part of the data section
        call    newline
        nop
        set     banner,%o0      !dump from address banner
        set     50,%o1          !dump 50 bytes
        call    memdump
        nop      
        
!Normal termination sequence
        set     EOP,%o0
        call    strout
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
prompt1: .asciz     "Enter a decimal number:"
prompt2: .asciz     "I got the decimal number:"
prompt3: .asciz     "Enter a hex number:"
prompt4: .asciz     "I got the hex number:"
prompt5: .asciz     "Enter a string:"
prompt6: .asciz     "I got the string:"
EOP:     .asciz     "\n\nEnd of Processing\n\n"
string:  .skip      200