***********************************************************************
* This program reads in 16 bit integers (from array data) until it reads 1000.  
* With each integer, n, if n is bigger than -1, it tries to compute n! 
* using unsigned multiplication.  If the number is negative, a message is sent.
* If overflow occurs (product becomes larger than 16 bit unsigned integer), a 
* message is sent. Otherwise, n! is outputed. 
******************************************************************************************
* Register Usage
* D0 - holds n
* D1 - becomes n!
* D2 - counter
*******************************************************************************************
* Name: 		Jane Doe
* Student number: 1234567
* Course:		74.222 
* Instructor: Ben Li
* Assignment 1 	Question 4
******************************************************************************************

*--------------------------------------------------------------------------------------
* Print ID banner
*--------------------------------------------------------------------------------------
start:	  	jsr	  initIO
	        lea       IDBanner,a0         address of IDBanner
		jsr       strout
                jsr       newline

*--------------------------------------------------------------------------------------
* The main loop getting integers from array.  It checks for negative numbers and the sentinel 1000
*--------------------------------------------------------------------------------------
		lea		data,a1
	
getnloop:	move.w		(a1)+,d0        ;get input from array data 
		cmpi		#1000,d0	;is it sentinel
		beq		theend		;if so go to print normal termination

*------------------------------------------------------------------------------
* input is echoed, counter is initialized to n,  n is checked to see if positive
*-------------------------------------------------------------------------------
		ext.l		d0		;sign extend to 32 bits for printing (only) 
		jsr		decout		;echo input
	 	cmpi.w		#0,d0		;compare n to 0
		bge		initize		;if n is positive. go to normal processing

*----------------------------------------------------------------------------------
* Processing for negative n
*------------------------------------------------------------------------------------
negfact:	lea		head1,a0	;if n is negative, load address of message in A0
		jsr 		strout		;so that meassage is printed
		jsr		newline		;spacing
		jsr		newline     	;spacing
    		bra 		getnloop	;go to get next n

*------------------------------------------------------------------------------------
*initialize registers for multiplication
*------------------------------------------------------------------------------------

initize:	clr.l 		d1		;make sure d1 (which becomes n!) has no trash in it
		move.w 		d0,d4		;initialize counter to n
		move.w      	#1,d1		;initialize d1 (which becomes n!) to 1
		bra		endfloop	;go and check if done



*----------------------------------------------------------------------------------
*Do the unsigned multiplication and look for overflow.
*The product is stored as an unsigned integer in D1 and overflow occur iff MSW of D1 
*is non-zero
*----------------------------------------------------------------------------------

factloop:	mulu		d4,d1		;multiply the product by the next multiplicand 
		swap		d1		;get MSW to see if overflow occurred
		cmpi.w		#0,d1		;if D1.w = 0 no overflow
		beq		noflow		;otherwise fall through branch and print message

*-----------------------------------------------------------------------------------
*Print message that overflow has occurred
*-----------------------------------------------------------------------------------

overflow:	lea		head3,a0	;address of overflow message id put in A0
		jsr		strout		;print message
		jsr		newline		;spacing
		jsr		newline		;spacing
		bra		getnloop	;go and get next n

*---------------------------------------------------------------------------------
*Decrement the counter
*---------------------------------------------------------------------------------

noflow: 	swap		d1		;put product back the way it was
		sub.w 		#1,d4		;decrement counter

*----------------------------------------------------------------------------------
*Check to see if multiplying is done
*----------------------------------------------------------------------------------

endfloop:	cmpi.w		#0,d4		;check if counter has reached 0
		bgt		factloop	;if it has not go and multiply again

*-----------------------------------------------------------------------------------
*Print regular heading and n!
*-----------------------------------------------------------------------------------

regout:		lea		head2,a0	;put address of output line in A0
		jsr		strout		;print heading
		
		move.l 		d1,d0		;ready d0 for output of n!
		jsr		decout		;print out n! (need long to treat all 16 bits as powers of 2)
		jsr 		newline	
		jsr		newline

*----------------------------------------------------------------------------------------------
*Get next n
*----------------------------------------------------------------------------------------------

		bra		getnloop	;get next n

*-------------------------------------------------------------------------------------------
*Print normal termination
*-------------------------------------------------------------------------------------------

theend:		lea		normterm,a0	;address of NT heading in A0
		jsr		strout		;write NT heading
		jsr		newline
		jsr		newline

		jsr		finish

*	DATA	Put your program data below this line
***********************************************************************
head1:		dc.b		'! is negative and is not defined',0
head2:		dc.b		'! is ',0
head3:		dc.b		'! caused overflow, can not compute',0
normterm:	dc.b		'NORMAL TERMINATION',0
IDBanner: 	dc.b		'Name: Jane Doe',cr,lf
		dc.b		'Student number: 1234567',cr,lf
		dc.b		'Course: 74.222',cr,lf
		dc.b		'Instructor: Ben Li',cr,lf
		dc.b		'Assignment: 2',cr,lf
		dc.b		'Question: 1',cr,lf
		dc.b		'--------------------------',cr,lf,null


		DS.W		0	; this makes sure next address is even

data:		dc.w		5,6,8,10,-2,4,1,1000	;this is the data we are using 
		include 68kIO.s
		end

