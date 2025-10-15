
package xbean;

public interface CircleTaskCompleteTimes extends mkdb.Bean {
	public CircleTaskCompleteTimes copy(); // deep clone
	public CircleTaskCompleteTimes toData(); // a Data instance
	public CircleTaskCompleteTimes toBean(); // a Bean instance
	public CircleTaskCompleteTimes toDataIf(); // a Data instance If need. else return this
	public CircleTaskCompleteTimes toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getCircletaskcounts(); // key 为循环类型
	public java.util.Map<Integer, Integer> getCircletaskcountsAsData(); // key 为循环类型
	public long getLogtime(); // 

	public void setLogtime(long _v_); // 
}
