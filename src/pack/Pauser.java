package pack;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Pauser 
{

	static SampleProvider rightColorSampleProvider = new EV3ColorSensor(SensorPort.S2).getRedMode();
	static SampleProvider leftColorSampleProvider = new EV3ColorSensor(SensorPort.S3).getRedMode();
	
	static float[] rightColorSample = new float[rightColorSampleProvider.sampleSize()];
	static float[] leftColorSample = new float[leftColorSampleProvider.sampleSize()];
	
	public List<Pausable> objects = new ArrayList<Pausable>();
	
	public void run()
	{
		
		while (true)
		{
		
			rightColorSampleProvider.fetchSample(rightColorSample, 0);
			leftColorSampleProvider.fetchSample(leftColorSample, 0);
			
			if (rightColorSample[0] == 0 && leftColorSample[0] == 0)
			{
				
				for (int i = 0; i < objects.size(); i++)
					objects.get(i).stop();
				
			}
			
		}
		
	}
	
}
