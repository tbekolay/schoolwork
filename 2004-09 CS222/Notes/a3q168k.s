* a3q168k.s
*****************************************************************
* NAME			Stew Dent
* STUDENT NUMBER	1234567
* COURSE		74.222
* INSTRUCTOR		Ima Bohr
* ASSIGNMENT		3
* QUESTION		1
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
* Print a table containing the values 1..25 and their squares.
* Align the columns on the units digit.       
*****************************************************************
*
*	Register Usage
*	d1	x
*	d2	x^2
*****************************************************************
start	jsr	initIO		initialize I/O

*Print Identification
	lea	banner,a0	address of banner
	jsr	strout

* Print table heading.
        lea     heading,a0	address of heading
        jsr     strout

* Initialize registers
        clr.l   D0		make sure MSW is clear
        moveq	#1,D1		x = 1

* Main loop: process all characters in buffer and count caps.
        bra     test            enter loop at bottom
loop    move.w	D1,D2		prepare to calc x^2
	muls	D1,D2		x*x
	
* Print this row of the table
* Print x. See if we need to print a blank to align this value.
	cmpi.l	#10,D1		does x have 2 digits?
	bge	prtx		if so, don't print a blank
prt1	lea	b1,a0		address of a single blank...
	jsr	strout		print it
prtx	move.l	D1,D0		prepare to print x...
	jsr	decout		print it
	
* Print x^2. See if it has 1, 2 or 3 digits and print correct # of blanks.	
	lea	b6,a0		prepare to print 6 blanks...
chk3	cmpi.w	#100,D2		does x^2 have 3 digits?	
	bge	prtb		go to print 6 blanks
	lea	b7,a0		prepare to print 7 blanks...
chk2	cmpi.w	#10,D2		does x^2 have 2 digits?
	bge	prtb		go to print 7 blanks
	lea	b8,a0		prepare to print 8 blanks...
prtb	jsr	strout		print the correct # of blanks
prtx2	clr.l	D0		prepare d0 to print x^2
	move.w	D2,D0		put x^2 into D0...
	jsr	decout		and print it
	jsr	newline
	
* Update and test steps for this loop.
next    add.l	#1,D1		x++
test    cmpi.w	#25,D1		x <= 25?
        ble     loop            do the loop

	
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
	dc.b	'ASSIGNMENT 3',cr,lf
	dc.b	'QUESTION 1',cr,lf,cr,lf
	dc.b	null                Banner string terminator
heading	dc.b    ' x      x^2',cr,lf
	dc.b	'-----------',cr,lf,null
b1	dc.b    ' ',null
b6	dc.b    '      ',null
b7	dc.b    '       ',null
b8	dc.b    '        ',null
EOP	dc.b	cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
	include	68kIO.s		Input/Output routines
*****************************************************************
	end

