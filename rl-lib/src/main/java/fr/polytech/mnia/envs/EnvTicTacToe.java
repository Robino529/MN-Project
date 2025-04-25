package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;
import fr.polytech.mnia.agents.Agent;
import fr.polytech.mnia.strategies.StratIterValue;
import java.util.List;

public class EnvTicTacToe implements Env {
	protected Agent agent;
	protected State currentState;
	protected int iteration = 1;
	protected int maxIterations = 100;

	public EnvTicTacToe(String typeAlgo, State initialState) {
		if (typeAlgo.equals("iterValue")) {
			agent = new Agent(this, new StratIterValue(this, 0.001));
		} else if (typeAlgo.equals("iterPolicy")) {
			throw new IllegalArgumentException("iterPolicy is not implemented yet.");
//			agent = new Agent(this, new StratIterPolicy(this, 0.001));
		} else {
			throw new IllegalArgumentException("Unknown algo: " + typeAlgo);
		}

		if (initialState != null) {
			currentState = initialState;
		} else {
			throw new IllegalArgumentException("initialState is null");
		}
	}

	public EnvTicTacToe(String typeAlgo, State initialState, int maxIterations) {
		this(typeAlgo, initialState);
		this.maxIterations = maxIterations;
	}

	@Override
	public void start(MyProb animator) {

	}

	@Override
	public State execAction() {
		return null;
	}

	@Override
	public double getReward(Transition transition) {
		State futureState = currentState.perform(transition.getName(), transition.getParameterPredicate()).explore();
		return getReward(futureState);
	}

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

	@Override
	public int getIteration() {
		return 0;
	}

	@Override
	public List<Transition> getActions() {
		return this.currentState.getOutTransitions();
	}

	@Override
	public void printAgent() {
		System.out.println(agent);
	}

	@Override
	public State getCurrentState() {
		return currentState;
	}
}
