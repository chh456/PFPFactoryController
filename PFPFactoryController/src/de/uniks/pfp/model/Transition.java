package de.uniks.pfp.model;

import de.uniks.pfp.interfaces.SensorListener;

public class Transition implements SensorListener { 
	
	public State begin, end;
	public Runnable procedure;
	
	boolean sensorStop;
	
	boolean finished;
	
	
	String description;
	
	public Transition (String desc, State b, State e) {
		begin = b;
		end = e;
		description = desc;
		finished = false;
		b.addSuccessor(e, this);
		sensorStop = true;
	}
	
	public void setProcedure(Runnable r) {
		procedure = r;
	}
	
	public State getBegin() {
		return begin;
	}
	
	public State getEnd() {
		return end;
	}

	@Override
	public void inform(boolean b) {
		sensorStop = true;
		
	}

}
