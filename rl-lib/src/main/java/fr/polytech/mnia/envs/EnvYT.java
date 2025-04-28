package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;

import java.util.List;

public class EnvYT extends EnvSimple {
	public EnvYT(String typeAlgo, State initialState) {
		super(typeAlgo, initialState);
	}

	public EnvYT(String typeAlgo, State initialState, int maxIterations) {
		super(typeAlgo, initialState, maxIterations);
	}

	@Override
	public double getReward(Transition transition) {
		List<String> video = transition.getParameterValues();

		double duration = (double) Integer.parseInt(transition.getReturnValues().get(0)) / 100;

		return duration - 0.5;
	}
}
