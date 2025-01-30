MACHINE 
    tictac
VARIABLES  
    square, turn
INVARIANT  
	turn : (0..1)
    & square : ((1..3) * (1..3)) +-> (0..1) 
DEFINITIONS
win(sq, p) == 
    (#x.(x : 1..3 & !y.(y : 1..3 => (x|->y)|->p : sq)))     /* Ligne */
    or (#y.(y : 1..3 & !x.(x : 1..3 => (x|->y)|->p : sq)))  /* Colonne */
    or (!x.(x : 1..3 => (x|->x)|->p : sq))                  /* Diagonale 1 */
    or (!x.(x : 1..3 => (x|->(4-x))|->p : sq));             /* Diagonale 2 */

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


