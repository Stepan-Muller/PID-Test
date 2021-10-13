package pack;

public class MenuItem
{

	public String name = " ";
	public float value = 0;
	public float increment = 1;
	public float minValue = Float.NEGATIVE_INFINITY;
	public float maxValue = Float.POSITIVE_INFINITY;
	public boolean secondaryFunction = false;
	
	public void run() {}
	public void secondFunction() {}
	
	public MenuItem() {}
	
	public MenuItem(String _name, float _value, float _increment, float _minValue, float _maxValue, boolean _secondaryFunction)
	{
		
		name = _name;
		value = _value;
		increment = _increment;
		minValue = _minValue;
		maxValue = _maxValue;
		secondaryFunction = _secondaryFunction;
		
	}
	
}
