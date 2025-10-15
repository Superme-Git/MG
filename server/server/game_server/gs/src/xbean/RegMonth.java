
package xbean;

public interface RegMonth extends mkdb.Bean {
	public RegMonth copy(); // deep clone
	public RegMonth toData(); // a Data instance
	public RegMonth toBean(); // a Bean instance
	public RegMonth toDataIf(); // a Data instance If need. else return this
	public RegMonth toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.RegDay> getDaymap(); // 
	public java.util.Map<Integer, xbean.RegDay> getDaymapAsData(); // 
	public int getSuppregnum(); // 补签次数

	public void setSuppregnum(int _v_); // 补签次数
}
