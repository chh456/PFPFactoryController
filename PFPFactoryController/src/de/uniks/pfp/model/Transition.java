package de.uniks.pfp.model;

public class Transition {
	
	public State begin, end;
	public Runnable procedure;
	
	String description;
	
	public Transition (String desc, State b, State e) {
		begin = b;
		end = e;
		description = desc;
		b.addSuccessor(e, this);
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

}
