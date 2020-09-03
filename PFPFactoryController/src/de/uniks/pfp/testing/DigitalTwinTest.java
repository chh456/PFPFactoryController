package de.uniks.pfp.testing;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.uniks.pfp.controller.FactoryController;
import de.uniks.pfp.digitaltwin.DigitalTwin;
import de.uniks.pfp.digitaltwin.SimTalkSim;
import de.uniks.pfp.hardware.PFPBrick;
import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.model.State;
import de.uniks.pfp.model.Transition;

public class DigitalTwinTest {

	static Boolean done = false;
	
	public static void setDone(Boolean b) {
		done = b;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newCachedThreadPool();
		PFPBrick b102 = new PFPBrick("192.168.0.102");
		b102.init();
		
		if (b102.brick == null) {
			System.out.println("brick ist null");
			System.exit(-1);
		}
			
		DigitalTwin twin = null;
		try {
			twin = new DigitalTwin();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (twin == null) {
			System.out.println("Twin ist null.");
			System.exit(-1);
		}
		
		System.out.println("ST");
		twin.sendMessage("ST");
		
		PFPMotor fuellturm = new PFPMotor(b102.brick.createRegulatedMotor("A", 'L'), "fuellturm");
		PFPMotor.setExecutioner(executor);
		
		
		// State s1 = new State("Warte auf Ladungsträger.");
		State s2 = new State("Voller Ladungsträger auf Anlieferungsförderband.");
		State s1 = new State("Leerer Ladungsträger auf Anlieferungsförderband.");
		
		Transition t1 = new Transition("Füllturm füllt Ladungsträger auf Anlieferungsförderband", s1, s2);

		
		
		t1.setProcedure(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Starte " + t1.getDescription());
				fuellturm.setSpeed(100);
				fuellturm.forward(5000);
				setDone(true);
				// FactoryController.setCurrentState(t1.getEnd());
				System.out.println("Ende " + t1.getDescription());
				// tcurrentState.(t1.getEnd());
			}
			
		});
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("CA");
		twin.sendMessage("CA");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("CL");
		twin.sendMessage("CL");
		
		
		executor.execute(t1.procedure);
		
		try {
			while(!done) {
				Thread.sleep(50);
				System.out.println("Wird befüllt.");
			}
				
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// CA, container zu container füllen
		// CV, container - anlieferung
		// CL, container ist leer befüll den
		// CB, befüllen
		// CD container zu drehtisch
		
		
		
		
		System.out.println("CB");
		twin.sendMessage("CB");
		
		
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("CD");
		twin.sendMessage("CD");
		
		fuellturm.close();
		twin.closeSocket();
		executor.shutdown();
		
	}

}
