* a3q268k.s
*****************************************************************
* NAME			Stew Dent
* STUDENT NUMBER	1234567
* COURSE		74.222
* INSTRUCTOR		Ima Bohr
* ASSIGNMENT		3
* QUESTION		2
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
* Use the Euclidean algorithm to find the GCD of 2 numbers.      
*****************************************************************
*
*	Register Usage
*	d1	'big' number
*	d2	'small' number
*	d3	temp
*****************************************************************
start	jsr	initIO		initialize I/O

*Print Identification
	lea	banner,a0	address of banner
	jsr	strout

* Main loop: read a pair of numbers

* Read the first number and echo it
begin	lea     prmt1,a0	address of prompt
        jsr     strout
        jsr	decin		read the number
        move.l	D0,D1		store the number...
        jsr	decout		and echo it
        jsr	newline
* If zero entered, quit
	tst.l	d1
	beq	done

* Read the second number and echo it
        lea     prmt2,a0	address of prompt
        jsr     strout
        jsr	decin		read the number
        move.l	D0,D2		store the number...
        jsr	decout		and echo it
        jsr	newline
* If zero entered, quit
	tst.l	d2
	beq	done
        
* Print out the first part of the answer line
	lea	ans1,a0
	jsr	strout
	move.l	d1,d0
	jsr	decout
	lea	ans2,a0
	jsr	strout
	move.l	d2,d0
	jsr	decout
	lea	ans3,a0
	jsr	strout

* Processing loop: divide big by small.
* If remainder != 0, then update big = small, small = remainder
* If remainder == 0, then GCD = small

	bra	enter		enter loop at bottom
loop	move.w	d2,d3		temp = small
	move.w	d1,d2		small = remainder
	clr.l	d1		clear MSW of big
	move.w	d3,d1		big = temp

enter	divs	d2,d1		do the division
	swap	d1		get the remainder in LSW
	tst.w	d1		is the remainder 0?
	bne	loop		if not, repeat

* Print the GCD in small (d2)
	clr.l	d0		clear MSW of d0
	move.w	d2,d0		get GCD in d0
	jsr	decout
	jsr	newline
	
* Get another pair of numbers
	bra	begin
	
* end of processing
done	lea	EOP,a0		address of message
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
	dc.b	'QUESTION 2',cr,lf,cr,lf
	dc.b	null                Banner string terminator
prmt1	dc.b    'enter first number (0 to quit): ',null
prmt2	dc.b    'enter second number (0 to quit): ',null
ans1	dc.b	'GCD(',null
ans2	dc.b	', ',null
ans3	dc.b	') = ',null
EOP	dc.b	cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
	include	68kIO.s		Input/Output routines
*****************************************************************
	end

