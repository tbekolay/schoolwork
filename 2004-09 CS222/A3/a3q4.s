*This program counts the number of words contained in a
*text file that is included in the compilation,
*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        3
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
* Register Dictionary:
* d2 - flag to determine if char is first char in a word
* d3 - counter
* a3 - pointer to input buffer
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

	  lea	    Quote,a0		echo the output from included file
	  jsr       strout
          lea       instring,a0	
          jsr       strout

          clr.l     d3			initialize counter
	  move.l    #1,d2		initialize flag
          lea       instring,a3		initialize buffer pointer

          bra       check            	begin looping

loop:     cmpi.b    #' ',(a3)       	if character is a space,
          beq       space           	goto space
	  cmpi.b    #1,d2		if not, determine if flag is on
	  bne	    next
	  addi.l    #1,d3		if so, increment counter
	  move.l    #0,d2		toggle flag off
	  bra	    next

space:    move.l    #1,d2		toggle flag on

next:     adda.l    #1,a3           	increment a3 address

check     tst.b     (a3)            	test if a3 had reached end of string
          bne       loop            
	
          lea       Answer1,a0		print the answer
          jsr       strout
          move.l    d3,d0			
          jsr       decout
	  lea       Answer2,a0
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
          dc.b      'ASSIGNMENT 4',cr,lf
          dc.b      'QUESTION 1',cr,lf,cr,lf
          dc.b      null                Banner string terminator
Quote:    dc.b      '"',null
Answer1:  dc.b      '" has ',null
Answer2:  dc.b      ' words.',cr,lf,null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   A3Q4D4.txt
*****************************************************************
          end
