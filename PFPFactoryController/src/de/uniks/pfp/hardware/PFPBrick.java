package de.uniks.pfp.hardware;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

public class PFPBrick {

	String ip;
	RemoteEV3 brick = null;
	Boolean initialized = false;
	
	// Number of times we try to init this brick if something soft fails
	static boolean TRYAGAIN = false;
	static int MAXTRIALS = 3;
	private int currentTrials = 0;
	
	// The brick has 4 ports for engines addressed by a letter (= Character) and 4 ports for sensors addressed by a number (= Integer)
//	ArrayList <Port<String, PFPMotor>> engines = new ArrayList<>();
//	ArrayList <Port<Integer, PFPSensor>> sensors = new ArrayList<>();
	

	
	public PFPBrick(String ip) {
		this.ip = ip;
		brick = null;
	}

	public boolean init() {
		initialized = true;
		try {
			brick = new RemoteEV3(ip);
		} catch (RemoteException e) {
			System.out.println("No connection to brick with ip " + ip);
			if (TRYAGAIN) {	
				System.out.println("Trying again.");
				currentTrials++;
				if (currentTrials < MAXTRIALS) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					initialized = init();
				} else initialized = false;
			} else initialized = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			initialized = false;
		} 
		
		return initialized;
	}
	
	public boolean initialized() {
		return initialized;
	}
	
	public void createMotor(String port, PFPMotor newMotor) {
		
		System.out.println("Creating new motor.");
		
		if (initialized && newMotor != null) {
			
			// We check for an old motor on port. If there is one, we close it.
	

			
		}
	
	}
	
}
