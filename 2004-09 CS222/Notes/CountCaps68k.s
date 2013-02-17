* CountCaps68k.s
*****************************************************************
* NAME			Stew Dent
* STUDENT NUMBER	1234567
* COURSE		74.222
* INSTRUCTOR		Ima Bohr
* ASSIGNMENT		0
* QUESTION		0
*****************************************************************
*setup low memory
	org	$0
	dc.l	$100000		stack pointer after reset
	dc.l	start		program counter after reset
	dc.l	zzBusErr	bus error (e.g. address out of address space)
	dc.l	zzAddErr	address error (e.g. odd address when even is needed)
	dc.l	zzIllIns	illegal instruction error
	dc.l	zzDivZer	divide by zero error
	org	$1000
*****************************************************************
*equ symbols
null	equ	$00	null string terminator
lf	equ	$0A	linefeed
cr	equ	$0D	carriage return
*****************************************************************
* Prompt for a string and count the number of uppercase letters.       
*****************************************************************
*
*	Register Usage
*	d3	counter
*	a3	pointer to input buffer
*****************************************************************
start	jsr	initIO		initialize I/O

*Print Identification
	lea	banner,a0	address of IDBanner
	jsr	strout

* Prompt for input string and echo it.
        lea     prompt,a0	address of prompt
        jsr     strout
        lea     buffer,a0	address of input buffer
        jsr     strin
        jsr     newline
        lea     buffer,a0	echo contents of input buffer
        jsr     strout
        jsr     newline

* Initialize the counter and buffer pointer
        clr.l   D3		count = 0
        lea     buffer,A3	A3 points to start of buffer

* Main loop: process all characters in buffer and count caps.
        bra     test            enter loop at bottom
loop    cmpi.b  #'A',(a3)       compare to 'A'
        blt     next            this letter is not uppercase
        cmpi.b  #'Z',(a3)	compare to 'Z'
        bgt     next            neither is this one
count   addq.l  #1,d3           count this one
next    adda.l  #1,a3           move to next char
test    tst.b   (a3)            test for null byte
        bne     loop            process this char

* Print the answer
        lea     answer,a0	address of message
        jsr     strout
        move.l  d3,d0		prepare to print answer
        jsr     decout
        jsr     newline

	
* end of processing
	lea	EOP,a0		address of message
	jsr	strout
	jsr	finish		end of execution
*****************************************************************
* your data section        
*****************************************************************
banner	dc.b	'NAME               Stew Dent',cr,lf
	dc.b	'STUDENT NUMBER     1234567',cr,lf
	dc.b	'COURSE             74.222',cr,lf
	dc.b	'INSTRUCTOR         Ima Bohr',cr,lf
	dc.b	'ASSIGNMENT 0',cr,lf
	dc.b	'QUESTION 0',cr,lf,cr,lf
	dc.b	null                Banner string terminator
prompt  dc.b    'Enter a string: ',null
answer  dc.b    'Number of caps is: ',null
buffer  ds.b    200             input buffer
EOP	dc.b	cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
	include	68kIO.s		Input/Output routines
*****************************************************************
	end

