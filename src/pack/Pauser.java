package pack;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class Pauser 
{

	static SampleProvider colorSampleProvider = new EV3ColorSensor(SensorPort.S2).getRedMode();
	
	static float[] colorSample = new float[colorSampleProvider.sampleSize()];
	
	public List<Pausable> objects = new ArrayList<Pausable>();
	
	public void run()
	{
		
		while (true)
		{
		
			colorSampleProvider.fetchSample(colorSample, 0);
			
			if (colorSample[0] == 0)
				for (int i = 0; i < objects.size(); i++)
					objects.get(i).stop();
			
		}
		
	}
	
}
