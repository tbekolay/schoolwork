*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        4
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
* This program reverses the bits of a certain number of 32-bit long values.  
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

          lea       head1,a0		print out a heading
	  jsr       strout 
	  
	  lea       n,a1		read in 'n' from included file
	  move.w    (a1),d1		get data at address of 'n'

	  move.w    d1,d0		output 'n'
	  jsr       decout
	  
	  lea       head2,a0		print second heading
	  jsr       strout

          lea       values,a1           points to the first value
	  
	  bra       test		bottom tested loop
loop      move.l    (a1)+,d0		get next value
          jsr       hexout		echo
	  lea       equals,a0		output for beautification
	  jsr       strout
	  jsr       reverse		call reverese
	  jsr       hexout		output the data returned by reverse
	  jsr       newline	  
  
test      dbra      d1,loop
	  

* end of processing
          lea       EOP,a0              address of message
          jsr       strout
          jsr       finish              end of execution
*****************************************************************
* Subprogram reverse
* reverses the bits of a 32-bit long value passed in D0
*****************************************************************
reverse   movem.w   d1-d3,-(SP)		save registers d1-d3
          move.w    #32,d1		set loop end condition
          clr.l     d2			d2 = 0
	  bra       revtest		bottom tested loop
revloop   move.l    d0,d3		move d0 to temporary register
	  lsl.l     #1,d2		shift d2 to the left
          and       #$01,d3		get rightmost bit from d0
	  or        d3,d2		add that bit to d2
	  lsr.l     #1,d0		shift d0 to the right
revtest   dbra      d1,revloop
done	  move.l    d2,d0		move d2 to d0 to be returned
	  movem.w   (SP)+,d1-d3		recover registers d1-d3
	  rts
*****************************************************************
* your data section        
*****************************************************************
IDBanner: dc.b      'NAME               Trevor Bekolay',cr,lf
          dc.b      'STUDENT NUMBER     6796723',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Van Rees',cr,lf
          dc.b      'ASSIGNMENT 4',cr,lf
          dc.b      'QUESTION 2',cr,lf,cr,lf
          dc.b      null                Banner string terminator
head1     dc.b      'Reading in ',null
head2     dc.b      ' values.',cr,lf,null
equals    dc.b      ' = (when reversed) ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   a4q2d1.txt
*****************************************************************
          end
