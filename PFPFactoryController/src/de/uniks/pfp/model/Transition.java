package de.uniks.pfp.model;

import java.util.ArrayList;

import de.uniks.pfp.hardware.PFPSensor;
import de.uniks.pfp.interfaces.SensorListener;

public class Transition implements SensorListener { 
	
	public State begin, end;
	public Runnable procedure;
	
	private ArrayList<Runnable> procedures = new ArrayList<>();
	
	boolean sensorStop;
	
	boolean finished;
	
	
	String description;
	
	public String getDescription() {
		return description;
	}
	
	public Transition (String desc, State b, State e) {
		begin = b;
		end = e;
		description = desc;
		finished = false;
		if (b != null)
			b.addSuccessor(e, this);
		sensorStop = true;
	}
	
	public void setProcedure(Runnable r) {
		procedure = r;
	}
	
	public boolean addProc() {
		return false;
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

	@Override
	public void register(PFPSensor s) {
		// TODO Auto-generated method stub
		
	}

}
