package fr.polytech.mnia.strategies;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;

import java.util.List;

public class StratIterPolicy extends StratMDP {
	public StratIterPolicy(Env env, double discount, double seuilConvergence) {
		super(env, discount, seuilConvergence);
		init();
	}

	private void init() {
		initState(env.getCurrentState());
	}

	private void initState(State state) {
		if (state.getOutTransitions() != null && state.getOutTransitions().isEmpty()) {
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

	@Override
	public void learn() {
		double delta;
		convergenceAtteinte = false;

		// calcule des valeurs de tous les états
		do {
			delta = 0;

			for (State state : valueState.keySet()) {
				double oldValue = valueState.get(state);
				double newValue = calculateValue(state);
				valueState.put(state, newValue);
				delta = Math.max(delta, Math.abs(oldValue - newValue));
			}
		} while (delta > seuilConvergence);
		convergenceAtteinte = true;
	}

	@Override
	public Transition choose(List<Transition> actions) {
		// Choisir l'action avec la valeur la plus élevée selon la politique actuelle
		Transition actionSelected = null;
		for (Transition transition : actions) {
			if (actionSelected == null) {
				actionSelected = transition;
			} else if (policy.get(actionSelected) < policy.get(transition)) {
				actionSelected = transition;
			}
		}

		return actionSelected;
	}

	@Override
	public boolean convergenceAtteinte() {
		return convergenceAtteinte;
	}

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

	@Override
	public String printTable() {
		return "####################################\nPolitique de l'agent :\n"+policy.toString()+"\n\nValeurs des états :\n"+valueState.toString();
	}
}
