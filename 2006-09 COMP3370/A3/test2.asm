         MOVE R3,1
         MOVE [R3],0
         MOVE R3,2
         MOVE [R3],3
         MOVE R3,0
         MOVE R4,1
         SRL  R4
         MOVE [R3],R4
         MOVE R0,0
         MOVE R3,0
getnum:  MOVE R2,[R3]
         BNE  R2,getnum
         MOVE R3,3
         MOVE R1,[R3]
         SRL  R1
         MOVE R3,0
         MOVE [R3],R1
         SRR  R1
         MOVE R3,0
getnums: MOVE R2,[R3]
         BNE  R2,getnums
         MOVE R3,4
decrypt: MOVE R4,[R3]
         SUB  R4,47
         MOVE [R3],R4
         ADD  R3,1
         SUB  R1,1
         BGT  R1,decrypt