package fr.polytech.mnia.envs;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.agents.Agent;

import java.util.List;

public abstract class Env {
	protected Agent agent;
	protected State currentState;
	protected final State initState;

	public Env(State initialState) {
		if (initialState != null) {
			currentState = initialState;
			initState = initialState;
		} else {
			throw new IllegalArgumentException("initialState is null");
		}
	}

	/**
	 * Permet de modifier l'agent
	 * @param agent L'agent à définir comme nouvel agent de l'environnement, ne doit PAS être null
	 */
	public void setAgent(Agent agent) {
		if (agent == null) {
			throw new IllegalArgumentException("agent is null !");
		}
		this.agent = agent;
	}

	/** Exécute l'action donnée sur l'état courant puis passe l'état courant à l'état résultant de l'action
	 */
	public void execAction(Transition transToApply) {
		currentState = simulActionOnCurrent(transToApply);
	}

	/** Simule l'exécution de l'action donnée sur l'état courant mais ne modifie pas l'état courant
	 */
	public State simulActionOnCurrent(Transition transToApply) {
		return simulAction(currentState, transToApply);
	}

	/** Simule l'exécution de l'action donnée sur l'état donné sans modifier aucune variable
	 */
	public State simulAction(State state, Transition transToApply) {
		return state.perform(transToApply.getName(), transToApply.getParameterPredicate()).explore();
	}

	/** Fonction chargée de renvoyer la récompense pour l'action donnée
	 */
	public abstract double getReward(Transition transition);

	/** Renvoie la liste de toutes les actions disponibles pour l'état courant
	 */
	public List<Transition> getActions() {
		return getActions(currentState);
	}

	/** Renvoie la liste de toutes les actions disponibles pour l'état donné
	 */
	public List<Transition> getActions(State state) {
		return state.getOutTransitions();
	}

	/** Imprime l'agent sur la sortie standard
	 */
	public void printAgent() {
		System.out.println(agent);
	}

	/** Retourne l'état courant de l'environnement
	 */
	public State getCurrentState() {
		return currentState;
	}

	/** Réinitialise l'état courant de l'environnement
	 * => Fait revenir l'environnement dans son état initial, l'agent conserve son apprentissage
	 */
	public void reset() {
		currentState = initState;
	}

	/** Renvoie l'agent associé à l'environnement
	 */
	public Agent getAgent() {
		return agent;
	}
}
