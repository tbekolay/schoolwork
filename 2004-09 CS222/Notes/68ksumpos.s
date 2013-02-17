*68ksumpos.s
*****************************************************************
*This program will read in numbers, sum the positive ones, until sum
*is > 100.  Then it will print the sum and the # of inputs <=0
*****************************************************************
*Register Usage:	
* d0 - hold the number read in using decin
* d1 - hold the sum
* d2 - hold the # of rejected (non-positive) inputs
*****************************************************************
					
start:    jsr       initIO
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout
          jsr       newline

*Initialize counters
	  move.l #0,d1			sum is 0
	  move.l #0,d2			reject count is 0

loop
	  lea prompt,a0			print prompt
	  jsr strout
	
	  jsr decin			read number
	
	  cmpi.l #0,d0			compare number to 0
	  bgt addsum
	  addi.l #1,d2			d0 is neg, add 1 to reject count
	
	  bra loop			goto top of loop
addsum
	  add.l d0,d1			add d0 to sum (d1)
	
	  cmpi.l #100,d1	
	  ble loop			continue reading if sum <=100
			
*Done reading and summing, print results

	  lea strsum,a0
	  jsr strout
	  move.l d1,d0			!print sum
	  jsr decout
	  jsr newline

	  lea strreject,a0
	  jsr strout
	  move.l d2,d0			!print reject count
	  jsr decout
	  jsr newline

	  lea EOF,a0
	  jsr strout
	  jsr newline
			  
          jsr finish


***********************************************************************
IDBanner  dc.b      'NAME               Jane Smith ',cr,lf
          dc.b      'STUDENT NUMBER     1234567',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Ben Li',cr,lf
          dc.b      'ASSIGNMENT 1',cr,lf
          dc.b      'QUESTION 4',cr,lf,lf
          dc.b      null                Banner string terminator
	 
strsum    dc.b      'sum=',null
strreject dc.b	    'reject count=',null
prompt    dc.b      'Enter a number:',null
EOF	  dc.b      'End of Processing!',null
	
***********************************************************************

*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
	  
          end
