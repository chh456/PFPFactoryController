package de.uniks.pfp.stations;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.model.State;
import de.uniks.pfp.model.Transition;

public class RotatingTable extends Station {

	State currentState;
	ExecutorService executor;
	
	public RotatingTable(State currentState, ExecutorService e) {
		this.currentState = currentState;
		executor = e;
	}

	public boolean goIntoState(State endState) {
		boolean success = false;
		int index;
		// Current state has a a direct way to end state
		if ((index = currentState.isConnected(endState)) != -1) {
			System.out.println(currentState);
			Transition t = currentState.getProcedure(endState);
			executor.execute(t.procedure);
			success = true;
			System.out.println(endState);
			currentState = endState;
		}
		return success;
	}

	public ArrayList<PFPMotor> getMotoren() {
		return motorList;
	}
	
}
