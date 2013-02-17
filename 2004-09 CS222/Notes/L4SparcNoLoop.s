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
!LAB               4
!QUESTION          SparcNoLoop
!---------------------------------------------------------
!This program counts the number of one bits in a 32-bit integer
! It uses masks and no loop
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Register use: %l0 - number in which to count bits
!               %l1 - count of one bits
!               %l2 - address of mask
!               %l3 - value of mask
        
! Read the first value
        set     prompt1,%o0
        call    strout
        nop
        call    hexin           !Call decin library routine
        nop
        mov     %o0,%l0         !number -> %l0
        
        clr     %l1             !0 -> count, initial value
bit0:        
        set     mask0,%l2
        ld      [%l2],%l3
        btst    %l0,%l3         !test bit
        bz      bit1
        nop
        inc     %l1             !count++
bit1:
        set     mask1,%l2
        ld      [%l2],%l3
        btst    %l0,%l3         !test bit
        bz      bit2
        nop
        inc     %l1             !count++
bit2:

!*********************************
!fill in part of program
!*********************************

! Report the answer
        set     mess1,%o0     !string address
        call    strout
        nop
        mov     %l1,%o0         !put the sum into %o0
        call    decout          !and write it out
        nop
        call    newline         !finish off the line
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
         .ascii     "LAB              4\n"
         .asciz     "QUESTION         SparcNoLoop\n\n"    
prompt1: .asciz     "Enter a number in hex: "
mask0:   .word      0x00000001
mask1:   .word      0x00000002
mask2:   .word      0x00000004
mask3:   .word      0x00000008

!*************************************
!fill in the rest of the masks
!*************************************

mask30:  .word      0x40000000
mask31:  .word      0x80000000
mess1:   .asciz     "The number of one bits is: "
goodbye: .asciz     "\n\nEnd of Processing\n\n"


