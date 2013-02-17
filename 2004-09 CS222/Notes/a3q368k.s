* a3q3.s
*****************************************************************
* NAME			Stew Dent
* STUDENT NUMBER	1234567
* COURSE		74.222
* INSTRUCTOR		Ima Bohr
* ASSIGNMENT		3
* QUESTION		3
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
* Decrypt an encryped string by rotating each char an amount
* specified by an integer key.
*****************************************************************
*
*	Register Usage
*	d1	holds one char from the string
*	d2	key value
*	d3	key value for digits = key % 10
*	a1	pointer to the character string
*****************************************************************
start	jsr	initIO		initialize I/O

*Print Identification
	lea	banner,a0	address of IDBanner
	jsr	strout
	
* Print the encrypted string
	lea	cryptm,a0
	jsr	strout
	lea	crypt,a0
	jsr	strout
	jsr	newline
	
* Get the key value and calculate key value for digits
	move.w	key,d2		get key for letters
	clr.l	d3		prepare to calculate key for digits
	move.w	d2,d3
	moveq	#10,d1		key for digits is key % 10
	divu	d1,d3
	swap	d3		we now have our key for digits
	
* Begin processing the string
	lea	crypt,a1	get pointer to string
	bra	test		enter loop at bottom
loop
* Check for and decrypt digits
	cmpi.b	#'0',(a1)	begin checking for a digit
	blt	next		leave this char as is
	cmpi.b	#'9',(a1)	it's a digit if <= 9
	bgt	upper		check for uppercase
	add.b	d3,(a1)		add key value to this char
	cmpi.b	#'9',(a1)	check for wrap around
	ble	next		all done here
	subi.b	#10,(a1)	wrap this one around
	bra	next		get the next char
	
upper
* Check for and decrypt uppercase letters
	cmpi.b	#'A',(a1)	begin checking for an uppercase
	blt	next		leave this char as is
	cmpi.b	#'Z',(a1)	it's an upper if <= Z
	bgt	lower		check for lowercase
	add.b	d2,(a1)		add key value to this char
	cmpi.b	#'Z',(a1)	check for wrap around
	ble	next		all done here
	subi.b	#26,(a1)	wrap this one around
	bra	next		get the next char

lower	
* Check for and decrypt lowercase letters
* We may get overflow from a byte when we add the key to it so we
* will work with the character in a register so we can use a word
	clr.w	d1		prepare to get a char
	move.b	(a1),d1		move the char into d1.w
	cmpi.b	#'a',d1		beging checking for a lowercase
	blt	next		leave this char as is
	cmpi.b	#'z',d1		it's a lower if <= z
	bgt	next		all done here
	add.w	d2,d1		add key value to this char
	cmpi.w	#'z',d1		check for wrap around
	ble	wchar		all done here
	subi.w	#26,d1		wrap this one around
wchar	move.b	d1,(a1)		put this char back into the buffer

next	adda.l	#1,a1		advance to next char
test	tst.b	(a1)		check for null byte
	bne	loop		process this char
	
	
* Print the transformed string
	lea	clearm,a0
	jsr	strout
	lea	crypt,a0
	jsr	strout
	jsr	newline
	
* end of processing
	lea	EOP,a0		address of message
	jsr	strout
	jsr	finish		end of execution
*****************************************************************
* your data section        
*****************************************************************
	include	A3Q3D3.txt		the data 
banner	dc.b	'NAME               Stew Dent',cr,lf
	dc.b	'STUDENT NUMBER     1234567',cr,lf
	dc.b	'COURSE             74.222',cr,lf
	dc.b	'INSTRUCTOR         Ima Bohr',cr,lf
	dc.b	'ASSIGNMENT 3',cr,lf
	dc.b	'QUESTION 3',cr,lf,cr,lf
	dc.b	null                Banner string terminator
EOP	dc.b	cr,lf,'End of Processing',cr,lf,null
cryptm	dc.b	'The encrypted string: ',null
clearm	dc.b	'The original string:  ',null
*****************************************************************
* your program ends here        
*****************************************************************
	include	68kIO.s		Input/Output routines
*****************************************************************
	end

