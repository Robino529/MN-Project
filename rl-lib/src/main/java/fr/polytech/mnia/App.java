package fr.polytech.mnia;
import de.prob.statespace.State;
import de.prob.statespace.Transition;

public class App 
{
    static MyProb animator = MyProb.INJECTOR.getInstance(MyProb.class);

    private static void SchedulerMainexec() throws Exception{
        animator.load("/scheduler_main.mch");
        State initial = animator.getStateSpace().getRoot() ;
    }
    
    private static void simpleRLexec() throws Exception{
        animator.load("/SimpleRL.mch");
        State initial = animator.getStateSpace().getRoot() ;

        Transition setup = initial.findTransition(Transition.SETUP_CONSTANTS_NAME);
        if (setup != null) {
            initial = setup.getDestination();
        }

        Transition initialisation = initial.findTransition(Transition.INITIALISE_MACHINE_NAME);
        if (initialisation != null) {
            initial = initialisation.getDestination();
        }

        animator.printState(initial) ;
        State state = initial.exploreIfNeeded() ;
        animator.printActions(state.getOutTransitions()) ;
        
        // Here we start the animation
        state = state.perform("choose", "film = A").explore() ;
        animator.printState(state) ;
        System.out.println("Evaluate formula (res = OK) : " + state.eval("res = OK")); 
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = B").explore() ;
        animator.printState(state) ;
        System.out.println("Evaluate formula (res = OK) : " + state.eval("res = OK"));
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = C").explore() ;
        animator.printState(state) ;
        System.out.println("Evaluate formula (res = OK) : " + state.eval("res = OK"));
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = A").explore() ;
        animator.printState(state) ;
        System.out.println("Evaluate formula (res = OK) : " + state.eval("res = OK"));
        animator.printActions(state.getOutTransitions()) ;
    }

    private static void schedulerExec() throws Exception{
        animator.load("/scheduler.mch");
        State initial = animator.getStateSpace().getRoot() ;

        Transition setup = initial.findTransition(Transition.SETUP_CONSTANTS_NAME);
        if (setup != null) {
            initial = setup.getDestination();
        }

        Transition initialisation = initial.findTransition(Transition.INITIALISE_MACHINE_NAME);
        if (initialisation != null) {
            initial = initialisation.getDestination();
        }

        animator.printState(initial) ;
        State state = initial.exploreIfNeeded() ;
        animator.printActions(state.getOutTransitions()) ;
        
        // Here we start the animation      
        
        state = state.perform("new", "pp = process1").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("new", "pp = process2").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("new", "pp = process3").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("ready", "rr = process1").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("ready", "rr = process2").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("ready", "rr = process3").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;
        
       
        animator.printState(((Transition)state.getOutTransitions().get(0)).getDestination()) ;

        animator.printState(((Transition)state.getOutTransitions().get(1)).getDestination()) ;
        

        // Il faut pouvoir revenir en arrière et explorer l'état suivant 

        /*
        state = state.perform("choose", "film = B").explore() ;
        animator.printState(state) ;
        System.out.println("Evaluate formula (res = OK) : " + state.eval("res = OK"));
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = C").explore() ;
        animator.printState(state) ;
        System.out.println("Evaluate formula (res = OK) : " + state.eval("res = OK"));
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = A").explore() ;
        animator.printState(state) ;
        System.out.println("Evaluate formula (res = OK) : " + state.eval("res = OK"));
        animator.printActions(state.getOutTransitions()) ;
        */
    }

    public static void main( String[] args ) throws Exception
    {           
        // simpleRLexec() ;
        // schedulerExec() ;
        SchedulerMainexec() ;
        System.exit(0);
    }
}
