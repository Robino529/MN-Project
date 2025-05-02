package fr.polytech.mnia.strategies;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.Env;

// Pour politique déterministe uniquement
public class StratIterPolicyWithImprovement extends StratIterPolicy {
	public StratIterPolicyWithImprovement(Env env) {
		super(env);
	}

	public StratIterPolicyWithImprovement(Env env, double seuilConvergence) {
		super(env, seuilConvergence);
	}

	public StratIterPolicyWithImprovement(Env env, double discount, double seuilConvergence) {
		super(env, discount, seuilConvergence);
	}

	@Override
	public void learn() {
		double delta;
		convergenceAtteinte = false;

		// apprentissage tant que l'on a pas atteint la convergence
		while (!convergenceAtteinte) {
			// calcul des valeurs des états jusqu'à stabilité (=> convergence)
			do {
				delta = 0;
				// calcul des valeurs de tous les états
				for (State state : valueState.keySet()) {
					double oldValue = valueState.get(state);
					double newValue = calculateValue(state);
					valueState.put(state, newValue);
					delta = Math.max(delta, Math.abs(oldValue - newValue));
				}
			} while (delta > seuilConvergence);
			convergenceAtteinte = true; // convergence atteinte pour les états

			// on améliore la politique actuelle sur chaque état
			for (State state : valueState.keySet()) {
				Transition ancienneAction = choose(env.getActions(state));
				improvePolicy(state);

				// si l'action choisie par la politique précédente est différente de celle choisie
				// par la nouvelle politique alors convergence non atteinte au niveau de la politique
				if (ancienneAction != choose(env.getActions(state))) {
					convergenceAtteinte = false;
				}
			}
		}
	}

	/* Fonction d'amélioration de la politique (déterministe) pour un état donné
	 * => Fait en sorte que la politique sélectionne la meilleure action
	 */
	protected void improvePolicy(State state) {
		// meilleure valeure est initialement la plus petite valeur possible
		// => n'importe quelle valeur est plus grande
		double bestValue = Double.NEGATIVE_INFINITY;
		// on conserve la meilleure action (celle correspondante à la meilleure valeur actuelle)
		Transition bestAction = null;
		// on évalue chaque action de l'état donné pour trouver la meilleure action et adapter la politique en fonction
		for (Transition action : env.getActions(state)) {
			double reward = env.getReward(action);
			double nextValue = valueState.get(action.getDestination());
			double value = (reward + discount * nextValue);

			// on regarde si l'action courante est la meilleure
			if (value > bestValue) {
				// la nouvelle meilleure action est maintenant toujours sélectionnée par la politique
				policy.put(action, 1.0);
				// on fait en sorte que la politique ne choisisse plus l'ancienne meilleure action
				if (bestAction != null) {
					policy.put(bestAction, 0.0);
				}
				bestValue = value;
				bestAction = action;
			}
		}
	}
}
