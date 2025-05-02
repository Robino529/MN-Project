package fr.polytech.mnia.strategies;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;

import java.util.HashMap;
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
		this.policy = new HashMap<>();
	}

	protected StratMDP(Env env, double seuilConvergence) {
		this.env = env;
		this.discount = 0.8;
		this.seuilConvergence = seuilConvergence;
		this.valueTransition = new HashMap<>();
		this.policy = new HashMap<>();
	}

	protected StratMDP(Env env, double discount, double seuilConvergence) {
		this.env = env;
		this.discount = discount;
		this.seuilConvergence = seuilConvergence;
		this.valueTransition = new HashMap<>();
		this.policy = new HashMap<>();
	}
}
