package de.uniks.pfp.model;

import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.hardware.PFPSensor;

public class PFPProcess {

	public String processId;
	private Runnable process = null;
	
	public PFPProcess(String id) {
		processId = id;
	}
	
	// direction true = forward, false = backward
	public void createProcess(PFPMotor motor, boolean direction) {
		
	}
	
	public void createProcess(PFPMotor motor, boolean direction, PFPSensor sensor) {
		sensor.registerListener(motor);
		createProcess(motor, direction);
	}
	
	public void createProcess(PFPMotor motor, boolean direction, int time) {
		
	}
	
	public void setProcess(Runnable r) {
		process = r;
	}
}
