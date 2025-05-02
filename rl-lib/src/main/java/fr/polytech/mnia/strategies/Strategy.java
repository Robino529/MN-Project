package fr.polytech.mnia.strategies;
import de.prob.statespace.Transition;

import java.util.List;

public interface Strategy {
	/** Fonction qui lance l'apprentissage en fonction de la stratégie
	 */
	public void learn();

	/** L'agent choisit une action parmi une liste en fonction de son apprentissage actuel
	 */
	public Transition choose(List<Transition> actions);

	/** Retourne True si l'apprentissage a convergé
	 * NB : La définition de la convergence dépend de la stratégie
	 */
	public boolean convergenceAtteinte();

	/** Imprime à l'écran les valeurs d'apprentissage de l'agent
	 * NB : Les valeurs imprimées à l'écran dépendent de la stratégie
	 */
	public String printTable();
}
