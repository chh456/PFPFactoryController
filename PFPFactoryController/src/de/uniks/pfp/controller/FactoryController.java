package de.uniks.pfp.controller;
 
import de.uniks.pfp.model.*;

public class FactoryController {

	public static void main(String[] args) {
		
		State s1 = new State("Leerer Ladungsträger steht auf Anlieferungsförderband");
		State s2 = new State("Voller Ladungsträger steht auf Anlieferungsförderband");
		Transition t1 = new Transition("Fülle Ladungsträger auf Anlieferungsförderband", s1, s2);

		System.out.println(s1.isConnected(s2) != -1 ? "yes" : "no");
		
	}

}
