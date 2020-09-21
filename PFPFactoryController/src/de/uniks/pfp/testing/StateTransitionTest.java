package de.uniks.pfp.testing;

import java.util.ArrayList;

import de.uniks.pfp.hardware.PFPBrick;
import de.uniks.pfp.model.Transition;

public class StateTransitionTest {

	ArrayList<PFPBrick> bricks = new ArrayList<>();
	
	private static String ipMask = "192.168.0.";
	
	public static Transition createTransition() {
		Transition tr = null;
		
		return tr;
	}
	
	// Takes an id and initialises the hardware
	// 000
	public static void createHwFromId(String str) {
		// Brick first three digits
		
		Integer brickId = Integer.parseInt(str.substring(0, 3));
		System.out.println(str.substring(0, 3));
		String brickIp = ipMask + brickId;
		System.out.println(brickIp);
		
		String type = str.substring(4,4);
		
		switch (type) {
		case "A":
			System.out.println("It's a motor.");
			break;
		}
		
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String brickTest = "001AL";
		createHwFromId(brickTest);
		
		
	}

}
