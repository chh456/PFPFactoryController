package de.uniks.pfp.controller;
 
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.uniks.pfp.hardware.PFPBrick;
import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.hardware.PFPSensor;
import de.uniks.pfp.model.*;
import de.uniks.pfp.stations.Delivery;
import de.uniks.pfp.stations.RotatingTable;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

public class FactoryController {

	// FactoryLayout
	public class FactoryLayout {
		String brickName;
		String brickIp;
		
		String [] motorPorts = new String[4];
		String [] motorTypes = new String[4];
		String [] sensorPorts = new String[4];
		String [] sensorTypes = new String[4];
	}
	
	static ArrayList<PFPSensor> sensorList = new ArrayList<>();
	static ArrayList<PFPMotor> motorList = new ArrayList<>();
	
// 	static ArrayList<BrickNameAndIp> bricks = new ArrayList<>();
	
	static Map<String, PFPBrick> bricks = new HashMap<String, PFPBrick>();
	
	public static class BrickToIp {
		
		String name;
		String ip;
		PFPBrick brick;
		
		public BrickToIp(String name, String ip) {
			this.name = name;
			this.ip = ip;
		}
		
		public void setPFPBrick(PFPBrick pfpBrick) {
			// TODO Auto-generated method stub
			brick = pfpBrick;
		}
		
	}

static	ArrayList<BrickToIp> brickList = new ArrayList<>();
	
	public static void initBrickList() {
		for (int i = 1; i < 14; i++) {
			String lastDigits = i < 9 ? "0" + i : "" + i;
			String ip = "192.168.0.1" + lastDigits;
			BrickToIp currentBrick = new BrickToIp(lastDigits, ip);
			currentBrick.setPFPBrick(new PFPBrick(ip));
			brickList.add(currentBrick);
		}
	}
	
	public void initStations() {
		
	}
	
	public static int initBricks(ExecutorService executor) {
		int result = 0;
		
		PFPBrick.setExecutor(executor);
		
		String ip = "192.168.0.1";		
		for (int i = 1; i < 14; i++) {
			if (i > 3 && i < 10) continue;
			String currentName = (i < 10) ? "0" + i : "" + i;
			PFPBrick currentBrick = new PFPBrick(ip + currentName);
			
			bricks.put(currentName, currentBrick);
			if (currentBrick.init()) result++;
		}
		
		return result;
	}
	
	static State currentState = null;

	public static State getCurrentState() {
		return currentState;
	}

	public static void setCurrentState(State currentState) {
		FactoryController.currentState = currentState;
	}

	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		PFPBrick b101 = new PFPBrick("192.168.0.101");
		PFPBrick b102 = new PFPBrick("192.168.0.102");
		PFPBrick b103 = new PFPBrick("192.168.0.103");
		PFPBrick b111 = new PFPBrick("192.168.0.111");
		PFPBrick b112 = new PFPBrick("192.168.0.112");
		PFPBrick b113 = new PFPBrick("192.168.0.113");
		
		ArrayList<PFPBrick> bricks = new ArrayList<>();
		
		bricks.add(b101);
		bricks.add(b102);
		bricks.add(b103);
		bricks.add(b111);
		bricks.add(b112);
		bricks.add(b113);
		

		for (PFPBrick b : bricks)
			b.init();
		

		
//		executor.shutdown();
		
	//	System.exit(-1);
		
		PFPSensor drehtischSensor = new PFPSensor(b102.brick.createSampleProvider("S4", "lejos.hardware.sensor.EV3TouchSensor", null), executor);
		PFPSensor drehtischSensorLager = new PFPSensor(b102.brick.createSampleProvider("S1", "lejos.hardware.sensor.EV3TouchSensor", null), executor);
		PFPSensor drehtischSensorEinspeisung = new PFPSensor(b101.brick.createSampleProvider("S1", "lejos.hardware.sensor.EV3TouchSensor", null), executor);
		PFPSensor einspeisungSensor = new PFPSensor(b102.brick.createSampleProvider("S3", "lejos.hardware.sensor.EV3TouchSensor", null), executor);
		
		sensorList.add(drehtischSensor);
		sensorList.add(drehtischSensorLager);
		sensorList.add(drehtischSensorEinspeisung);
		sensorList.add(einspeisungSensor);
		
		for (PFPSensor p : sensorList)
			p.activate();
		
		
		PFPMotor fuellturm = new PFPMotor(b102.brick.createRegulatedMotor("A", 'L'), "fuellturm");
		PFPMotor anlieferungfb = new PFPMotor(b103.brick.createRegulatedMotor("A", 'L'), "anlieferungfb");
		PFPMotor drehtischfb = new PFPMotor(b102.brick.createRegulatedMotor("D", 'L'), "");
		PFPMotor drehtischRotation = new PFPMotor(b102.brick.createRegulatedMotor("C", 'L'), "");
		PFPMotor einspeisungfb = new PFPMotor(b103.brick.createRegulatedMotor("D", 'L'), "");
		PFPMotor greifarmA = new PFPMotor(b101.brick.createRegulatedMotor("A", 'L'), "");
		PFPMotor greifarmB = new PFPMotor(b101.brick.createRegulatedMotor("B", 'L'), "");
		PFPMotor heberA = new PFPMotor(b101.brick.createRegulatedMotor("D", 'L'), "");
		PFPMotor heberB = new PFPMotor(b101.brick.createRegulatedMotor("C", 'L'), "");
		
		motorList.add(fuellturm);
		motorList.add(anlieferungfb);
		motorList.add(drehtischfb);
		motorList.add(drehtischRotation);
		motorList.add(einspeisungfb);
		motorList.add(greifarmA);
		motorList.add(greifarmB);
		motorList.add(heberA);
		motorList.add(heberB);
		
		// System.out.println(initBricks(executor) + " initialisiert");
		
// 		State currentState = new State("Der derzeitige Zustand");
			
			
		// Delivery delivery = new Delivery();
		
		// Station Anlieferung
		
		State s1 = new State("Leerer Ladungstr�ger steht auf Anlieferungsf�rderband");
		State s2 = new State("Voller Ladungstr�ger steht auf Anlieferungsf�rderband");
		Transition t1 = new Transition("F�lle Ladungstr�ger auf Anlieferungsf�rderband", s1, s2);
		Transition t2 = new Transition("Bewege vollen Ladungstr�ger in Richtung Drehtisch", s2, null);
		
		t1.setProcedure(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				fuellturm.setSpeed(100);
				fuellturm.forward(5000);
				FactoryController.setCurrentState(t1.getEnd());
				System.out.println("Setze Zustand t2");
				// tcurrentState.(t1.getEnd());
			}
			
		});
		
		t2.setProcedure(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				drehtischSensor.registerListener(anlieferungfb);
				while(FactoryController.getCurrentState() != t1.getEnd()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Warte auf Ende t1");
				}
				System.out.println("Warte auf Drehtischsensor");
				anlieferungfb.backward();
				drehtischSensor.removeListener(anlieferungfb);
			}
			
		});
		
		// Station Drehtisch
		
		
		State s4 = new State("Ladungstr�ger steht auf Drehtisch");
		State s5 = new State("Drehtisch steht Richtung Anlieferung");
		State s6 = new State("Drehtisch steht Richtung Lager");
		State s7 = new State("Drehtisch steht Richtung Einspeisung");
		
		Transition t3 = new Transition("Ladungstr�ger wird auf Drehtisch bef�rdert", null, s4);
		// Transition t4 = new Transition("Ladungstr�ger verl�sst Drehtisch", null, null);
		Transition t5 = new Transition("Drehtisch dreht Richtung Einspeisung", s5, s6);
		Transition t6 = new Transition("Drehtisch dreht Richtung Lager", s6, s5);
		
		t3.setProcedure(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				drehtischSensor.registerListener(drehtischfb);
				while(FactoryController.getCurrentState() != t1.getEnd()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Warte auf Ende t1");
				}
				System.out.println("Warte auf Drehtischsensor");
				drehtischfb.forward();
				FactoryController.setCurrentState(s4);
				drehtischSensor.removeListener(drehtischfb);
			}
			
		});
		
		t5.setProcedure(new Runnable() {

			@Override
			public void run() {
				drehtischSensorEinspeisung.registerListener(drehtischRotation);
				while(FactoryController.getCurrentState() != s4) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Warte auf Ende t1");
				}
				System.out.println("Warte auf Rotationssensor.");
				drehtischRotation.setSpeed(70);
				drehtischRotation.forward();
				FactoryController.setCurrentState(s7);
				drehtischSensorEinspeisung.removeListener(drehtischRotation);
				
			}
		});
		
		executor.execute(t1.procedure);
		executor.execute(t2.procedure);
		executor.execute(t3.procedure);
		executor.execute(t5.procedure);
		
		
		try {
			while (FactoryController.getCurrentState() != s7)
				Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sensorWork(false);
		for (PFPMotor pMotor : motorList) {
			pMotor.close();
		}
		executor.shutdown();
		
		
// 		System.exit(-1);
		
//		delivery.addBrick(new PFPBrick());
		
		
			// System.exit(-1);
		
		
		
		
		

		
		
		
	
		
		
	
		
		
		
		// rotTable.goIntoState(s6);
		
		
		// rotTable.goIntoState(s5);
		
		
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
