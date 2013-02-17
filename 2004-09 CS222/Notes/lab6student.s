
! Name: Agatha Christie
! Student number: 1234567
! Course:  74.222 
! Instructor:  D. Meek
! Lab: 6
!-------------------------------------------------------
! This program reads a file lab6data.txt contains two integers ( a key and a 
! value) on a line until -1 -1 is encountered.  It constructs a binary
! search tree with the data iin nodes.  It prints the tree using an in-order
! traversal.  It then reads from the same file some more lines containing a
! capital I or D and two integers.  The letter signifies whether the
! second integer is to be incremented or decremented from the node with the key
! that is equal to the first integer.  When EOF is reached, the tree is
! printed again using an in-order traversal.
! node consists of:
! 4 byte integer = key           offset = 0
! 4 byte integer = value         offset = 4
! 4 byte pointer = leftpointer   offset = 8
! 4 byte pointer = rightpointer  offset =12
! REGISTER USAGE
! %o0 Used with I/O
! %g1 - temporary storage
! %L0 - operator 'I' or 'D'
! %L1 - key of current node
! %L2 - value of current node
! %L4 - root of binary tree
!---------------------------------
!       Standard prologue for code
!---------------------------------

        .section    ".text"
        .global     main
!----------------
! global symbols
!----------------
space   =    0x20    !code for a blank space
! node description follows
key     =    0
value   =    4
llink   =    8
rlink   =    12
nodelen =    16

        .align   4
main:   save     %sp,-96,%sp !allocate min 96 bytes
        
! print an identification banner
        set     idinfo,%o0
        call    strout        !Print the banner string (multi-line)
        nop 
! call READTREE
        set     root,%g1      !get address of top
        set     tree,%L4      !put address of tree in L4
        st      %L4,[%g1]     !store it in root
        call    readtree      !call read a tree routine
        mov     %L4,%o0       !delay slot - address of tree is param. 1
! print title and binary search tree
        set     treetitl,%o0  !address of tree title
        call    strout        !print it
        nop
        call    printtree     !print out tree
        mov     %L4,%o0       !delay slot - address of tree is param. 1
! read and print modifications to tree
        set     modtitle,%o0  !address of modification heading
        call    strout        !print heading
        nop
modloop:
        call    newline       !spacing
        nop
        call    charin
        nop
        cmp     %o0,-1        !if EOF finish by printing tree
        beq     outtree
        mov     %o0,%L0       !delay slot - operator 'I' or 'D'
        call    decin         !get key
        nop
        mov     %o0,%L1       !save key
        call    decin         !get value
        nop
        mov     %o0,%L2       !save value
!echo input line
        call    charout       !echo modification task 'I' or 'D'
        mov     %L0,%o0       !delay slot - 'I' or 'D'
        call    charout
        mov     space,%o0     !put in a space between key and value
        call    decout
        mov     %L1,%o0       !delay slot - key
        call    charout
        mov     space,%o0     !delay slot - put in a space between key and value
        call    decout
        mov     %L2,%o0       !delay slot - value
!calling sequence for modnode 
        mov     %L4,%o0       !address of tree = param. 1
        mov     %L0,%o1       !operator = param. 2
        mov     %L1,%o2       !key of node = param. 3
        call    modtree       !call modification routine
        mov     %L2,%o3       !value of node = param. 4
        ba      modloop       !get next line of input
        nop
!calling sequence for printtree to print out modified tree
outtree:
        set     mbsttitle,%o0
        call    strout
        nop
        mov     %L4,%o0       !delay slot - root of tree
        call    printtree     !print it
        nop

        set     QuitMsg,%o0
        call    strout        !Print a termination message
        nop
!---------------------------------
!       Standard epilogue for code
!---------------------------------
        ret                   !Return to the OS.
        restore               !Actually done before "ret".
       
     .section   ".data"
QuitMsg:  .asciz  "\nNormal termination\n\n"
idinfo:   .ascii  "Name: Agatha Christie\n"
          .ascii  "Student number: 1234567\n"
          .ascii  "Course: 74.222 \n"
          .ascii  "Instructor: D. Meek\n"
          .ascii  "Lab: 6\n"
          .asciz  "--------------------------\n"
modtitle: .asciz  "\nTree Modification List"
treetitl: .asciz  "\nBinary Search Tree\n"
mbsttitle:.asciz  "\nModified BST\n"
          .align  4
root:     .skip   4
tree:     .skip   25*16         ! 25*nodelen

!--------------------------------------------------------------
! READTREE
! This procedure accepts the address of a binary search tree and reads a
! file that contains two integers per line , the first is the key and the
! second is the value.  It inserts the data into the binary search tree 
! based on the key.
!
! Input: from file
! Output: creates the tree
!
! REGISTER USAGE
! %i0, %L0 - root of binary search tree
! %L1 - key
! %L2 - value
! %L5 - address of free node
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

       .section  ".text"
       .align   4
readtree:
        save    %sp,-96,%sp
        mov     %i0,%L0        !save address of bst
        mov     %L0,%L5        !free node starts life here
readloop:
        call    decin          !read key
        nop
        mov     %o0,%L1        !save key
        call    decin          !read value
        nop
        cmp     %o0,-1         !end of tree data?
        be      endinput       !if so stop tree read
        mov     %o0,%L2        !delay slot - save value
        st      %L1,[%L5+key]   !put key into free node
        st      %L2,[%L5+value] !put value into free node
        st      %g0,[%L5+llink] !NULL in left link
        st      %g0,[%L5+rlink] !NULL in right link
!calling sequence for linknode
        mov     %L0,%o0        !address of tree = param. 1
        mov     %L5,%o1        !address of free node
        call    addnode        !call routine to add node
        mov     %L1,%o2        !delay slot - key of current node
        add     %L5,nodelen,%L5 !get new free node, its next to old one
        ba      readloop       !get next node
        nop
endinput:
        ret
        restore
        
!===========================================================================
!PRINTTREE
! This routine does an in-order traversal of the tree using recursion.
! It prints the left subtree, its root node, and the right subtree.
!
! Input: %i0 - root of tree
! Output: printed key, value pairs
!
! REGISTER USAGE
! %o0 - Used with I/O
! %L0 - Address of current node
!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
       .section  ".text"
       .align    4
printtree:
        save     %sp,-96,%sp
        mov      %i0,%L0        !root of bstree
        tst      %L0            !is this NULL node 
        beq      alldone        !if so we are done 
        nop
! calling sequence for left tree
        call     printtree      !call print routine
        ld      [%L0+llink],%o0 !delay slot - root of left subtree
!Print the current node
        call    decout          !print key
        ld      [%L0+key],%o0   !delay slot
        call    charout
        mov     space,%o0       !delay slot - put in a space between key and value
        call    decout          !print value
        ld      [%L0+value],%o0 !delay slot
        call    newline
        nop
!calling sequence for right tree
        call    printtree       !call print tree
        ld     [%L0+rlink],%o0  !delay slot - root of right subtree
alldone:
        ret
        restore

!====================================================================
!ADDNODE
!
! This routine adds a new node to the tree.
! It is assumed that the node is not in the tree.  Hence the key of the node
! being added is either < or > the key of the current node, but never the same.
!
! Input: %i0 - root of tree
!        %i1 - address of new node
!        %i2 - key  of new node
! Output: changes to the tree
!
! Register Usage:
! %L0 - Address of the current node of the binary search tree being considered
! %L1 - Address of the node to be added to tree
! %L2 - Key value of the node to be added
! %g1 - Used for holding the key value of the current node of tree during traversal.
!   - Also used to hold address to next node to consider in tree.

! Offsets the same as READTREE
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         .section  ".text"
         .align    4
addnode:
         save    %sp,-96,%sp
         mov     %i0,%L0        !address of tree
         mov     %i1,%L1        !address of free node
         mov     %i2,%L2        !key of node to be added
         cmp     %L0,%L1        !is this the NULL tree
         beq     doneaddnode    !we are done
         nop
chkcurrnode:
         ld      [%L0+key],%g1  !get key of tree node
         cmp     %L2,%g1        !see if equal to current node
    !Remember, the keys cannot be equal
         bg      rightbr        !not equal so we go right 
         nop                    !if tree node smaller
leftbr:                         !left otherwise
         ld      [%L0+llink],%g1 !get address of left child
         tst     %g1            !is it NULL
         bne     goleftbr       !if not go to follow link
         nop
         ba      doneaddnode    !we are done linking
         st      %L1,[%L0+llink] !delay slot - link new node here
goleftbr:
         ba      chkcurrnode    !check out node at top of loop
         mov     %g1,%L0        !delay slot - follow link
rightbr:
         ld      [%L0+rlink],%g1 !get address of right child
         tst     %g1            !is it NULL?
         bne     gorightbr      !if not goto follow link
         nop
         ba      doneaddnode    !we are done linking
         st      %L1,[%L0+rlink] !delay slot - link new node here
gorightbr:
         ba      chkcurrnode    !check out node at top of loop
         mov     %g1,%L0        !delay slot - follow link
   
doneaddnode:   
        ret
        restore     
         
!====================================================================
! MODTREE
! This routine either finds the node to be modified.
! This node must exist.  If it doesn't this routine does nothing
! If the node is found, then the value must be added or subtracted 
! depending on whether %L1 is 'I' or 'D'.
!
! Input: %i0 - root of tree
!        %i1 - operator 'I' or 'D'
!        %i2 - key
! Output: changes to the node values
!
! Register Usage:
! %L0 - root of binary search tree
! %L1 - operator 'I' or 'D'
! %L2 - key of current node
! %L3 - value of current node
!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        .section  ".text"
        .align    4
modtree:
        save      %sp,-96,%sp
        mov       %i0,%L0         !root of tree
        mov       %i1,%L1         !operator 'I' or 'D'
        mov       %i2,%L2         !key of node to be modified
        mov       %i3,%L3	  !value of node
chknode:



   !Complete this routine
   
   
   
doneroutine:
        ret
        restore
!===========================================================================


