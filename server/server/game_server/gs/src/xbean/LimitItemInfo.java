
package xbean;

public interface LimitItemInfo extends mkdb.Bean {
	public LimitItemInfo copy(); // deep clone
	public LimitItemInfo toData(); // a Data instance
	public LimitItemInfo toBean(); // a Bean instance
	public LimitItemInfo toDataIf(); // a Data instance If need. else return this
	public LimitItemInfo toBeanIf(); // a Bean instance If need. else return this

	public int getMaxnum(); // 
	public int getNum(); // 

	public void setMaxnum(int _v_); // 
	public void setNum(int _v_); // 
}
