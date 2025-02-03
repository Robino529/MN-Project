package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;

import java.util.*;

public class AgentEGreedy extends Agent {
	final double defaultValue = 0.0;
	final double tauxApprentissage = 0.2;
	final double facteurDiscount = 0.9;
	final double facteurExploratoire = 0.2;

	public AgentEGreedy(Env env) {
		super(env);
	}

	@Override
	public Transition choose(List<Transition> actions) {
		Transition choice = null;
		Random rand = new Random();

		if (rand.nextDouble() < facteurExploratoire) {
			choice = actions.get(rand.nextInt(actions.size()));
		} else {
			choice = notRandChoice(actions);
		}

		return choice;
	}

	private Transition notRandChoice(List<Transition> actions) {
		Double lastVal = 0.0;
		Transition choice = null;

		for (Transition action : actions) {
			if (!table.containsKey(action.getDestination().getId())) {
				table.put(action.getDestination().getId(), defaultValue);
			}

			if (table.get(action.getDestination().getId()) > lastVal) {
				lastVal = table.get(action.getDestination().getId());
				choice = action;
			}

		}

		if (choice == null) {
			choice = actions.get(0);
		}

		return choice;
	}

	@Override
	public void reward(double reward, Transition transition) {
		String destination = transition.getDestination().getId();
		Transition future = notRandChoice(transition.getDestination().getOutTransitions());
		table.put(
				destination,
				table.get(destination) + tauxApprentissage * (reward + facteurDiscount * env.getReward(future) - table.get(destination)));
	}
}
