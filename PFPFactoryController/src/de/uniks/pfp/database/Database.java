package de.uniks.pfp.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* MariaDB server with root rights
 * 
 * create database javatest;
 * grant all privileges on javatest.* TO 'chh'@'%' identified by '';
 * 
 */

public class Database {

	// private static String ip = "192.168.0.7";
	private static String ip = "[fe80::4982:525:dd05:4eac]";
	private static Integer port = 3306;
	
	private static String username = "chh";
	private static String password = "";
	final private static String database_sim = "pfp_simulation";
	final private static String database_model = "pfp_model";
	
	
    public static String path = "db\\database.db";
    private static Connection connection = null;
    private static Connection connection_model = null;
    private static Connection connection_sim = null;

    public void createDatabaseFile(String name) {
        File db = new File(name);
        if (!db.exists()) {
            try {
                db.createNewFile();
                path = name;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    
    public Connection getSimConnection() {
    	if (connection_sim == null)
    		connect(database_sim);
    	
    	return connection_sim;
    }
    
    public Connection connect(String dbName) /* throws FileNotFoundException */ {

    	Connection con = null;
    	
    	try {
	    	switch (dbName) {
	    		case database_sim:
	    			con = DriverManager.getConnection("jdbc:mariadb://" + ip + ":" + port + "/" + database_sim + "?user=" + username + "&password=" + password + "&createDatabaseIfNotExist=True");
	    			connection_sim = con;
	    			break;
	    			
	    		case database_model:
	    			con = DriverManager.getConnection("jdbc:mariadb://" + ip + ":" + port + "/" + database_model + "?user=" + username + "&password=" + password + "&createDatabaseIfNotExist=True");
	    			connection_model = con;
	    			break;
	    	}
    	} catch (SQLException e) {
    		e.printStackTrace();
    		System.out.println(e.getErrorCode());
    	}
    	

        return con;

    }

    public void disconnect() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}