package pack;

import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class Menu
{

	static int selected = 0;
	static List<? extends MenuItem> list;
	
	public Menu(List<? extends MenuItem> _list)
	{
		
		list = _list;
		int buttonID;
		boolean opened = true;
		
		renderMenu();
		
		while (opened)
		{
			
			buttonID = Button.waitForAnyPress();
			
			if(buttonID == Button.ID_UP && selected > 0)
			{	
			
				selected--;
				renderMenu();
				
			}
			
			if(buttonID == Button.ID_DOWN && selected < list.size() - 1)
			{
				
				selected++;
				renderMenu();
				
			}	

			if(buttonID == Button.ID_LEFT && list.get(selected).value >= list.get(selected).minValue + list.get(selected).increment)
			{	
			
				list.get(selected).value -= list.get(selected).increment;
				renderMenu();
				
			}
			
			if(buttonID == Button.ID_RIGHT && list.get(selected).value <= list.get(selected).maxValue - list.get(selected).increment)
			{
				
				list.get(selected).value += list.get(selected).increment;
				renderMenu();
				
			}
			
			if(buttonID == Button.ID_ENTER)
			{
				
				list.get(selected).run();
				renderMenu();
				
			}
			
			if(buttonID == Button.ID_ESCAPE)
				if(list.get(selected).secondaryFunction) 
				{
					
					list.get(selected).secondFunction();
					renderMenu();
					
				}
				else
				{
					
					opened = false;
					
				}
				
		}
		
	}
	
	static void renderMenu()
	{
		
		LCD.clear();
		
		for (int i = 0; i < list.size(); i++)
		{
			
			LCD.drawString(list.get(i).name, 0, i, i == selected);
			
			LCD.drawString(String.valueOf(list.get(i).value), 17 - String.valueOf(list.get(i).value).length(), i);
			
		}

		LCD.refresh();
		
	}
	
}
