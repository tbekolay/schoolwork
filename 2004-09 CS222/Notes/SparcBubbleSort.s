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
!This program does a bubble sort of a list in memory
!---------------------------------------------------------

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

! Register use: %l0 - i
!               %l1 - n
!               %l2 - j
!               %l3 - address of a[j]
!               %l4 - address of a[j+1]
!               %l5 - value of a[j]
!               %l6 - value of a[j+1]
        
! Show original list
! ------------------
        set     mess1,%o0
        call    strout
        nop                     !The "delay slot".
        clr     %l0             !0 -> i
        set     n,%g1
        ld      [%g1],%l1       !n -> %l1
        set     list,%l3        !%l3 is pointer in array
        ba      showlooptest1
        nop
 showloop1:
        ld      [%l3],%o0
        call    decout
        nop
        call    newline
        nop
        inc     4,%l3           !move pointer in array
        inc     %l0             !i++
 showlooptest1:
        cmp     %l0,%l1
        bl      showloop1
        nop
 showloopend1:
 
! do the sort
! -----------
! translation of the following:
!  for(i=n-1;i>=1;i--){
!     for(j=0;j<i;j++){
!        if(a[j]>a[j+1]){ //switch a[j] and a[j+1]
!           temp = a[j]; a[j] = a[j+1]; a[j+1] = temp;
!        }//if
!     }//for
!  }//for

        sub     %l1,1,%l0       !n-1 -> i
        ba      sortlooptest1
        nop
 sortloop1:
        clr     %l2             !0 -> j
        ba      sortlooptest2
        nop
 sortloop2:
 !----------- if ------------
        mov     %l0,%o0
        call    decout
        nop
        mov     %l2,%o0
        call    decout
        nop
 !-------- end of if --------
        inc     %l2             !j++
 sortlooptest2:
        cmp     %l2,%l0
        bl      sortloop2       !branch j < i
        nop
 sortloopend2:
        dec     %l0             !i--
 sortlooptest1:
        cmp     %l0,1
        bge     sortloop1       !branch i >= 1
        nop
 sortloopend1:
        
! Show sorted list
! ----------------
        set     mess2,%o0
        call    strout
        nop                     !The "delay slot".
        clr     %l0             !0 -> i
        set     n,%g1
        ld      [%g1],%l1        !n -> %l1
        set     list,%l3        !%l3 is pointer in array
        ba      showlooptest2
        nop
 showloop2:
        ld      [%l3],%o0
        call    decout
        nop
        call    newline
        nop
        inc     4,%l3           !move pointer in array
        inc     %l0             !i++
 showlooptest2:
        cmp     %l0,%l1
        bl      showloop2
        nop
 showloopend2:
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
mess1:   .asciz     "The unsorted array\n"
mess2:   .asciz     "The sorted array\n"
goodbye: .asciz     "\n\nEnd of Processing\n\n"
n:       .word      9              !number of integers
list:    .word      30             !the list of integers starts here
         .word      2
         .word      17
         .word      53
         .word      7
         .word      6
         .word      23
         .word      17
         .word      27
         .word      9