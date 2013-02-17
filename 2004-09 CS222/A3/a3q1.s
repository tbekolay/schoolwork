*This program outputs the first 10 square numbers.
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
* Register Dictionary:
* d1 - the counter
* d2 - the number and then the number squared
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

          lea	    Header,a0		print header
	  jsr       strout
	  clr.w     d1

	  bra       check		begin loop
	  
loop:	  addi.w    #1,d1 		increment the counter
	  move.w    d1,d0		
	  move.w    d1,d2		move the counter to d2
  
	  jsr       decout		output the counter
	  lea       Space,a0		and a space
	  jsr       strout
	  
	  mulu.w    d2,d2		square d2
	  move.w    d2,d0		output it

	  jsr	    decout
	  jsr       newline
 
check:    cmpi.w    #10,d1	  	loop until we reach 10, inclusive
  	  blt       loop

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
Header:   dc.b      'x       x^2',cr,lf
          dc.b      '------------',cr,lf,null
Space:    dc.b      '         ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
          end
