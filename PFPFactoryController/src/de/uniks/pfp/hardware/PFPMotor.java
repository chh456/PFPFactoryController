package de.uniks.pfp.hardware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import de.uniks.pfp.interfaces.SensorListener;
import lejos.remote.ev3.RMIRegulatedMotor;

public class PFPMotor implements SensorListener {

	String id;
	RMIRegulatedMotor motor; 
	Character type; // Large or medium
	String port;
	
	// These booleans are used to stop the motor
	volatile boolean sensorStop = false;
	volatile boolean motorStop = false;
	
	// Distance this motor covered
	int distance = 0;
	
	// Adds to this motor distance
	public void addDistance(int dist) {
		distance += dist > 0 ? dist : dist*-1;
	}
	
	// Sleep interval in ms
	int interval = 100;
		
	// While the motor completes a task status is true
	boolean status = false;
	
	// If there was an error with the last task this is true
	private boolean error = false;
	
	// Puts this motor into debug mode. 
	boolean debug = false;

	
	// Every task this motor has to fulfill is saved into this list and later saved into a database
	private static ArrayList<PFPMotorTask> internalTasks = new ArrayList<>();
	private static List<PFPMotorTask> tasks = Collections.synchronizedList(internalTasks);
	
	// Current task this motor is working on
	PFPMotorTask currentTask;
	
	ArrayList <String> timings;
	
	static ExecutorService executor;
	public static void setExecutioner(ExecutorService exec) {
		executor = exec;
	}
	
	public PFPMotor(RMIRegulatedMotor m, String port, Character type) {
		motor = m;
		this.type = type;
		this.port = port;
	}
	
	public PFPMotor(RMIRegulatedMotor m, String name) {
		motor = m;
		id = name;
	}
	
	public void init() {
		timings = new ArrayList<>();
		
		// These lists are used to record when this motor starts, finishes or fails
		ArrayList<Long> start = new ArrayList<>();
		ArrayList<Long> finish = new ArrayList<>();
		ArrayList<Long> failure = new ArrayList<>();
		
	}
	
	private long getTime() {
		return System.nanoTime();
	}
	
	public static PFPMotor createMotorWithName(RMIRegulatedMotor m, String name) {
		return new PFPMotor(m, name);
	}
	
	public char getType() {
		return type;
	}
	
	// This resets the internals for the next task to start	
	public void resetMotor() {
		setMotorStop(false);
		setSensorStop(false);
	}
	
	public void setMotorStop(boolean s) {
		if (debug) System.out.println("Motorstop was set on " + id + " to " + s);
		motorStop = s;
	}
	
	private void setSensorStop(boolean s) {
		if (debug) System.out.println("Motorstop was set on " + id + " to " + s);
		sensorStop = s;
	}
	
	public boolean getMotorStop() {
		return motorStop;
	}
	public boolean getSensorStop() {
		return sensorStop;
	}
	
	// This waits for a stop signal either from a sensor or the controller
	public void waitForStop() {
		while (!getSensorStop() && !getMotorStop()) {
			try {
				Thread.sleep(interval);
				if (System.currentTimeMillis() % 100 == 0)
					System.out.println("Sensorstop " + getSensorStop() + " Motorstop " + getMotorStop());
			} catch (InterruptedException e) {
				if (debug) System.out.println("Motor " + id + " was interrupted.");
				emergencyStop();
			}
		}
	}
	
	private void emergencyStop() {
		if (debug) System.out.println("Emergencystop for motor " + id);
		try {
			motor.stop(true);
		} catch (RemoteException e) {
			setStatus(false);
			error = true;
		}
	}
	
	// Determines if there were errors while completing old tasks
	public boolean stillFunctional() {
		return error;
	}

	// Direction that the motor should move. true forward, false backward
	public Runnable move(boolean direction, int taskPosition) {
		
		Runnable currentRun = new Runnable() {

			@Override
			public void run() {
				if (debug) System.out.println("Motor " + id + " moving " + (direction ? "forward" : "backward") + ".");
								
				if (debug) System.out.println(getTachoCount());
				
				try {
					setStatus(true);
					if (direction) {
						motor.forward();
					} else {
						motor.backward();
					}
					waitForStop();
					motor.stop(true);
					resetMotor();
					if (debug) System.out.println("Motor " + id + " stopped.");
					setStatus(false);
				} catch (RemoteException e) {
					if (debug) System.out.println("Connectionproblem with motor " + id);
					setStatus(false);
					error = true;
				}
				
				if (debug) System.out.println(getTachoCount());
								
				internalTasks.get(taskPosition).end(getTachoCount());
				System.out.println(internalTasks.get(taskPosition));
				
			}
			
		};
		
		return currentRun;

		
	}
	
	// Returns the current tachoStand or null if connection fails
	public Integer getTachoCount() {
		Integer tachoCount = null;
		try {
			tachoCount = motor.getTachoCount();
		} catch (RemoteException e) {
			if (debug) System.out.println("Connection failed to motor " + id + " while retrieving tachocount.");
			tachoCount = null;
			error = true;
		}
		return tachoCount;
	}
	
	protected PFPMotorTask getTask() {
		PFPMotorTask newTask = new PFPMotorTask(id, System.currentTimeMillis(), 0);
		return newTask;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
		
	public void forward() {
		if (debug) System.out.println("Executor runs motor forward.");
		PFPMotorTask forwardTask = getTask();
		tasks.add(forwardTask);
		executor.execute(move(true, tasks.indexOf(forwardTask)));
		
		
	}
	
	public void backward() {
		if (debug) System.out.println("Executor runs motor backward.");
		PFPMotorTask backwardTask = getTask();
		tasks.add(backwardTask);
		executor.execute(move(false, tasks.indexOf(backwardTask)));
	}
	
	
	public void forward(int time) {
		try {
			motor.forward();
			Thread.sleep(time);
			motor.stop(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void close() {
		if (debug) System.out.println("Closing motor.");
		// TODO Auto-generated method stub
		try {
			motor.close();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			error = true;
			e.printStackTrace();
			// We were not able to close the port. Not much we can do anymore.
		}
	}


	@Override
	public void inform(boolean b) {
		System.out.println("Informed about sensor status.");
		sensorStop = true;
		
	}


	public void setSpeed(int i) {
		try {
			motor.setSpeed(i);
		} catch (RemoteException e) {
			if (debug) System.out.println("Was not able to set speed for motor " + id);
			error = true;
		}
		
	}

	public void setDebug(boolean deb) {
		debug = deb;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("PFPMotor with id " + id + "\n");
		str.append("debug is " + debug + " error is " + error);
		return str.toString();
	}

}
