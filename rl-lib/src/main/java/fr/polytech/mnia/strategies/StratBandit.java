package fr.polytech.mnia.strategies;

import fr.polytech.mnia.envs.EnvSimple;

import java.util.HashMap;
import java.util.Map;

public abstract class StratBandit implements Strategy {
	protected Map<String, Double> table = new HashMap<>();
	protected double defaultTableValue = 0.0;
	protected double limiteConvergence;
	protected boolean convergenceAtteinte = false;
	protected EnvSimple env;
}
