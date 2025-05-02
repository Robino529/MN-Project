package fr.polytech.mnia.strategies;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.EnvTicTacToe;

import java.util.ArrayList;
import java.util.List;

public class StratIterValue extends StratMDP {
	protected ArrayList<State> terminalStates = new ArrayList<>();

	public StratIterValue(EnvTicTacToe env) {
		super(env);
		init();
	}

	public StratIterValue(EnvTicTacToe env, double seuil) {
		super(env, seuil);
		init();
	}

	public StratIterValue(EnvTicTacToe env, double seuil, double facteurDiscount) {
		super(env, facteurDiscount, seuil);
		init();
	}

	private void init() {
		exploreAllStates(env.getCurrentState());
	}

	private void exploreAllStates(State state) {
		if (state != null) {
			if (state.eval("win(0)").toString().equals("TRUE") || state.eval("win(1)").toString().equals("TRUE") || state.getOutTransitions().isEmpty()) {
				terminalStates.add(state);
				// on initialise les valeurs des états terminaux avec la récompense donnée pour les avoir atteints
				valueState.put(state, ((EnvTicTacToe) env).getReward(state));
			} else {
				for (Transition t : state.getOutTransitions()) {
					// on initialise les valeurs des états non terminaux à 0
					valueState.put(state, 0.0);
					// on explore les autres états atteignables en sortant de l'état courant
					// NB : on ignore le cas où on repasse plusieurs sur un même état car cela
					//      ne change pas le résultat final, cela ralentit simplement un peu le programme.
					exploreAllStates(t.getDestination());
				}
			}
		}
	}

	@Override
	public double calculateValue(State state) {
		double max = 0.0;
		double val;
		// on récupère les actions de sortie possibles
		List<Transition> transitions = state.getOutTransitions();

		/* Inutile car environnement déterministe
		// on récupère la probabilité (choix d'une répartition uniforme) de faire chaque action
		double probabilityChooseEachAction = transitions.isEmpty() ? 1.0 : 1.0/transitions.size();
		*/
		double probabilityChooseEachAction = 1.0;

		// pour chaque action, on calcule une valeur et on conserve le maximum entre l'ancien maximum et la valeur calculée
		for (Transition t : transitions) {
			// valeur de cette transition
			val = probabilityChooseEachAction * (env.getReward(t) + discount*valueState.get(t.getDestination()));
			// on récupère le maximum
			if (val > max) {
				max = val;
			}
		}
		return max;
	}
}
