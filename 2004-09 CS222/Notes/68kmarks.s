*Example program for 68000   D. Meek 16 Nov 01
*****************************************************************
* NAME              JANE SMITH
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        Ben Li 
* ASSIGNMENT        1
* QUESTION          4
*****************************************************************
*setup low memory
          org       $0
          dc.l      $100000             stack pointer after reset
          dc.l      start               program counter after reset
          dc.l      zzBusErr
          dc.l	    zzAddErr
          dc.l	    zzIllIns
          dc.l      zzDivZer
          org       $1000

*equ symbols
null      equ       $00                 null string terminator
lf        equ       $0A                 linefeed
cr        equ       $0D                 carriage return

***********************************************************************
*    W R I T E   Y O U R   C O D E   B E L O W   T H I S   L I N E
***********************************************************************
* This program prompts for and reads the following integers from the user:
*    - a set of five assignment marks, all assumed to be out of 10
*    - a midterm exam mark, assumed to be out of 100
*    - a final exam mark,  assumed to be out of 100
* It calculates and prints the total mark, out of 100 and rounded off to
* the nearest integer, assuming that the assignments are worth 15%, the
* midterm is worth 20%, and the final is worth 65%.
* The formula is:
* ( (A1+A2+A3+A4+A5)*30+midtermn*20+final*65+fudgefactor )/100
* Fudgefactor is always half of divisor, i.e. fudgefactor=50

start	jsr		initIO
	
* ---------------------------------
* 1. Print an identification banner
* ---------------------------------

	lea		idinfo,A0
	jsr		strout		Print the banner string (multi-line)
	
* ---------------------------------
* 2. Read in the input values. Find the sum of the assignment marks
*    When finished, the registers will hold:
*    D1 - the sum of the 5 assignment marks
*    D2 - the midterm mark
*    D3 - the final exam mark
* ---------------------------------

	lea		Aprompt,A0
	jsr	 	strout		;Write the assignment prompt
	
	jsr		decin		;Read the 1st assignment
	move.w		D0,D1		;Initialize the sum to this value
	jsr		decin		;Read the 2nd assignment
	add.w		D0,D1		;Add it to the sum
	jsr		decin		;Read the 3rd assignment
	add.w		D0,D1		;Add it to the sum
	jsr		decin		;Read the 4th assignment
	add.w		D0,D1		;Add it to the sum
	jsr		decin		;Read the 5th assignment
	add.w		D0,D1		;Add it to the sum

	lea		Mprompt,A0
	jsr		strout		;Write the midterm prompt
	
	jsr		decin		;Read the midterm mark
	move.w		D0,D2		;Move it to D2
	
	lea		Fprompt,A0

	jsr		strout		;Write the final exam prompt
	
	jsr		decin		;Read the final exam mark
	move.w		D0,D3		;Move it to D3

* ---------------------------------
* 3. Calculate the total
*    Register dictionary:
*       D1 - the sum of the 5 assignment marks
*       D2 - the midterm mark
*       D3 - the final exam mark
*       D4 - the total mark
* ---------------------------------

	muls		#30,D1		;Multiply assignments by 30
	muls		#20,D2		;Multiply midterm by 20
	muls		#65,D3		;Multiply final exam by 65
	move		#50,D4		;Start with 50 (for rounding)
	add.w		D1,D4		;Add scaled assignment mark
	add.w		D2,D4		;Add scaled midterm mark
	add.w		D3,D4		;Add scaled final exam mark
	divs		#100,D4		;Divide by 100

* ---------------------------------------------
* 4. Print the result (which is in register D4)
* ---------------------------------------------
	
	jsr		newline		;Leave a blank line (it looks nice)
	lea		TotHdg,A0
	jsr		strout		;Write the heading for the total

	move.w		D4,D0		;Put the total into D0
	jsr		decout		;and write it out
	jsr		newline		;Finish off the line
	
* ---------------------------------
* 5. Print a final message and quit
* ---------------------------------
	
	jsr		newline		;leave a blank line (it looks nicer)
	lea		QuitMsg,A0
	jsr		strout		;Print a termination message
	jsr		finish  	;and terminate (what else?)
	
* -----------
* The strings
* -----------


	
Aprompt		 	dc.b		'Enter 5 assignment marks, one per line',10,13,null

Mprompt: 		dc.b		'Enter the midterm mark: ',null


Fprompt:		dc.b		'Enter the final exam mark: ',null

TotHdg:			dc.b		'The total mark is ',null


QuitMsg: 		dc.b		'Program terminated normally.',10,13,10,13,null

idinfo: 		dc.b		'Name: Jane Austen',10,13 	
   			dc.b		'Student number: 1234567',lf,cr
			dc.b		'Course: 74.222',10,13
			dc.b		'Instructor: John van Rees',$0a,$0d
			dc.b		'Assignment: 2',10,13
			dc.b		'Question: 1',10,13
			dc.b		'--------------------------',10,13,null


***********************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************	  
	  end
