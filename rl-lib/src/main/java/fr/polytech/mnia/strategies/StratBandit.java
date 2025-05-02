package fr.polytech.mnia.strategies;

import de.prob.statespace.Transition;
import fr.polytech.mnia.agents.Agent;
import fr.polytech.mnia.envs.Env;

import java.util.HashMap;
import java.util.Map;

public abstract class StratBandit implements Strategy {
	protected Map<String, Double> table = new HashMap<>();
	protected double defaultTableValue = 0.0;
	protected double seuilConvergence;
	protected boolean convergenceAtteinte = false;
	protected Env env;
	protected Agent agent;

	StratBandit(Env env) {
		// => ne sert à rien car au moment de la création d'une instance de stratégie, l'agent n'est pas encore créé
		// this.agent = env.getAgent();
		this.env = env;
	}

	@Override
	public void learn() {
		// avant de lancer l'apprentissage, on récupère l'agent depuis l'environnement si besoin
		if (agent == null || env.getAgent() != agent) {
			agent = env.getAgent();
		}
		// remise à l'état initial
		env.reset();
		agent.iteration = 1;
		convergenceAtteinte = false;

		// tant que l'on n'a pas atteint la convergence ou le nombre d'iterations maximal
		while (!convergenceAtteinte && agent.iteration <= agent.maxIterations) {
			// on apprend, en faisant un choix et en mettant à jour l'agent
			Transition transToApply = choose(env.getActions());
			reward(env.getReward(transToApply), transToApply);
			env.execAction(transToApply);
			agent.iteration ++;
		}
	}

	/** Met à jour l'agent en fonction de la récompense pour une transition donnée
	 * => Fonction d'apprentissage pour une transition
	 */
	public abstract void reward(double reward, Transition transition);

	@Override
	public boolean convergenceAtteinte() {
		return convergenceAtteinte;
	}

	@Override
	public String printTable() {
		return table.toString();
	}
}
