*****************************************************************
* NAME              Stew Dent
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        Sarajohn Vanson
* LAB               5
*****************************************************************
*setup low memory
          org       $0
          dc.l      $100000     stack pointer after reset
          dc.l      start       program counter after reset
          dc.l      zzBusErr    bus error (e.g. address out of address space)
          dc.l      zzAddErr    address error (e.g. odd address when even is needed)
          dc.l      zzIllIns    illegal instruction error
          dc.l      zzDivZer    divide by zero error
          org       $1000

*****************************************************************
*equ symbols
null      equ       $00          null string terminator
lf        equ       $0A          linefeed
cr        equ       $0D          carriage return
*****************************************************************
* This program implements a bubble sort on an array.
*
* Register Usage
*  a0 - I/O and temp
*  a1 - Pointer to the array
*  d2 - outer loop index
*  d3 - inner loop index
*  d4 - Holds one array element for comparison and swapping
*****************************************************************
start     jsr       initIO
*Print Identification
          lea       IDBanner,a0   address of IDBanner
          jsr       strout
          jsr       newline
* Print the unsorted array
          lea       unsort,a0      address of title
          jsr       strout         "The unsorted array  "
          lea       array1,a0      address parameter
          move.l    a0,-(sp)       push on stack
          move.w    asize1,-(sp)   size parameter pushed on stack
          jsr       printarr       invoke the subprogram
          adda.l    #6,sp          "pop" parameters from stack

****************************************************************
* Beginning of bubble sort
bubble
next      equ       1           the next element is one byte away
          move.w    asize1,d2   d2 is the outer loop index
          subq.w    #1,d2       outer loop is done one fewer time
          bra       test2
loop2
          lea       array1,a1   get a pointer to the array
          move.w    d2,d3       get the loop index
          addq.w    #1,d3       we need one more 
          bra       test3       enter loop at bottom
loop3    
          move.b    (a1),d4     get a value from the array...
          cmp.b     next(a1),d4 and compare it to the next value
          ble       noswap      no swap required here
swap  
          move.b    next(a1),(a1)  note addressing mode
          move.b    d4,next(a1) finish swap
noswap    adda.l    #next,a1    advance the pointer
test3     dbra      d3,loop3
test2     dbra      d2,loop2

* End of bubble sort
****************************************************************

* Print the sorted array
* It is common to print titles outside of print data routines
          lea       sort,a0     get title ready
          jsr       strout      "The sorted array   "
          lea       array1,a0   address parameter..
          move.l    a0,-(sp)    ..pushed on stack
          move.w    asize1,-(sp) size parameter pushed on stack
          jsr       printarr    invoke routine
          adda.l    #6,sp       "pop" parameters from stack
   
* end of processing
          lea       EOP,a0      address of message
          jsr       strout
          jsr       newline
          jsr       finish

********************************************************************
* subroutine printarr
* Print an array of bytes.
*
* Stack Frame
*  sp+0  saved registers
*  sp+16 return address 
*  sp+20 Parameter 2 - size of array
*  sp+22 Parameter 1 - address of array
* Register usage
*  a1 copy of the array address
*  d1 loop counter
*       d0,a0  used for I/O
*********************************************************************
arroff    equ       22    par. 1 is 22 bytes from sp 
aszoff    equ       20    par. 2 is 20 bytes from sp
          ds.w      0                 alignment for label printarr
printarr
          movem.l   d0-d1/a0-a1,-(sp) save work registers on the stack
          move.l    arroff(sp),a1     get the pointer to the array
          move.w    aszoff(sp),d1     put the size into the loop index
          clr.l     d0                read element into D0 so it is ready to print
          lea       blank,a0          load the address of a blank
          bra       ptest             enter the loop at the bottom
ploop     jsr       decout            print the element...
          jsr       strout            and a blank
ptest     move.b    (a1)+,d0          get the next byte
          dbra      d1,ploop
          jsr       newline           all done, start a new line
          movem.l  (sp)+,d0-d1/a0-a1  restore work registers 
          rts                         return 
blank     dc.b     ' ',null

* end of subroutine printarr
********************************************************************
IDBanner  dc.b      'NAME               Stew Dent',cr,lf
          dc.b      'STUDENT NUMBER     1234567',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Jona Arnrees',cr,lf
          dc.b      'LAB                5',cr,lf,lf
          dc.b      null                Banner string terminator
unsort    dc.b      'The unsorted array:  ',null
sort      dc.b      'The sorted array:    ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,cr,lf,null
asize1    dc.w      14
array1    dc.b      81
          dc.b      15
          dc.b      12
          dc.b      77
          dc.b      44
          dc.b      90
          dc.b      6
          dc.b      45
          dc.b      29
          dc.b      55
          dc.b      33
          dc.b      66
          dc.b      11
          dc.b      22
asize2    dc.w      10
array2    dc.b      82
          dc.b      17
          dc.b      13
          dc.b      75
          dc.b      42
          dc.b      97
          dc.b      6
          dc.b      43
          dc.b      19
          dc.b      21
***********************************************************************
          include   68kIO.s
          end

