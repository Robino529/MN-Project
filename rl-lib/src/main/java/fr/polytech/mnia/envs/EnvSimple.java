package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;
import fr.polytech.mnia.strategies.StratBanditGradient;
import fr.polytech.mnia.strategies.StratEGreedy;
import fr.polytech.mnia.strategies.StratUCB;
import fr.polytech.mnia.agents.Agent;
import fr.polytech.mnia.utils.convergenceAtteinte;

import java.util.List;

public class EnvSimple {
	protected Agent agent;
	protected State currentState;
	protected int iteration = 1;
	protected int maxIterations = 100;

	public EnvSimple(String typeAlgo, State initialState) {
		if (typeAlgo.equals("e-greedy")) {
			agent = new Agent(this, new StratEGreedy(this));
		} else if (typeAlgo.equals("ucb")) {
			agent = new Agent(this, new StratUCB(this));
		} else {
			agent = new Agent(this, new StratBanditGradient(this));
		}

		if (initialState != null) {
			currentState = initialState;
		} else {
			throw new IllegalArgumentException("initialState is null");
		}
	}

	public EnvSimple(String typeAlgo, State initialState, int maxIterations) {
		this(typeAlgo, initialState);
		this.maxIterations = maxIterations;
	}

	public void start(MyProb animator) {
		for (iteration = 1; iteration <= maxIterations; iteration++) {
			try {
				execAction();
				// animator.printState(currentState);
			} catch (convergenceAtteinte e) {
				break;
			}
		}

		// System.out.println("Itérations réalisées = "+(iteration-1));
	}

	public State execAction() {
		Transition transToApply = agent.choose(getActions());
		double reward = getReward(transToApply);
		currentState = currentState.perform(transToApply.getName(), transToApply.getParameterPredicate()).explore();

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

	public int getIteration() {
		return iteration;
	}

	public List<Transition> getActions() {
		return this.currentState.getOutTransitions();
	}

	public void printAgent() {
		System.out.println(agent);
	}
}
