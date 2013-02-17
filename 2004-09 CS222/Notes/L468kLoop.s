*****************************************************************
* NAME              JANE SMITH
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        Dereck Meek
* LAB               4
* QUESTION          68kLoop
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
* This program counts the number of one bits in a 32-bit integer
* It uses a loop with shift left
*****************************************************************
*
* Register Usage
*     d1 - the integer in which to count the bits
*     d2 - a count of the number of bits
*     d3 - index of loop, 0 to 31

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
* test bit 31 separately
          tst.l     d1
          bge       uplimit
          add       #1,d2               count++
uplimit
* now loop for the rest of the bits
const31   equ       31                  upper limit of loop
          move.l    #const31,d3         index = 31
          bra       looptest
loop

*******************************************
* fill in the inside of the loop
*******************************************

looptest
          dbra      d3,loop
loopend
  
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
          dc.b      'LAB                1',cr,lf
          dc.b      'QUESTION           68kLoop',cr,lf,cr,lf
          dc.b      null                Banner string terminator
prompt    dc.b      'Enter the number in hex: ',null
mess1     dc.b      'The number of one bits is: ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
          end