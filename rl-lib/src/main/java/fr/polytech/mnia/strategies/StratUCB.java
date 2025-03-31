package fr.polytech.mnia.strategies;

import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.EnvSimple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StratUCB extends StratEGreedy {
	protected Map<String, Integer> tableOccurences = new HashMap<>();
	protected int iteration = 0;

	public StratUCB(EnvSimple env) {
		super(env);
	}

	public StratUCB(EnvSimple env, double limiteConvergence) {
		super(env, limiteConvergence);
	}

	public StratUCB(EnvSimple env, double facteurExploratoire, double limiteConvergence) {
		super(env, limiteConvergence);
		this.facteurExploratoire = facteurExploratoire;
	}

	@Override
	public Transition choose(List<Transition> actions) {
		double maxValue = 0.0;
		int transition = 0;

		for (int i = 0; i < actions.size(); i++) {
			String actionName = actions.get(i).getParameterValues().get(0);

			if (!table.containsKey(actionName)) {
				table.put(actionName, defaultTableValue);
			}

			if (!tableOccurences.containsKey(actionName)) {
				tableOccurences.put(actionName, 0);
			}
			// vérifie que l'on est pas dans une exploration du futur + pas encore exploré
			if (iteration != env.getIteration() && tableOccurences.get(actionName) == 0) {
				tableOccurences.put(actionName, 1);
				transition = i;
				break;
			}

			// calcul valeur d'une action
			double value = table.get(actionName) + facteurExploratoire * Math.sqrt(Math.log(env.getIteration()) / tableOccurences.get(actionName));
			// probabilité plus importante => nouveau choix
			if (value > maxValue) {
				maxValue = value;
				transition = i;
			}
		}

		// on vérifie que l'on est pas dans un appel futur
		if (iteration != env.getIteration()) {
			tableOccurences.put(actions.get(transition).getParameterValues().get(0), tableOccurences.get(actions.get(transition).getParameterValues().get(0)) + 1);
			iteration = env.getIteration();
		}
		return actions.get(transition);
	}

	@Override
	public void reward(double reward, Transition transition) {
		String actionName = transition.getParameterValues().get(0);
		double lastValue = table.get(actionName);

//      Technique à la main, pas incroyable
//		table.put(
//				actionName,
//				lastValue + reward / tableOccurences.get(actionName)
//		);

//      Réutilisation de EGreedy
		Transition future = choose(transition.getDestination().getOutTransitions());

		table.put(
				actionName,
				table.get(actionName) + tauxApprentissage * (reward + facteurDiscount * env.getReward(future) - table.get(actionName)));

		if (Math.abs(table.get(actionName) - lastValue) < limiteConvergence && table.get(actionName) != lastValue) {
			convergenceAtteinte = true;
		}
	}
}
