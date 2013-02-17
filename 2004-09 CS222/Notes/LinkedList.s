! File: LinkedList.s

! This program will read in long integers from
! the user, stopping when the sentinel -1 is read.
! While reading, it will insert these integers into
! an ordered linked list (so that the integers are
! kept in ascending order). It will then print out
! the resulting linked list, and dispose of it.

! The structure of a node is simply:
!    record
!      Data: integer; {4 bytes at offset 0}
!      Link: NodePtr; {4 bytes at affset 4}
!    end

    Data = 0
    Link = 4
    NodeSize = 8 !8 bytes is the size of one node
    
!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
!=========================================================

!---------------------------------------------------
!Initialize. Write a prompt. Set %L0 to NIL(0).
! Register usage:
!     L0 - the top pointer to a linked list of nodes


        set     prompt,%o0      !address of prompt string
        call    strout          !write the prompt
        nop                     !to avoid SET in delay slot
        
        clr     %L0             !top pointer is nil/null/0
        ba      ReadEnter       !Enter the loop
        nop                     !delay slot
        
!---------------------------------------------------
! Set up a new node for the new integer just read.
! Register usage:
!     L1 - the latest integer read from the user
!     L2 - "NewNode": a pointer to the new node

ReadLoop:
	mov 	NodeSize,%o0    !Pass the # of bytes desired
        call    malloc          !This is like NewNode := new(Node)
	nop
	
        mov     %o0,%L2         !L2 is the pointer to the new node  
        st      %L1,[%L2+Data]  !Store the data in the new node.

!---------------------------------------------------
! Now find the correct place for the new node.
! Register dictionary:
!     L0 - "Top": the top pointer to a linked list of nodes
!     L1 - "NewData": the latest integer read from the user
!     L2 - "NewNode": a pointer to the new node
!     L3 - "Next": a pointer which moves through the list
!     L4 - "Prev": points to the node just before the one Next does

        mov     %L0,%L3         ! Next:=Top
	clr	%L4		! Prev:=nil
        ba      InsertIn        ! Enter the insertion loop
        nop 
InsertLoop:
        ld      [%L3+Data],%g1  ! Next^.Data
        cmp     %g1,%L1         ! compare to NewData
        bge     PutItIn         ! if Next^.Data >= NewData, insert here
        nop
        mov     %L3,%L4         ! Prev := Next;
        ld      [%L3+Link],%L3  ! Next := Next^.Link;
InsertIn:
        tst     %L3             !while Next <> nil do
        bne     InsertLoop
        nop
        
!---------------------------------------------------
! Now insert the new node in between those pointed to by Prev and Next
! Register dictionary:
!     L0 - "Top": the top pointer to a linked list of nodes
!     L2 - "NewNode": a pointer to the new node
!     L3 - "Next": a pointer which moves through the list
!     L4 - "Prev": points to the node just before the one Next does
        
PutItIn:
       
        st      %L3,[%L2+Link]  !NewNode^.Link:=Next; in either case
	tst     %L4             !if Prev = nil (we add to beginning)	
        be      PutAtFront      !then no previous node, put at front
	nop
        st      %L2,[%L4+Link]  !Prev^.Link:=NewNode; (delay slot)	
        ba      ReadEnter       !Go on to the next iteration, but first...
	nop
PutAtFront:
        mov     %L2,%L0         !Top:=NewNode;

!---------------------------------------------------
! Read another integer, and watch for the sentinel
! Register dictionary:
!     L1 - the latest integer read from the user
    
ReadEnter:
        call    decin		!read another integer
        nop  
	mov     %o0,%L1         !with the new integer in %L1                   
        cmp     %o0,-1          !check for the sentinel
        bne     ReadLoop        !if not sentinel, loop
	nop

!---------------------------------------------------
! Echo the integers in ascending order.
! Register dictionary:
!     L0 - "Top": the top pointer to a linked list of nodes
!     L1 - "Next": sequences through the nodes of the list
!     L2 - use to save "Next^.Link" (the node that follows Next)
	
        set     header,%o0      !address of header string
        call    strout          !write the header string
        nop
	
        mov     %L0,%L1         !...Next:=Top (delay slot)        
        ba      WriteIn         !Enter the writing loop with...
	nop
WriteLoop:


        ld      [%L1+Data],%o0  !write Next^.Data
        ld      [%L1+Link],%L2  !save Next^.Link
	
        call    decout
	nop
	call 	newline
	nop
	
	mov     %L1,%o0         !...that Next is pointing to
        call    free            !dispose of the node...
	nop
	
        mov     %L2,%L1         !Next:=Next^.Link
WriteIn:
        tst     %L1             !while Next<>nil do
        bne     WriteLoop
        nop
        
        call    newline         !Finish off the line
        nop
        
!---------------------------------------------------
!Finish the program

AllDone:
        set     endMsg,%o0      !print a final message
        call    printf
        nop

!========== Standard epilogue for code ===================
        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
!=========================================================
        
!=========================

        .section ".data"
        
prompt:  .asciz "Enter integers terminated by -1:\n"
header:  .asciz "\nThe integers in ascending order:\n"
endMsg:  .asciz "\nProgram ended.\n"
