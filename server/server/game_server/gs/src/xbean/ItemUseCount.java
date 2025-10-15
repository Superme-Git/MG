
package xbean;

public interface ItemUseCount extends mkdb.Bean {
	public ItemUseCount copy(); // deep clone
	public ItemUseCount toData(); // a Data instance
	public ItemUseCount toBean(); // a Bean instance
	public ItemUseCount toDataIf(); // a Data instance If need. else return this
	public ItemUseCount toBeanIf(); // a Bean instance If need. else return this

	public int getItemid(); // 道具id
	public int getUsetimes(); // 使用次数
	public long getLastusetime(); // 上次使用时间

	public void setItemid(int _v_); // 道具id
	public void setUsetimes(int _v_); // 使用次数
	public void setLastusetime(long _v_); // 上次使用时间
}
