package fr.polytech.mnia.strategies;

import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.EnvSimple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StratBanditGradient extends StratBandit {
	protected double baselineReward = 0.0;
	protected double tauxApprentissage = 0.2;
	protected Map<Transition, Double> lastProbabilites;

	public StratBanditGradient(EnvSimple env) {
		super(env);
		this.seuilConvergence = 0.00001;
	}

	public StratBanditGradient(EnvSimple env, double limiteConvergence) {
		this(env);
		this.seuilConvergence = limiteConvergence;
	}

	public StratBanditGradient(EnvSimple env, double tauxApprentissage, double limiteConvergence) {
		this(env, limiteConvergence);
		this.tauxApprentissage = tauxApprentissage;
	}

	public StratBanditGradient(EnvSimple env, double tauxApprentissage, double baselineReward, double limiteConvergence) {
		this(env, limiteConvergence, tauxApprentissage);
		this.baselineReward = baselineReward;
	}

	@Override
	public Transition choose(List<Transition> actions) {
		lastProbabilites = new HashMap<>();
		double[] probabilities = new double[actions.size()];
		int indexChoose = 0;
		String actionName;

		// si une action n'existe pas dans la table, on l'initialise
		for (Transition action : actions) {
			actionName = action.getParameterValues().get(0);
			if (!table.containsKey(actionName)) {
				table.put(actionName, defaultTableValue);
			}
		}

		// on calcule la somme des exponentiels de chacune des valeurs dans la table des actions à traiter
		double sumB = sommeExp(actions);

		// on calcule les probabilités de chaque action
		for (int i = 0; i < actions.size(); i++) {
			actionName = actions.get(i).getParameterValues().get(0);

			probabilities[i] = Math.exp(table.get(actionName)) / sumB;
			lastProbabilites.put(actions.get(i), probabilities[i]);
		}

		// on cherche l'action sélectionnée
		double rand = new Random().nextDouble();
		for (int i = 0; i < probabilities.length; i++) {
			// si le nombre tiré correspond à la probabilité de l'action, on la sélectionne
			if (rand < probabilities[i]) {
				indexChoose = i;
				break;
			}
			// sinon, on diminue le nombre tiré de la valeur de la probabilité de l'action
			// => permet de bien tirer un nombre aléatoire
			else {
				rand -= probabilities[i];
			}
		}

		// on renvoie l'action sélectionnée
		return actions.get(indexChoose);
	}

	/**
	 * Réalise la somme des exponentiels de chaque valeur de chaque action donnée dans la table de l'agent
	 */
	private double sommeExp(List<Transition> actions) {
		double result = 0;

		for (Transition action : actions) {
			result += Math.exp(table.get(action.getParameterValues().get(0)));
		}

		return result;
	}

	@Override
	public void reward(double reward, Transition transition) {
		baselineReward = baselineReward + (1/(double) agent.iteration) * (reward - baselineReward);
		boolean convergence = true;
		double lastValue;

		String choose = transition.getParameterValues().get(0);
		lastValue = table.get(choose);
		// on modifie la valeur de l'action sélectionnée
		table.put(choose, table.get(choose) + tauxApprentissage*(reward - baselineReward)*(1 - lastProbabilites.get(transition)));
		// verif si choose action n'a pas convergé
		if (!(Math.abs(table.get(choose) - lastValue) < seuilConvergence && table.get(choose) != lastValue)) {
			convergence = false;
		}

		// non-selected actions
		for(Transition action : lastProbabilites.keySet()) {
			if (action != transition) { // on ignore l'action choisie
				String actionName = action.getParameterValues().get(0);
				lastValue = table.get(actionName); // on récupère la valeur avant modif
				// on modifie la valeur de l'action courante
				table.put(actionName, table.get(actionName) - tauxApprentissage*(reward - baselineReward)*lastProbabilites.get(action));
				// on verif si action n'a pas convergé
				if (!(Math.abs(table.get(actionName) - lastValue) < seuilConvergence && table.get(actionName) != lastValue)) {
					convergence = false;
				}
			}
		}

		// si toutes les actions ont convergé, alors convergence atteinte
		if (convergence) {
			convergenceAtteinte = true;
		}
	}
}
