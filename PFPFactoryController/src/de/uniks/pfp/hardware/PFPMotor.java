package de.uniks.pfp.hardware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import de.uniks.pfp.database.Timing;
import de.uniks.pfp.interfaces.SensorListener;
import lejos.remote.ev3.RMIRegulatedMotor;

public class PFPMotor implements SensorListener {

	String motorId;
	RMIRegulatedMotor motor; 
	Character type; // Large or medium
	String port;
	
	// These booleans are used to stop the motor
	volatile boolean sensorStop = false;
	volatile boolean motorStop = false;
	

	
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
	
	
	
	static ExecutorService executor;
	
	/* These attributes are set when the process is created. 
	 * 
	 */
	
	int timeToRun;
	

	
	/* ---------------- */
	/* STATISTICAL DATA */
	/* ---------------- */
	
	// Distance this motor covered
	int distance = 0;
	
	// Adds to this motor distance
	public void addDistance(int dist) {
		distance += dist > 0 ? dist : dist*-1;
	}
	
	// Saves start,end,failure times of this motor
	ArrayList <Timing> timings;
	
	/* -------------------- */
	/* STATISTICAL DATA END */
	/* -------------------- */
	
	public PFPMotor(RMIRegulatedMotor m, String port, Character type) {
		motor = m;
		this.type = type;
		this.port = port;
	}
	
	public PFPMotor(RMIRegulatedMotor m, String name) {
		motor = m;
		motorId = name;
	}
	
	public void init() {
		timings = new ArrayList<>();
		
		// These lists are used to record when this motor starts, finishes or fails
		ArrayList<Long> start = new ArrayList<>();
		ArrayList<Long> finish = new ArrayList<>();
		ArrayList<Long> failure = new ArrayList<>();
		
	}
	
	/* private long getTime() {
		return System.nanoTime();
	} */
	
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
		if (debug) System.out.println("Motorstop was set on " + motorId + " to " + s);
		motorStop = s;
	}
	
	private void setSensorStop(boolean s) {
		if (debug) System.out.println("Motorstop was set on " + motorId + " to " + s);
		sensorStop = s;
	}
	
	public boolean getMotorStop() {
		return motorStop;
	}
	public boolean getSensorStop() {
		return sensorStop;
	}
	

	
	private void emergencyStop() {
		if (debug) System.out.println("Emergencystop for motor " + motorId);
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
				if (debug) System.out.println("Motor " + motorId + " moving " + (direction ? "forward" : "backward") + ".");
								
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
					if (debug) System.out.println("Motor " + motorId + " stopped.");
					setStatus(false);
				} catch (RemoteException e) {
					if (debug) System.out.println("Connectionproblem with motor " + motorId);
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
			if (debug) System.out.println("Connection failed to motor " + motorId + " while retrieving tachocount.");
			tachoCount = null;
			error = true;
		}
		return tachoCount;
	}
	
	protected PFPMotorTask getTask() {
		PFPMotorTask newTask = new PFPMotorTask(motorId, System.currentTimeMillis(), 0);
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

	public void backward(int time) {
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

		
	
	public long getTime() {
		return System.currentTimeMillis();
	}
	
	private void moveMotor(boolean direction) {
		setStatus(true);
		try {
			if (direction) 
				motor.forward();
			else
				motor.backward();
		} catch (Exception e) {
			emergencyStop();
		}
		setStatus(false);
	}
	
	public void movement(int time, boolean direction) {
		
		// (time > -1 ) ? motor.forward() : motor.backward();
		
		Runnable move = new Runnable() {

			@Override
			public void run() {
				Timing t = new Timing();
				t.setStart(getTime());
				// setStatus(true);
				if (time > 0) {
					try {
						moveMotor(direction);
						Thread.sleep(time);
					} catch (Exception e) {
						t.setFailure(getTime());
						if (debug) e.printStackTrace();
					} finally {
						// setStatus(false);
						t.setEnd(getTime());
					}
				} else { 
					// No time was provided so we wait for a sensor or a controller stop
					moveMotor(direction);
					waitForStop();
				}
				
			}
	
		};
		
	}
	
	public boolean close() {
		if (debug) System.out.println("Closing motor.");

		try {
			motor.close();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			error = true;
			return false;
			// We were not able to close the port. Not much we can do anymore.
		} finally {
			setStatus(false);
		}
		
		return true;
		
		
	}





	public void setSpeed(int i) {
		try {
			motor.setSpeed(i);
		} catch (RemoteException e) {
			if (debug) System.out.println("Was not able to set speed for motor " + motorId);
			error = true;
		}
		
	}

	// rotates the motor. direction true = forward. direction false = backwards.
	public void rotate(Integer angle) {
		if (debug) System.out.println("Rotating motor " + motorId + " with angle " + angle + " and direction ");
		
		
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("PFPMotor with id " + motorId + "\n");
		str.append("debug is " + debug + " error is " + error);
		return str.toString();
	}
	
	/* ------ */
	/* Sensor */
	/* ------ */
	
	PFPSensor sensor = null;
	
	// This gets called when a sensor that this motor is registered to gets a new value
	@Override
	public void inform(boolean b) {
		if (debug) System.out.println("Sensor " + sensor.getDescription() + " sends " + b);
		setSensor(b);
	}	

	private void setSensor(boolean b) {
		sensorStop = b;
	}
	
	// This gets called by the sensor
	@Override
	public void register(PFPSensor s) {
		if (debug) System.out.println(s != null ? "Registering motor " + motorId + " on sensor " + s.getDescription() : "Deleting motor " + motorId + " on sensor" + s.getDescription());
		sensor = s;
	}
	
	// This waits for a stop signal either from a sensor or the controller
	public void waitForStop() {
		while (!getSensorStop() && !getMotorStop()) {
			try {
				Thread.sleep(interval);
				if (getTime() % 100 == 0)
					if (debug) System.out.println("Sensorstop " + getSensorStop() + " Motorstop " + getMotorStop());
			} catch (InterruptedException e) {
				if (debug) System.out.println("Motor " + motorId + " was interrupted.");
				emergencyStop();
			} finally {
				// Reset the internal sensor value
				setSensor(false);
			}
		}
	}
	
	
	public void setDebug(boolean deb) {
		debug = deb;
	}
	
	public static void setExecutioner(ExecutorService exec) {
		executor = exec;
	}



}
