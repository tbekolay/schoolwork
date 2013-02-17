
! Name: 		Agatha Christie
! Student number: 	1234567
! Course:  		74.222 
! Instructor:  		J. van Rees
! Assignment 		3 Question 1

! For each student this program prompts for and reads the
! following integers from the user:
!    - a set of five assignment marks, all assumed to be out of 10
!    - a midterm exam mark, assumed to be out of 100
!    - a final exam mark,  assumed to be out of 100
! It calculates and prints the total mark, out of 100 and rounded off to
! the nearest integer, assuming that the assignments are worth 15%, the
! midterm is worth 20%, and the final is worth 65%.
! formula for final mark is:
! ( (A1+A2+A3+A4+A5)*30+midterm*20+exam*65+fudgefactor )/100
! where fudgefactor is half of divisor

!---------------------------------
!       Standard prologue for code
!---------------------------------

        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-96,%sp !allocate min 96 bytes
        
! ---------------------------------
! 1. Print an identification banner
! ---------------------------------

        set     idinfo,%o0
        call    strout          !Print the banner string (multi-line)
        nop
        
! ---------------------------------
! 2. Read in the input values for one student. Find the sum of 
!    the assignment marks. 
!    When finished, the registers will hold:
!       %L1 - the sum of the 5 assignment marks
!       %L2 - the midterm mark
!       %L3 - the final exam mark
! ---------------------------------

        set     Aprompt,%o0
        call    strout          !Write the assignment prompt
        nop
        
        call    decin	        !Read the 1st assignment
        nop
        mov     %o0,%L1     !Initialize the sum to this value
        call    decin	        !Read the 2nd assignment
        nop
        add     %L1,%o0,%L1     !Add it to the sum
        call    decin	        !Read the 3rd assignment
        nop
        add     %L1,%o0,%L1     !Add it to the sum
        call    decin	        !Read the 4th assignment
        nop
        add     %L1,%o0,%L1     !Add it to the sum
        call    decin	        !Read the 5th assignment
        nop
        add     %L1,%o0,%L1     !Add it to the sum

        set     Mprompt,%o0
        call    strout          !Write the midterm prompt
        nop
        
        call    decin	        !Read the midterm mark
        nop
        add     %g0,%o0,%L2     !Move it to %L2; change to MOV instr.
        
        set     Fprompt,%o0
        call    strout          !Write the final exam prompt
        nop
        
        call    decin	        !Read the final exam mark
        nop
        mov     %o0,%L3     !Move it to %L3

! ---------------------------------
! 3. Calculate the total
!    Register dictionary:
!       %L1 - the sum of the 5 assignment marks
!       %L2 - the midterm mark
!       %L3 - the final exam mark
!       %L4 - the total mark
! ---------------------------------

        smul    %L1,30,%L1      !Multiply assignments by 30
        smul    %L2,20,%L2      !Multiply midterm by 30
        smul    %L3,65,%L3      !Multiply final exam by 30
        add     %g0,50,%L4      !Start with 50 (for rounding)
        add     %L4,%L1,%L4     !Add scaled assignment mark
        add     %L4,%L2,%L4     !Add scaled midterm mark
        add     %L4,%L3,%L4     !Add scaled final exam mark
        sra     %L4,31,%g1      !put 32 bit sign extension in %g1
        mov	%g1,%y          !put sign ext. in %y to protect divide
        sdiv    %L4,100,%L4     !Divide by 100

! ---------------------------------------------
! 4. Print the result (which is in register %L4)
! ---------------------------------------------
        
        set     TotHdg,%o0
        call    strout          !Write the heading
        nop

        add     %L4,0,%o0       !Put the total into %o0 change to MOV
        call	decout		!and print it
        nop
        
        call	newline		!Finish off the line
        nop


alldone:
! ------------------------
! 5. Print a final message
! ------------------------
        
        set     QuitMsg,%o0
        call    strout          !Print a termination message
        nop

!---------------------------------
!       Standard epilogue for code
!---------------------------------

        ret                     !Return to the OS.
        restore                 !Actually done before "ret".
       
! -----------
! The strings
! -----------

        .section ".data"
Aprompt: .asciz '\n\nEnter 5 assignment marks: '
Mprompt: .asciz 'Enter the midterm mark: '
Fprompt: .asciz 'Enter the final exam mark: '
TotHdg:  .asciz '\nThe total mark is '
QuitMsg: .asciz '\nProgram terminated normally.\n'

idinfo: .ascii  'Name: Agatha Chritie\n'
        .ascii  'Student number: 1234567\n'
        .ascii  'Course: 74.222 \n'
        .ascii  'Instructor: J. van Rees\n'
        .ascii  'Assignment: 3\n'
        .ascii  'Question: 1\n'
        .asciz  '--------------------------\n\n'

