package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;

import java.util.List;

public class AgentUCB extends Agent {

	public AgentUCB(Env env) {
		super(env);
	}

	@Override
	public Transition choose(List<Transition> actions) {
		return null;
	}

	@Override
	public void reward(double reward, Transition transition) {

	}
}
