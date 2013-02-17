* a5q1.s
*****************************************************************
* NAME			Stew Dent
* STUDENT NUMBER	1234567
* COURSE		74.222
* INSTRUCTOR		Ima Bohr
* ASSIGNMENT		5
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
parmsize equ    4       size of parameters to gcd
*****************************************************************
* Print the first 21 Fibonacci numbers in a table.
*****************************************************************
*       Register Usage
*       d0      fib(n)
*       d1      n (loop index)
*****************************************************************
start   jsr     initIO          initialize I/O

*Print Identification
        lea     banner,a0       address of IDBanner
        jsr     strout

        clr.l   d1              initialize loop index
        lea     head,a0         prepare to print table heading
        jsr     strout
        bra     enter
* Main loop
loop    move.l  d1,d0           prepare to print n
        jsr     decout
        lea     space,a0        
        jsr     strout
        move.l  d1,-(sp)        put n on stack...
        jsr     fibonacci       and call subroutine
        adda.l  #parmsize,sp    remove parameter from stack
        jsr     decout          print fib(n)
        jsr     newline
        addq.l  #1,d1           increment loop counter
enter   cmpi.l  #20,d1          test loop index
        ble     loop            do the loop


* end of processing
done    lea     EOP,a0          address of message
        jsr     strout
        jsr     finish          end of execution
*****************************************************************
* your data section        
*****************************************************************
banner	dc.b	'NAME               Stew Dent',cr,lf
	dc.b	'STUDENT NUMBER     1234567',cr,lf
	dc.b	'COURSE             74.222',cr,lf
	dc.b	'INSTRUCTOR         Ima Bohr',cr,lf
	dc.b	'ASSIGNMENT 5',cr,lf
	dc.b	'QUESTION 1',cr,lf,cr,lf
	dc.b	null                Banner string terminator
head    dc.b    'n   fib(n)',cr,lf,null
space   dc.b    '     ',null
EOP	dc.b	cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
* Subroutine fibonacci
*****************************************************************
* Find the n-th Fibonacci number iteratively.
*****************************************************************
* Stack frame
nparm   equ     8       parameter
local   equ     0       local storage
*****************************************************************
*       Register Usage
*       d0      fib(n)
*       d1      fib(n-1)
*       d2      fib(n-2)
*       d3      n
*****************************************************************
        ds.w    0               alignment
fibonacci
        link    a6,#local       set up frame pointer
        movem.l d1/d3,-(sp)     save registers

        move.l  nparm(a6),d3    get n
Fchk0   tst.l   d3              check for n=0
        bgt     Fchk1
        clr.l   d0              store fib(0)...
        bra     Fdone           and return it
Fchk1   cmpi.l  #1,d3           check for n=1
	bgt	Finit		get ready to calculate f(n)
        move.l  #1,d0           store fib(1)...
        bra     Fdone           and return it

Finit   clr.l   d2              set d2 to fib(0)
        move.l  #1,d1           set d1 to fib(1)
        subq.l  #1,d3           this loop runs 1 less time
        bra     Fenter          enter at bottom
Floop   move.l  d2,d0           put fib(n-2) into d0...
        add.l   d1,d0           and add fib(n-1) to it
        move.l  d1,d2           update fib(n-2)
        move.l  d0,d1           update fib(n-1)
Fenter  dbra    d3,Floop        loop test

Fdone   movem.l (sp)+,d1/d3     restore registers
        unlk    a6
        rts                     return

*****************************************************************
	include	68kIO.s		Input/Output routines
*****************************************************************
	end

