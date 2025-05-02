package fr.polytech.mnia.strategies;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;

public class StratIterPolicy extends StratMDP {
	public StratIterPolicy(Env env) {
		super(env);
		init();
	}

	public StratIterPolicy(Env env, double seuilConvergence) {
		super(env, seuilConvergence);
		init();
	}

	public StratIterPolicy(Env env, double discount, double seuilConvergence) {
		super(env, discount, seuilConvergence);
		init();
	}

	private void init() {
		initState(env.getCurrentState());
	}

	private void initState(State state) {
		if (state != null) {
			if (state.getOutTransitions().isEmpty()) {
				valueState.put(state, 0.0);
			} else {
				for (Transition transition : state.getOutTransitions()) {
					valueTransition.put(transition, 0.0);
					valueState.put(transition.getDestination(), 0.0);
					policy.put(transition, 0.0);
					initState(transition.getDestination());
				}
			}
		}
	}

	@Override
	protected double calculateValue(State state) {
		// Calculer la valeur de la transition en utilisant la politique actuelle
		double value = 0;
		if (state.getOutTransitions() != null && env.getActions(state).isEmpty()) {
			value = valueState.get(state);
		} else {
			for (Transition nextTransition : env.getActions(state)) {
				double reward = env.getReward(nextTransition);
				double nextValue = valueState.get(nextTransition.getDestination());
				value += policy.get(nextTransition) * (reward + discount * nextValue);
			}
		}
		return value;
	}
}
