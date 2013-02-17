* a5q2.s
*****************************************************************
* NAME			Stew Dent
* STUDENT NUMBER	1234567
* COURSE		74.222
* INSTRUCTOR		Ima Bohr
* ASSIGNMENT		5
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
parmsize equ    4       size of parameters to gcd
*****************************************************************
* Find the GCD of pairs of numbers until a zero value is entered.
*****************************************************************
*	Register Usage
*	d1	The first value
*	d2	The second value
*****************************************************************
start	jsr	initIO		initialize I/O

*Print Identification
	lea	banner,a0	address of IDBanner
	jsr	strout

* Main loop: read a pair of numbers

* Read the first number and echo it
begin   lea     prmt1,a0        address of prompt
        jsr     strout
        jsr     decin           read the number
        move.l  D0,D1           store the number...
        jsr     decout          and echo it
        jsr     newline
* If zero entered, quit
        tst.l   d1
        beq     done

* Read the second number and echo it
        lea     prmt2,a0        address of prompt
        jsr     strout
        jsr     decin           read the number
        move.l  D0,D2           store the number...
        jsr     decout          and echo it
        jsr     newline
* If zero entered, quit
        tst.l   d2
        beq     done

* Print out the first part of the answer line
        lea     ans1,a0
        jsr     strout
        move.l  d1,d0
        jsr     decout
        lea     ans2,a0
        jsr     strout
        move.l  d2,d0
        jsr     decout
        lea     ans3,a0
        jsr     strout

* Call the GCD subroutine
        move.w  d1,-(sp)        put parm 1 on stack
        move.w  d2,-(sp)        put parm 2 on stack
        jsr     gcd             call gcd subroutine
        adda.l  #parmsize,sp    remove parameters from stack

* Print the GCD (d0)
        jsr     decout
        jsr     newline

* Get another pair of numbers
        bra     begin

* end of processing
done	lea	EOP,a0		address of message
	jsr	strout
	jsr	finish		end of execution
*****************************************************************
* your data section        
*****************************************************************
banner  dc.b    'NAME               Stew Dent',cr,lf
        dc.b    'STUDENT NUMBER     1234567',cr,lf
        dc.b    'COURSE             74.222',cr,lf
        dc.b    'INSTRUCTOR         Ima Bohr',cr,lf
        dc.b    'ASSIGNMENT 5',cr,lf
        dc.b    'QUESTION 2',cr,lf,cr,lf
        dc.b    null                Banner string terminator
prmt1   dc.b    'enter first number (0 to quit): ',null
prmt2   dc.b    'enter second number (0 to quit): ',null
ans1    dc.b    'GCD(',null
ans2    dc.b    ', ',null
ans3    dc.b    ') = ',null
EOP     dc.b    cr,lf,'End of Processing',cr,lf,null
*****************************************************************

*****************************************************************
* Subroutine gcd
*****************************************************************
* Recursive Euclidean algorithm to find the GCD of 2 numbers.
* Processing: divide a by b.
* (base case) remainder == 0, return b
* (recursion) remainder != 0, return gcd(b, remainder)
*****************************************************************
* Stack frame
aparm   equ     10      parameter a
bparm   equ     8       parameter b
local   equ     0       local storage
*****************************************************************
*       Register Usage
*       d1      a
*       d2      b
*****************************************************************
        ds.w    0               alignment
gcd     link    a6,#local       set up frame pointer
        movem.l d1/d2,-(sp)     save registers

        clr.l   d1              prepare for the division
        move.w  aparm(a6),d1    get parameter a
        move.w  bparm(a6),d2    get parameter b
        divs    d2,d1           do the division
        swap    d1              get the remainder in LSW
        tst.w   d1              is the remainder 0?
        beq     base            base case

* do the recursive call
        move.w  d2,-(sp)        put parm 1 (b) on stack
        move.w  d1,-(sp)        put parm 2 (remainder) on stack
        jsr     gcd             call gcd subroutine
        adda.l  #parmsize,sp    remove parameters from stack
        bra     done1           return this value

base    clr.l   d0              clear the return value
        move.w  d2,d0           return b

done1   movem.l (sp)+,d1/d2     restore registers
        unlk    a6
        rts                     return

*****************************************************************
* your program ends here
*****************************************************************
	include	68kIO.s		Input/Output routines
*****************************************************************
	end

