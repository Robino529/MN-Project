package fr.polytech.mnia.tools;

import de.prob.statespace.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentUCB extends Agent {
	double facteurExploratoire;
	Map<String, Integer> tableOccurences;

	public AgentUCB(Env env) {
		super(env);
		tableOccurences = new HashMap<>();
		facteurExploratoire = 0.2;
	}

	public AgentUCB(Env env, double limiteConvergence) {
		super(env, limiteConvergence);
		tableOccurences = new HashMap<>();
		facteurExploratoire = 0.2;
	}

	public AgentUCB(Env env, double limiteConvergence, double facteurExploratoire) {
		this(env, limiteConvergence);
		this.facteurExploratoire = facteurExploratoire;
	}

	@Override
	public Transition choose(List<Transition> actions) {
		double maxValue = 0.0;
		int transition = 0;

		for (int i = 0; i < actions.size(); i++) {
			String target = actions.get(i).getDestination().getId();

			if (!table.containsKey(target)) {
				table.put(target, defaultValue);
			}

			if (!tableOccurences.containsKey(target)) {
				tableOccurences.put(target, 1);
				return actions.get(i);
			} else {
				tableOccurences.put(target, tableOccurences.get(target) + 1);
			}

			// calcul valeur d'une action
			double value = table.get(target) + facteurExploratoire * Math.sqrt(Math.log(env.iteration) / tableOccurences.get(target));
			// probabilité plus importante => nouveau choix
			if (value > maxValue) {
				maxValue = value;
				transition = i;
			}
		}
		return actions.get(transition);
	}

	@Override
	public void reward(double reward, Transition transition) {
		String destination = transition.getDestination().getId();
		double lastValue = table.get(destination);

		// TODO : env.iteration =>
		table.put(
				destination,
				lastValue + facteurExploratoire * Math.sqrt(Math.log(env.iteration) / tableOccurences.get(destination))
		);
	}
}
