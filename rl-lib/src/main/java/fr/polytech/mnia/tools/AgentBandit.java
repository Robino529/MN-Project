package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AgentBandit extends Agent {
	private double baselineReward = 0.0;
	double tauxApprentissage = 0.2;
	Map<Transition, Double> lastProbabilites;

	public AgentBandit(Env env) {
		super(env);
	}

	@Override
	public Transition choose(List<Transition> actions) {
		lastProbabilites = new HashMap<>();
		double[] probabilities = new double[actions.size()];
		int indexChoose = 0;
		String actionName;

		if (env.iteration == 1) {
			for (Transition action : actions) {
				actionName = action.getParameterValues().get(0);

				table.put(actionName, defaultValue);
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
		baselineReward = baselineReward + (1/(double) env.iteration) * (reward - baselineReward);

		String choose = transition.getParameterValues().get(0);
		// selected action
		table.put(choose, table.get(choose) + tauxApprentissage*(reward - baselineReward)*(1 - lastProbabilites.get(transition)));

		// non-selected actions
		for(Transition action : lastProbabilites.keySet()) {
			if (action != transition) {
				String actionName = action.getParameterValues().get(0);
				table.put(actionName, table.get(actionName) - tauxApprentissage*(reward - baselineReward)*lastProbabilites.get(action));
			}
		}
	}
}
