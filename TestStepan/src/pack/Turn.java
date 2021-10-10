package pack;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.Sound;

public class Turn extends MenuItem 
{

	List<MenuItem> values;
	
	UnregulatedMotor motorL;
	UnregulatedMotor motorR;
	SampleProvider gyroSampleProvider;
	
	float p;
	float i;
	float d;
	float acceleration;
	float minPortion;
	float accelDistance;
	float error;
	float lastError;
	float originalAngle;
	int fromLastError;
	int motorLSpeed;
	int motorRSpeed;
	static float[] gyroSample;
	
	public Turn()
	{
		
		values = new ArrayList<MenuItem>();
		values.add(new MenuItem("konst. P", 0.015f, 0.001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("konst. I", 0/*.00002f*/, 0.000001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("konst. D", /*200*/0, 100, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("I limit", 0.5f, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("delay", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("accel rate", 1, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("max speed", 360, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("min speed", 45, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("end delay", 100, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		
		this.increment = 10;
		this.secondaryFunction = true;
		
	}
	
	@Override
	public void secondFunction()
	{
		
		Menu subMenu = new Menu(values);
		
	}
	
	@Override
	public void run()
	{
		
		gyroSample = new float[gyroSampleProvider.sampleSize()];
		
		gyroSampleProvider.fetchSample(gyroSample, 0);
		
		run(value);
		
	}
	
	public void run(float goal)
	{
		
		final MotorPid motorLPid = new MotorPid();
		
		Runnable lRunnable = new Runnable() 
		{
		
			public void run()
			{
				
				motorLPid.run();
				
			}
			
		};
		
		new Thread(lRunnable).start();
		
		motorLPid.motor = motorL;
		
		final MotorPid motorRPid = new MotorPid();
		
		Runnable rRunnable = new Runnable() 
		{
		
			public void run()
			{
				
				motorRPid.run();
				
			}
			
		};
		
		new Thread(rRunnable).start();
		
		motorRPid.motor = motorL;
		
		gyroSample = new float[gyroSampleProvider.sampleSize()];
		originalAngle = gyroSample[0];
		
		fromLastError = 0;
		i = 0;
		
		minPortion = values.get(7).value / values.get(6).value;
		accelDistance = (values.get(6).value - values.get(7).value) * values.get(5).value;
		
		if(accelDistance > Math.abs(goal - originalAngle) / 2)
			accelDistance = Math.abs(goal - originalAngle) / 2;
		
		while(fromLastError < values.get(8).value)
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
			
			if (Math.abs(gyroSample[0]) < accelDistance)
				acceleration = Math.abs(gyroSample[0]) * (1 - minPortion) / accelDistance + minPortion;
			else if (Math.abs(gyroSample[0] - goal) > accelDistance)
				acceleration = Math.abs(goal - gyroSample[0]) * (1 - minPortion) / accelDistance + minPortion;
			else
				acceleration = 1;
			
			motorLSpeed = (int) (acceleration * (p + i) * values.get(6).value);
			motorRSpeed = (int) (acceleration * (p + i) * values.get(6).value);
			
			if (motorLSpeed > 100)
				motorLSpeed = 100;
			if (motorLSpeed < -100)
				motorLSpeed = -100;
			if (motorRSpeed < -100)
				motorRSpeed = -100;
			if (motorRSpeed > 100)
				motorRSpeed = 100;
			
			if(error == 0f)
				fromLastError++;
			else
				fromLastError = 0;
			lastError = error;
			
			motorLPid.speed = motorLSpeed;
			motorRPid.speed = motorRSpeed;
			
			Delay.msDelay((long) values.get(4).value);

		}
		
		motorLPid.speed = 0;
		motorRPid.speed = 0;

		Sound.beep();
	}
	
}
