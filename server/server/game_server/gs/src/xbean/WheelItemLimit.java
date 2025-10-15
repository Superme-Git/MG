
package xbean;

public interface WheelItemLimit extends mkdb.Bean {
	public WheelItemLimit copy(); // deep clone
	public WheelItemLimit toData(); // a Data instance
	public WheelItemLimit toBean(); // a Bean instance
	public WheelItemLimit toDataIf(); // a Data instance If need. else return this
	public WheelItemLimit toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getLimitmap(); // 
	public java.util.Map<Integer, Integer> getLimitmapAsData(); // 

}
