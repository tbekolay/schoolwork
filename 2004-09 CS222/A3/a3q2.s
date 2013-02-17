*This program calculates a recursive function:
*	 f(n) = 3*f(n-1)-2*f(n-2)
*for f(2 ... 10) and outputs the result.
*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        3
* QUESTION          2
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
* d1 - f(n-2)
* d2 - f(n-1)
* d3 - f(n)
* d4 - Loop counter
* d5 - 2 * f(n-2)
* d6 - 3 * f(n-1)
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

          lea	    Head1,a0		print heading
	  jsr       strout

	  move.w    f0,d1		print f(0)
	  move.w    d1,d0
	  jsr       decout

	  lea       Head2,a0		print heading
	  jsr       strout
	  
	  move.w    f1,d2		print f(1)
	  move.w    d2,d0
	  jsr       decout

	  lea       Head3,a0
	  jsr       strout

	  clr.w     d3			make sure d3 is clear first
	  move.w    #2,d4		begin the loop at 2
	  bra       check		begin looping
	  
loop:	  move.w    d1,d5		move f(n-2) to d5
          move.w    d2,d6		move f(n-1) to d6
	  mulu.w    #2,d5		multiply d5 (f(n-2)) by 2
	  mulu.w    #3,d6		multiply d6 (f(n-1)) by 3

	  add.w     d6,d3		add d6 (3*f(n-1)) to f(n)
	  sub.w     d5,d3		subtract d5 (2*f(n-2)) from f(n)

	  move.w    d4,d0		output the counter
  
	  jsr       decout
	  lea       Space,a0
	  jsr       strout
	  
	  move.w    d3,d0		output f(n)

	  jsr	    decout
	  jsr       newline

	  move.w    d2,d1		f(n-1) becomes f(n-2)
	  move.w    d3,d2		f(n) becomes f(n-1)
	  clr.w     d3			prepare for new f(n)
	  
	  addi.w    #1,d4		increment counter

check:    cmpi.w    #10,d4		loop until 10, inclusive	  
	  ble       loop

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
          dc.b      'QUESTION 2',cr,lf,cr,lf
          dc.b      null                Banner string terminator
Head1:    dc.b      'f(0) = ',null
Head2:    dc.b      ', f(1) = ',null
Head3:    dc.b      cr,lf,'----------',cr,lf,null
Space:    dc.b      '        ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   A3Q2D1.txt
*****************************************************************
          end
