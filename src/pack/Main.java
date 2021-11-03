package pack;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.Sound;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Font;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class Main
{
	
	static GraphicsLCD graphicsLCD = BrickFinder.getDefault().getGraphicsLCD();
	
	static UnregulatedMotor motorL = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorR = new UnregulatedMotor(MotorPort.C);
	
	static SampleProvider gyroSampleProvider = new EV3GyroSensor(SensorPort.S1).getAngleMode();
	
	static float[] gyroSample = new float[gyroSampleProvider.sampleSize()];
	
	public static void main(String[] args)
	{
		
		graphicsLCD.setAutoRefresh(false);
		graphicsLCD.setFont(Font.getSmallFont());
		
		final MotorPid motorLPid = new MotorPid(motorL);
		
		Runnable lRunnable = new Runnable() 
		{
		
			public void run()
			{
				
				motorLPid.run();
				
			}
			
		};
		
		new Thread(lRunnable).start();
		
		final MotorPid motorRPid = new MotorPid(motorR);
		
		Runnable rRunnable = new Runnable() 
		{
		
			public void run()
			{
				
				motorRPid.run();
				
			}
			
		};
		
		new Thread(rRunnable).start();
		
		final Pauser pauser = new Pauser();
		
		Runnable pauseRunnable = new Runnable()
		{
			
			public void run()
			{
				
				pauser.run();
				
			}
			
		};
		
		new Thread(pauseRunnable).start();
		
		List<Ride> rides = new ArrayList<Ride>();
		
		rides.add(new Ride(Keyboard.keyboard(), motorLPid, motorRPid, motorL, motorR, gyroSampleProvider, pauser));
		rides.add(new Ride(Keyboard.keyboard(), motorLPid, motorRPid, motorL, motorR, gyroSampleProvider, pauser));
		
		pauser.objects.addAll(rides);
		
		Sound.beep();
		
		@SuppressWarnings("unused")
		Menu menu = new Menu(rides);
		
	}
	
}
