*Example program for 68000   D. Meek 22 Apr 03
*****************************************************************
* NAME              JANE SMITH
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        Dereck Meek
* ASSIGNMENT        1
* QUESTION          4
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
* THIS PROGRAM PROMPTS THE USER FOR TWO 32-BIT SIGNED INTEGERS A AND B;
* AND CALCULATES AND PRINTS A+B
*
* REGISTER USAGE
*     d3 - THE VALUE OF A
*     d4 - THE VALUE OF B AND THEN A+B
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout
* read A
          lea       APrompt,a0          address of APrompt
          jsr       strout
          jsr       decin
          move.l    d0,d3               A into d3
* read B
          lea       BPrompt,a0          address of BPrompt
          jsr       strout
          jsr       decin
          move.l    d0,d4               B into d4
* calculate A+B
          add.l     d3,d4
* print a heading and A+B
          lea       heading,a0          address of heading
          jsr       strout
          move.l    d4,d0
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
APrompt   dc.b      'ENTER A:',null
BPrompt   dc.b      'ENTER B:',null
heading   dc.b      'A + B = ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
          end