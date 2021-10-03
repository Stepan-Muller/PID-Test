package pack;

import lejos.hardware.port.*;
import lejos.utility.Delay;
import lejos.hardware.Button;

public class Main
{
	
	public static void main(String[] args) 
	{
        
		float p = 6;
		float i = 0.0003f;
		float d = 10000;
		float goal;
		
		Pid pid = new Pid(p, i, d, 75, 0, 10, MotorPort.B, MotorPort.C, SensorPort.S1);
		
		int buttonId;
		
		while (true)
		{
			
			buttonId = Button.waitForAnyPress();
			
			if (buttonId == Button.ID_DOWN)
				d -= 100f;
			if (buttonId == Button.ID_UP)
				d += 100f;
			
			if (buttonId == Button.ID_LEFT)
				i -= 0.0001f;
			if (buttonId == Button.ID_RIGHT)
				i += 0.0001f;
			
			if (buttonId == Button.ID_ESCAPE)
			{
				
				p = 0;
				i = 0;
				d = 0;
				
			}
			
			System.out.println("P:" + p + ", I:" + i + ", D:" + d);
			
			if (buttonId == Button.ID_ENTER)
			{
				
				pid.kP = p;
				pid.kI = i;
				pid.kD = d;
				
				goal = pid.gyro();
				
				pid.run(1000, goal, 100, 40);
				Delay.msDelay(500);
				//pid.runBack(1000, goal);
				
			}
			
		}
		
    }

}
