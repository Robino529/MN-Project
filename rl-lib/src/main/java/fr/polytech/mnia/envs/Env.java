package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;

import java.util.List;

public interface Env {
	public void execAction(Transition transToApply);

	public double getReward(Transition transition);

	public List<Transition> getActions();

	public void printAgent();

	public State getCurrentState();
}
