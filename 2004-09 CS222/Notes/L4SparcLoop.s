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
!QUESTION:         SparcLoop
!---------------------------------------------------------
!This program counts the number of one bits in a 32-bit integer.
!It uses a loop and left shift.
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Register use: %l0 - number in which to count bits
!               %l1 - count of one bits
!               %l2 - index of loop, 0 to 32
        
! Read the first value
        set     prompt1,%o0
        call    strout
        nop
        call    hexin           !Call decin library routine
        nop
        mov     %o0,%l0         !number -> %l0
        
        clr     %l1             !0 -> count, initial value
        clr     %l2             !0 -> index of loop
const32 =       32              !upper limit of loop
        ba      looptest
        nop
loop:

!*******************************
! fill in the inside of the loop
!*******************************

        inc     %l2
looptest:
        cmp     %l2,const32     !index < 32 ?
        bl      loop
        nop
loopend:

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
         .asciz     "QUESTION         SparcLoop\n\n"    
prompt1: .asciz     "Enter a number in hex: "
mess1:   .asciz     "The number of one bits is: "
goodbye: .asciz     "\n\nEnd of Processing\n\n"


