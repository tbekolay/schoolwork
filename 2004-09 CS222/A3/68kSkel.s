*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        3
* QUESTION          1
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
null:     equ       $00          null string terminator
lf        equ       $0A          linefeed
cr        equ       $0D          carriage return
*****************************************************************
* your program starts here        
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 



* end of processing
          lea       EOP,a0              address of message
          jsr       strout
          jsr       finish              end of execution
*****************************************************************
* your data section        
*****************************************************************
IDBanner: dc.b      'NAME               Trevor Bekolay',cr,lf
          dc.b      'STUDENT NUMBER     6796723',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Van Rees',cr,lf
          dc.b      'ASSIGNMENT 3',cr,lf
          dc.b      'QUESTION 1',cr,lf,cr,lf
          dc.b      null                Banner string terminator
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
          end
