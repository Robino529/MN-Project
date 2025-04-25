package fr.polytech.mnia.agents;

import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;
import fr.polytech.mnia.envs.EnvSimple;
import fr.polytech.mnia.strategies.Strategy;
import fr.polytech.mnia.utils.convergenceAtteinte;

import java.util.List;

public class Agent {
	private Env env;
	private Strategy strategy;

	public Agent(Env env, Strategy strategy) {
		this.env = env;
		this.strategy = strategy;
	}

	public Transition choose(List<Transition> actions) {
		return strategy.choose(actions);
	}

	public void reward(double reward, Transition transition) {
		strategy.reward(reward, transition);

		if (strategy.convergenceAtteinte()) {
			throw new convergenceAtteinte("Agent convergence atteinte.");
		}
	}

	@Override
	public String toString() {
		return strategy.printTable();
	}
}
