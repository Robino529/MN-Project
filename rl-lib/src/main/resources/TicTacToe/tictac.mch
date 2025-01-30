MACHINE 
    tictac
VARIABLES  
    square, turn
INVARIANT  
	turn : (0..1)
    & square : ((1..3) * (1..3)) +-> (0..1) 
DEFINITIONS
win(sq, p) == 
    (#x.(x : 1..3 & ( (x|->1)|->p : sq & (x|->2)|->p : sq & (x|->3)|->p : sq )))    /* Ligne */
    or (#y.(y : 1..3 & ( (1|->y)|->p : sq & (2|->y)|->p : sq & (3|->y)|->p : sq ))) /* Colonne */
    or ( (1|->1)|->p : sq & (2|->2)|->p : sq & (3|->3)|->p : sq )                   /* Diagonale 1 */
    or ( (1|->3)|->p : sq & (2|->2)|->p & (3|->1)|->p : sq ) ;                      /* Diagonale 2 */
INITIALISATION 
    turn,square := 0,{}
OPERATIONS
place0(xx,yy) =
	PRE 
		turn = 0 &
		xx:(1..3) & yy:(1..3) &
		xx|->yy /: dom(square)
	THEN 
		square(xx|->yy) := 0 ||
		turn := 1
	END ;
place1(xx,yy) =
	PRE
		turn = 1 &
		xx:(1..3) & yy:(1..3) &
		xx|->yy /: dom(square)
	THEN 
		square(xx|->yy) := 1 ||
		turn := 0
	END
END


