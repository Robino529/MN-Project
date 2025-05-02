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
		this.limiteConvergence = 0.00001;
		this.env = env;
	}

	public StratBanditGradient(EnvSimple env, double limiteConvergence) {
		this(env);
		this.limiteConvergence = limiteConvergence;
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

		if (env.getIteration() == 1) {
			for (Transition action : actions) {
				actionName = action.getParameterValues().get(0);

				table.put(actionName, defaultTableValue);
			}
		}

		double sumB = sommeExp(actions);

		for (int i = 0; i < actions.size(); i++) {
			actionName = actions.get(i).getParameterValues().get(0);

			probabilities[i] = Math.exp(table.get(actionName)) / sumB;
			lastProbabilites.put(actions.get(i), probabilities[i]);
		}

		double rand = new Random().nextDouble();

		for (int i = 0; i < probabilities.length; i++) {
			if (rand < probabilities[i]) {
				indexChoose = i;
				break;
			} else {
				rand -= probabilities[i];
			}
		}

		return actions.get(indexChoose);
	}

	private double sommeExp(List<Transition> actions) {
		double result = 0;

		for (Transition action : actions) {
			result += Math.exp(table.get(action.getParameterValues().get(0)));
		}

		return result;
	}

	@Override
	public void reward(double reward, Transition transition) {
		baselineReward = baselineReward + (1/(double) env.getIteration()) * (reward - baselineReward);
		boolean convergence = true;
		double lastValue;

		String choose = transition.getParameterValues().get(0);
		lastValue = table.get(choose);
		// selected action
		table.put(choose, table.get(choose) + tauxApprentissage*(reward - baselineReward)*(1 - lastProbabilites.get(transition)));
		// verif si choose action n'a pas convergé
		if (!(Math.abs(table.get(choose) - lastValue) < limiteConvergence && table.get(choose) != lastValue)) {
			convergence = false;
		}

		// non-selected actions
		for(Transition action : lastProbabilites.keySet()) {
			if (action != transition) {
				String actionName = action.getParameterValues().get(0);
				lastValue = table.get(actionName); // on récupère la valeur avant modif
				table.put(actionName, table.get(actionName) - tauxApprentissage*(reward - baselineReward)*lastProbabilites.get(action));
				// on verif si action n'a pas convergé
				if (!(Math.abs(table.get(actionName) - lastValue) < limiteConvergence && table.get(actionName) != lastValue)) {
					convergenceAtteinte = false;
				}
			}
		}

		// si toutes les actions ont convergé, alors convergence atteinte
		if (convergence) {
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
