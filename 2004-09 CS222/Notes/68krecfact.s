*Recursive factorial program
*****************************************************************
* your program starts here        

start	  jsr	    initIO
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout
          jsr       newline

	  move.w    data,d0		get n
	  jsr 	    decout		print it
	  lea       strfact,a0
	  jsr       strout
	  
	  move.w    d0,-(sp)		put n onto stack (arg)
	  jsr       fact		compute n!
	  adda.l    #2,sp		restore stack
	  
	  jsr       decout		print n!
	  jsr       newline
	   
	  
          jsr       finish

*****************************************************************
*equ symbols
null      equ       $00          null string terminator
lf        equ       $0A          linefeed
cr        equ       $0D          carriage return
***************************************
*subroutine: fact
*Input: n (on the stack)
*Output: n! (in register d0)
****************************************
*Stack Frame
*  NAME		SP OFFSET	FP OFFSET
*old a6 VALUE	 sp+0           fp+0
*return address  sp+4		fp+4
*N - par. 1	 sp+8		fp+8
*either fp or sp could be used
***********************************************
*Register Usage
* d1 contains parameter 1 the value of n on stack
* d0 contains n-1 ready for stack and later holds (n-1)! 
fact:
	  link 		a6,#0
	  move.l 	d1,-(sp)		save value of d1
	  move.w 	8(a6),d1		get n
	  
	  cmpi.w 	#0,d1			is n==0?
	  beq 		base			yes, base case
	  move 		d1,d0			no, move to d0
	  subi.w 	#1,d0			sub 1 from d0 (n-1)
	  move.w 	d0,-(sp)		put n-1 onto stack
	  jsr 		fact			compute (n-1)!
	  adda.l 	#2,sp			restore stack
	  mulu 		d1,d0			compute n*(n-1)!
	  bra 		done  			
base:
	  move.w 	#1,d0
done:
          move.l 	(sp)+,d1		restore value of d1
	  unlk 		a6
	  rts				return n!
	  
***********************************************************************
IDBanner: dc.b      'NAME               Jane Smith ',cr,lf
          dc.b      'STUDENT NUMBER     1234567',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Ben Li',cr,lf
          dc.b      'ASSIGNMENT 7',cr,lf
          dc.b      'QUESTION 4',cr,lf,lf
          dc.b      null                Banner string terminator
strfact:  dc.b	    '!=',null	
	  ds.w 	    0
data:	  dc.w      4
			   
***********************************************************************


*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************	  
          end
