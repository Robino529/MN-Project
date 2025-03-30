package fr.polytech.mnia.strategies;
import de.prob.statespace.Transition;

import java.util.List;

public interface Strategy {
	public Transition choose(List<Transition> actions);

	public void reward(double reward, Transition transition);

	public boolean convergenceAtteinte();

	public String printTable();
}
