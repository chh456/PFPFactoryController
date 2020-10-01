package de.uniks.pfp.testing;

import de.uniks.pfp.exceptions.MotorNotSetException;
import de.uniks.pfp.hardware.PFPMotor;
import de.uniks.pfp.hardware.PFPSensor;
import de.uniks.pfp.interfaces.SensorListener;

public class MotorMovement {

	// The motor that is moved by this movement
	PFPMotor motor;
	
	// Duration the motor should rotate: time in ms or null if angle is used
	Integer duration;
	
	// Direction the motor should rotate: null, forward, backward
	Boolean direction;
	
	// Angle the motor should be moved: angle in degree or null if duration is used
	Integer angle;
	
	// Rotate speed
	Integer speed;
	
	public MotorMovement(PFPMotor m) {
		motor = m;
	}
	
	public void start() throws MotorNotSetException {
		if (motor != null) {
			
			if (angle != null) { // Rotate motor with angle
				motor.rotate(angle);
			} else if (duration != null) { // Rotate motor duration
				if (direction)
					motor.forward(duration);
				else
					motor.backward(duration);
				
			}
		} else throw new MotorNotSetException("Motor was not set in movement " + this);
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
