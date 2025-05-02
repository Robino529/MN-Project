package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.agents.Agent;
import fr.polytech.mnia.strategies.StratIterPolicy;
import fr.polytech.mnia.strategies.StratIterPolicyWithImprovement;
import fr.polytech.mnia.strategies.StratIterValue;

public class EnvTicTacToe extends Env {
	/**
	 * Constructor
	 * @param typeAlgo type de stratégie à utiliser par l'agent de l'environnement (l'agent est modifiable après création de l'environnement) :
	 *                 - "iterValue" => choix de la stratégie epsilon greedy
	 *                 - "iterPolicyImprovement" => choix de la stratégie Iterative Policy Evaluation avec amélioration (nécessite une politique déterministe)
	 *                 - "iterPolicy" => choix de la stratégie Iterative Policy Evaluation (sans amélioration, et avec une politique uniforme ou e-greedy)
	 *                 - une autre chaîne => IllegalArgumentException
	 * @param initialState état initial de l'environnement
	 */
	public EnvTicTacToe(String typeAlgo, State initialState) {
		super(initialState);
		switch (typeAlgo) {
			case "iterValue":
				agent = new Agent(this, new StratIterValue(this, 0.001));
				break;
			case "iterPolicy":
				agent = new Agent(this, new StratIterPolicy(this, 0.8, 0.001));
				break;
			case "iterPolicyImprovement":
				agent = new Agent(this, new StratIterPolicyWithImprovement(this, 0.8, 0.001));
				break;
			default:
				throw new IllegalArgumentException("Unknown algo: " + typeAlgo);
		}
	}

	/**
	 * Constructor
	 * @param typeAlgo type de stratégie à utiliser par l'agent de l'environnement (l'agent est modifiable après création de l'environnement) :
	 *                 - "iterValue" => choix de la stratégie epsilon greedy
	 *                 - "iterPolicyImprovement" => choix de la stratégie Iterative Policy Evaluation avec amélioration (nécessite une politique déterministe)
	 *                 - "iterPolicy" => choix de la stratégie Iterative Policy Evaluation (sans amélioration, et avec une politique uniforme ou e-greedy)
	 *                 - une autre chaîne => IllegalArgumentException
	 * @param initialState état initial de l'environnement
	 * @param maxIterations nombre d'itérations maximal pour l'apprentissage de l'agent
	 */
	public EnvTicTacToe(String typeAlgo, State initialState, int maxIterations) {
		super(initialState);
		switch (typeAlgo) {
			case "iterValue":
				agent = new Agent(this, new StratIterValue(this, 0.001), maxIterations);
				break;
			case "iterPolicy":
				agent = new Agent(this, new StratIterPolicy(this, 0.8, 0.001), maxIterations);
				break;
			case "iterPolicyImprovement":
				agent = new Agent(this, new StratIterPolicyWithImprovement(this, 0.8, 0.001),  maxIterations);
				break;
			default:
				throw new IllegalArgumentException("Unknown algo: " + typeAlgo);
		}
	}

	@Override
	public double getReward(Transition transition) {
		State futureState = currentState.perform(transition.getName(), transition.getParameterPredicate()).explore();
		return getReward(futureState);
	}

	/** Fonction complémentaire
	 */
	public double getReward(State state) {
		if (state.eval("win(0)").toString().equalsIgnoreCase("TRUE")) {
			return 1.0;
		} else if (state.eval("win(1)").toString().equalsIgnoreCase("TRUE")) {
			return -1.0;
		} else if (state.getOutTransitions().isEmpty()) {
			return 0.0;
		} else {
			return -0.25;
		}
	}
}
