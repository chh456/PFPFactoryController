package de.uniks.pfp.hardware;

import java.rmi.RemoteException;

import de.uniks.pfp.interfaces.SensorListener;
import lejos.remote.ev3.RMIRegulatedMotor;

public class PFPMotor implements SensorListener {

	RMIRegulatedMotor motor; 
	Character type; // Large or medium
	String port;
	boolean sensorStop;
	
	String id;
	
	public PFPMotor(RMIRegulatedMotor m, String port, Character type) {
		motor = m;
		this.type = type;
		this.port = port;
		sensorStop = false;
	}
	
	public PFPMotor(RMIRegulatedMotor m, String name) {
		motor = m;
		id = name;
	}
	
	public static PFPMotor createMotorWithName(RMIRegulatedMotor m, String name) {
		return new PFPMotor(m, name);
	}
	
	public char getType() {
		return type;
	}
	
	public void forward() {
		try {
			motor.forward();
//			motor.rotate(15000, true);
			// int counter = 0;
			System.out.println("Sensorstatus " + sensorStop);
			while(!sensorStop) {
				Thread.sleep(10);
				
			}
			motor.stop(true);
			sensorStop = false;
			System.out.println("Motor stopped.");
			// System.out.println("Counter ist " + counter);
		} catch (RemoteException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		System.out.println("Closing motor.");
		// TODO Auto-generated method stub
		try {
			motor.close();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void backward() {
		try {
			motor.backward();
			System.out.println("Sensorstatus " + sensorStop);
			while(!sensorStop) {
				Thread.sleep(10);
				
			}
			motor.stop(true);
			sensorStop = false;
			System.out.println("Motor stopped.");
		} catch (RemoteException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
