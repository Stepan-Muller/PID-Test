package pack;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Pid extends MenuItem
{

	List<MenuItem> values;
	
	UnregulatedMotor motorL;
	UnregulatedMotor motorR;
	SampleProvider gyroSampleProvider;
	MotorPid motorLPid;
	MotorPid motorRPid;
	
	float p;
	float i;
	float d;
	float acceleration;
	int distanceTravelled;
	float minPortion;
	float accelDistance;
	float error;
	float lastError;
	int motorLSpeed;
	int motorRSpeed;
	static float[] gyroSample;
	
	public Pid(MotorPid _motorLPid, MotorPid _motorRPid, UnregulatedMotor _motorL, UnregulatedMotor _motorR, SampleProvider _gyroSampleProvider)
	{
		
		values = new ArrayList<MenuItem>();
		values.add(new MenuItem("konst. P", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("konst. I", 0, 0.0001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("konst. D", 0, 1000, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("I limit", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("delay", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("accel rate", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("max speed", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("min speed", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		
		this.increment = 100;
		this.minValue = 0;
		this.secondaryFunction = true;
		
		motorLPid = _motorLPid;
		motorRPid = _motorRPid;
		
		motorL = _motorL;
		motorR = _motorR;
		
		gyroSampleProvider = _gyroSampleProvider;
		
	}
	
	@Override
	public void secondFunction()
	{
		
		@SuppressWarnings("unused")
		Menu subMenu = new Menu(values);
		
	}
	
	@Override
	public void run()
	{
		
		gyroSample = new float[gyroSampleProvider.sampleSize()];
		
		gyroSampleProvider.fetchSample(gyroSample, 0);
		
		run((int) value, gyroSample[0]);
		
	}
	
	public void run(int distance, float goal)
	{
		
		gyroSample = new float[gyroSampleProvider.sampleSize()];
		
		i = 0;
		
		int initialTachoCountL = motorL.getTachoCount();
		int initialTachoCountR = motorR.getTachoCount();
		
		distanceTravelled = 0;
		
		minPortion = values.get(7).value / values.get(6).value;
		accelDistance = (values.get(6).value - values.get(7).value) * values.get(5).value;
		
		if(accelDistance > distance / 2)
		{
			
			accelDistance = distance / 2;
			
		}
		
		while(distanceTravelled <= distance)
		{
			
			gyroSampleProvider.fetchSample(gyroSample, 0);
			
			error = gyroSample[0] - goal;
			
			p = error * values.get(0).value;
			i = i + (error * values.get(1).value);
			d = (error - lastError) * values.get(2).value;
			
			if (i > values.get(3).value)
				i = values.get(3).value;
			if (i < -1 * values.get(3).value)
				i = -1 * values.get(3).value;
			
			if (distanceTravelled < accelDistance)
				acceleration = distanceTravelled * (1 - minPortion) / accelDistance + minPortion;
			else if (distanceTravelled > distance - accelDistance)
				acceleration = (distance - distanceTravelled) * (1 - minPortion) / accelDistance + minPortion;
			else
				acceleration = 1;
			
			motorLSpeed = (int) (acceleration * (values.get(6).value + p + i + d));
			motorRSpeed = (int) (acceleration * (-1 * values.get(6).value + p + i + d));
			
			if (motorLSpeed > 100)
				motorLSpeed = 100;
			if (motorLSpeed < 0)
				motorLSpeed = 0;
			if (motorRSpeed < -100)
				motorRSpeed = -100;
			if (motorRSpeed > 0)
				motorRSpeed = 0;
			
			motorLPid.setSpeed(motorLSpeed);
			motorRPid.setSpeed(motorRSpeed);
			
			lastError = error;
			
			Delay.msDelay((long) values.get(4).value);
			
			distanceTravelled = ((motorL.getTachoCount() - initialTachoCountL) - (motorR.getTachoCount() - initialTachoCountR)) / 2;
			
		}
		
		motorLPid.setSpeed(0);
		motorRPid.setSpeed(0);
		
	}
	
}
 