package fr.polytech.mnia;

import fr.polytech.mnia.envs.EnvYT;

public class YTRunner extends Runner {
	/*
	 * Le constructeur lance ProB sur la machine SimpleRL.mch
	 * et initialise la machine
	 */
	public YTRunner() throws Exception{
		super("/Simple/YouTube.mch") ;
		this.initialise();
	}

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
		EnvYT env = new EnvYT(typeAlgo, state);

		env.start(animator);

		System.out.println("\n######### TABLE "+typeAlgo+" de l'Agent (avec "+(env.getIteration()-1)+" iterations) ###########");
		env.printAgent();
	}

	public void execN(int n, String typeAlgo) {
		EnvYT env = new EnvYT(typeAlgo, state);

		for (int i = 0; i < n; i++) {
			state = env.execAction();
			animator.printState(state);
		}

		System.out.println("\n######### TABLE "+typeAlgo+" de l'Agent ###########");
		env.printAgent();
	}
}
