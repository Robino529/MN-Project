package fr.polytech.mnia.strategies;

import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.EnvSimple;

import java.util.List;
import java.util.Random;

public class StratEGreedy extends StratBandit {
	protected final double tauxApprentissage;
	protected final double facteurDiscount;
	protected double facteurExploratoire;
	protected Random rand = new Random();

	public StratEGreedy(EnvSimple env) {
		this.tauxApprentissage = 0.2;
		this.facteurDiscount = 0.9;
		this.facteurExploratoire = 0.2;
		this.limiteConvergence = 0.00001;
		this.env = env;
	}

	public StratEGreedy(EnvSimple env, double limiteConvergence) {
		this(env);
		this.limiteConvergence = limiteConvergence;
	}

	public StratEGreedy(EnvSimple env, double tauxApprentissage, double facteurDiscount, double facteurExploratoire) {
		this.tauxApprentissage = tauxApprentissage;
		this.facteurDiscount = facteurDiscount;
		this.facteurExploratoire = facteurExploratoire;
		this.limiteConvergence = 0.00001;
	}

	public StratEGreedy(EnvSimple env, double tauxApprentissage, double facteurDiscount, double facteurExploratoire, double limiteConvergence) {
		this(env, tauxApprentissage, facteurDiscount, facteurExploratoire);
		this.limiteConvergence = limiteConvergence;
	}

	@Override
	public Transition choose(List<Transition> actions) {
		Transition choice = null;

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
				table.put(actionName, defaultTableValue);
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
			convergenceAtteinte = true;
		}
	}

	@Override
	public boolean convergenceAtteinte() {
		return convergenceAtteinte;
	}

	@Override
	public String printTable() {
		return table.toString();
	}
}
