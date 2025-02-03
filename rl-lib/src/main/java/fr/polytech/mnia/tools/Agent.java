package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Agent {
	Env env;
	Map<String, Double> table;

	public Agent(Env env) {
		this.env = env;
		this.table = new HashMap<>();
	}

	public abstract Transition choose(List<Transition> actions);

	public abstract void reward(double reward, Transition transition);

	@Override
	public String toString() {
		return table.toString();
	}
}
