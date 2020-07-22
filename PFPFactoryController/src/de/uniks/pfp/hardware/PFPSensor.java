package de.uniks.pfp.hardware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import de.uniks.pfp.interfaces.SensorListener;
import lejos.remote.ev3.RMISampleProvider;


public class PFPSensor {

    ExecutorService executor;
    RMISampleProvider sp;
    
    // Listeners will be notified by this sensor when the state changes
    private ArrayList<SensorListener> listener;

    // Time between two samples
    private static int SAMPLE_FREQ = 200;

    // We keep the sensor deactivated so we can register listeners before starting
    private boolean active = false;

    private boolean lastValue;
    
    public PFPSensor(RMISampleProvider sp, ExecutorService executor) {
    	
    	// Ask the sensor for a sample for one time. If it fails throw an exception.
    	boolean sensorTest = true;
    	
    	try {
    		float f[] = sp.fetchSample();
    		if (f[0] == 1)
    			lastValue = true;
    		else lastValue = false;
			// lastValue = (sp.fetchSample())[0];
		} catch (RemoteException e) {
			sensorTest = false;
		}
    	
    	if (sensorTest)
    		this.sp = sp;
    	// else throw exception
    	
        listener = new ArrayList<>();
        this.executor = executor;
    }

    private Runnable run() {
    	Runnable r = new Runnable() {

			@Override
			public void run() {
				System.out.println("Sensor started");
				
				while(active) {
					try {
						boolean currentValue = getSample();
						if(lastValue != currentValue) {
							inform(currentValue);
							lastValue = currentValue;
						}
						Thread.sleep(SAMPLE_FREQ);
					} catch (InterruptedException e) {
						shutdown();
						e.printStackTrace();
					}
				}
				
				System.out.println("Sensor shutdown");
			}
    		
    	};
    	return r;
    }
    
    private void inform(boolean currentValue) {
    	for (SensorListener s : listener) {
    		s.inform(currentValue);
    	}
    }
    
    private boolean getSample() {
    	float f = -1;
    	boolean status = false;
    	if (sp != null) {
    		try {
				f = (sp.fetchSample())[0];
				if (f == 1)
					status = true;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return status;
    }
    
    public void activate() {
        active = true;
        executor.execute(run());
    }

    public void deactivate() {
        active = false;
    }
    
    public void shutdown() {
    	deactivate();
    	try {
			sp.close();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public boolean registerListener(SensorListener sl) {
        if (!listener.contains(sl)) {
            return listener.add(sl);
        } else return false;
    }

    public boolean removeListener(SensorListener sl) {
        if (listener.contains(sl)) {
            return listener.remove(sl);
        } else return true;
    }

}