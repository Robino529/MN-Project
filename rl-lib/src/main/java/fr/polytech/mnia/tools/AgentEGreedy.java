package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;

import java.util.*;

public class AgentEGreedy extends Agent {
	double tauxApprentissage;
	double facteurDiscount;
	double facteurExploratoire;

	public AgentEGreedy(Env env) {
		super(env);
		this.tauxApprentissage = 0.2;
		this.facteurDiscount = 0.9;
		this.facteurExploratoire = 0.2;
	}

	public AgentEGreedy(Env env, double limiteConvergence) {
		super(env, limiteConvergence);
	}

	public AgentEGreedy(Env env, double tauxApprentissage, double facteurDiscount, double facteurExploratoire) {
		super(env);
		this.tauxApprentissage = tauxApprentissage;
		this.facteurDiscount = facteurDiscount;
		this.facteurExploratoire = facteurExploratoire;
	}

	public AgentEGreedy(Env env, double tauxApprentissage, double facteurDiscount, double facteurExploratoire, double limiteConvergence) {
		this(env, tauxApprentissage, facteurDiscount, facteurExploratoire);
		this.limiteConvergence = limiteConvergence;
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
			String actionName = action.getParameterValues().get(0);

			if (!table.containsKey(actionName)) {
				table.put(actionName, defaultValue);
			}

			if (table.get(actionName) > lastVal) {
				lastVal = table.get(actionName);
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
		String actionName = transition.getParameterValues().get(0);
		Transition future = notRandChoice(transition.getDestination().getOutTransitions());

		double lastValue = table.get(actionName);
		table.put(
				actionName,
				table.get(actionName) + tauxApprentissage * (reward + facteurDiscount * env.getReward(future) - table.get(actionName)));

		if (Math.abs(table.get(actionName) - lastValue) < limiteConvergence && table.get(actionName) != lastValue) {
			throw new convergenceAtteinte("Convergence atteinte :b");
		}
	}
}
