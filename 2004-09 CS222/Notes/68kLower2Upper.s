*Example program for 68000   D. Meek 22 Apr 03
*****************************************************************
* NAME              JANE SMITH
* STUDENT NUMBER    1234567
* COURSE            74.222
* INSTRUCTOR        Dereck Meek
* ASSIGNMENT        1
* QUESTION          4
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
null      equ       $00          null string terminator
lf        equ       $0A          linefeed
cr        equ       $0D          carriage return
*****************************************************************
* your program starts here        
*****************************************************************
* This reads a string and changes all the lower case letters
* to upper case letters. Other characters are copied
*****************************************************************
*
* Register Usage
*     a1 - pointer in string
mask      equ       %11011111           mask for bit 5

start     jsr       initIO              initialize I/O
*Print Identification
          lea       IDBanner,a0         address of IDBanner
          jsr       strout
          
* read and echo string
          lea       prompt,a0
          jsr       strout
          lea       string,a0
          jsr       strin
          lea       mess1,a0
          jsr       strout
          lea       string,a0
          jsr       strout
          jsr       newline
* run through string converting lower to upper          
          lea       string,a1
          bra       looptest
loop
* translation of the following if statememt
*   if('a'<=ch &&ch<='z') ch = ch & 0xDF
          cmpi.b    #'a',(a1)
          blt       notlower            branch if (a1) < 'a'
          cmpi.b    #'z',(a1)
          bgt       notlower            branch if (a1) > 'a'
          andi.b    #mask,(a1)          do conversion
notlower     
          adda.l    #1,a1               move to next character
looptest
          tst.b     (a1)
          bne       loop
loopend
* print converted string
          lea       mess2,a0
          jsr       strout
          lea       string,a0
          jsr       strout
          jsr       newline
* end of processing
          lea       EOP,a0              address of message
          jsr       strout
          jsr       finish              end of execution
*****************************************************************
* your data section        
*****************************************************************
IDBanner  dc.b      'NAME               Jane Smith',cr,lf
          dc.b      'STUDENT NUMBER     1234567',cr,lf
          dc.b      'COURSE             74.222',cr,lf
          dc.b      'INSTRUCTOR         Dereck Meek',cr,lf
          dc.b      'ASSIGNMENT 1',cr,lf
          dc.b      'QUESTION 4',cr,lf,cr,lf
          dc.b      null                Banner string terminator
prompt    dc.b      'Enter a string to convert.',cr,lf,null
string    ds.b      300
mess1     dc.b      'Original string is: ',null
mess2     dc.b      ' Changed string is: ',null
EOP       dc.b      cr,lf,'End of Processing',cr,lf,null
*****************************************************************
* your program ends here        
*****************************************************************
          include   68kIO.s             Input/Output routines
*****************************************************************
          end