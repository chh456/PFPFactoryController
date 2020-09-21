package de.uniks.pfp.database;

import java.text.SimpleDateFormat;

public class Timing {

	long startTime;
	long endTime;
	long failureTime;
	
	private static SimpleDateFormat format = new SimpleDateFormat(("HH:mm:ss:SS"));
	
	public Timing() {
		setFailure(-1);
	}
	
	public long getStart() {
		return startTime;
	}
	public void setStart(long start) {
		this.startTime = start;
	}
	public long getEnd() {
		return endTime;
	}
	public void setEnd(long end) {
		this.endTime = end;
	}
	public long getFailure() {
		return failureTime;
	}
	public void setFailure(long failure) {
		this.failureTime = failure;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Start/End");
		if (failureTime != -1)
			str.append("/Failure");
		str.append(": " + format.format(startTime) + "/" + format.format(endTime));
		if (failureTime != -1)
			str.append("/" + format.format(failureTime));
		return str.toString();
	}
	
}
