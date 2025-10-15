
package xbean;

public interface RandomNameInfo extends mkdb.Bean {
	public RandomNameInfo copy(); // deep clone
	public RandomNameInfo toData(); // a Data instance
	public RandomNameInfo toBean(); // a Bean instance
	public RandomNameInfo toDataIf(); // a Data instance If need. else return this
	public RandomNameInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRandomtime(); // 

	public void setRandomtime(long _v_); // 
}
