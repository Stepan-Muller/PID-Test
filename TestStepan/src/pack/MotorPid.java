package pack;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import lejos.utility.Timer;

public class MotorPid
{
	
	UnregulatedMotor motor;
	
	float kP = 2;
	float kI = 0.1f;
	float kD;
	float iLimit = 100;
	float speed;
	float delay;
	
	float p;
	float i;
	float d;
	float error;
	float lastError;
	int motorSpeed;
	float goal;
	
	public void run()
	{
		
		i = 0;
		
		motor.resetTachoCount();
		Stopwatch stopwatch = new Stopwatch();
		
		while(true)
		{
			
			goal = goal + speed * stopwatch.elapsed() / 1000;
			
			stopwatch.reset();
			
			error = goal - motor.getTachoCount();
			
			p = error * kP;
			i = i + (error * kI);
			d = (error - lastError) * kD;
			
			if (i > iLimit)
				i = iLimit;
			if (i < -1 * iLimit)
				i = -1 * iLimit;
			
			motorSpeed = (int) (p + i + d);
			
			if (motorSpeed > 100)
				motorSpeed = 100;
			if (motorSpeed < -100)
				motorSpeed = -100;
			
			motor.setPower(motorSpeed);
			
			lastError = error;
			
			Delay.msDelay((long) delay);
			
		}
		
	}
	
}
