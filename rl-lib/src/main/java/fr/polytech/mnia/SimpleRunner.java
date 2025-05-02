package fr.polytech.mnia;

import fr.polytech.mnia.envs.Env;
import fr.polytech.mnia.envs.EnvSimple;

/*
 * Cette classe illustre l'exécution de SimpleRL.mch
 */
public class SimpleRunner extends Runner{
    /*
     * Le constructeur lance ProB sur la machine SimpleRL.mch
     * et initialise la machine
     */
    public SimpleRunner() throws Exception{
        super("/Simple/SimpleRL.mch") ;
        this.initialise();
    } 

    /*
     * La méthode execSequence donne un exemple d'interaction avec l'animateur étape
     * par étape. A chaque étape on affiche l'état et les transitions 
     * déclenchables dans cet état.
     */
    public void execSequence() throws Exception {
        // e-greedy
        // execN(100, "e-greedy");
        execAuto("e-greedy");
        // UCB
        // execN(100, "ucb");
        execAuto("ucb");
        // Bandit
        // execN(100, "bandit");
        execAuto("bandit");
    }

    public void execAuto(String typeAlgo) {
        Env env = new EnvSimple(typeAlgo, state, 10000);

        env.getAgent().learn();

        System.out.println("\n######### TABLE "+typeAlgo+" de l'Agent (avec "+(env.getAgent().iteration-1)+" iterations) ###########");
        env.printAgent();
    }
}
