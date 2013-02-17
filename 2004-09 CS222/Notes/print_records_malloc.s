!Example Program :	print_records_malloc.s
!By Ben Li
!This example illustrates how to deal with an array of records.
!We will create a array of records and print them
!This example uses malloc to allocate the array initially.
!The record will look like this:	

!struct student {
!  int stNum;	at offset 0
!  short grade;	at offset 4
!};
	 
!========== Standard prologue for Sparc code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
	
!=========================================================
!NAME              John Doe
!STUDENT NUMBER    1234567
!COURSE            74.222
!INSTRUCTOR        Ben Li
!ASSIGNMENT        0
!QUESTION          0
!---------------------------------------------------------
!
!___________________________________________________________________	

!Task 1: Read in records
!Register Usage:
!	l0 - pointer to current record in array
!	l1 - hold # of records read	
!	o0 - used with I/O

	set prompt0,%o0
	call strout
	nop	
	!first prompt and allocate memory using malloc
	call decin
	nop

	cmp %o0,0
	ble the_end
	nop

	sll %o0,3,%o0	!8 bytes for each record
	call malloc
	nop

	cmp %o0,0	!0 means failure
	be the_end
	nop

	mov %o0,%l0	!make a copy of array address to work with in %l0
	
	set pArray,%o0	!save pointer to array in variable pArray
	st %l0,[%o0]
		
	mov 0,%l1

	set prompt1, %o0
	call strout
	nop
	call decin	!read in student number
	nop
	
	cmp %o0, 0
	bl done_read
	nop
read_loop:	
	st %o0, [%l0]	!save student # in current record
	set prompt2,%o0
	call strout
	nop
	call decin	!read in grade
	nop				

	sth %o0,[%l0+4] !save grade in current record 

	add %l0,8,%l0	!move to next record in array
	add %l1,1,%l1	!increment count
		
	set prompt1, %o0
	call strout
	nop
	call decin	!read in student number
	nop
		
	cmp %o0, 0	!not done yet, continue reading
	bg read_loop
	nop
done_read:
	set numRec, %l0
	st %l1,[%l0]

	set pArray,%l2	!get the address of start of array from pArray variable
	ld [%l2],%o0	!1st arg to print is start of array (put in %o0)
	mov %l1,%o1	!2nd arg is # of records
	
	call print	!print records
	nop

	mov %l2,%o0	
	call free	!free memory allocated using malloc
	nop
the_end:
	
	
!========== Standard epilogue for Sparc code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================

! The data section
	
	.section ".data"	!data section
prompt0:	.asciz  "How many records do you need to store:	"	
prompt1:	.asciz	"Enter student number (-1 to quit)"
prompt2:	.asciz	"Enter grade"
printStr:	.asciz	"The Records are"
sortStr:	.asciz	"Sorting..."
	
	.align 4
numRec:		.word	0	!hold # of students	
pArray:		.word	0
					

!Task 2: Print Sorted Record
!Subroutine print
!========== Standard prologue for Sparc code ===================
        .section    ".text"
        .global     print
        .align      4
print:   save        %sp,-96,%sp !allocate min 96 bytes
	
!=========================================================
!Register Usage
!	l0 - address of current student record in array
!	l1 - address of storage containing # of records
!	l2 - loop counter
!	l3 - holds the # of records in array
!	o0 - for I/O	
!	i0 - the address of array passed to subroutine
!	i1 - # of records passed to subroutine
			
	set printStr,%o0	!print a header
	call strout
	nop
	call newline
	nop

	
	mov %i0,%l0		!move the starting addr into l0
	mov %i1,%l3		!store count in l3

	mov 0,%l2		!l2 will count from 0 to l1-1

				!notice loop is not optimized
print_loop:
	cmp %l2,%l3
	bge done_print
	ld [%l0],%o0		!load student # and print
	call decout
	nop
	call newline
	nop

	lduh [%l0+4],%o0	!load (unsigned) grade and print
	call decout
	nop
	call newline
	nop
	call newline		!put an blank line to separate records
	nop

	add %l2,1,%l2		!increment loop counter by 1
	add %l0,8,%l0		!move to next record
	
	ba print_loop
	nop
done_print:	
	
!========== Standard epilogue for Sparc code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================	