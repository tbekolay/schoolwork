        MOVE R0,0
        MOVE R1,0
        MOVE R5,0
        MOVE R3,-1
        MOVE R4,9
        BEQ  R4,done
loop:   ADD  R5,R3
        SUB  R4,1
        BNE  R4,loop
done:   MOVE [R1],R5
        MOVE R4,R2
        ADD  R1,1
        MOVE [R1],R5

        
