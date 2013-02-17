        MOVE R0,30
        MOVE R1,0
        MOVE R2,0
        MOVE R3,1
        MOVE [R1],R2
        ADD  R1,1
        MOVE R5,2
loop:   MOVE [R1],R3
        ADD  R1,1
        MOVE R4,0
        OR   R4,R3
        ADD  R3,R2
        MOVE R2,0
        OR   R2,R4
        ADD  R5,1
        BLT  R5,loop
