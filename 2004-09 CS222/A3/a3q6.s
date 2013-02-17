*****************************************************************
* NAME              Trevor Bekolay
* STUDENT NUMBER    6796723
* COURSE            74.222
* INSTRUCTOR        Van Rees
* ASSIGNMENT        3
* QUESTION          1
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

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout 

*Print unsorted array
* d1 = counter

	  lea	    unsPrompt,a0
	  jsr       strout

          lea	    data,a3
	  clr       d1
	  bra       unsCheck

unsLoop:
	  move.l    (a3),d0
	  jsr       decout
	  move.b    #' ',d0
	  jsr       charout
	  
	  adda.l    #4,a3
	  addi.l    #1,d1
	  
unsCheck:
	  cmp.l     numData,d1
	  bne       unsLoop
	  
	  jsr	    newline

*a3 = first item in data
*a6 = second item

	 lea        data,a3
	 movea.l    a3,a6
	 adda.l	    #4,a6

	 move.l     (a3),d2
	 move.l	    (a6),d3	 
	 
	 move.l     d2,(a6)
	 move.l     d3,(a3) 


*Print sorted array

	  lea	    sortPrompt,a0
	  jsr       strout

          lea	    data,a3
	  clr       d1
	  bra       sortCheck

sortLoop:
	  move.l    (a3)+,d0
	  jsr       decout
	  move.b    #' ',d0
	  jsr       charout
	  
	  addi.l    #1,d1
	  
sortCheck:
	  cmp.l     numData,d1
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
          dc.b      'QUESTION 1',cr,lf,cr,lf
          dc.b      null                Banner string terminator
unsPrompt:dc.b      'Unsorted Array: ',null
sortPromtp:dc.b     'Sorted Array: ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
          include   68kIO.s             Input/Output routines
	  include   A3Q3D2.txt
*****************************************************************
          end
