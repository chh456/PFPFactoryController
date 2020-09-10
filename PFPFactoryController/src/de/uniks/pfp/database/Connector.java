package de.uniks.pfp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/* This class connects you to the model and simulation database and returns
 * a Connection object.
 * 
 */

public class Connector {

	private static String ip = "192.168.0.121";
	private static Integer port = 3306;
	
	private static String username = "pfp";
	private static String password = "pfp";
	
	private static String simName = "simulation";
	public static Connection simCon;
	
	private static String modName = "model";
	public static Connection modCon;
	
	private static ArrayList<Connection> connections = new ArrayList<>();
	
	private static int internalError = 0;
	
	public static Connection connectSim() {
		if (simCon != null)
			close(simCon);
		simCon = connect(simName);
		return simCon;
	}
	
	public static Connection connectModel() {
		if (modCon != null)
			close(modCon);
		modCon = connect(modName);
		return modCon;
	}
	
	private static Connection connect(String type) {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mariadb://" + ip + ":" + port + "/" + type + "?user=" + username + "&password=" + password + "&createDatabaseIfNotExist=True");
			connections.add(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	public static void close() {
		for (Connection con : connections) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean close(Connection con) {
		boolean res = false;
		try {
			con.close();
			res = true;
		} catch (SQLException e) {
			internalError = e.getErrorCode();
		}
		return res;
	}

	public static String getSimName() {
		return simName;
	}

	public static void setSimName(String simName) {
		Connector.simName = simName;
	}

	public static String getModName() {
		return modName;
	}

	public static void setModName(String modName) {
		Connector.modName = modName;
	}
	
}
