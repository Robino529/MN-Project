package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;

import java.util.List;

public class EnvYT extends EnvSimple {
	/**
	 * Constructor
	 * @param typeAlgo type de stratégie à utiliser par l'agent de l'environnement (l'agent est modifiable après création de l'environnement) :
	 *                 - "e-greedy" => choix de la stratégie epsilon greedy
	 *                 - "ucb" => choix de la stratégie UCB
	 *                 - une autre chaîne => choix de la stratégie Bandit Gradient
	 * @param initialState état initial de l'environnement
	 */
	public EnvYT(String typeAlgo, State initialState) {
		super(typeAlgo, initialState);
	}

	/**
	 * Constructor
	 * @param typeAlgo type de stratégie à utiliser par l'agent de l'environnement (l'agent est modifiable après création de l'environnement) :
	 *                 - "e-greedy" => choix de la stratégie epsilon greedy
	 *                 - "ucb" => choix de la stratégie UCB
	 *                 - une autre chaîne => choix de la stratégie Bandit Gradient
	 * @param initialState état initial de l'environnement
	 * @param maxIterations nombre maximum d'itérations à réaliser lors de l'apprentissage par l'agent
	 */
	public EnvYT(String typeAlgo, State initialState, int maxIterations) {
		super(typeAlgo, initialState, maxIterations);
	}

	@Override
	public double getReward(Transition transition) {
		List<String> video = transition.getParameterValues();

		double duration = (double) Integer.parseInt(transition.getReturnValues().get(0)) / 100;

		return duration - 0.5;
	}
}
