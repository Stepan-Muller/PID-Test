package pack;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;

public class Keyboard 
{
	
	static GraphicsLCD graphicsLCD = BrickFinder.getDefault().getGraphicsLCD();
	
	static char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	public static String keyboard()
	{
		
		int outputIndex = 0;
		int keysIndex = 0;
		
		char[] output = new char[178 / graphicsLCD.getFont().width];
		
		render(output, outputIndex, keysIndex);
		
		while (true)
		{
			
			int buttonID = Button.waitForAnyPress();
			
			if(buttonID == Button.ID_UP)
			{	
				
				keysIndex--;
				if (keysIndex < 0)
					keysIndex = keys.length - 1;
				
				render(output, outputIndex, keysIndex);
				
			}
			
			if(buttonID == Button.ID_DOWN)
			{
				
				keysIndex++;
				if (keysIndex >= keys.length)
					keysIndex = 0;
				
				render(output, outputIndex, keysIndex);
				
			}	

			if(buttonID == Button.ID_LEFT)
			{	
					
				outputIndex--;
				if (outputIndex < 0)
					outputIndex = output.length;
				
				render(output, outputIndex, keysIndex);
				
			}
			
			if(buttonID == Button.ID_RIGHT)
			{
				
				outputIndex--;
				if (outputIndex >= output.length)
					outputIndex = 0;
				
				render(output, outputIndex, keysIndex);
				
			}
			
			if(buttonID == Button.ID_ENTER)
			{
				
				output[outputIndex] = keys[keysIndex];
				
				if (outputIndex < output.length)
					outputIndex++;
				
				render(output, outputIndex, keysIndex);
				
			}
			
			if(buttonID == Button.ID_ESCAPE)
			{
				
				return String.valueOf(output);
				
			}
				
		}
		
	}
	
	static void render(char[] output, int outputIndex, int keysIndex)
	{
		
		for (int i = 0; i < output.length; i++)
		{
			
			if (i != outputIndex)
				graphicsLCD.drawChar(output[i], i * graphicsLCD.getFont().width, graphicsLCD.getFont().width * 10, 0);
			
		}
		
		for (int i = 0; i < 128 / graphicsLCD.getFont().getHeight(); i++)
			graphicsLCD.drawChar(keys[(i + keysIndex - 10) % keys.length], outputIndex * graphicsLCD.getFont().width, i * graphicsLCD.getFont().getHeight(), 0);
		
	}
	
}
