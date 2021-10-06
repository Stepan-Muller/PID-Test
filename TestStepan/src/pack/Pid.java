package pack;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Pid extends MenuItem
{

	List<MenuItem> values;
	
	UnregulatedMotor motorL;
	UnregulatedMotor motorR;
	SampleProvider gyroSampleProvider;
	
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
	
	public Pid()
	{
		
		values = new ArrayList<MenuItem>(8);
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
		
		run((int) value, gyroSample[0]);
		
	}
	
	public void run(int distance, float goal)
	{
		
		gyroSample = new float[gyroSampleProvider.sampleSize()];
		
		i = 0;
		
		motorL.resetTachoCount();
		motorR.resetTachoCount();
		
		distanceTravelled = 0;
		
		minPortion = values.get(7).value / values.get(6).value;
		accelDistance = (values.get(6).value - values.get(7).value) * values.get(5).value;
		
		if(accelDistance > distance / 2)
			accelDistance = distance / 2;
		
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
			
			motorL.setPower(motorLSpeed);
			motorR.setPower(motorRSpeed);
			
			lastError = error;
			
			Delay.msDelay((long) values.get(4).value);
			
			distanceTravelled = (motorL.getTachoCount() - motorR.getTachoCount()) / 2;

		}
		
		motorL.setPower(0);
		motorR.setPower(0);
		
	}

	/*public void runBack(int distance, float goal)
{
		
		i = 0;
		
		motorL.resetTachoCount();
		motorR.resetTachoCount();
		
		distanceTravelled = 0;
		
		firstPortion = firstSpeed / baseSpeed;
		lastPortion = lastSpeed / baseSpeed;
		
		while(distanceTravelled <= distance)
		{
			
			gyroSampleProvider.fetchSample(gyroSample, 0);
			
			error = gyroSample[0] - goal;
			
			p = error * kP;
			i = i + (error * kI);
			d = (error - lastError) * kD;
			
			if (i > iLimit)
				i = iLimit;
			if (i < -1 * iLimit)
				i = -1 * iLimit;
			
			if (distanceTravelled < accelDistance)
				acceleration = distanceTravelled * (1 - firstPortion) / accelDistance + firstPortion;
			else if (distanceTravelled > distance - decelDistance)
				acceleration = (distanceTravelled - distance) * (1 - lastPortion) / decelDistance * -1 + lastPortion;
			else
				acceleration = 1;
			
			motorRSpeed = (int) (acceleration * (baseSpeed + p + i + d));
			motorLSpeed = (int) (acceleration * (-1 * baseSpeed + p + i + d));
			
			if (motorRSpeed > 100)
				motorRSpeed = 100;
			if (motorRSpeed < 0)
				motorRSpeed = 0;
			if (motorLSpeed < -100)
				motorLSpeed = -100;
			if (motorLSpeed > 0)
				motorLSpeed = 0;
			
			motorL.setPower(motorLSpeed);
			motorR.setPower(motorRSpeed);
			
			lastError = error;
			
			Delay.msDelay(delay);
			
			distanceTravelled = (motorR.getTachoCount() - motorL.getTachoCount()) / 2;
			
		}
		
		motorL.setPower(0);
		motorR.setPower(0);
		
	}*/
	
}
 