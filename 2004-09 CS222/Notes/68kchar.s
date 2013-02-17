*Example program for 68000   D. Meek 16 Nov 01
*****************************************************************
* NAME              JANE SMITH
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        The Best
* ASSIGNMENT        0
* QUESTION          1
*****************************************************************
*setup low memory
          org       $0
          dc.l      $100000             stack pointer after reset
          dc.l      start               program counter after reset
          org       $1000
*****************************************************************
*initialize for I/O
start:    lea       zzduart,a1
          move.b    #%00010000,zzcra(a1)     Reset MR?A pointer
          move.b    #%00100011,zzmr1a(a1)    8 data bits
          move.b    #%00010111,zzmr2a(a1)    Normal Mode
          move.b    #%10111011,zzcsra(a1)    Set clock to 9600
          move.b    #%00000101,zzcra(a1)     Enable Rx and Tx
*****************************************************************
*equ symbols
null      equ       $00                 null string terminator
lf        equ       $0A                 linefeed
cr        equ       $0D                 carriage return
*****************************************************************
      

*******************************************************************
* barebones: read characters and give the hex code and decimal code for 
* them one at a time.
**********************************************************************
* The main loop is a repeat loop.  The sentinel, "Q", is processed also.
LOOP:		
		LEA		PROMPT,A0
		JSR		STROUT
		JSR             newline

		LEA 		CHAR,A0
		JSR		STRIN

		LEA		HEAD1,A0
		JSR		STROUT

		LEA		CHAR,A0
		JSR		STROUT

		LEA  		HEAD2,A0
		JSR		STROUT

		CLR.W		D0
		MOVE.B		CHAR,D0
		JSR		HEXOUT

		LEA		HEAD3,A0
		JSR		STROUT

		CLR.W		D0
		MOVE.B		CHAR,D0
		JSR		DECOUT
		JSR		NEWLINE
		JSR		NEWLINE
	
	

ENDLOOP:	CMP.B		#'Q',CHAR
		BNE		LOOP

		
* end of processing
                lea            EOP,a0              address of message
                jsr            newline
                jsr            strout
                break

PROMPT	        dc.b		'TYPE A CHARACTER(Q to QUIT)',null

HEAD1:		DC.B		'THE ASCII FOR "',null

HEAD2:   	DC.B		'" IS ',null

HEAD3:      	DC.B		' WHEREAS THE DECIMAL VALUE IS ',null

CHAR:		DS.B		82
END:		DC.B		null

	        dc.b           'STUDENT NUMBER     1234567',cr,lf
                dc.b           'COURSE             74.222',cr,lf
                dc.b           'INSTRUCTOR         The Best',cr,lf
                dc.b           'ASSIGNMENT 1',cr,lf
                dc.b           'QUESTION 4',cr,lf,lf
                dc.b           null                Banner string terminator
 
EOP:            dc.b           cr,lf,'End of Processing',cr,lf,cr,lf,null
***********************************************************************
               include         68kIO.s             Input/Output routines
               end
