******************************************************************
*Start of 68kIO.s file     D. Meek  22 Apr 03
******************************************************************
*Recent changes
*  13 Jun 03: added charin and charout
*  22 Apr 03: added initIO, finish, zzBusErr, zzAddErr, zzIllIns, zzDivZer
*  11 May 02: changed and.b to and.l in zzstr2dec routine
*   1 Mar 02: changed the format of name and alignment on routines
*  20 Feb 02: on input, go the the next line on <return>
*  20 Feb 02: allow strings of length 400 on input
*********************************************************************
*
* I/O routines plus initIO, finish, and interrupt routines
*
* The following are the routines you are most likely to use
*
*charin: ascii character input from keyboard into d0.
*
*charout: ascii character output to screen from d0
*
*decin: signed long decimal input from keyboard into d0.
*   Input terminated by a <return> key press
*
*decout: signed long decimal output to screen from d0 using 11 spaces.
*   Stays on the same line after output.
*
*finish: terminate execution
*
*hexin: (up to) 8-digit long hex number from keyboard into d0.
*   Input terminated by a <return> key press
*
*hexout: 8-digit long hex output to screen from d0. Stays on same line
*   after output.
*
*initIO: initialize bytes for Input/Output
*
*newline: output a newline command to the screen (carriage return, linefeed)
*
*strin: any length (up to 400 characters) string of valid characters
*   ($20 to $7E inclusive) input from keyboard to area pointed at by a0.
*   Input terminated by a <return> key press.
*
*strout: any length (up to 400 characters) string output to screen.
*   Stays on same line after output.
*
* The routines starting with zz are internal, but can be used if you wish
*
* zzputbyte: output one byte
* zzgetbyte: input one byte
* zzputstr: output a string
* zzgetstr: input a string (<return> terminates the string)
* zzdec2str: convert 32-bit integer to string (decimal)
* zzhex2str: convert 32-bit long to string (hexadecimal)
* zzstr2dec: convert decimal string to 32-bit integer
* zzstr2hex: convert hex string to 32-bit integer
* zzputeol: output an end-of-line
* zzbell: sound the bell
* zzBusErr: bus error interrupt (e.g. address out of address space)
* zzAddErr: address error interrupt (e.g. odd address when an even one is required)
* zzIllIns: illegal instruction interrupt (e.g. use of the illegal instruction)
* zzDivZer: divide by zero interrupt (attempt to divide by zero)
*
* All routine names and labels start with zz so they are easily
*   distinguished from other labels.
*
* equates for special characters
*
zznull    equ       $00       null (for terminating strings)
zzbe      equ       $07       bell
zzbs      equ       $08       backspace
zzlf      equ       $0A       linefeed (new line \n)
zzcr      equ       $0D       carriage return (\r)
*
* equate for string length on output
zzmaxstrlen   equ   400       maximum string length
*
*********************************************************************
*charin
*Purpose: get an ascii character from keyboard
*Input: from keyboard
*Output: character code in lowest byte of d0
          ds.w      0                   align on word boundary
charin    jsr zzgetbyte
          rts
*********************************************************************
*charout
*Purpose: put an ascii character to the screen
*Input: lowest byte of d0
*Output: character displayed on screen
          ds.w      0                   align on word boundary
charout   jsr zzputbyte
          rts
*********************************************************************
*decin
*Purpose: get a signed decimal integer from keyboard
*Input: from keyboard
*Output: number in binary in d0
          ds.w      0                   align on word boundary
decin     movem.l   a0-a6/d1-d7,-(a7)   save registers
          lea       zzdinum,a0          input string
          move.l    #12,d0              max length of string
          jsr       zzgetstr
          jsr       zzstr2dec
          movem.l   (a7)+,a0-a6/d1-d7   restore registers
          rts
*
zzdinum   dcb.b     12,zznull           input string
*
*********************************************************************
*decout
*Purpose: output d0 in decimal, stay on same line
*Input: d0
*Output: the value in d0 converted to decimal
          ds.w      0                   align on word boundary
decout    movem.l   a0-a6/d0-d7,-(a7)   save all registers
          lea       zzdonum,a0          output string
          jsr       zzdec2str
          jsr       zzputstr
          movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
*
zzdonum   dcb.b    12,zznull
*
*********************************************************************
*finish
*Purpose: flush the output buffer and stop a program
*Input: none
*Output: none
          ds.w      0                   align on word boundary
finish    jsr       newline             flush output buffer
          break
*********************************************************************
*hexin
*Purpose: get a hex integer from keyboard
*Input: from keyboard
*Output: number in binary in d0
          ds.w      0                   align on word boundary
hexin     movem.l   a0-a6/d1-d7,-(a7)   save registers
          lea       zzhinum,a0          input string
          move.l    #9,d0               max length of string
          jsr       zzgetstr
          jsr       zzstr2hex
          movem.l   (a7)+,a0-a6/d1-d7   restore registers
          rts
*
zzhinum   dcb.b     9,zznull            input string
*********************************************************************
*hexout
*Purpose: output d0 in hexadecimal, stay on same line
*Input: d0
*Output: the value in d0 in hexadecimal
          ds.w      0                   align on word boundary
hexout    movem.l   a0-a6/d0-d7,-(a7)   save all registers
          lea       zzhonum,a0          output string
          jsr       zzhex2str
          jsr       zzputstr
          movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
*
zzhonum   dcb.b    12,zznull
*
*********************************************************************
*initIO
*Purpose: initialize Input/Output
*Input: none
*Output: sets some flags 
          ds.w      0                   align on word boundary
initIO    lea       zzduart,a1
          move.b    #%00010000,zzcra(a1)     Reset MR?A pointer
          move.b    #%00100011,zzmr1a(a1)    8 data bits
          move.b    #%00010111,zzmr2a(a1)    Normal Mode
          move.b    #%10111011,zzcsra(a1)    Set clock to 9600
          move.b    #%00000101,zzcra(a1)     Enable Rx and Tx
          rts
*
*********************************************************************
*newline
*Purpose: go to a new line
*Input: none
*Output: move the cursor to the left end of the next line 
          ds.w      0                   align on word boundary
newline   jsr       zzputeol
          rts
*
*********************************************************************
*strin
*Purpose: get a string from keyboard
*Input: from keyboard
*Output: a null-terminated string starting at the address in a0
          ds.w      0                   align on word boundary
strin     movem.l   a0-a6/d0-d7,-(a7)   save all registers
          move.w    #zzmaxstrlen,d0     maximum string length
          jsr       zzgetstr
          movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
*
*********************************************************************
*strout
*Purpose: output a null-terminated string, stays on the same line
*Input: a0 points to the start of the string
*Output: the stirng on the screen
          ds.w      0                   align on word boundary
strout    movem.l   a0-a6/d0-d7,-(a7)   save all registers
          jsr       zzputstr
          movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
*
*********************************************************************
zzduart   equ       $effc01
zzmr1a    equ       $00
zzmr2a    equ       $00
zzsra     equ       $02
zzcsra    equ       $02
zzcra     equ       $04
zztba     equ       $06       transmit byte
zzrba     equ       $06       receive byte
*
*********************************************************************
*zzgetbyte
*Purpose: get one byte from keyboard
*Input: from keyboard
*Output: byte in lower end of d0
          ds.w      0                   align on word boundary
zzgetbyte btst      #0,zzsra+zzduart    test if receive byte is ready
          beq       zzgetbyte           if not ready, keep polling
          move.b    zzrba+zzduart,d0    input byte
          rts
*
*********************************************************************
*zzputbyte
*Purpose: put one byte to the screen
*Input: byte in lower end of d0
*Output: byte on the screen
          ds.w      0                   align on word boundary
zzputbyte btst      #2,zzsra+zzduart    test if transmit byte is ready
          beq       zzputbyte           if not ready, keep polling
          move.b    d0,zztba+zzduart    output byte
          rts
*
**********************************************************************
*zzputstr
*Purpose: output a null-terminated string to the screen
*Input: a0 points to beginning of string
*Output: a string on the screen (minus the terminating null byte)
*Note: the number of bytes in the string is limited to zzmaxstrlen bytes.
*   This will catch strings where the null terminator was forgotten
          ds.w      0                   align on word boundary
zzputstr  movem.l   a0-a6/d0-d7,-(a7)   save all registers
          move.w    #zzmaxstrlen,d1     byte counter
zzputloop move.b    (a0)+,d0       
          beq       zzputend            stop at null byte
          jsr       zzputbyte           output byte
          sub.w     #1,d1
          ble       zzputerr            too many bytes
          bra       zzputloop
zzputend  movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
*         
zzputerr  jsr       zzbell              error message
          jsr       zzputeol
          lea       zzputmess,a0  
          jsr       zzputstr
          jsr       zzputeol
          bra       zzputend
zzputmess dc.b      'zzputstr: String is too long, check terminating null',zznull
*
**********************************************************************
*zzgetstr
*Purpose: input a string
*Input: a string terminated by a <return> from the keyboard
*    a0 points at the buffer receiving the string
*    lower word of d0 has the maximum length (including terminating null), 0 < d0 < zzmaxstrlen
*Output: a null-terminated string starting at address in a0
*Note: the input is checked for valid ascii bytes in range $20 to $7E inclusive
          ds.w      0                   align on word boundary
zzgetstr  movem.l   a0-a6/d0-d7,-(a7)   save all registers
          cmp.w     #0,d0               0 < d0?
          blt       zzgeterr
          cmp.w     #zzmaxstrlen,d0     d0 < zzmaxstrlen?
          bgt       zzgeterr
          move.w    #0,d1               current string length in d1
          move.w    d0,d2               save max length in d2
zzgetloop jsr       zzgetbyte           main input loop
          cmp.b     #zzbs,d0            backspace?
          beq       zzgetbs   
          cmp.b     #zzcr,d0            carriage return? Use #zzlf for UNIX?
          beq       zzgetend
          cmp.b     #$20,d0             below $20?
          blt       zzgetinv
          cmp.b     #$7E,d0             above $7E?
          bgt       zzgetinv
          cmp.w     d2,d1               reached max length yet?
          bge       zzgetinv
          move.b    d0,0(a0,d1)         put byte in buffer
          add.w     #1,d1
          jsr       zzputbyte
          bra       zzgetloop 
zzgetinv  jsr       zzbell              invalid input
          bra       zzgetloop
zzgetbs   cmp.w     #0,d1               backspace, is it possible?
          ble       zzgetinv
          move.b    #zzbs,d0            do the backspace
          jsr       zzputbyte
          move.b    #' ',d0
          jsr       zzputbyte
          move.b    #zzbs,d0
          jsr       zzputbyte
          sub.w     #1,d1               decrease byte counter
          bra       zzgetloop
zzgetend  move.b    #zznull,0(a0,d1)    string terminator
          jsr       zzputeol
          movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
          
zzgeterr  jsr       zzbell              error message
          jsr       zzputeol
          lea       zzgetmes,a0
          jsr       zzputstr
          jsr       zzputeol
          bra       zzgetend
*
zzgetmes  dc.b      'zzgetstr: Illegal max length of string, must be 1 to 400 bytes',zznull
*
***********************************************************************
*zzdec2str
*Purpose: convert 32-bit integer to a decimal string
*Input: 32-bit signed integer in d0
*    a0 pointing to where result string should go in the callers program
*Output: null-terminated string of up to 12 bytes pointed to by a0
*    the string is left justified in the field of 12 bytes
*Note: leave space for 12 bytes as that is the max length string
*    (including terminating null)
          ds.w      0                   align on word boundary
zzdec2str movem.l   a0-a6/d0-d7,-(a7)   save all registers
          lea       zzdsbend,a1         right end of number field
          move.b    #0,zzdssgn
          cmp.l     #0,d0               is d0 positive?
          bgt       zzdsloop            positive int
          blt       zzdsne              negative int
          move.b    #'0',-(a1)          zero int
          bra       zzdscpy
zzdsne    move.b    #1,zzdssgn          indicate negative
          neg.l     d0
zzdsloop  jsr       zzdiv10             main loop, divide d0 by 10
          add.w     #'0',d0             convert remainder to character
          move.b    d0,-(a1)
          move.l    d1,d0               quotient to d0
          bne       zzdsloop
          cmp.b     #0,zzdssgn
          beq       zzdscpy
          move.b    #'-',-(a1)
zzdscpy   move.b    (a1)+,(a0)+
          bne       zzdscpy             stop the copy after moving a null
zzdsend   movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
*
zzdsbuff  dcb.b     11,zznull           ascii version before copying to caller
zzdsbend  dc        zznull              terminating null
zzdssgn   ds.b      1                   sign, 0=positive, 1=negative
*
*zzdiv10
*Purpose: divide a positive 32-bit integer dividend by 10
*Input: dividend in d0
*Output: remainder in lower word of d0, quotient (32-bits) in d1

zzdivisor equ       10
          ds.w      0                   align on word boundary
zzdiv10   move.l    d0,d1               copy dividend = n0 | n1
          clr.w     d0                  d0 = n0 | 0
          swap      d0                  d0 =  0 | n0
          divu      #zzdivisor,d0       d0 = r0 | q0
          swap      d1                  d1 = n1 | n0
          move.w    d0,d1               d1 = n1 | q0
          swap      d1                  d1 = q0 | n1
          move.w    d1,d0               d0 = r0 | n1
          divu      #zzdivisor,d0       d0 = r1 | q1
          move.w    d0,d1               d1 = q0 | q1
          clr.w     d0                  d0 = r1 | 0
          swap      d0                  d0 =  0 | r1
          rts
*
***********************************************************************
*zzhex2str
*Purpose: convert 32-bit long to a hexadecimal string
*Input: 32-bit value in d0
*    a0 pointing to where result string should go in the callers program
*Output: null-terminated string of 9 bytes pointed to by a0
*Note: leave space for 9 bytes as that is the length of the output string
*    (including terminating null)
          ds.w      0                   align on word boundary
zzhex2str movem.l   a0-a6/d0-d7,-(a7)   save all registers
          move.l    a0,a1               save pointer
          adda.l    #9,a0
          move.b    #zznull,-(a0)       terminating null
          move.l    d0,d1               save d0 in d1
zzhsloop  cmp.l     a0,a1
          bge       zzhsend
          and.b     #$0F,d0
          jsr       zzhexdig
          move.b    d0,-(a0)
          move.l    d1,d0
          and.w     #$00F0,d0
          lsr.l     #4,d0               remove lower hex digit
          jsr       zzhexdig
          move.b    d0,-(a0)
          lsr.l     #8,d1               remove lower byte of d1
          move.l    d1,d0               put back in d0
          bra       zzhsloop
zzhsend   movem.l   (a7)+,a0-a6/d0-d7   restore all registers
          rts
*
*zzhexdig
*Purpose: convert a hex digit to ascii equivalent
*Input: a hex digit in the lower byte of d0
*Output: the ascii equivalent in the lower byte of d0
          ds.w      0                   align on word boundary
zzhexdig  cmp.b       #$00,d0           check size of hex digit
          blt         zzhderr
          cmp.b       #$0F,d0
          bgt         zzhderr
          cmp.b       #$0A,d0           decimal digit or letter?
          bge         zzhdlet
          add.b       #'0',d0           decimal digit
          rts
zzhdlet   add.b       #('A'-$0A),d0     letter
zzhdend   rts
*
zzhderr   jsr       zzbell              error message
          jsr       zzputeol
          lea       zzhdmes,a0
          jsr       zzputstr
          jsr       zzputeol
          bra       zzhdend
*
zzhdmes   dc.b      'zzhex2dec: Illegal hex digit',zznull
*
***************************************************************
*zzstr2dec
*Purpose: convert decimal string to 32-bit integer
*Input: a0 points to string
*Output: binary result in d0
          ds.w      0                   align on word boundary
zzstr2dec movem.l   a0-a6/d1-d7,-(a7)   save registers
          move.l    #0,d0
          move.b    (a0)+,d1            get first byte
          beq       zzsdend             nothing there
          move.b    #0,zzsdsgn
          cmp.b     #'-',d1             is number positive?
          bne       zzsdloop            number is positive
          move.b    #1,zzsdsgn          number is negative
          move.b    (a0)+,d1
zzsdloop  cmp.b     #zznull,d1          is byte null?
          beq       zzsdsn
          cmp.b     #'0',d1             '0' <= byte?
          blt       zzsderr1
          cmp.b     #'9',d1             byte <= '9'?
          bgt       zzsderr1
          and.l     #$0F,d1             zero upper bits (changed 11 June 2002 Ben Li)
* multiply by 10 to make room for new digit, do not use muls because it has 16-bit operands
          move.l    d0,d2
          lsl.l     #1,d2
          bcs       zzsderr2            check for overflow
          move.l    d2,d0               d0 <- 2*d0
          lsl.l     #1,d2
          bcs       zzsderr2            check for overflow
          lsl.l     #1,d2
          bcs       zzsderr2
          add.l     d2,d0               d0 <- (8+2)*d0
          add.l     d1,d0               add in new digit
          move.b    (a0)+,d1            get digit
          bra       zzsdloop
zzsdsn    cmp.b     #0,zzsdsgn          positive?
          beq       zzsdend
          neg.l     d0
zzsdend   movem.l   (a7)+,a0-a6/d1-d7   restore registers
          rts
*
zzsderr1  jsr       zzbell              error message 1
          jsr       zzputeol
          lea       zzsdmes1,a0
          jsr       zzputstr
          jsr       zzputeol
          bra       zzsdend
zzsderr2  jsr       zzbell              error message 2
          jsr       zzputeol
          lea       zzsdmes2,a0
          jsr       zzputstr
          jsr       zzputeol
          bra       zzsdend
*
zzsdmes1  dc.b      'zzstr2dec: Illegal character in decimal number',zznull
zzsdmes2  dc.b      'zzstr2dec: Number too large for 32-bit integer',zznull
zzsdsgn   ds.b      1                   sign, 0=positive, 1=negative
*
***************************************************************
*zzstr2hex
*Purpose: convert hex string to 32-bit integer
*Input: a0 points to string
*Output: binary result in d0
          ds.w      0                   align on word boundary
zzstr2hex movem.l   a0-a6/d1-d7,-(a7)   save registers
          move.l    #0,d0               start with zero
          move.l    #0,d1               clear d1
          move.b    (a0)+,d1            get first byte
          beq       zzshend             nothing there
zzshloop  cmp.b     #zznull,d1          is byte null?
          beq       zzshend
          cmp.b     #'0',d1             try between '0' and '9'
          blt       zzsherr1
          cmp.b     #'9',d1
          bgt       zzshAF1
          and.b     #$0F,d1             digit in d1
          bra       zzshcont
zzshAF1   cmp.b     #'A',d1             try between 'A' and 'F'
          blt       zzsherr1
          cmp.b     #'F',d1
          bgt       zzshaf2
          sub.l     #'A',d1
          add.l     #10,d1              digit in d1
          bra       zzshcont
zzshaf2   cmp.b     #'a',d1             try between 'a' and 'f'
          blt       zzsherr1
          cmp.b     #'f',d1
          bgt       zzsherr1
          sub.l     #'a',d1
          add.l     #10,d1
* assume the digit is in d1   
zzshcont  lsl.l     #1,d0               make room for new digit
          bcs       zzsherr2            branch C set, shifted a 1 bit out
          lsl.l     #1,d0
          bcs       zzsherr2
          lsl.l     #1,d0
          bcs       zzsherr2
          lsl.l     #1,d0
          bcs       zzsherr2
          or.l      d1,d0               add in new digit
          move.b    (a0)+,d1            get digit
          bra       zzshloop
zzshend   movem.l   (a7)+,a0-a6/d1-d7   restore registers
          rts
*
zzsherr1  jsr       zzbell              error message 1
          jsr       zzputeol
          lea       zzshmes1,a0
          jsr       zzputstr
          jsr       zzputeol
          bra       zzshend
zzsherr2  jsr       zzbell              error message 2
          jsr       zzputeol
          lea       zzshmes2,a0
          jsr       zzputstr
          jsr       zzputeol
          bra       zzshend
*
zzshmes1  dc.b      'zzstr2hex: Illegal character in hex number',zznull
zzshmes2  dc.b      'zzstr2hex: Number too large for 32-bit integer',zznull
*
**********************************************************************
*zzputeol
*Purpose: output an end-of-line
*Input: none
*Output: linefeed byte
          ds.w      0                   align on word boundary
zzputeol  move.l    d0,-(a7)            save d0
          move.b    #zzcr,d0
          jsr       zzputbyte
          move.b    #zzlf,d0
          jsr       zzputbyte
          move.l    (a7)+,d0            restore d0
          rts
*
**********************************************************************
*zzbell
*Purpose: sound the bell
*Input: none
*Output: bell sound
          ds.w      0                   align on word boundary
zzbell    move.l    d0,-(a7)            save d0
          move.b    #zzbe,d0
          jsr       zzputbyte
          move.l    (a7)+,d0            restore d0
          rts
*
**********************************************************************
*zzBusErr
*Purpose: report a bus error interrupt
*Input: none
*Output: address near instruction that caused it
          ds.w      0                   align on word boundary
zzBusErr
          lea       zzBEMess,a0
          jsr       strout
          move.w    10(sp),d0
          swap      d0
          move.w    12(sp),d0
          jsr       hexout
          jsr       newline
          break
zzBEmess  dc.b      'bus error just before address ',null
*
**********************************************************************
*zzAddErr
*Purpose: report an address error interrupt
*Input: none
*Output: address near instruction that caused it
          ds.w      0                   align on word boundary
zzAddErr
          lea       zzAEmess,a0
          jsr       strout
          move.w    10(sp),d0
          swap      d0
          move.w    12(sp),d0
          jsr       hexout
          jsr       newline
          break
zzAEmess  dc.b      'address error just before address ',null
*
**********************************************************************
*zzIllIns
*Purpose: report an illegal instruction interrupt
*Input: none
*Output: address near instruction that caused it
          ds.w      0                   align on word boundary
zzIllIns
          lea       zzIImess,a0
          jsr       strout
          move.w    2(sp),d0
          swap      d0
          move.w    4(sp),d0
          jsr       hexout
          jsr       newline
          break
zzIImess  dc.b      'illegal instruction error at address ',null
*
**********************************************************************
*zzDivZer
*Purpose: report a divide by zero interrupt
*Input: none
*Output: address near the instruction that caused it
          ds.w      0                   align on word boundary
zzDivZer
          lea       zzDZmess,a0
          jsr       strout
          move.w    2(sp),d0
          swap      d0
          move.w    4(sp),d0
          jsr       hexout
          jsr       newline
          break
zzDZmess  dc.b      'divide by zero error just before address ',null
*
******************************************************************
*End of 68kIO.s file
******************************************************************
