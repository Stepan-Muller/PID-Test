package pack;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Pid
{

	float kP;
	float kI;
	float kD;
	float iLimit;
	long delay;
	float accelRate;
	
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
	
	static UnregulatedMotor motorL;
	static UnregulatedMotor motorR;
	
	static SampleProvider gyroSampleProvider;
	
	static float[] gyroSample;

	public Pid(float _kP, float _kI, float _kD, float _iLimit, long _delay, float _accelRate, Port motorPortL, Port motorPortR, Port gyroPort)
	{
		
		kP = _kP;
		kI = _kI;
		kD = _kD;
		iLimit = _iLimit;
		delay = _delay;
		accelRate =_accelRate;
		
		motorL = new UnregulatedMotor(motorPortL);
		motorR = new UnregulatedMotor(motorPortR);
		
		gyroSampleProvider = new EV3GyroSensor(gyroPort).getAngleMode();
		
		gyroSample = new float[gyroSampleProvider.sampleSize()];
		
	}
	
	public void run(int distance, float goal, int maxSpeed, int minSpeed)
	{
		
		i = 0;
		
		motorL.resetTachoCount();
		motorR.resetTachoCount();
		
		distanceTravelled = 0;
		
		minPortion = (float)minSpeed / (float)maxSpeed;
		accelDistance = (maxSpeed - minSpeed) * accelRate;
		
		if(accelDistance > distance / 2)
			accelDistance = distance / 2;
		
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
				acceleration = distanceTravelled * (1 - minPortion) / accelDistance + minPortion;
			else if (distanceTravelled > distance - accelDistance)
				acceleration = (distance - distanceTravelled) * (1 - minPortion) / accelDistance + minPortion;
			else
				acceleration = 1;
			
			motorLSpeed = (int) (acceleration * (maxSpeed + p + i + d));
			motorRSpeed = (int) (acceleration * (-1 * maxSpeed + p + i + d));
			
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
			
			Delay.msDelay(delay);
			
			distanceTravelled = (motorL.getTachoCount() - motorR.getTachoCount()) / 2;
			
		}
		
		motorL.setPower(0);
		motorR.setPower(0);
		
	}

	public void runBack(int distance, float goal)
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
		
	}
	
	public float gyro()
	{
		
		gyroSampleProvider.fetchSample(gyroSample, 0);
		
		return gyroSample[0];
		
	}
	
}
 