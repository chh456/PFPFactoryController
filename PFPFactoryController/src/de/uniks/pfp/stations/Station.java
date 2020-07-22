package de.uniks.pfp.stations;

import java.util.ArrayList;

import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.model.Transition;

public class Station {

	ArrayList<PFPMotor> motorList;
	ArrayList<Transition> transitions;
	
	public Station() {
		motorList = new ArrayList<>();
		transitions = new ArrayList<>();
	}
	
	public void addMotor(PFPMotor motor) {
		motorList.add(motor);
	}
	
	public void addTransition(Transition t) {
		transitions.add(t);
	}
	
}
