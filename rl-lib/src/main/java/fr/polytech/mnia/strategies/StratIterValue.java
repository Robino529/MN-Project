package fr.polytech.mnia.strategies;

import de.prob.statespace.State;
import de.prob.statespace.Transition;
import fr.polytech.mnia.envs.EnvTicTacToe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StratIterValue extends StratMDP {
	protected HashMap<State, Double> tableStateValue;
	protected ArrayList<State> terminalStates;
	protected final double defaultTableValue = 0.0;
	protected double seuil;
	protected double facteurDiscount;
	protected boolean convergenceAtteinte = false;
	protected EnvTicTacToe env;

	public StratIterValue(EnvTicTacToe env) {
		this.env = env;
		tableStateValue = new HashMap<>();
		terminalStates = new ArrayList<>();
	}

	public StratIterValue(EnvTicTacToe env, double seuil) {
		this(env);
		this.seuil = seuil;
	}

	public StratIterValue(EnvTicTacToe env, double seuil, double facteurDiscount) {
		this(env, seuil);
		this.facteurDiscount = facteurDiscount;
	}

	private void InitTable() {
		tableStateValue.put(env.getCurrentState(), defaultTableValue);
		exploreAllStates(env.getCurrentState());
	}

	private void exploreAllStates(State state) {
		for (Transition t : state.getOutTransitions()) {
			exploreAllStates(t.getDestination());
			// cas terminaux
			if (state.eval("win(0)").toString().equals("TRUE") || state.eval("win(1)").toString().equals("TRUE") || state.getOutTransitions().isEmpty()) {
				terminalStates.add(state);
				tableStateValue.put(state, env.getReward(state));
			} else {
				tableStateValue.put(state, defaultTableValue);
			}
		}
	}

	public Transition choose(List<Transition> actions) {
		//
		return null;
	}

	public HashMap<State, Double> learn() {
		double delta = 0.0;
		do {
			double lastVal;

			for (State s : tableStateValue.keySet()) {
				lastVal = tableStateValue.get(s);
				tableStateValue.put(s, value(s));

				delta = Math.max(delta, Math.abs(lastVal - tableStateValue.get(s)));
			}
		} while (delta < seuil);

		return (HashMap<State, Double>) tableStateValue.clone();
	}

	public double value(State state) {
		double max = 0.0;
		double val;
		List<Transition> transitions = state.getOutTransitions();
		double probabilityChooseEachAction = 1.0/transitions.size();
		for (Transition t : transitions) {
			val = probabilityChooseEachAction * (env.getReward(t) + facteurDiscount*tableStateValue.get(t.getDestination()));

			if (val > max) {
				max = val;
			}
		}

		return max;
	}

	public void reward(double reward, Transition transition) {
		//
	}

	public boolean convergenceAtteinte() {
		//
		return false;
	}

	public String printTable() {
		//
		return null;
	}
}
