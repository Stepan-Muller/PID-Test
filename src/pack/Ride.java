package pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
	String program;
	
	public Ride(String _fileName, MotorPid motorLPid, MotorPid motorRPid, UnregulatedMotor motorL, UnregulatedMotor motorR, SampleProvider gyroSampleProvider)
	{
		
		values = new ArrayList<MenuItem>();
		values.add(new MenuItem("S konst. P", 1, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S konst. I", 0, 0.0001f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S konst. D", 0, 1000, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S I limit", 100, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S delay", 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S accel rate", 10, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S max speed", 75, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		values.add(new MenuItem("S min speed", 35, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, false));
		
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
		
		fileName = _fileName;
		
		this.name = fileName;
		this.secondaryFunction = true;
		this.maxValue = 0;
		this.minValue = 0;
		
		File file = new File(fileName + ".txt");
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
		
		for (int i = 0; i < values.size(); i++)
			values.get(i).value = scanner.nextFloat();
			
		scanner.nextLine();
		
  		while (scanner.hasNextLine()) 
  		{
  			
  			program = program + System.lineSeparator() + scanner.nextLine();
  			
      	}
  		
  		scanner.close();
		
  		setPidValues();
  		
	}
	
	@Override
	public void secondFunction()
	{
		
		@SuppressWarnings("unused")
		Menu subMenu = new Menu(values);
		
		setPidValues();
		save();
		
	}
	
	@Override
	public void run()
	{
		
		Scanner scanner = new Scanner(program);
		String  line;
    	Scanner lineScanner = null;
		String command = null;
		
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
	
	@Override
	public void save()
	{
		
		File file = new File(fileName + ".txt");
		FileWriter writer = null;
		
		try 
		{
			
			writer = new FileWriter(file);
			
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
			
		}
		
		for (int i = 0; i < values.size(); i++)
			try 
			{
				
				writer.write(values.get(i).value + " ");
				
			} 
			catch (IOException e) 
			{
				
				e.printStackTrace();
				
			}
		
		try 
		{
			
			writer.write(System.lineSeparator() + program);
			
			writer.flush();
			writer.close();
			
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
			
		}
		
	}
	
	public void setPidValues()
	{
		
		for (int i = 0; i < pid.values.size(); i++)
			pid.values.set(i, values.get(i));
		
		for (int i = 0; i < turn.values.size(); i++)
			turn.values.set(i, values.get(i + pid.values.size()));

	}
	
}
