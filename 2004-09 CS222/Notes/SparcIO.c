/* IO routines for the Sparc      D. Meek  12 Jun 03   */

#include <stdio.h>

static char     null=0x00;    /*string terminator code  */

char charin(){
/*Purpose: read one character from the keyboard                    */
/*Input: from keyboard                                             */
/*Output: 8-bit character                                          */
    return getchar();
}//charin

void charout(char ch){
/*Purpose: read a signed decimal 32-bit integer from the keyboard  */
/*Input: from keyboard                                             */
/*Output: 32-bit value                                             */
    putchar(ch);
}//charout

int decin(){
/*Purpose: read a signed decimal 32-bit integer from the keyboard  */
/*Input: from keyboard                                             */
/*Output: 32-bit value                                             */
    char       ch;
    int        x;
    scanf("%ld",&x);
    ch = getchar(); /*eat up a \n in the input stream*/
    return x;
}/*decin*/
    
void decout(int x){
/*Purpose: write a signed decimal 32-bit integer to the screen     */
/*Input: 32-bit value                                              */
/*Output: the value converted to decimal                           */
    printf("%ld",x);
}/*decout*/

int hexin(){
/*Purpose: read a 32-bit hex value from the keyboard               */
/*Input: from keyboard                                             */
/*Output: 32-bit value                                             */
    char       ch;
    int        x;
    scanf("%lX",&x);
    ch = getchar(); /*eat up a \n in the input stream*/
    return x;
}/*hexin*/
    
void hexout(int x){
/*Purpose: write a 32-bit integer in hex to the screen             */
/*Input: 32-bit value                                              */
/*Output: value in hexadecimal                                     */
    printf("%08lX",x);
}/*hexout*/

void newline(){
/*Purpose: newline on the screen                                   */
/*Input: none                                                      */
/*Output: new line                                                 */
   printf("\n");
}/*newline*/
    
void memdump(char* ptr,int nobytes){
/*Purpose: dump nobytes of memory starting at address ptr          */
/*Input: starting address, number of bytes                         */
/*Output: part of memory, address followed by 16 bytes per line    */

   const int  bytesperline=16; /*bytes per line*/
   int      i; //index for bytes
   int      line,nolines; /*index for lines, number of full lines*/
   char*    baseaddr; /*address of the start of each line*/
   int		remain; /*number of bytes in last partial line*/
   
   if(nobytes>1024) nobytes = 1024; /*limit output*/
   nolines = nobytes/bytesperline;
   for(line=0;line<nolines;line++){
      baseaddr = ptr+line*bytesperline;
      printf("%08lX: ",baseaddr); /*show address*/
      for(i=0;i<bytesperline;i++){
         printf("%02X",*(baseaddr+i));
         if(i%2==1) printf(" ");
         if(i%4==3) printf(" ");
      }//for
      printf("\n");
   }//for
   remain = nobytes-nolines*bytesperline;
   if(remain==0) return;
   baseaddr = ptr+nolines*bytesperline;
   printf("%08lX: ",baseaddr); /*show address*/
   for(i=0;i<remain;i++){
      printf("%02X",*(baseaddr+i));
      if(i%2==1) printf(" ");
      if(i%4==3) printf(" ");
   }//for
   printf("\n");
}/*memdump*/

void strin(char *buf){
/*Purpose: read a string from the keyboard terminated by a <return> key */
/*Input: string of valid characters (0x20 to 0x7E inclusive)            */
/*Output: the string in the caller's data area terminated by a null     */
   char       ch;
   ch = getchar();
   while(ch!='\n'){
      if(ch<0x20||0x7E<ch) printf("\nstrin: illegal character\n");
      *buf++ = ch;
      ch = getchar();
   }/*while*/
   *buf = null;
}/*strin*/
   
void strout(char *s){
/*Purpose: write a string to the screen                            */
/*Input: string pointer                                            */
/*Output: the string on the screen                                 */
/*Note: limit the string to 200 characters in case the user forgot the null terminator*/
   char        ch;
   int         i;
   for(i=0;i<200;i++){
      ch = *s++;
      if(ch!=null) printf("%c",ch);
      else break;
    }/*for*/
   if(i>=200){
      printf("\nstrout: string longer than 200 characters, maybe you forgot the terminating null\n");
   }/*if*/
}/*strout*/

