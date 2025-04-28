package fr.polytech.mnia.agents;

import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;
import fr.polytech.mnia.strategies.Strategy;
import fr.polytech.mnia.utils.ConvergenceAtteinte;

import java.util.List;

public class Agent {
	protected Env env;
	protected Strategy strategy;
	protected int iteration = 1;
	protected int maxIterations = 100;

	public Agent(Env env, Strategy strategy) {
		this.env = env;
		this.strategy = strategy;
	}

	public Agent(Env env, Strategy strategy, int maxIterations) {
		this.env = env;
		this.strategy = strategy;
		this.maxIterations = maxIterations;
	}

	public void learn() {
		for (iteration = 1; iteration <= maxIterations; iteration++) {
			try {
				Transition transToApply = choose(env.getActions());
				reward(env.getReward(transToApply), transToApply);
				env.execAction(transToApply);
			} catch (ConvergenceAtteinte e) {
				break;
			}
		}

		// System.out.println("Itérations réalisées = "+(iteration-1));
	}

	protected Transition choose(List<Transition> actions) {
		return strategy.choose(actions);
	}

	protected void reward(double reward, Transition transition) {
		strategy.reward(reward, transition);

		if (strategy.convergenceAtteinte()) {
			throw new ConvergenceAtteinte("Agent convergence atteinte.");
		}
	}

	public int getIteration() {
		return iteration;
	}

	@Override
	public String toString() {
		return strategy.printTable();
	}
}
