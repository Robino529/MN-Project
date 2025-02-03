package fr.polytech.mnia.tools;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;

import java.util.ArrayList;

public class Env {
	Agent agent;
	State currentState;

	public Env(String typeAlgo, State initialState) {
		if (typeAlgo.equals("e-greedy")) {
			agent = new AgentEGreedy(this);
		} else if (typeAlgo.equals("ucb")) {
			agent = new AgentUCB(this);
		} else {
			agent = new AgentBandit(this);
		}

		if (initialState != null) {
			currentState = initialState;
		} else {
			throw new IllegalArgumentException("initialState is null");
		}
	}

	public State execAction() {
		Transition transToApply = agent.choose(currentState.getOutTransitions());
		currentState = currentState.perform(transToApply.getName(), transToApply.getParameterPredicate()).explore();
		double reward = getReward(currentState.eval("res").toString().equalsIgnoreCase("OK"));

		agent.reward(reward, transToApply);
		return currentState;
	}

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

	public void printAgent() {
		System.out.println(agent);
	}
}
