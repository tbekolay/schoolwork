*****************************************************************
* NAME              Stew Dent
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        Gord Boyer
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
null      equ       $00                 null string terminator
lf        equ       $0A                 linefeed
cr        equ       $0D                 carriage return
*****************************************************************
* your program starts here
*****************************************************************
* THIS PROGRAM CALCULATES AND PRINTS A+B USING DATA SUPPLIED FROM
* INCLUDED FILES
*
* REGISTER USAGE
*     D3 - THE VALUE OF A
*     D4 - THE VALUE OF B AND THEN A+B
*****************************************************************

start     jsr       initIO              initialize I/O bits
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout
* read A
          lea       APrompt,a0          address of APrompt
          jsr       strout
      move.l    AInput,d3           load A into d3
      move.l    d3,d0               get ready to print A
      jsr       decout
      jsr       newline
	  
* read B
          lea       BPrompt,a0          address of BPrompt
          jsr       strout
      move.l    BInput,d4           load B into d4
      move.l    d4,d0               get ready to print B
      jsr       decout
      jsr       newline
	  
* calculate A+B
          add.l     d3,d4
* print a heading and A+B
          lea       heading,a0          address of heading
          jsr       strout
          move.l    d4,d0
          jsr       decout
* end of processing
          lea       EOP,a0              address of message
          jsr       strout
          jsr       finish

*****************************************************************
* your data section        
*****************************************************************
IDBanner  dc.b      'NAME               Stew Dent',cr,lf
          dc.b      'STUDENT NUMBER     1234567',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Gord Boyer',cr,lf
          dc.b      'ASSIGNMENT 1',cr,lf
          dc.b      'QUESTION 4',cr,lf,lf
          dc.b      null                Banner string terminator
APrompt   dc.b      'A = ',null
BPrompt   dc.b      'B = ',null
heading   dc.b      'A + B = ',null
EOP:      dc.b      cr,lf,'End of Processing',cr,lf,cr,lf,null

          include   AplusBdat1.txt     declarations AInput, BInput
          
*****************************************************************
* your program ends here        
*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
          end

