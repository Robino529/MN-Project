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

		if (env.iteration == 1) {
			for (Transition action : actions) {
				String target = action.getDestination().getId();

				table.put(target, defaultValue);
			}
		}

		double sumB = sommeExp(actions);

		for (int i = 0; i < actions.size(); i++) {
			String target = actions.get(i).getDestination().getId();

			probabilities[i] = Math.exp(table.get(target)) / sumB;
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

		for (int i = 0; i < actions.size(); i++) {
			result += Math.exp(table.get(actions.get(i).getDestination().getId()));
		}

		return result;
	}

	@Override
	public void reward(double reward, Transition transition) {
		baselineReward = baselineReward + (1/(double) env.iteration) * (reward - baselineReward);

		String choose = transition.getDestination().getId();
		// selected action
		table.put(choose, table.get(choose) + tauxApprentissage*(reward - baselineReward)*(1 - lastProbabilites.get(transition)));

		// non-selected actions
		for(Transition action : lastProbabilites.keySet()) {
			if (action != transition) {
				table.put(action.getDestination().getId(), table.get(action.getDestination().getId()) - tauxApprentissage*(reward - baselineReward)*lastProbabilites.get(action));
			}
		}
	}
}
