package de.uniks.pfp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.uniks.pfp.hardware.PFPBrick;
import de.uniks.pfp.hardware.PFPMotor;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ExecutorService executor = Executors.newCachedThreadPool();
		
		PFPBrick b103 = new PFPBrick("192.168.0.103");
		b103.init();
		
		PFPMotor anlieferungfb = new PFPMotor(b103.brick.createRegulatedMotor("A", 'L'), "anlieferungfb");
		PFPMotor.setExecutioner(executor);
		anlieferungfb.setDebug(false);
		
		
		anlieferungfb.forward();
		System.out.println(System.currentTimeMillis());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis());
		anlieferungfb.setMotorStop(true);
		
		System.out.println(anlieferungfb);
		
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		anlieferungfb.backward();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		anlieferungfb.setMotorStop(true);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		anlieferungfb.close();
		
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.shutdownNow();
		
	}

}
