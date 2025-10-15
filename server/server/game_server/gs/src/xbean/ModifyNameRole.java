
package xbean;

public interface ModifyNameRole extends mkdb.Bean {
	public ModifyNameRole copy(); // deep clone
	public ModifyNameRole toData(); // a Data instance
	public ModifyNameRole toBean(); // a Bean instance
	public ModifyNameRole toDataIf(); // a Data instance If need. else return this
	public ModifyNameRole toBeanIf(); // a Bean instance If need. else return this

	public long getLastbuytime(); // 上次购买的时间
	public long getLastmodtime(); // 上次改名的时间
	public int getBuycount(); // 记录已经购买的次数
	public int getModcount(); // 记录已经修改名字的次数

	public void setLastbuytime(long _v_); // 上次购买的时间
	public void setLastmodtime(long _v_); // 上次改名的时间
	public void setBuycount(int _v_); // 记录已经购买的次数
	public void setModcount(int _v_); // 记录已经修改名字的次数
}
