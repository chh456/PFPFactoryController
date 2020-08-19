package de.uniks.pfp.hardware;

import java.text.SimpleDateFormat;

public class PFPMotorTask {
	
	String id;
	
	long startTime;
	long endTime;
	
	int startCounter;
	int endCounter;
	
	private static SimpleDateFormat format = new SimpleDateFormat(("HH:mm:ss:SS"));
	
	public PFPMotorTask() {
		
	}
	
	public PFPMotorTask(String id, long startTime, int startCounter) {
		this.id = id;
		this.startTime = startTime;
		this.startCounter = startCounter;
	}
	
	public void end(int endCounter) {
		endTime = System.currentTimeMillis();
		this.endCounter = endCounter;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Start: " + startCounter + "/" + format.format(startTime) + " End: " + endCounter + "/" + format.format(endTime));
		return str.toString();
	}
	
}
