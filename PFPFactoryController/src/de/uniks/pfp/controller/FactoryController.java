package de.uniks.pfp.controller;
 
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.hardware.PFPSensor;
import de.uniks.pfp.model.*;
import de.uniks.pfp.stations.RotatingTable;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

public class FactoryController {

	static ArrayList<PFPSensor> sensorList = new ArrayList<>();
	static ArrayList<PFPMotor> motorList = new ArrayList<>();
	
	public static void main(String[] args) {
		
		PFPSensor drehtischSensor;
		
		RemoteEV3 brick102 = null;
		RemoteEV3 brick101 = null;
		
		try {
			brick102 = new RemoteEV3("192.168.0.102");
			brick101 = new RemoteEV3("192.168.0.101");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
			// System.exit(-1);
		
		// RMISampleProvider s = null;
		RMISampleProvider s = brick102.createSampleProvider("S4", "lejos.hardware.sensor.EV3TouchSensor", null);
		RMISampleProvider sensor1 = brick102.createSampleProvider("S1", "lejos.hardware.sensor.EV3TouchSensor", null);
		RMISampleProvider sensor2 = brick101.createSampleProvider("S1", "lejos.hardware.sensor.EV3TouchSensor", null);
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		drehtischSensor = new PFPSensor(s, executor);
		PFPSensor drehtischSensorLager = new PFPSensor(sensor1, executor);
		PFPSensor drehtischSensorEinspeisung = new PFPSensor(sensor2, executor);
		
		sensorList.add(drehtischSensorEinspeisung);
		sensorList.add(drehtischSensorLager);
		sensorList.add(drehtischSensor);
		
		// Station Anlieferung
		
		State s1 = new State("Leerer Ladungstr�ger steht auf Anlieferungsf�rderband");
		State s2 = new State("Voller Ladungstr�ger steht auf Anlieferungsf�rderband");
		Transition t1 = new Transition("F�lle Ladungstr�ger auf Anlieferungsf�rderband", s1, s2);
		Transition t2 = new Transition("Bewege vollen Ladungstr�ger in Richtung Drehtisch", s2, null);

		// Station Drehtisch
		
		
		State s4 = new State("Ladungstr�ger steht auf Drehtisch");
		State s5 = new State("Drehtisch steht Richtung Anlieferung");
		State s6 = new State("Drehtisch steht Richtung Lager");
		State s7 = new State("Drehtisch steht Richtung Einspeisung");
		
		// Transition t3 = new Transition("Ladungstr�ger wird auf Drehtisch bef�rdert", null, null);
		// Transition t4 = new Transition("Ladungstr�ger verl�sst Drehtisch", null, null);
		Transition t5 = new Transition("Drehtisch dreht Richtung Einspeisung", s5, s6);
		Transition t6 = new Transition("Drehtisch dreht Richtung Lager", s6, s5);
		
		RotatingTable rotTable = new RotatingTable(s5, executor);
		
		
		RMIRegulatedMotor drehtischRotation = brick102.createRegulatedMotor("C", 'L');
		PFPMotor dtRot = new PFPMotor(drehtischRotation, "C", 'L');
		drehtischSensorEinspeisung.registerListener(dtRot);
		rotTable.addMotor(dtRot);
		motorList.add(dtRot);
		t5.setProcedure(new Runnable() {

			@Override
			public void run() {
				System.out.println("Transition started.");
				dtRot.setSpeed(100);
				dtRot.forward();
				System.out.println("Transition stopped.");
				drehtischSensorEinspeisung.removeListener(dtRot);
				
			}
			
			
		});
		
		t6.setProcedure(new Runnable() {

			@Override
			public void run() {
				drehtischSensorLager.registerListener(dtRot);
				System.out.println("Transition 2 started.");
				dtRot.setSpeed(100);
				dtRot.backward();
				System.out.println("Transition 2 ended.");
				
			}
			
		});
		
		
		// Activate Sensors
		sensorWork(true);
		
		rotTable.goIntoState(s6);
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		rotTable.goIntoState(s5);
		
		
		// System.out.println(s1.isConnected(s2) != -1 ? "yes" : "no");
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sensorWork(false);
		for (PFPMotor pMotor : motorList) {
			pMotor.close();
		}
		executor.shutdown();
		
	}
	
	// TODO: change name
	public static void sensorWork(boolean activate) {
		for (PFPSensor sensor : sensorList) {
			if (activate)
				sensor.activate();
			else
				sensor.shutdown();
		}
	}
	/*
	 * Transition t1 = new Transition("Füllturm füllt LT", s1, s2);
	Transition t2 = new Transition("FörderbandA transportiert LT auf DT", s2, s3);
	//A für Anlieferung
	Transition t4 = new Transition("Drehtisch dreht Richtung Einspeisung", s3, s5);
	Transition t5 = new Transition("Drehtisch dreht Richtung Einspeisung", s4, s5);
	Transition t6 = new Transition("Drehtisch dreht Richtung Lager", s3, s4);
	Transition t7 = new Transition("Drehtisch dreht Richtung Lager", s5, s4);
	Transition t8 = new Transition("Drehtisch dreht Richtung Anlieferung", s4, s3);
	Transition t9 = new Transition("Drehtisch dreht Richtung Anlieferung", s5, s3);
	Transition t10 = new Transition("FörderbandD transportiert LT auf Station", null, null); 
	//D für Drehtisch; Darf jede transition nur für einen Zustandsübergang verwendet werden?
	Transition t11 = new Transition("Greifer greift LT", s10, s9);
	Transition t12 = new Transition("Kipper hebt LT an", s9, s7);
	Transition t13 = new Transition("Kipper senkt LT ab", s7, s8);
	Transition t14 = new Transition("Greifer öffnet", s8, s10);
	Transition t15 = new Transition("FörderbandE transportiert LT auf DT", s10, s5);


	State s1 = new State ("Leerer LT an Anlieferung");
	State s2 = new State ("Voller LT an Anlieferung");
	State s3 = new State("Drehtisch steht Richtung Anlieferung");
	State s4 = new State("Drehtisch steht Richtung Lager");
	State s5 = new State("Drehtisch steht Richtung Einspeisung");
	State s6 = new State ("Kipper unten");
	State s7 = new State ("Kipper oben");
	State s8 = new State ("Greifer eingefahren");
	State s9 = new State ("Greifer ausgefahren");
	State s10 = new State ("LT an Einspeisung");

	*/
}
