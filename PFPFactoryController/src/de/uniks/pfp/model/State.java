package de.uniks.pfp.model;

import java.util.ArrayList;

import de.uniks.pfp.stations.Station;

public class State {

	String description;
	ArrayList<Transition> transitions;
	
	// Corresponding station, can be null
	Station station;
	
	public State(String desc) {
		this(desc, null);
	}
	
	public State(String desc, Station s) {
		transitions = new ArrayList<>();
		description = desc;
		station = s;
	}
	
	public int isConnected(State s) {
		int index = -1;
		boolean found = false;
		for (int i = 0; i < transitions.size() && !found; i++) {
			State currentState = transitions.get(i).getEnd();
			if (currentState != null && currentState.equals(s)) {
				index = i;
				found = true;
			}
		}
				
		return index;
	}
	
	// Returns procedure for getting into State s or null if this State can't go into s
	public Transition getProcedure(State s) {
		Transition procedure = null;
		
		int index;
		
		// When this state has a transition into s
		if ((index = isConnected(s)) != -1) {
			procedure = transitions.get(index);  
		}
		
		return procedure;
	}
	
	public void addSuccessor(State end) { 
		addSuccessor(end, null);
	}
	
	public void addSuccessor(State end, Transition t) {
		int index = isConnected(end);
		
		// states were connected before. we update the transition
		if (index > -1) {
			transitions.set(index, t);
		} else {
			transitions.add(t);
		}
	}
}
