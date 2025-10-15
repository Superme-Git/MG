
package xbean;

public interface OffLineHook extends mkdb.Bean {
	public OffLineHook copy(); // deep clone
	public OffLineHook toData(); // a Data instance
	public OffLineHook toBean(); // a Bean instance
	public OffLineHook toDataIf(); // a Data instance If need. else return this
	public OffLineHook toBeanIf(); // a Bean instance If need. else return this

	public long getHooktime(); // 开始挂机时间
	public long getRemainfivebeitime(); // 点击使用5倍的时候封存5倍时间
	public long getFivebeitimestart(); // 开始使用五倍时间的时间
	public long getFivebeitotalusetime(); // 最后一次点击前总的使用时间
	public int getFlag(); // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机

	public void setHooktime(long _v_); // 开始挂机时间
	public void setRemainfivebeitime(long _v_); // 点击使用5倍的时候封存5倍时间
	public void setFivebeitimestart(long _v_); // 开始使用五倍时间的时间
	public void setFivebeitotalusetime(long _v_); // 最后一次点击前总的使用时间
	public void setFlag(int _v_); // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机
}
