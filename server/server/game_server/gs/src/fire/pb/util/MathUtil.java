package fire.pb.util;

public class MathUtil {

	
	public static int fixValueRange(int curValue, int minValue, int maxValue)
	{
		return Math.min(Math.max(curValue, minValue), maxValue);
	}
}
