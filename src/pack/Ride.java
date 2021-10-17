package pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.robotics.SampleProvider;

public class Ride extends MenuItem
{
	
	List<MenuItem> values;
	
	Pid pid;
	Turn turn;
	
	String fileName;
	
	public Ride(MotorPid motorLPid, MotorPid motorRPid, UnregulatedMotor motorL, UnregulatedMotor motorR, SampleProvider gyroSampleProvider)
	{
		
		fileName = "filename.txt";
		
		values = new ArrayList<MenuItem>();
		values.add(new MenuItem("S konst. P", 0.003f, 0.001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S konst. I", 0, 0.00001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S konst. D", 0, 1000, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S I limit", 1, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S delay", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S accel rate", 2, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S max speed", 270, 5, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S min speed", 15, 5, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S end delay", 10, 100, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		
		values.add(new MenuItem("T konst. P", 0.003f, 0.001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T konst. I", 0, 0.00001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T konst. D", 0, 1000, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T I limit", 1, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T delay", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T accel rate", 2, 0.1f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T max speed", 270, 5, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T min speed", 15, 5, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("T end delay", 10, 100, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		
		pid = new Pid(motorLPid, motorRPid, motorL, motorR, gyroSampleProvider);
		turn = new Turn(motorLPid, motorRPid, gyroSampleProvider);
		
		this.name = fileName;
		this.secondaryFunction = true;
		this.maxValue = 0;
		this.minValue = 0;
		
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
		
		File file = new File(fileName);
      	Scanner scanner;
      	
		try 
		{
			
			scanner = new Scanner(file);
			
		} 
		catch (FileNotFoundException e) 
		{
			
			System.out.println("Soubor nenalezen");
			return;
			
		}
		
		String  line;
    	Scanner lineScanner = null;
		String command;
		
  		while (scanner.hasNextLine()) 
  		{
  			
  			line = scanner.nextLine();
  			lineScanner = new Scanner(line);
  			command = lineScanner.next();
  			
  			if (command.equals("Straight"))
  			{
  				
  				pid.run(lineScanner.nextInt(), lineScanner.nextInt());
  				
  			}
  			else if (command.equals("Turn"))
  			{
  				
  				turn.run(lineScanner.nextInt());
  				
  			}
  			
      	}
  		
  		scanner.close();
  		lineScanner.close();
  		
	}
	
}
