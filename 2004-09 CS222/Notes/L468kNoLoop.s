*****************************************************************
* NAME              JANE SMITH
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        Dereck Meek
* LAB               4
* QUESTION          68kNoLoop
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
* your program starts here        
*****************************************************************
* This program counts the number of ones in a 32-bit integer
* It uses masks to test the bits and does not use a loop
*****************************************************************

* Register Usage
*     d1 - the integer in which to count the bits
*     d2 - a count of the number of bits
*     d3 - copy of the input integer

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout
* read the number
          lea       prompt,a0
          jsr       strout
          jsr       hexin
          move.l    d0,d1
          
          clr.l     d2                  count = 0
bit0
          move.l    d1,d3
          and.l     mask0,d3            check bit 0
          beq       bit1
          add.l     #1,d2               count++
bit1
          move.l    d1,d3
          and.l     mask1,d3            check bit 1
          beq       bit2
          add.l     #1,d2               count++
bit2

************************************
* fill in the program here
************************************          
          
* report the number of one bits
          lea       mess1,a0            address of heading
          jsr       strout
          move.l    d2,d0
          jsr       decout
          jsr       newline
* end of processing
          lea       EOP,a0              address of message
          jsr       strout
          jsr       finish              end of execution
*****************************************************************
* your data section        
*****************************************************************
IDBanner  dc.b      'NAME               Jane Smith',cr,lf
          dc.b      'STUDENT NUMBER     1234567',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Dereck Meek',cr,lf
          dc.b      'ASSIGNMENT 1',cr,lf
          dc.b      'QUESTION 4',cr,lf,cr,lf
          dc.b      null                Banner string terminator
prompt    dc.b      'Enter the number in hex: ',null
mask0     dc.l      $00000001
mask1     dc.l      $00000002
mask2     dc.l      $00000004

***************************************
* fill in the rest of the masks
***************************************

mask30    dc.l      $40000000
mask31    dc.l      $80000000
mess1     dc.b      'The number of one bits is: ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
          end