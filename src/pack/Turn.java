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

	SampleProvider gyroSampleProvider;
	final MotorPid motorLPid;
	final MotorPid motorRPid;
	
	float p;
	float i;
	float d;
	float acceleration;
	float accelDistance;
	float error;
	float lastError;
	float pid;
	float originalAngle;
	int fromLastError;
	float motorSpeed;
	static float[] gyroSample;
	
	public Turn(UnregulatedMotor motorL, UnregulatedMotor motorR)
	{
		
		values = new ArrayList<MenuItem>();
		values.add(new MenuItem("konst. P", 0.003f, 0.001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("konst. I", 0, 0.00001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("konst. D", 0, 1000, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("I limit", 1, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("delay", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("accel rate", 2, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("max speed", 270, 5, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("min speed", 20, 5, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("end delay", 100, 100, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		
		this.increment = 10;
		this.secondaryFunction = true;
		
		motorLPid = new MotorPid(motorL);
		
		Runnable lRunnable = new Runnable() 
		{
		
			public void run()
			{
				
				motorLPid.run();
				
			}
			
		};
		
		new Thread(lRunnable).start();
		
		motorRPid = new MotorPid(motorR);
		
		Runnable rRunnable = new Runnable() 
		{
		
			public void run()
			{
				
				motorRPid.run();
				
			}
			
		};
		
		new Thread(rRunnable).start();
		
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
		
		gyroSample = new float[gyroSampleProvider.sampleSize()];
		originalAngle = gyroSample[0];
		
		fromLastError = 0;
		i = 0;
		
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
				acceleration = Math.abs(gyroSample[0]) / accelDistance;
			else if (Math.abs(gyroSample[0] - goal) < accelDistance)
				acceleration = Math.abs(goal - gyroSample[0]) / accelDistance;
			else
				acceleration = 1;
			
			pid = p + i + d;
			
			if(pid > 1)
				pid = 1;
			if(pid < -1)
				pid = -1;
			
			motorSpeed = acceleration * pid * values.get(6).value;
			
			if (motorSpeed < values.get(7).value && motorSpeed > 0)
				motorSpeed = values.get(7).value;
			else if (motorSpeed > values.get(7).value * -1 && motorSpeed < 0)
				motorSpeed = values.get(7).value * -1;
			
			if(error == 0f)
				fromLastError++;
			else
				fromLastError = 0;
			lastError = error;
			
			motorLPid.setSpeed(motorSpeed);
			motorRPid.setSpeed(motorSpeed);
			
			Delay.msDelay((long) values.get(4).value);

		}
		
		motorLPid.setSpeed(0);
		motorRPid.setSpeed(0);

		Sound.beep();
	}
	
}