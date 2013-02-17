        MOVE R0,0
        MOVE R1,0
        MOVE R2,1
        MOVE R5,0
        MOVE R3,[R1]
        MOVE R4,[R2]
        BEQ  R4,done
loop:   ADD  R5,R3
        BNE  R4,loop
done:   MOVE [R1],R5

        
