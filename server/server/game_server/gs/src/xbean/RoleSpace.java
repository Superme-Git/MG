
package xbean;

public interface RoleSpace extends mkdb.Bean {
	public RoleSpace copy(); // deep clone
	public RoleSpace toData(); // a Data instance
	public RoleSpace toBean(); // a Bean instance
	public RoleSpace toDataIf(); // a Data instance If need. else return this
	public RoleSpace toBeanIf(); // a Bean instance If need. else return this

	public int getGift(); // 空间礼物
	public int getPopularity(); // 人气
	public int getRecvgift(); // 收到的礼物
	public int getGetgiftnum(); // 踩空间获得礼物的次数
	public long getGetgifttime(); // 上次踩空间获得礼物的时间

	public void setGift(int _v_); // 空间礼物
	public void setPopularity(int _v_); // 人气
	public void setRecvgift(int _v_); // 收到的礼物
	public void setGetgiftnum(int _v_); // 踩空间获得礼物的次数
	public void setGetgifttime(long _v_); // 上次踩空间获得礼物的时间
}
