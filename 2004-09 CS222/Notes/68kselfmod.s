*****************************************************************
* your program starts here        

start:      jsr		initIO
line:	    move.l    	#$5200,d1
            add.l       #$0040,d1
            move.w      d1,space 
            clr.l       d0

* By the time the control unit gets to the next line, there is an instruction 
* there ready to execute

space:      ds.b        2    	  
	    jsr         decout
	    jsr         newline

*Next we print out part of the instruction starting at main
 
	    move.l      line,d0
            jsr         hexout
            jsr         newline
      	    jsr		finish
            DS.W	0		;ENSURES NEXT ADDRESS IS EVEN



***********************************************************************
          include   68kIO.s             Input/Output routines
          end
