package pack;

public class MenuItem
{

	public String name = " ";
	public double value = 0;
	public double increment = 1;
	public double minValue = Double.NEGATIVE_INFINITY;
	public double maxValue = Double.POSITIVE_INFINITY;
	public boolean secondaryFunction = false;
	
	public void run() {}
	public void secondFunction() {}
	
	public MenuItem() {}
	
	public MenuItem(String _name, double _value, double _increment, double _minValue, double _maxValue, boolean _secondaryFunction)
	{
		
		name = _name;
		value = _value;
		increment = _increment;
		minValue = _minValue;
		maxValue = _maxValue;
		secondaryFunction = _secondaryFunction;
		
	}
	
}
