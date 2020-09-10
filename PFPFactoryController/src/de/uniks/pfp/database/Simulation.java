package de.uniks.pfp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.uniks.pfp.hardware.PFPMotor;

public class Simulation {
	
	private String dbName = Connector.getSimName();
	
	private Integer simId;
	private String simName;
	
	private Connection connection;

	ArrayList<String> tables = new ArrayList<>();
	
	
	public Simulation(Connection con, String simName) throws Exception {
		connection = con;
		this.simName = simName;
		createSimulation();
	}
	
	public void createSimulation() throws Exception {
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO Simulation (simName, simStart) VALUES (?,?)";
		try {
			pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, simName);
			pstmt.setLong(2, System.currentTimeMillis());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next())
				simId = rs.getInt("simId");
			else throw new Exception("Was not able to create a new simulation in database.");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pstmt.close();
	}
	

	
	public void alter(String sql) {
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			Integer id = rs.getInt("simId");
			System.out.println("SimId: " + id);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean insertMotor(PFPMotor motor) {
		boolean result = true;
		String sql = "INSERT INTO SimulationMotor (simId, motorId) VALUES (?,?)";
		
		
		return result;
	}
	
	public boolean createTables() {
		
		String simTable = "CREATE TABLE IF NOT EXISTS Simulation ( "
				+ "simId Integer NOT NULL AUTO_INCREMENT, "
				+ "simName TEXT(128), "
				+ "simStart BIGINT, "
				+ "simEnd BIGINT, "
				+ "PRIMARY KEY (simId)"
				+ ");";
		tables.add(simTable);
		
		
		String simTableMotor = "CREATE TABLE IF NOT EXISTS SimulationMotor ( "
				+ "simId Integer NOT NULL, "
				+ "motorId Integer NOT NULL, "
				+ "PRIMARY KEY (simId, motorId)"
				+ ");";
		tables.add(simTableMotor);
		
		String simMotor = "CREATE TABLE IF NOT EXISTS Motor ( "
				+ "motorId Integer, "
				+ "transId Integer, "
				+ "timeId Integer, "
				+ "PRIMARY KEY (motorId)"
				+ ");";
		tables.add(simMotor);
		
		String simTime = "CREATE TABLE IF NOT EXISTS Timings ( "
				+ "timeId Integer, "
				+ "start BIGINT, "
				+ "end BIGINT, "
				+ "PRIMARY KEY(timeId)"
				+ ");";
		tables.add(simTime);
		
		String simSensorMotorWindow = "CREATE TABLE IF NOT EXISTS SensorMotorWindow ( "
				+ "sensorId Integer, "
				+ "motorId Integer, "
				+ "begin BIGINT, "
				+ "end BIGINT"
				+ ");";
		tables.add(simSensorMotorWindow);
		
		String simSensorMotor = "CREATE TABLE IF NOT EXISTS SensorMotor ( "
				+ "sensorId Integer, "
				+ "motorId Integer);";
		// tables.add(simSensorMotor);
		
		String simSensor = "CREATE TABLE IF NOT EXISTS Sensor ( "
				+ "sensorId Integer, "
				+ "fired BIGINT, "
				+ "PRIMARY KEY(sensorId));";
		tables.add(simSensor);
		
		Statement stmt = null;
		
		try {
			for (String current : tables) {
				stmt = connection.createStatement();
				stmt.execute(current);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	public void setName(String string) {
		// TODO Auto-generated method stub
		simName = string;
	}
	
	public void end(Long end) {
		
		String sql = "UPDATE Simulation SET simEnd = " + end + " WHERE simId = " + simId;
		System.out.println(sql);
		Statement st = null;
		try {
			st = connection.createStatement();
			st.execute(sql);
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
