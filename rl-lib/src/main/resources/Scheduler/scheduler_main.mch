MACHINE 
    scheduler_main
INCLUDES
    scheduler
PROMOTES
    delete, activate
DEFINITIONS
    SET_PREF_MAXINT == 100000 ;
    SET_PREF_MAX_OPERATIONS == 100 ;
    SET_PREF_INVARIANT_CHECKING == FALSE ;
CONSTANTS
    time 
PROPERTIES
    time : PID --> NAT & 
    time = {
        (process1 |-> 10),
        (process2 |-> 5),
        (process3 |-> 2),
        (process4 |-> 4),
        (process5 |-> 7)
    }
VARIABLES
    tic, waiting_time, execution_time
INVARIANT   
    tic : NAT
    & waiting_time : PID +-> NAT
    & execution_time : PID +-> NAT
INITIALISATION
    tic := 0 ||
    waiting_time := {} ||
    execution_time := {}
OPERATIONS
start(pp) = 
    PRE pp : PID THEN
        new(pp) || waiting_time(pp) := 0 || execution_time(pp) := time(pp)
    END ;
step = 
    BEGIN
        tic := tic + 1 ; 
        execution_time := execution_time <+ {pp, ee | pp : active & ee = execution_time(pp) - 1} ; 
        IF execution_time[active] = {0} THEN
            waiting_time := active <<| waiting_time ; 
            execution_time := active <<| execution_time ;
            swap
        ELSE
            skip
        END ;
        waiting_time := (%pp.(pp : PID | waiting_time(pp) + 1))
    END
END