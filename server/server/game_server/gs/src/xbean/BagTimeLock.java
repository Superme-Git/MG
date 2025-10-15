
package xbean;

public interface BagTimeLock extends mkdb.Bean {
	public BagTimeLock copy(); // deep clone
	public BagTimeLock toData(); // a Data instance
	public BagTimeLock toBean(); // a Bean instance
	public BagTimeLock toDataIf(); // a Data instance If need. else return this
	public BagTimeLock toBeanIf(); // a Bean instance If need. else return this

	public int getItemkey(); // 
	public int getNum(); // 
	public long getTimeout(); // 超时时间

	public void setItemkey(int _v_); // 
	public void setNum(int _v_); // 
	public void setTimeout(long _v_); // 超时时间
}
