
package xbean;

public interface InstanceTimer extends mkdb.Bean {
	public InstanceTimer copy(); // deep clone
	public InstanceTimer toData(); // a Data instance
	public InstanceTimer toBean(); // a Bean instance
	public InstanceTimer toDataIf(); // a Data instance If need. else return this
	public InstanceTimer toBeanIf(); // a Bean instance If need. else return this

	public final static int STATE_UNSTART = 0; // 
	public final static int STATE_START = 1; // 
	public final static int STATE_END = 2; // 

	public int getState(); // 状态 0=未开始；1=已开始；2=已结束
	public long getStarttime(); // 开始时间
	public long getEndtime(); // 结束时间
	public long getFuturekey(); // 在timerfutures中的key

	public void setState(int _v_); // 状态 0=未开始；1=已开始；2=已结束
	public void setStarttime(long _v_); // 开始时间
	public void setEndtime(long _v_); // 结束时间
	public void setFuturekey(long _v_); // 在timerfutures中的key
}
