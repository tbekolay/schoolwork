*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        4
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
null:     equ       $00          null string terminator
lf        equ       $0A          linefeed
cr        equ       $0D          carriage return
*****************************************************************
* This program determines nCr; that is, the combination of n and r
* (the number of ways you can choose r elements from a list of n)
*****************************************************************

start     jsr       initIO	initialize I/O
*Print Identification
          lea       IDBanner,a0	address of IDBanner
          jsr       strout 

          lea       head1,a0	print out a heading
	  jsr       strout 
	  
          clr.l     d0		prepare d0 and d1
	  clr.l     d1
	  lea       num,a1	get the number of pairs from included file
	  move.w    (a1),d1

          move.w    d1,d0	output the number	
	  jsr       decout
	  
	  lea       head2,a0	print out the rest of the heading
	  jsr       strout

          lea       data,a1	points to the first value
	  
	  bra       test	bottom tested loop
loop      clr.l     d2		
          move.w    (a1)+,d2	get next value from file
          move.w    d2,d0	put it in lower half of d0
          jsr       decout	echo it
	  lea       c,a0	beautify output
	  jsr       strout
	  move.w    (a1)+,d0	get next value from file
	  jsr       decout	echo it
	  swap      d2		swap lower and higher half of d2
          move.w    d0,d2	put input in lower half of d2
	  move.l    d2,d0	put into d0 to be passed to subroutine
	  lea       equals,a0
	  jsr       strout
	  jsr       combinations	call subroutine
	  jsr       decout	output result of subroutine
	  jsr       newline	  
  
test      dbra      d1,loop


* end of processing
          lea       EOP,a0              address of message
          jsr       strout
          jsr       finish              end of execution
*****************************************************************
* Subprogram combinations
* Recursively determines nCr, using the formula
* nCr = (n-1)Cr + (n-1)C(r-1), with nC0 = 1 and nCn=1
*****************************************************************
combinations

          movem.l   d2-d4,-(SP)		save registers d2-d4
          clr.l     d2
	  move.w    d0,d2               d2 = r
	  swap      d0
          clr.l     d3
	  move.w    d0,d3               d3 = n

          cmp       d2,d3		if n = r
	  beq       base
          cmp       #0,d2		or if r = 0
          beq       base		this is the base case

          clr.l     d4
          subq.w    #1,d3		n = n - 1
	  move.w    d3,d0
	  swap      d0
	  move.w    d2,d0		put n and r into d0
	  jsr       combinations	recursive call
          add.l     d0,d4		add result of call to d4

          subq.w    #1,d2		r = r - 1
	  move.w    d3,d0
	  swap      d0
	  move.w    d2,d0		put n and r into d0
	  jsr       combinations	recursive call
	  add.l     d0,d4		add result of call to d4

	  move.l    d4,d0		move that result to d0
          bra       done
base      move.l    #1,d0          	base case output is 1
done      movem.l   (SP)+,d2-d4
          rts
          

*****************************************************************
* your data section        
*****************************************************************
IDBanner: dc.b      'NAME               Trevor Bekolay',cr,lf
          dc.b      'STUDENT NUMBER     6796723',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Van Rees',cr,lf
          dc.b      'ASSIGNMENT 4',cr,lf
          dc.b      'QUESTION 4',cr,lf,cr,lf
          dc.b      null                Banner string terminator
head1     dc.b      'Reading in ',null
head2     dc.b      ' pairs.',cr,lf,null
c         dc.b      ' C ',null
equals    dc.b      ' = ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   a4q4d1.txt
*****************************************************************
          end
