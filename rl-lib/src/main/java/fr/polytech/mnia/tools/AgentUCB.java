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
			String actionName = actions.get(i).getParameterValues().get(0);

			if (!table.containsKey(actionName)) {
				table.put(actionName, defaultValue);
			}

			if (!tableOccurences.containsKey(actionName)) {
				tableOccurences.put(actionName, 1);
				return actions.get(i);
			}

			// calcul valeur d'une action
			double value = table.get(actionName) + facteurExploratoire * Math.sqrt(Math.log(env.iteration) / tableOccurences.get(actionName));
			// probabilité plus importante => nouveau choix
			if (value > maxValue) {
				maxValue = value;
				transition = i;
			}
		}

		tableOccurences.put(actions.get(transition).getParameterValues().get(0), tableOccurences.get(actions.get(transition).getParameterValues().get(0)) + 1);
		return actions.get(transition);
	}

	@Override
	public void reward(double reward, Transition transition) {
		String actionName = transition.getParameterValues().get(0);
		double lastValue = table.get(actionName);

//		table.put(
//				destination,
//				lastValue + facteurExploratoire * Math.sqrt(Math.log(env.iteration) / tableOccurences.get(destination))
//		);

		table.put(
				actionName,
				lastValue + reward / tableOccurences.get(actionName)
		);
	}
}
