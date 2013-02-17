!This example show how to declare local variables
!and pass arguments on the stack.
	
!========== Standard prologue for code ===================
        .section    ".text"
        .global     main
        .align      4
main:   save        %sp,-104,%sp !allocate min 96 bytes
!++++++++++++++++++++=+++++++++++++++++++++++++++++++++++++++++
!
! main has eight parameters
! Two go on stack
!
        mov     33,%L0			
        mov     10,%o0			!arg1=10
        mov     1,%o1			!arg2=1
        mov     2,%o2			!arg3=2
        mov     3,%o3			!arg4=3
        mov     4,%o4			!arg5=4
        mov     5,%o5			!arg6=5
        st      %L0,[%sp+92]		!arg7=33
        st      %L0,[%sp+96]		!arg8=33
        call    routine			!call routine
        nop
	ret
	restore
	

! routine has 3 local variables say x,y,z
!
	
routine: save        %sp,-104,%sp	!space for 3 (12 bytes) local words
         ld          [%fp+92],%L1	!get the 7th parameter
         ld          [%fp+96],%L2	!get the 8th parameter
         mov         39,%L3
         st          %L3,[%fp-4]	!store 39 into x
         mov         40,%L3
         st          %L3,[%fp-8]	!store 40 into y
         mov         41,%L3
         st          %L3,[%fp-12]	!store 41 into z
         ret
         restore

