*This program takes a sentence, or sentences, from the included
*input file and rids it of extraneous spaces, so all words are
*separated by only one space.
*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        3
* QUESTION          5
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
* d3 - flag
* a3 - pointer to input buffer
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

	  lea	    Quote1,a0		echos the string found
	  jsr       strout		in the included file
          lea       instring,a0	
          jsr       strout
	  lea	    Quote2,a0
	  jsr       strout

	  move      #0,d3		initialize the flag
          lea       instring,a3		initialize the pointer	

	  bra	    checkfirst		begin loop to skip leading spaces
	  
loopfirst:
	  cmpi.b    #' ',(a3)		if the character is not a space
	  bne       check		begin the main loop
	  adda.l    #1,a3		if not, increment a3
	  
checkfirst:
	  tst.b     (a3)		loop while still valid
	  bne       loopfirst

*Main loop

loop:     cmpi.b    #' ',(a3)       	if the character at a3 is
          beq       space           	a space, goto space

	  cmpi.b    #1,d3		if not a space, and flag is off
	  bne       continue		goto continue

	  move.l    #' ',d0		if flag is on, output a leading
	  jsr       charout		space
          move.l    #0,d3		then toggle flag off

continue: move.b    (a3),d0		output current char
	  jsr	    charout

	  bra	    next

space:    move.l    #1,d3		toggle flag on

next:     adda.l    #1,a3           	increment a3

check:    tst.b     (a3)            	check if a3 has reached end of string
          bne       loop            	

	  lea       Answer,a0		finish output
          jsr       strout

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
          dc.b      'QUESTION 5',cr,lf,cr,lf
          dc.b      null                Banner string terminator
Quote1:   dc.b      '"',null
Quote2:   dc.b      '" => "',null
Answer:   dc.b      '"',cr,lf,null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   A3Q5D4.txt
*****************************************************************
          end
