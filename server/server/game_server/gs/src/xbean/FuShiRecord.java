
package xbean;

public interface FuShiRecord extends mkdb.Bean {
	public FuShiRecord copy(); // deep clone
	public FuShiRecord toData(); // a Data instance
	public FuShiRecord toBean(); // a Bean instance
	public FuShiRecord toDataIf(); // a Data instance If need. else return this
	public FuShiRecord toBeanIf(); // a Bean instance If need. else return this

	public long getAddtime(); // 增加的时间
	public int getCurrentnum(); // 当前数量

	public void setAddtime(long _v_); // 增加的时间
	public void setCurrentnum(int _v_); // 当前数量
}
