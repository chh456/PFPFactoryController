package de.uniks.pfp.interfaces;

import de.uniks.pfp.hardware.PFPSensor;

public interface SensorListener {

	public void inform(boolean b);

	public void register(PFPSensor s);
}
