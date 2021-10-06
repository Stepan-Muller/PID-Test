package pack;

import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class Menu
{
	
	public Menu(List<? extends MenuItem> list)
	{
		
		int buttonID;
		boolean opened = true;
		int selected = 0;
		int scrolled = 0;
		
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
				
				if(selected < list.size() - 1)
				{
					
					selected++;
					
					if(selected > scrolled + 7)
						scrolled++;
					
					renderMenu(list, selected, scrolled);
					
				}
				else
					Sound.buzz();
				
			}	

			if(buttonID == Button.ID_LEFT)
			{	
			
				if(list.get(selected).value >= list.get(selected).minValue + list.get(selected).increment)
				{
					
					list.get(selected).value -= list.get(selected).increment;
					renderMenu(list, selected, scrolled);
					
				}
				else
					Sound.buzz();
				
			}
			
			if(buttonID == Button.ID_RIGHT)
			{
				
				if(list.get(selected).value <= list.get(selected).maxValue - list.get(selected).increment)
				{
					
					list.get(selected).value += list.get(selected).increment;
					renderMenu(list, selected, scrolled);
					
				}
				else
					Sound.buzz();
				
			}
			
			if(buttonID == Button.ID_ENTER)
			{
				
				list.get(selected).run();
				renderMenu(list, selected, scrolled);
				
			}
			
			if(buttonID == Button.ID_ESCAPE)
				if(list.get(selected).secondaryFunction) 
				{
					
					list.get(selected).secondFunction();
					renderMenu(list, selected, scrolled);
					
				}
				else
				{
					
					opened = false;
					
				}
				
		}
		
	}
	
	static void renderMenu(List<? extends MenuItem> list, int selected, int scrolled)
	{
		
		LCD.clear();
		
		for (int i = 0; i < 8 && i < list.size(); i++)
		{
			
			LCD.drawString(list.get(i + scrolled).name, 0, i, i + scrolled == selected);
			
			LCD.drawString(String.valueOf(list.get(i + scrolled).value), 17 - String.valueOf(list.get(i + scrolled).value).length(), i);
			
		}

		LCD.refresh();
		
	}
	
}
