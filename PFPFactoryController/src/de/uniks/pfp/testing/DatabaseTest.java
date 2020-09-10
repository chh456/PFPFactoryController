package de.uniks.pfp.testing;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import de.uniks.pfp.database.Connector;
import de.uniks.pfp.database.Database;
import de.uniks.pfp.database.Simulation;

public class DatabaseTest {

	static Database db;
	
	public static void createTimeTest() {
		String sql = "CREATE TABLE TimeTest ("
				+ "Motormovement TEXT, "
				+ "TransId Integer, "
				+ "MotorId Integer, "
				+ "Start "
				+ ");";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		db = new Database();
		// db.createDatabaseFile(db.path);
		// db.connect();
		
		long startTime = System.currentTimeMillis();
		long exactStart = System.nanoTime();
		
		int i = 0;
		while (i < 10) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		
		
		
		
		System.out.println(System.nanoTime());
		
		long exactEnd = System.nanoTime();
		long endTime = System.currentTimeMillis();
		
		
		System.out.println("nanoTime: " + TimeUnit.NANOSECONDS.toMillis(exactEnd - exactStart));
		System.out.println("milliTime: " + TimeUnit.MILLISECONDS.toMillis(endTime - startTime));
		// db.disconnect();
		
		Connection con = Connector.connectSim();
		
		Simulation sim = null;
		try {
			sim = new Simulation(con, "Testsimulation");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sim.setName("Test123");
		sim.createTables();
		// sim.createTables();
		// sim.alter("ALTER TABLE Motors RENAME TO SimulationMotor");
		String simName = "Erste Sim";
		Long start = System.currentTimeMillis();
		System.out.println(start);
		try {
			Thread.sleep(1345);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long end = System.currentTimeMillis();
		System.out.println(end);
		// sim.alter("INSERT INTO Simulation (simName, simStart, simEnd) VALUES ('"+ simName + "', " + start + ", " + end +"); SELECT LAST_INSERT_ID()");
		// sim.createSimulation();
		sim.end(end);
		Connector.close(con);
		
	}

}
