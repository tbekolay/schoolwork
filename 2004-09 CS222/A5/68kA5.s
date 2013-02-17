*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        5
* QUESTION          2
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
* This program creates a linked list, prints it out, then deletes certain sections
* based on input from a file, printing out the current state of the linked list
* after each set of deletions.
*
* The structure of a node is simply:
*    record
*       Link: NodePtr; {4 bytes at offset 0}
*	Freq: short int; {2 bytes at offset 4}
*	Char: char;    {1 byte at offset 6}
*	1 byte of padding
*    end  
*****************************************************************
* Register Dictionary
* 
* a1 - pointer to the top of the linked list (dummy node)
* a2 - a pointer that will point to various elements of the 'range' array
* d1 - the number of deletions to make
* d2 - the first character to start deleting
* d3 - the second character to finish deleting

NODESIZE  equ	    8			size of a node 
LINKOFF	  equ       0			offset of data
FREQOFF	  equ       4			offset of link
CHAROFF	  equ	    6			offset of char

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

	  lea	    head0,a0		print heading
	  jsr       strout

	  lea       theList,a1		a1 = pointer to top (dummy node)

	  move.l    a1,-(sp)		put a1 onto stack
	  jsr	    createLL		call createLL
	  adda.l    #4,sp		restore stack pointer

	  jsr	    newline

	  lea	    prnthead,a0		print heading for printLL
	  jsr       strout

	  move.l    a1,-(sp) 		put a1 onto stack
	  jsr       printLL		call printLL
	  adda.l    #4,sp		restore stack pointer

	  move.w    n,d1		d1 = number of deletions to make		
	  lea       range,a2		a2 = start of range

	  bra       delcheck		bottom tested

delloop:
	  move.b    (a2)+,d2		d2 = first character
	  move.b    (a2)+,d3		d3 = second character
	
	  
	  lea	    delhead1,a0		print heading
	  jsr	    strout
	
	  move.b    d2,d0		print first character
	  jsr	    charout
	
	  lea	    delhead2,a0		print heading
	  jsr	    strout
	
	  move.b    d3,d0		print second character
	  jsr	    charout
	
	  jsr	    newline		beautify

	  ext.w     d2			extend byte to word for pushing on the stack
	  ext.w     d3			extend byte to word for pushing on the stack
	  move.w    d2,-(sp)		put d2 (first char) onto stack
	  move.w    d3,-(sp)		put d3 (second char) onto stack
	  move.l    a1,-(sp)		put a1 (pointer to list) onto stack
	  jsr       deleteLLSub		call deleteLLSub
	  adda.l    #6,sp		restore stack pointer

	  jsr	    newline

	  move.l    a1,-(sp)		put a1 onto stack 
	  jsr       printLL		call printLL
	  adda.l    #4,sp		restore stack pointer
	
delcheck:
	  dbra	    d1,delloop		continue until d1 <= 0

* end of processing
          lea       EOP,a0              address of message
          jsr       strout
          jsr       finish              end of execution
*****************************************************************
* Subprogram createLL
* 
* Creates a linked list, based on the parallel arrays found in the
* included file
* 
* Stack Frame:
* 	Old fp: fp + 4
* 	TOP: fp + 8
*****************************************************************
* Register Dictionary
* 
* a1 - current.link (usually)
* a2 - frequency list
* a3 - character list
* a4 - current node
* d1 - the current frequency
* d2 - the current char
*

LOCALS:   equ       0
TOPOFF:   equ	    8
createLL
	  link      a6,#LOCALS	  	set the frame pointer
	  movem.l   a1-a4/d1-d2,-(sp)
	  
	  move.l    TOPOFF(a6),a0 	pointer to topList
	  movea.l   a0,a1		a1 = current node
	  lea       freq,a2		a2 = frequency list
	  lea       key,a3		a3 = char list
	  
	  bra       createcheck		bottom tested

createloop
	  move.b    (a3)+,d2		d2 = current char
	  move.w    d1,d0
	  jsr	    decout		output current freq
	  move.b    #' ',d0
	  jsr	    charout		output current char
	  move.b    d2,d0
	  jsr	    charout
	  jsr       newline
	  
	  movea.l   a1,a4		a4 = current node
	  adda.l    #NODESIZE,a1	a1 = current.link
	  move.l    a1,LINKOFF(a4)	current = current.link
	  move.l    #0,LINKOFF(a1)	sets current.link to null
	  move.w    d1,FREQOFF(a1)      sets current.freq
	  move.b    d2,CHAROFF(a1)      sets current.char

createcheck
	  move.w    (a2)+,d1		d1 = current freq
	  tst.w	    d1			check if d1 <= 0
	  bne       createloop

	  movem.l   (sp)+,a1-a4/d1-d2
	  unlk	    a6			restore old frame pointer/stack pointer etc
	  rts
	  
*****************************************************************
* Subprogram printLL
*
* Prints out the linked list pointed to by the passed argument.
*
* Stack Frame:
* 	Old fp: fp + 4
* 	TOP: fp + 8
*****************************************************************
* Register Dictionary
*
* a1 - pointer to current node
*

printLL
	  link      a6,#LOCALS	  	set the frame pointer
	  movem.l   a1,-(sp)
	  
	  move.l    TOPOFF(a6),a1 	pointer to top of list
	  movea.l   LINKOFF(a1),a1	curr = top.link

	  bra       printcheck		bottom tested

printloop
	  move.b    #'(',d0		output (
	  jsr       charout
	  
	  move.w    FREQOFF(a1),d0	output frequency
	  jsr       decout
	  
	  move.b    #' ',d0		output space
	  jsr       charout
	  
	  move.b    CHAROFF(a1),d0	output char
	  jsr       charout
	  
	  move.b    #')',d0		output )
	  jsr       charout
	  
	  jsr	    newline

	  movea.l   LINKOFF(a1),a1	curr = curr.link

printcheck
	  cmpa.l    #0,a1		check curr != null
	  bne 	    printloop

	  jsr       newline		beautify

	  movem.l   (sp)+,a1
	  unlk      a6			reset stack/frame pointer etc.
	  rts

*****************************************************************
* Subprogram deleteLLSub
*
* Deletes a section of nodes, beginning with the first passed char,
* ending with the second passed char.
*
* Stack Frame:
* 	Old fp: fp + 4
* 	TOP: fp + 8
*	CHAR2: fp + 12
*	CHAR1: fp + 14
*****************************************************************
* Register Dictionary
*
* a1 - pointer to dummy node
* a2 - prev
* a3 - curr
* d2 - the first character
* d3 - the second character
* d4 - curr.char
*

CHAR1OFF: equ       14
CHAR2OFF: equ       12
deleteLLSub
	  link      a6,#LOCALS	  	set frame pointer
	  movem.l   a1-a3/d2-d4,-(sp)
	  
	  move.l    TOPOFF(a6),a1	pointer to dummy node
	  move.w    CHAR1OFF(a6),d2	first character
	  move.w    CHAR2OFF(a6),d3	second character

	  movea.l   a1,a2		previous pointer
	  movea.l   LINKOFF(a1),a3      current (previous.link)

	  bra       delete1check	bottom tested

delete1loop
	  movea.l   a3,a2		prev = curr
	  movea.l   LINKOFF(a3),a3	curr = curr.link
	  
delete1check
	  move.b    CHAROFF(a3),d4	d4 = current.char
	  cmp.b     d2,d4		check if we have found the first node
	  bne       delete1loop

	  bra       delete2check	bottom tested

delete2loop
	  movea.l   LINKOFF(a3),a3	curr = curr.link
	  move.l    a3,LINKOFF(a2)	prev.link = curr (effectively deletes the old curr)
	 
delete2check
	  move.b    CHAROFF(a3),d4	d4 = current.char
	  cmp       d3,d4		check if we have found the second node
	  bne	    delete2loop

delete2done
	  movea.l   LINKOFF(a3),a3	curr = curr.link
	  move.l    a3,LINKOFF(a2)	prev.link = curr

	  movem.l   (sp)+,a1-a3/d2-d4
	  unlk      a6			restore stack/frame pointer etc
	  rts



*****************************************************************
* your data section        
*****************************************************************
IDBanner: dc.b      'NAME               Trevor Bekolay',cr,lf
          dc.b      'STUDENT NUMBER     6796723',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Van Rees',cr,lf
          dc.b      'ASSIGNMENT 5',cr,lf
          dc.b      'QUESTION 2',cr,lf,cr,lf
          dc.b      null                Banner string terminator
head0:    dc.b      'Creating a linked list . . .',cr,lf,null
prnthead: dc.b      'Printing out linked list . . .',cr,lf,null
delhead1: dc.b      'Deleting ',null
delhead2: dc.b      ' to ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
theList:  ds.w      800			Enough for 100 nodes
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   a5q2.txt
*****************************************************************
          end
