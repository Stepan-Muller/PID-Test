package pack;

import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.GraphicsLCD;

public class Menu
{
	
	static GraphicsLCD graphicsLCD = BrickFinder.getDefault().getGraphicsLCD();
	
	public Menu(List<? extends MenuItem> list)
	{
		
		int selected = 0;
		int scrolled = 0;
		
		int buttonID;
		boolean opened = true;
		
		renderMenu(list, selected, scrolled);
		
		while (opened)
		{
			
			buttonID = Button.waitForAnyPress();
			
			if(buttonID == Button.ID_UP)
			{	
				if(selected > 0)
				{
					
					selected--;
					
					if(selected < scrolled)
						scrolled--;
					
					renderMenu(list, selected, scrolled);
					
				}
				else
					Sound.buzz();
				
			}
			
			if(buttonID == Button.ID_DOWN)
			{
				
				if(selected < list.size())
				{
					
					selected++;
					
					if(selected > scrolled + (128 / graphicsLCD.getFont().getHeight()) - 1)
						scrolled++;
					
					renderMenu(list, selected, scrolled);
					
				}
				else
					Sound.buzz();
				
			}	

			if(buttonID == Button.ID_LEFT)
			{	
					
				if (selected < list.size())
					if (list.get(selected).value >= list.get(selected).minValue + list.get(selected).increment)
					{
						
						list.get(selected).value -= list.get(selected).increment;
						renderMenu(list, selected, scrolled);
						
					}
					else
						Sound.buzz();
				else
					Sound.buzz();
				
			}
			
			if(buttonID == Button.ID_RIGHT)
			{
				
				if (selected < list.size())
					if (list.get(selected).value <= list.get(selected).maxValue - list.get(selected).increment)
					{
						
						list.get(selected).value += list.get(selected).increment;
						renderMenu(list, selected, scrolled);
						
					}
					else
						Sound.buzz();
				else
					Sound.buzz();
				
			}
			
			if(buttonID == Button.ID_ENTER)
			{
				if (selected < list.size())
				{

					list.get(selected).run();
					renderMenu(list, selected, scrolled);
					
				}
				
			}
			
			if(buttonID == Button.ID_ESCAPE)
				if (selected < list.size())
					if (list.get(selected).secondaryFunction) 
					{
						
						list.get(selected).secondFunction();
						renderMenu(list, selected, scrolled);
						
					}
					else
					{
						
						opened = false;
						
					}
				else
					Sound.buzz();
				
		}
		
	}
	
	static void renderMenu(List<? extends MenuItem> list, int selected, int scrolled)
	{
		
		graphicsLCD.clear();
		
		for (int i = 0; i < 128 / graphicsLCD.getFont().getHeight() && i <= list.size(); i++)
		{
			
			if (i + scrolled < list.size())
			{
			
				graphicsLCD.drawString(list.get(i + scrolled).name, 0, i * graphicsLCD.getFont().getHeight(), 0, i + scrolled == selected);
			
				graphicsLCD.drawString(String.valueOf(list.get(i + scrolled).value), 178 - graphicsLCD.getFont().stringWidth(String.valueOf(list.get(i + scrolled).value)), i * graphicsLCD.getFont().getHeight(), 0);
			
			}
			else
			{
				
				graphicsLCD.drawString("+", 0, i * graphicsLCD.getFont().getHeight(), 0, selected == list.size());
				
			}
			
		}

		graphicsLCD.refresh();
		
	}
	
}
