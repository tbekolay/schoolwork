a4q1.s
*****************************************************************
* NAME			Stew Dent
* STUDENT NUMBER	1234567
* COURSE		74.222
* INSTRUCTOR		Ima Bohr
* ASSIGNMENT		4
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
low2up	equ	-33	lowercase to uppercase mask
*****************************************************************
* Determine if a string is a palindrome.
* Ignore punctuation, spaces and capitalization, but consider
* digits.
*****************************************************************
*
*	Register Usage
*	d2	holds one char for comparison
*	a1	forward pointer
*	a2	backward pointer
*****************************************************************
start	jsr	initIO		initialize I/O

*Print Identification
	lea	banner,a0	address of IDBanner
	jsr	strout

* Print the string
	moveq	#'"',d0		print opening quote
	jsr	charout
	lea	instr,a0	print the string
	jsr	strout
	moveq	#'"',d0		print closing quote
	jsr	charout

* Set up the pointers to the start and end of the string
	lea	instr,a1	initialize forward pointer
	lea	endstr,a2	initialize backward pointer
	subq.l	#2,a2		backward pointer now initialized

* Processing loop: compare chars until a mismatch found or the
* pointers 'cross'
	bra	Penter		enter loop at bottom
Ploop
* Incemeent forward pointer until we have a letter or digit
* Or until we reach the end of the string
* Change lowercase to uppercase as well
Ftest
Fdigit	cmpi.b	#'0',(a1)	check for a digit
	blt	Fnext		advance to next letter
	cmpi.b	#'9',(a1)	is it a digit?
	ble	Rloop		if so, go to the reverse loop
Fupper	cmpi.b	#'A',(a1)	check for uppercase	
	blt	Fnext		advance to next letter
	cmpi.b	#'Z',(a1)	is it uppercase?
	ble	Rloop		if so, go to the reverse loop
Flower	cmpi.b	#'a',(a1)	check for lowercase	
	blt	Fnext		advance to next letter
	cmpi.b	#'z',(a1)	is it lowercase?
	bgt	Fnext		if not, advance to the next char
	andi.b	#low2up,(a1)	change to uppercase
	bra	Rloop		go to the reverse loop
Fnext	adda.l	#1,a1		increment pointer
	tst.b	(a1)		see if we have reached end of string
	bne	Ftest		if not, keep looking
	bra	winner		we have a palindrome
	
Rloop
* Decrement backward pointer until we have a letter or digit
* Or until we reach the beginning of the string
* Change lowercase to uppercase as well
Rtest
Rdigit	cmpi.b	#'0',(a2)	check for a digit
	blt	Rnext		advance to next letter
	cmpi.b	#'9',(a2)	is it a digit?
	ble	compare		if so, go to the reverse loop
Rupper	cmpi.b	#'A',(a2)	check for uppercase	
	blt	Rnext		advance to next letter
	cmpi.b	#'Z',(a2)	is it uppercase?
	ble	compare		if so, go to the reverse loop
Rlower	cmpi.b	#'a',(a2)	check for lowercase	
	blt	Rnext		advance to next letter
	cmpi.b	#'z',(a2)	is it lowercase?
	bgt	Rnext		if not, advance to the next char
	andi.b	#-33,(a2)	change to uppercase
	bra	compare		go to the comparison step
Rnext	suba.l	#1,a2		increment pointer
	tst.b	(a2)		see if we have reached end of string
	bne	Rtest		if not, keep looking
	bra	winner		we have a palindrome
	
compare
	move.b	(a2),d2		get a char into a register for comparison
	cmp.b	(a1),d2		compare the two characters
	bne	loser		we have a mismatch
advance	adda.l	#1,a1		advance forward pointer
	suba.l	#1,a2		advance backward pointer
	
Penter	cmpa.l	a1,a2		compare forward and backward pointer
	bgt	Ploop		keep going
	
winner	lea	ispal,a0	we have a palindrome
	jsr	strout
	bra	alldone
loser	lea	notpal,a0	we have a non-palindrome
	jsr	strout
	
alldone
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
	dc.b	'ASSIGNMENT 4',cr,lf
	dc.b	'QUESTION 1',cr,lf,cr,lf
	dc.b	null                Banner string terminator
ispal	dc.b	' is a palindrome',cr,lf,null
notpal	dc.b	' is NOT a palindrome',cr,lf,null
EOP	dc.b	cr,lf,'End of Processing',cr,lf,null
	include	A4Q1D6.txt
endstr	ds.b	0	a label for the end of the input string
*****************************************************************
* your program ends here        
*****************************************************************
	include	68kIO.s		Input/Output routines
*****************************************************************
	end

