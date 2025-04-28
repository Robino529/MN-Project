package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;
import fr.polytech.mnia.strategies.StratBanditGradient;
import fr.polytech.mnia.strategies.StratEGreedy;
import fr.polytech.mnia.strategies.StratUCB;
import fr.polytech.mnia.agents.Agent;
import fr.polytech.mnia.utils.ConvergenceAtteinte;

import java.util.List;

public class EnvSimple implements Env {
	public Agent agent;
	protected State currentState;

	public EnvSimple(String typeAlgo, State initialState) {
		if (typeAlgo.equals("e-greedy")) {
			agent = new Agent(this, new StratEGreedy(this, 0.001));
		} else if (typeAlgo.equals("ucb")) {
			agent = new Agent(this, new StratUCB(this, 0.001));
		} else {
			agent = new Agent(this, new StratBanditGradient(this, 0.001));
		}

		if (initialState != null) {
			currentState = initialState;
		} else {
			throw new IllegalArgumentException("initialState is null");
		}
	}

	public EnvSimple(String typeAlgo, State initialState, int maxIterations) {
		if (typeAlgo.equals("e-greedy")) {
			agent = new Agent(this, new StratEGreedy(this, 0.001), maxIterations);
		} else if (typeAlgo.equals("ucb")) {
			agent = new Agent(this, new StratUCB(this, 0.001), maxIterations);
		} else {
			agent = new Agent(this, new StratBanditGradient(this, 0.001), maxIterations);
		}

		if (initialState != null) {
			currentState = initialState;
		} else {
			throw new IllegalArgumentException("initialState is null");
		}
	}

	@Override
	public void execAction(Transition transToApply) {
		currentState = currentState.perform(transToApply.getName(), transToApply.getParameterPredicate()).explore();
	}

	@Override
	public double getReward(Transition transition) {
		State futureState = currentState.perform(transition.getName(), transition.getParameterPredicate()).explore();
		return getReward(futureState.eval("res").toString().equalsIgnoreCase("OK"));
	}

	public double getReward(boolean result) {
		if (result) {
			return 1.0;
		} else {
			return 0.0;
		}
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
