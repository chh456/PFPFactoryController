package de.uniks.pfp.testing;

import java.util.concurrent.TimeUnit;

import de.uniks.pfp.database.Database;

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
		
	}

}
