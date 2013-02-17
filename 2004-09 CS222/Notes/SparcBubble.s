!
!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
!NAME              Stew Dent
!STUDENT NUMBER    1234567
!COURSE            74.222
!INSTRUCTOR        Gordy Boyer
!LAB               5
!=========================================================

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

!Print the unsorted array
        set     unsort,%o0
        call    strout
        nop
        set     array1,%o0  !put the array pointer into o0
        set     asize1,%g1  !get the address of the array size...
        ld      [%g1],%o1   !and load it into %o1
        call    printarr
        nop

!=========================================================
!Start of bubble sort
bubble:
        set     asize1,%g1  !get the address of the size...
        ld      [%g1],%L1   !and load it into %L1
        sub     %L1,1,%L1   !do the outer loop one less time
        ba      test1       !enter outer loop at bottom
        nop
loop1:  set     array1,%L0  !get the array pointer
        or      %L1,%g0,%L2 !get inner loop index
        add     %L2,1,%L2   !we need one more
        ba      test2       !enter loop at bottom
        nop
loop2:  ldub    [%L0],%L3   !get an array element...
        ldub    [%L0+1],%L4 !and the next one
        subcc   %L3,%L4,%g0 !compare them
        ble     noswap      !no swap needed here
        nop
swap:   stb     %L4,[%L0]
        stb     %L3,[%L0+1]

noswap: add     %L0,1,%L0   !advance to the next element
test2:  subcc   %L2,%g0,%g0 !test the loop index
        bg      loop2
        sub     %L2,1,%L2   !decrement loop index (delay slot)
test1:  subcc   %L1,%g0,%g0 !test the loop index
        bg      loop1
        sub     %L1,1,%L1   !decrement loop index (delay slot)

!End of bubble sort
!=========================================================
!Print the sorted array
        set     sort,%o0
        call    strout
        nop
        set     array1,%o0  !put the array pointer into o0
        set     asize1,%g1  !get the address of the array size...
        ld      [%g1],%o1   !and load it into o1
        call    printarr
        nop
!Normal termination sequence
        set     goodby,%o0
        call    strout
        nop
                
!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

!=========================================================
! subroutine printarr
! Print an array of bytes
! Input parameters:
!  i0 address of array
!  i1 number of elements
! Output parameters: none
! Register usage:
!  L0 pointer to array elements
!  L1 loop index

        .section    ".text"
        .global     printarr
        .align      4
printarr:
        save     %sp,-96,%sp !allocate min 96 bytes

        or       %i0,%g0,%L0 !move pointer to L0
        or       %i1,%g0,%L1 !move count into loop index
        ba       ptest       !enter loop at bottom
        nop
ploop:  ldub     [%L0],%o0   !get next array element...
        call     decout      !and print it
        add      %L0,1,%L0   !increment pointer (delay slot)
        set      blank,%o0   !get address of a blank...
        call     strout      !and print it
        sub      %L1,1,%L1   !decrement counter (delay slot)

ptest:  subcc   %L1,%g0,%g0  !test loop index
        bg       ploop       !branch to top of loop
        nop

        call     newline     !All done, start a new line
        nop
        ret                  !Return to the caller.
        restore              !Actually done before "ret".

!=========================================================

! The data section

         .section   ".data"
banner:  .ascii     "NAME             Stew Dent\n"
         .ascii     "STUDENT NUMBER   1234567\n"
         .ascii     "COURSE           74.222\n"
         .ascii     "INSTRUCTOR       Gordy Boyer\n"
         .asciz     "LAB              5\n\n"
sort:    .asciz     "The sorted array:   "
unsort:  .asciz     "The unsorted array: "
blank:   .asciz     " "
goodby:  .asciz     "\n\nEnd of Processing\n\n"
         .align     4           !make sure label asize1 is word aligned
asize1:  .word      14
array1:  .byte      81
         .byte      15
         .byte      12
         .byte      77 
         .byte      44
         .byte      90
         .byte      6
         .byte      45
         .byte      29
         .byte      55
         .byte      33
         .byte      66
         .byte      11
         .byte      22
         .align     4         !make sure label asize2 is word aligned
asize2:  .word      10
array2:  .byte      82
         .byte      17
         .byte      13
         .byte      75
         .byte      42
         .byte      97
         .byte      6
         .byte      43
         .byte      19
         .byte      21
