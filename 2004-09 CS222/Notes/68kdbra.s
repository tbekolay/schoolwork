start:	  jsr	    initIO        


*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout
          jsr       newline

	  lea	    prompt,a0		prompt for a number
	  jsr	    strout

	  jsr       decin		d0 hold input

	  lea	    str,a0	        get ptr to output string
	  bra	    check
	
loop
	  jsr	    strout		print the line of text
check
	  dbra      d0,loop		decrement and check
	
	  jsr       newline		print newline before quitting
          jsr	    finish

	  
***********************************************************************
IDBanner: dc.b      'NAME               Jane Smith ',cr,lf
          dc.b      'STUDENT NUMBER     1234567',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Ben Li',cr,lf
          dc.b      'ASSIGNMENT 1',cr,lf
          dc.b      'QUESTION 4',cr,lf,lf
          dc.b      null                Banner string terminator
	 
prompt: dc.b      'Enter a non-negative number',null
str:	dc.b	  'This is a line',cr,lf,null	
***********************************************************************


*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************	  
          end
