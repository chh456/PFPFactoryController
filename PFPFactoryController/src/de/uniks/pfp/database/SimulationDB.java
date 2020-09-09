package de.uniks.pfp.database;

import java.sql.Connection;

/* This class handles the simulation part of the factory controller
 * 
 * It needs a valid Connection object
 * 
 */

public class SimulationDB {

	static Connection connection;
	
	public SimulationDB(Connection con) {
		connection = con;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Database db = new Database();
		db.getSimConnection();
		// db.connectSimulation();
		
		
		db.disconnect();
	}

}
