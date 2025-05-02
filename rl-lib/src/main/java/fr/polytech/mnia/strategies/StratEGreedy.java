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
		super(env);
		this.tauxApprentissage = 0.2;
		this.facteurDiscount = 0.9;
		this.facteurExploratoire = 0.2;
		this.seuilConvergence = 0.00001;
	}

	public StratEGreedy(EnvSimple env, double limiteConvergence) {
		this(env);
		this.seuilConvergence = limiteConvergence;
	}

	public StratEGreedy(EnvSimple env, double tauxApprentissage, double facteurDiscount, double facteurExploratoire) {
		super(env);
		this.tauxApprentissage = tauxApprentissage;
		this.facteurDiscount = facteurDiscount;
		this.facteurExploratoire = facteurExploratoire;
		this.seuilConvergence = 0.00001;
	}

	public StratEGreedy(EnvSimple env, double tauxApprentissage, double facteurDiscount, double facteurExploratoire, double limiteConvergence) {
		this(env, tauxApprentissage, facteurDiscount, facteurExploratoire);
		this.seuilConvergence = limiteConvergence;
	}

	@Override
	public Transition choose(List<Transition> actions) {
		Transition choice = null;

		// si on tire un nombre aléatoire qui est plus petit que epsilon alors on explore
		if (rand.nextDouble() < facteurExploratoire) {
			choice = actions.get(rand.nextInt(actions.size()));
		}
		// sinon on utilise notre table
		else {
			choice = notRandChoice(actions);
		}

		return choice;
	}

	/**
	 * Effectue un choix non aléatoire qui sélectionne l'action possédant la plus grande valeur
	 * @param actions : Liste des actions parmi lesquelles faire un choix
	 * @return l'action choisie
	 */
	private Transition notRandChoice(List<Transition> actions) {
		Double lastVal = Double.NEGATIVE_INFINITY;
		Transition choice = null;

		// parcours de toutes les actions
		for (Transition action : actions) {
			String actionName = action.getParameterValues().get(0);

			// si l'action n'existe pas dans la table de l'agent
			if (!table.containsKey(actionName)) {
				table.put(actionName, defaultTableValue);
			}

			// si la valeur stockée dans la table est plus grande que la valeur actuellement retenue
			if (table.get(actionName) > lastVal) {
				// on retient l'action courante
				lastVal = table.get(actionName);
				choice = action;
			}
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

		if (Math.abs(table.get(actionName) - lastValue) < seuilConvergence && table.get(actionName) != lastValue) {
			convergenceAtteinte = true;
		}
	}
}
