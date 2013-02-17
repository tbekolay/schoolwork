!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================
!NAME              Trevor Bekolay
!STUDENT NUMBER    6796723
!COURSE            74.222
!INSTRUCTOR        Prof. Van Rees
!ASSIGNMENT	   4
!QUESTION	   1
!---------------------------------------------------------
!This program reads in two strings from stdin and determines
!if the second string is a substring of the first, and if so,
!where in the first string the substring occurs (first).
!---------------------------------------------------------
! Register usage:
! 	%o1 - Address of Pattern (during isSubstring call)
! 	%o0 - Address of InputString (during isSubstring call)
!	%L0 - Gets output of isSubstring placed in it

!Print identification banner
        set     banner,%o0
        call    strout
        nop                     !the "delay slot"

	set 	head,%o0	!Print out a heading
	call	strout
	nop

	set     InputString,%o0	!Read in InputString
	call    strin
	nop
	
	call	strout		!Echo InputString
	nop
	call	newline		!Separate from next line
	nop
	
	set     Pattern,%o0	!Read in Pattern
	call    strin
	nop

	call	strout		!Echo Pattern
	nop
	call	newline		!Separate from next line
	nop

	mov     %o0,%o1		!Move address of Pattern to parameter 2
	set     InputString,%o0 !Move address of InputString to parameter 1

	call	isSubstring	!Call isSubstring subprogram
	nop
	mov	%o0,%L0		!Move output of isSubstring to local register

	cmp	%L0,-1		!If output is not -1, branch to issub
	bne	issub
	nop

	set	Pattern,%o0	!Output some strings that tell the
	call	strout		!user that Pattern is not a substring
	nop			!of InputString
	set	notsub,%o0
	call	strout
	nop
	set	InputString,%o0
	call	strout
	nop

	ba	done		!Go to the end
	nop
	
issub:	
	set	Pattern,%o0	!Output some strings that tell the
	call	strout		!user that Pattern is a substring
	nop			!of InputString
	set	sub1,%o0
	call	strout
	nop
	set	InputString,%o0
	call	strout
	nop
	set	sub2,%o0
	call	strout
	nop
	inc	%L0		!Also output the position where the
	mov	%L0,%o0		!Substring begins
	call	decout
	nop

done:
	call	newline
	nop

!Normal termination sequence

       set      goodbye,%o0
       call     strout
       nop

!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section
         .section   ".data"
banner:  .ascii     "NAME             Trevor Bekolay\n"
         .ascii     "STUDENT NUMBER   6796723\n"
         .ascii     "COURSE           74.222\n"
         .ascii     "INSTRUCTOR       Prof. Van Rees\n"
	 .ascii	    "ASSIGNMENT	      4\n"
	 .asciz	    "QUESTION	      1\n"
head:    .asciz     "\nReading in two strings:\n"
length:  .asciz	    "Length: "
notsub:  .asciz     " is not a substring of "
sub1:    .asciz     " is a substring of "
sub2:    .asciz     " at position "
InputString: .skip 80
Pattern:     .skip 80
goodbye: .asciz     "\n\nEnd of Processing\n\n"

!=========================================================
!        isSubstring subroutine
!Determintes if the string pointed to in the second parameter
!is a substring of the string pointed to in the first parameter
!and if so, where in the first string the substring occurs.
!=========================================================
! Register usage:
!	%i1 - Input parameter; address of Pattern
!	%i0 - Input parameter; address of Inputstring
!	%i0 - Output parameter; position of substring
!	%L0 - Address of InputString
!	%L1 - Address of Pattern
!	%L2 - Length of InputString
!	%L3 - Length of Pattern
!	%L4 - Loop counter
!	%L5 - Loop counter
!	%L6 - Stop condition for outer loop
!	%L7 - Temporarily stores some addresses
!	%g1 - Temporarily stores value of InputString[i+j]
!	%g2 - Temporarily stores value of Pattern[j]

	.section	".text"
	.global		isSubstring
	.align		4
isSubstring:
	save	%sp,-96,%sp

	mov	%i0,%L0		!Address of InputString
	mov	%i1,%L1 	!Address of Pattern

	mov	%L0,%o0		!Sets address to pass to stringLength
	call	stringLength
	nop
	mov	%o0,%L2		!Length of InputString

	mov	%L1,%o0		!Sets address to pass to stringLength
	call	stringLength
	nop
	mov	%o0,%L3		!Length of Pattern

	mov	-1,%L4		!i
	sub	%L2,%L3,%L6	!Stop condition
	
	ba	subtest		!Enter outer loop
	nop

subloop:
	clr	%L5		!j
	ba	subintest	!Bottom tested
	nop

subinloop:
	inc	%L5		!j = j+1

subintest:
	add	%L4,%L5,%L7	!i+j
	add	%L0,%L7,%L7	!i+j+&InputString
	ldub	[%L7],%g1	!Get InputString[i+j]
	add	%L5,%L1,%L7	!j+&Pattern
	ldub	[%L7],%g2	!Get Pattern[j]

	cmp	%L5,%L3		!Compare J to length of Pattern
	bge	subindone	!If >=, done
	nop

	cmp	%g1,%g2		!Compare InputString[i+j] to Pattern[j]
	beq	subinloop	!If ==, continue looping
	nop

subindone:	
	cmp	%L5,%L3		!Compare J to length of Pattern
	bne	subtest		!If !=, continue looping
	nop
	mov	%L4,%i0		!If ==, set output to current i
	ba	subdone		!Break out of loop
	nop

subtest:
	add	%L4,1,%L4	!Increment counter
	cmp	%L4,%L6		!Compare counter to stop condition
	blt	subloop
	nop
	
	mov	-1,%i0		!If no substring found, set output to -1

subdone:
	ret
	restore
	
!=========================================================
!        stringLength subroutine
!=========================================================
! register usage
!    %i1 - input parameter n
!    %i0 - output parameter - value of fibon(n)
!    %l1 - holds fibon(n-1)
!
	.section	".text"
	.global		stringLength
	.align		4
stringLength:
	save	%sp,-96,%sp
	
	mov	%i0,%L0		!Address of input string
	clr	%L2		!Length counter
	
	ba	strtest		!Bottom tested loop
	nop
strloop:
	add	%L0,1,%L0	!Increment address
	add	%L2,1,%L2	!Increment counter
strtest:
	ldub	[%L0],%L1	!Load character at the address in %L0
	cmp	%L1,'\0'	!If not EOF, continue looping
	bne	strloop
	nop
strdone:
	mov	%L2,%i0		!Set output parameter
	
	ret
	restore
