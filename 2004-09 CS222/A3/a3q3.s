*This program sorts integers included from a file using a
*selection sort.  The selection sort algorithm is based on this
*java-like pseudocode:
*
*  for (i = 0; i < array_size-1; i++)
*  {
*    min = i;
*    for (j = i+1; j < array_size; j++)
*    {
*      if (numbers[j] < numbers[min])
*        min = j;
*    }
*    temp = numbers[i];
*    numbers[i] = numbers[min];
*    numbers[min] = temp;
*  }
*
*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        3
* QUESTION          3
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
* d1 - counter for outputting unsorted and sorted array
* d2 - temporary register (stores a value pointed to by an address)
* d3 - temporary register (stores a value pointed to by an address)
* a1 - points to inner loop counter
* a2 - points to outer loop counter
* a3 - points to last data element (and all in outputting the array)
* a5 - points to second to last data element
* a6 - points to current min in inner loop
*****************************************************************

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

*Print unsorted array

	  lea	    unsPrompt,a0	print prompt for unsorted
	  jsr       strout

          lea	    data,a3		begin at start of array
	  clr       d1			intialize counter
	  bra       unsCheck		begin loop

unsLoop:
	  move.l    (a3)+,d0		output current element
	  jsr       decout
	  move.b    #' ',d0		formatting
	  jsr       charout
	  
	  addi.l    #1,d1		increment counter
	  
unsCheck:
	  cmp.l     numData,d1		loop until we hit numData
	  bne       unsLoop
	  
	  jsr	    newline

*Sort the array

startsort:
	  suba.l    #4,a3		sets a3 to arraySize
	  lea	    data,a2		sets a2 to the beginning of the array
	  movea.l   a3,a5		sets a5 to arraySize - 1
	  suba.l    #4,a5

*This implements the outer for loop
   	  bra  	    outerlooptest       test if past end of the array

outerLoop:
	 movea.l    a2,a6		sets a6 to the outside loop counter
	 movea.l    a2,a1		sets a1 to the outside loop counter
	  
*This implements the (inner) while loop
innerLoop:
	 cmpa.l     a1,a3		determine if we've reached end of array
	 adda.l	    #4,a1		increment a1
	 ble	    doneIter
	 move.l     (a1),d1		
	 move.l     (a6),d2
	 cmp.l      d1,d2		if data[min] < data[j]
	 ble        innerLoop     	continue looping

	 movea.l    a1,a6		else, a1 is the new min
	 bra        innerLoop

doneIter:
	 move.l     (a2),d2		swap the current min
	 move.l	    (a6),d3	 	with the outside loop counter	 

	 move.l     d2,(a6)
	 move.l     d3,(a2)

	 adda.l     #4,a2		inc outside loop counter
outlooptest:
	 cmpa.l	    a5,a2		check if past the array
         ble	    outerLoop	  


*Print sorted array

	  lea	    sortPrompt,a0	print prompt for sorted
	  jsr       strout

          lea	    data,a3		begin at start of array
	  clr       d1			intialize counter
	  bra       sortCheck		begin loop

sortLoop:
	  move.l    (a3)+,d0		output current element
	  jsr       decout
	  move.b    #' ',d0		formatting
	  jsr       charout
	  
	  addi.l    #1,d1		increment counter
	  
sortCheck:
	  cmp.l     numData,d1		loop until we hit numData
	  bne       sortLoop

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
          dc.b      'ASSIGNMENT 3',cr,lf
          dc.b      'QUESTION 3',cr,lf,cr,lf
          dc.b      null                Banner string terminator
unsPrompt:dc.b      'Unsorted Array: ',null
sortPromtp:dc.b     'Sorted Array: ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   A3Q3D5.txt
*****************************************************************
          end
