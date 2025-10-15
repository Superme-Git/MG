
package xbean;

public interface ShowInfoBean extends mkdb.Bean {
	public ShowInfoBean copy(); // deep clone
	public ShowInfoBean toData(); // a Data instance
	public ShowInfoBean toBean(); // a Bean instance
	public ShowInfoBean toDataIf(); // a Data instance If need. else return this
	public ShowInfoBean toBeanIf(); // a Bean instance If need. else return this

	public int getShowtype(); // 类型
	public long getRoleid(); // 角色id
	public long getShopid(); // 商店id,成就类型的时候是完成时间
	public int getCounterid(); // 柜台id,成就类型的时候是成就进度
	public int getThisid(); // 唯一id,成就类型的时候是是成就id

	public void setShowtype(int _v_); // 类型
	public void setRoleid(long _v_); // 角色id
	public void setShopid(long _v_); // 商店id,成就类型的时候是完成时间
	public void setCounterid(int _v_); // 柜台id,成就类型的时候是成就进度
	public void setThisid(int _v_); // 唯一id,成就类型的时候是是成就id
}
