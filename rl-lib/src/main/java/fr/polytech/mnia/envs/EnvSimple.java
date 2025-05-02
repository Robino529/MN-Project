package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.strategies.StratBanditGradient;
import fr.polytech.mnia.strategies.StratEGreedy;
import fr.polytech.mnia.strategies.StratUCB;
import fr.polytech.mnia.agents.Agent;

import java.util.List;

public class EnvSimple extends Env {
	public EnvSimple(String typeAlgo, State initialState) {
		super(initialState);
		if (typeAlgo.equals("e-greedy")) {
			agent = new Agent(this, new StratEGreedy(this, 0.001));
		} else if (typeAlgo.equals("ucb")) {
			agent = new Agent(this, new StratUCB(this, 0.001));
		} else {
			agent = new Agent(this, new StratBanditGradient(this, 0.001));
		}
	}

	public EnvSimple(String typeAlgo, State initialState, int maxIterations) {
		super(initialState);
		if (typeAlgo.equals("e-greedy")) {
			agent = new Agent(this, new StratEGreedy(this, 0.001), maxIterations);
		} else if (typeAlgo.equals("ucb")) {
			agent = new Agent(this, new StratUCB(this, 0.001), maxIterations);
		} else {
			agent = new Agent(this, new StratBanditGradient(this, 0.001), maxIterations);
		}
	}

	@Override
	public double getReward(Transition transition) {
		State futureState = simulActionOnCurrent(transition);
		return getReward(futureState.eval("res").toString().equalsIgnoreCase("OK"));
	}

	/** Fonction complémentaire de récompense qui la calcule en fonction d'un booléen
	 */
	public double getReward(boolean result) {
		if (result) {
			return 1.0;
		} else {
			return 0.0;
		}
	}
}
