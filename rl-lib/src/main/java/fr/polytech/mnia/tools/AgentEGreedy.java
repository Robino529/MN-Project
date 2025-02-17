package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;
import fr.polytech.mnia.MyProb;

import java.util.*;

public class AgentEGreedy extends Agent {
	final double defaultValue = 0.0;
	double tauxApprentissage;
	double facteurDiscount;
	double facteurExploratoire;
	double limiteConvergence;

	public AgentEGreedy(Env env) {
		super(env);
		this.tauxApprentissage = 0.2;
		this.facteurDiscount = 0.9;
		this.facteurExploratoire = 0.2;
		this.limiteConvergence = 0.00001;
	}

	public AgentEGreedy(Env env, double limiteConvergence) {
		this(env);
		this.limiteConvergence = limiteConvergence;
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

		double lastValue = table.get(destination);
		table.put(
				destination,
				table.get(destination) + tauxApprentissage * (reward + facteurDiscount * env.getReward(future) - table.get(destination)));
		if (Math.abs(table.get(destination) - lastValue) < limiteConvergence && table.get(destination) != lastValue) {
			throw new convergenceAtteinte("Convergence atteinte :b");
		}
	}
}
