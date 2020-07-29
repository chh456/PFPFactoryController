package de.uniks.pfp.stations;

import java.util.ArrayList;

import de.uniks.pfp.exceptions.BrickNotInitialized;
import de.uniks.pfp.hardware.PFPBrick;
import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.model.Transition;

public class Station {

	ArrayList<PFPBrick> bricks;
	ArrayList<PFPMotor> motorList;
	ArrayList<Transition> transitions;
	
	String name;
	
	public Station(String name) {
		
		this.name = "".equals(name) ? this.getClass().getName() : name;
		
		bricks = new ArrayList<>();
		motorList = new ArrayList<>();
		transitions = new ArrayList<>();
	}
	
	public Station() {
		this("");
	}
	
	public boolean initBrickIp(String brickIp) {
		PFPBrick currentBrick = new PFPBrick(brickIp);
		return currentBrick.initialized();
	}
	
	public void addBrick(PFPBrick brick) throws BrickNotInitialized {
		if (brick == null) throw new BrickNotInitialized(brick + " is not initialized.");
		if (!bricks.contains(brick))
			bricks.add(brick);
	}
	
	public void addMotor(PFPMotor motor) {
		motorList.add(motor);
	}
	
	public void addTransition(Transition t) {
		transitions.add(t);
	}
	
}
