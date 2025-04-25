package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;
import fr.polytech.mnia.utils.convergenceAtteinte;

import java.util.List;

public interface Env {
	public void start(MyProb animator);

	public State execAction();

	public double getReward(Transition transition);

	public int getIteration();

	public List<Transition> getActions();

	public void printAgent();

	public State getCurrentState();
}
