package fr.polytech.mnia.strategies;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StratMDP implements Strategy {
	protected Map<Transition, Double> valueTransition;
	protected Map<State, Double> valueState;
	protected Map<Transition, Double> policy;
	protected double discount; // Facteur de discount
	protected double seuilConvergence; // Seuil de convergence
	protected boolean convergenceAtteinte = false;
	protected Env env;

	protected StratMDP(Env env) {
		this.env = env;
		this.discount = 0.9;
		this.seuilConvergence = 0.001;
		this.valueTransition = new HashMap<>();
		this.valueState = new HashMap<>();
		this.policy = new HashMap<>();
	}

	protected StratMDP(Env env, double seuilConvergence) {
		this.env = env;
		this.discount = 0.8;
		this.seuilConvergence = seuilConvergence;
		this.valueTransition = new HashMap<>();
		this.valueState = new HashMap<>();
		this.policy = new HashMap<>();
	}

	protected StratMDP(Env env, double discount, double seuilConvergence) {
		this.env = env;
		this.discount = discount;
		this.seuilConvergence = seuilConvergence;
		this.valueTransition = new HashMap<>();
		this.valueState = new HashMap<>();
		this.policy = new HashMap<>();
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

	protected abstract double calculateValue(State state);

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

	@Override
	public String printTable() {
		return "####################################\nPolitique de l'agent :\n"+policy.toString()+"\n\nValeurs des états :\n"+valueState.toString();
	}
}
