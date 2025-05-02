package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.agents.Agent;
import fr.polytech.mnia.strategies.StratIterPolicy;
import fr.polytech.mnia.strategies.StratIterValue;

public class EnvTicTacToe extends Env {
	public EnvTicTacToe(String typeAlgo, State initialState) {
		super(initialState);
		if (typeAlgo.equals("iterValue")) {
			agent = new Agent(this, new StratIterValue(this, 0.001));
		} else if (typeAlgo.equals("iterPolicy")) {
			agent = new Agent(this, new StratIterPolicy(this, 0.8, 0.001));
		} else {
			throw new IllegalArgumentException("Unknown algo: " + typeAlgo);
		}
	}

	public EnvTicTacToe(String typeAlgo, State initialState, int maxIterations) {
		super(initialState);
		if (typeAlgo.equals("iterValue")) {
			agent = new Agent(this, new StratIterValue(this, 0.001), maxIterations);
		} else if (typeAlgo.equals("iterPolicy")) {
			agent = new Agent(this, new StratIterPolicy(this, 0.8, 0.001), maxIterations);
		} else {
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
			return 5.0;
		} else if (state.eval("win(1)").toString().equalsIgnoreCase("TRUE")) {
			return -5.0;
		} else if (state.getOutTransitions().isEmpty()) {
			return 0.0;
		} else {
			return -0.05;
		}
	}
}
