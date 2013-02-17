* File: 68kintpal.s
	
	
* This program will get its data, long integers 
* from the array data, and then indicate whether or not
* each is a palindromic integer. (A palindromic
* integer has digits which read the same in both
* directions, such as 1384831. The number must be positive and must
* fit in 16 bits after 1st division by 10 (ie, must be <=655359)


*------------------------------------------------        
* Register usage:
* d0,a0 - the usual
* d3 - the long integer entered by the user
* a1 - point to the current element in array to be used as data
*-------------------------------------------------
start:  jsr	initIO
	lea	data,a1		;point to start of data array
	lea     heading,a0	;print out heading
	jsr     strout		;print it
	jsr	newline
MainLoop:
        move.l  (a1)+,d0	; read in current input value
	jsr	decout		; echo input right away
	jsr	newline		; start newline
	
        move.l  d0,d3           ;put it in d3
        beq     AllDone         ;if 0, quit the program
        
        lea     Head,a0         ;address of heading message
        jsr     strout          ;print the heading
        
        move.l  d3,d0           ;put the integerback in d0
        jsr     decout	        ;and print it

*;-------------------------
*; Determine whetheror not the number in d3
*; is a palindromic number.
*; Register use:
*;   d1- contains quotient and remainder of shrinking n after division by 10
*;   d2- contains shrinking n
*;   d3- contains original number
*;   d4- is used to construct righmost digit
*;   d5- is used to construct reversed number 

        move.l  d3,d2           ;make of copy of the integer in d2.
        clr.l   d5              ;clear the reversed copy.
        bra     checkPt         ;see if done
        move.l  d2,d1           ;put d2 into di ready for division
revLoop:
        divu    #10,d1	        ;divide shrinking n by 10
        move.w  d1,d2           ;put quotient(=shrinking n) into d2

*zero out top 16 bits of d2 (only required if original input > 65535)		
	and.l   #$FFFF,d2	

        swap    d1              ;get remainder (= units digit)ready
        move.w  d1,d4           ;and put in d4
        mulu    #10,d5          ;multiply reversed number by 10
        add.w   d4,d5           ;and add on units digit

checkPt:
        clr.l   d1              ;ready di for division 
        move.l  d2,d1           ;put in shrinking n
        cmp.l   #0,d2           ;if d2 is 0 we are done reversing
                   
        bne     revLoop         ;if not move another digit over

*;-------------------------
*; Check to see if there is a palindrome there.

        cmp.l   d3,d5           ;if original<>reversed copy
        bne     NoItIsnt        ;then it is not a palindrome
YesItIs:
        lea     YesMsg,a0       ;address of answer message
        jsr     strout          ;print the answer
        bra     EndIf
NoItIsnt:
        lea     NoMsg,a0        ;address of answer message
        jsr     strout          ;print the answer
EndIf:
        jsr     newline         ;end the output line
        jsr     newline         ;and leave a blank line, too.
        
        clr.l   d5              ;clear reversed number register
        jmp     MainLoop        ;Do it all again
        
*;-------------------------
*;Finish the program

AllDone:
        lea     EndMsg,a0       ;print a final message
        jsr     strout
        jsr	newline

	jsr	finish			
        
*;=========================


        

heading: 	dc.b	'A list of integers',null
        
Head:           dc.b    'The integer ',null

YesMsg:         dc.b    ' is palindromic.',null

NoMsg:          dc.b    ' is NOT palindromic.',null

EndMsg:         dc.b    'End of Processing.',null

		DS.W	0	; this makes sure next address is even

data:		dc.l	400004,456654,12345,99997,4554,54145,12321,12312,0
	
		include 68kIO.s
		end
	

		
