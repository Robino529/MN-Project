package fr.polytech.mnia;

import de.prob.statespace.State;
import de.prob.statespace.Transition;

/*
 * Cette classe illustre l'exécution de SimpleRL.mch
 */
public class SimpleRunner {
    private MyProb animator = MyProb.INJECTOR.getInstance(MyProb.class);
    private State initial ; // état initial
    private State state ;   // ce champ représente l'état courant

    /*
     * Le constructeur lance ProB sur la machine SimpleRL.mch
     * ensuite initialise les constantes et les variables 
     */
    public SimpleRunner(){
        try {
            animator.load("/Simple/SimpleRL.mch");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.initial = animator.getStateSpace().getRoot() ;

        Transition setup = initial.findTransition(Transition.SETUP_CONSTANTS_NAME);
        if (setup != null) {
            initial = setup.getDestination();
        }

        Transition initialisation = initial.findTransition(Transition.INITIALISE_MACHINE_NAME);
        if (initialisation != null) {
            initial = initialisation.getDestination();
        }

        animator.printState(this.initial) ;

        this.state = initial.exploreIfNeeded() ;
        animator.printActions(state.getOutTransitions()) ;
    } 

    /*
     * La méthode exec donne un exemple d'interaction avec l'animateur étape
     * par étape. A chaque étape on affiche l'état et les transitions 
     * déclenchables dans cet état.
     */
    public void execSequence() throws Exception {        
        // Here we start the animation
        this.state = state.perform("choose", "film = A").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = B").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = C").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;

        state = state.perform("choose", "film = A").explore() ;
        animator.printState(state) ;
        animator.printActions(state.getOutTransitions()) ;
    }

    /*
     * Cette méthode montre comment afficher la source et la cible d'une
     * transition issue de l'état courant avant d'explorer la cible.
     * Ici on prend la transition à l'indice 2 parmi les transitions
     * sortantes
     */
    public void checkTransition(){
        Transition t = state.getOutTransitions().get(2) ;
        System.out.println("Source state : ") ; animator.printState(t.getSource()) ;
        System.out.println("Destination state : ") ; animator.printState(t.getDestination()) ;
        state = t.getDestination().explore();
        animator.printActions(state.getOutTransitions()) ;
    }
}
