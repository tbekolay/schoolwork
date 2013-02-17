         MOVE R2,3
         MOVE R3,[R2]
         MOVE R0,0
         MOVE R4,4
encrypt: MOVE R5,[R4]
         ADD  R5,47
         MOVE [R4],R5
         ADD  R4,1
         SUB  R3,1
         BGT  R3,encrypt
         MOVE R3,1
         MOVE [R3],0
         MOVE R3,2
         MOVE [R3],3
         MOVE R1,3
         MOVE R2,[R1]
         ADD  R2,1
         SRL  R2
         OR   R2,1
         MOVE R3,0
         MOVE [R3],R2
         MOVE R0,1
loop:    MOVE R2,[R3]
         BNE  R2,loop