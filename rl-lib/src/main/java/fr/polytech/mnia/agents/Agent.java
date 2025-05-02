package fr.polytech.mnia.agents;

import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;
import fr.polytech.mnia.strategies.Strategy;

import java.util.List;

public class Agent {
	protected Env env;
	protected Strategy strategy;
	// WARNING : L'itération n'est pas actualisé et donc incorrecte pour les stratégies MDP inclues dans la bibliothèque
	public int iteration = 1;
	public final int maxIterations;

	public Agent(Env env, Strategy strategy) {
		this.env = env;
		this.strategy = strategy;
		this.maxIterations = 100;
	}

	public Agent(Env env, Strategy strategy, int maxIterations) {
		this.env = env;
		this.strategy = strategy;
		this.maxIterations = maxIterations;
	}

	/** Fonction qui lance l'apprentissage selon la stratégie
	 */
	public void learn() {
		iteration = 1;
		strategy.learn();
	}

	/** Fonction qui retourne l'action choisie par la politique de la stratégie
	 */
	protected Transition choose(List<Transition> actions) {
		return strategy.choose(actions);
	}

	@Override
	public String toString() {
		return strategy.printTable();
	}
}
