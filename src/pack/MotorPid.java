package pack;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import lejos.utility.Timer;

public class MotorPid
{
	
	UnregulatedMotor motor;
	
	float kP = 2;
	float kI;
	float kD;
	float iLimit = 100;
	float goal;
	float delay = 1;
	
	float p;
	float i;
	float d;
	float error;
	float lastError;
	int motorSpeed;
	
	public MotorPid(UnregulatedMotor _motor)
	{
		
		motor = _motor;
		
	}
	
	public void setSpeed(float speed)
	{
		
		goal = speed;
		
	}
	
	public void run()
	{
		
		Stopwatch stopwatch = new Stopwatch();
		
		i = 0;
		
		int lastTachoCount = motor.getTachoCount();
		
		Delay.msDelay(1);
		
		while(true)
		{
			
			error = goal - (motor.getTachoCount() - lastTachoCount / stopwatch.elapsed() / 1000);
			
			stopwatch.reset();
			lastTachoCount = motor.getTachoCount();
			
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
