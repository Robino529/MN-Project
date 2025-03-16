package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Agent {
	double defaultValue = 0.0;
	Env env;
	double limiteConvergence;
	Map<String, Double> table;
	// TODO : au lieu d'implémenter différents agents, utiliser des objets Stratégies ?

	public Agent(Env env) {
		this.env = env;
		this.table = new HashMap<>();
		this.limiteConvergence = 0.00001;
	}

	public Agent(Env env, double limiteConvergence) {
		this(env);
		this.limiteConvergence = limiteConvergence;
	}

	public abstract Transition choose(List<Transition> actions);

	public abstract void reward(double reward, Transition transition);

	@Override
	public String toString() {
		return table.toString();
	}
}
