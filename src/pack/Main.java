package pack;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class Main
{
	
	static UnregulatedMotor motorL = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorR = new UnregulatedMotor(MotorPort.C);
	
	static SampleProvider gyroSampleProvider = new EV3GyroSensor(SensorPort.S1).getAngleMode();
	
	static float[] gyroSample = new float[gyroSampleProvider.sampleSize()];
	
	public static void main(String[] args)
	{
		
		LCD.setAutoRefresh(false);
		
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
		
		Sound.beep();
		
		/*List<Pid> pids = new ArrayList<Pid>();
		List<Turn> turns = new ArrayList<Turn>();
		
		pids.add(new Pid(motorLPid, motorRPid, motorL, motorR));
		pids.add(new Pid(motorLPid, motorRPid, motorL, motorR));
		pids.add(new Pid(motorLPid, motorRPid, motorL, motorR));
		
		pids.get(0).values.get(0).value = 1;
		//pids.get(0).values.get(1).value = 0.0003f;
		//pids.get(0).values.get(2).value = 10000;
		pids.get(0).values.get(3).value = 100;
		pids.get(0).values.get(4).value = 0;
		pids.get(0).values.get(5).value = 10;
		pids.get(0).name = "default";
		pids.get(0).values.get(6).value = 75;
		pids.get(0).values.get(7).value = 35;
		pids.get(0).gyroSampleProvider = gyroSampleProvider;*/
		
		List<Ride> rides = new ArrayList<Ride>();
		
		rides.add(new Ride(motorLPid, motorRPid, motorL, motorR, gyroSampleProvider));
		
		@SuppressWarnings("unused")
		Menu menu = new Menu(rides);
		
	}
	
}
