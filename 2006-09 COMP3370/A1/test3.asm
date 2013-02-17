        MOVE R0,7
        MOVE R1,0
        MOVE R2,1
        MOVE R5,0
        MOVE R3,[R1]
        MOVE R4,[R2]
        BEQ  R4,done
        ADD  R5,R3
        SUB  R4,1
        JR   R0
done:   MOVE [R1],R5

        
