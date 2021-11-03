package pack;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;

public class Keyboard 
{
	
	static GraphicsLCD graphicsLCD = BrickFinder.getDefault().getGraphicsLCD();
	
	static char[] lowerCase = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' '};
	static char[] upperCase = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '};
	static char[] numbers = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', ',', ':', '!', '?', ';', '-', '_', ' '};
	
	static char[][] keys = new char[][]{lowerCase, upperCase, numbers};
	
	public static String keyboard()
	{
		
		int outputIndex = 0;
		int keysIndex = 0;
		int keyboardIndex = 0;
		
		graphicsLCD.setAutoRefresh(false);
		
		char[] output = new char[178 / graphicsLCD.getFont().width];
		
		render(output, outputIndex, keysIndex, keyboardIndex);
		
		while (true)
		{
			
			int buttonID = Button.waitForAnyPress();
			
			if(buttonID == Button.ID_UP)
			{	
				
				keysIndex--;
				if (keysIndex < 0)
					keysIndex = keys[keyboardIndex].length - 1;
				
				render(output, outputIndex, keysIndex, keyboardIndex);
				
			}
			
			if(buttonID == Button.ID_DOWN)
			{
				
				keysIndex++;
				if (keysIndex >= keys[keyboardIndex].length)
					keysIndex = 0;
				
				render(output, outputIndex, keysIndex, keyboardIndex);
				
			}	

			if(buttonID == Button.ID_LEFT)
			{	
				
				output[outputIndex] = keys[keyboardIndex][keysIndex];
				
				outputIndex--;
				if (outputIndex < 0)
					outputIndex = 0;
				
				if (findKey(keys, output[outputIndex]) != null)
				{
					
					keyboardIndex = findKey(keys, output[outputIndex])[0];
					keysIndex = findKey(keys, output[outputIndex])[1];
					
				}	
				
				render(output, outputIndex, keysIndex, keyboardIndex);
				
			}
			
			if(buttonID == Button.ID_RIGHT)
			{
				
				output[outputIndex] = keys[keyboardIndex][keysIndex];
				
				outputIndex++;
				if (outputIndex >= output.length)
					outputIndex = output.length - 1;
				
				if (findKey(keys, output[outputIndex]) != null)
				{
					
					keyboardIndex = findKey(keys, output[outputIndex])[0];
					keysIndex = findKey(keys, output[outputIndex])[1];
					
				}	
				
				render(output, outputIndex, keysIndex, keyboardIndex);
				
			}
			
			if(buttonID == Button.ID_ENTER)
			{
				
				keyboardIndex++;
				
				if (keyboardIndex >= keys.length)
					keyboardIndex = 0;
				
				if (keysIndex >= keys[keyboardIndex].length)
					keysIndex = keys[keyboardIndex].length - 1;
				
				render(output, outputIndex, keysIndex, keyboardIndex);
				
			}
			
			if(buttonID == Button.ID_ESCAPE)
				return String.valueOf(output).trim();
				
		}
		
	}

	static int[] findKey(char[][] array, char key)
	{

	    for (int i = 0; i < array.length; ++i)
	    	for (int j = 0; j < array[i].length; ++j)
	    		if (key == array[i][j])
	    			return new int[]{i, j};
	        	
		return null;
	    
	}
	
	static void render(char[] output, int outputIndex, int keysIndex, int keyboardIndex)
	{
		
		graphicsLCD.clear();
		
		for (int i = 0; i < output.length; i++)
		{
			
			if (i != outputIndex)
				graphicsLCD.drawChar(output[i], i * graphicsLCD.getFont().width, graphicsLCD.getFont().height * (64 / graphicsLCD.getFont().getHeight()), 0);
			
		}
		
		for (int i = 0; i < 128 / graphicsLCD.getFont().getHeight(); i++)
			graphicsLCD.drawString(String.valueOf(keys[keyboardIndex][(i + keysIndex - (64 / graphicsLCD.getFont().getHeight()) + keys[keyboardIndex].length) % keys[keyboardIndex].length]), outputIndex * graphicsLCD.getFont().width, i * graphicsLCD.getFont().getHeight(), 0, i == (64 / graphicsLCD.getFont().getHeight()));
		
		graphicsLCD.refresh();
		
	}
	
}
