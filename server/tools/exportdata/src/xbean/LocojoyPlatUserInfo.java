
package xbean;

public interface LocojoyPlatUserInfo extends xdb.Bean {
	public LocojoyPlatUserInfo copy(); // deep clone
	public LocojoyPlatUserInfo toData(); // a Data instance
	public LocojoyPlatUserInfo toBean(); // a Bean instance
	public LocojoyPlatUserInfo toDataIf(); // a Data instance If need. else return this
	public LocojoyPlatUserInfo toBeanIf(); // a Bean instance If need. else return this

	public int getUserid(); // 游戏里用的userid
	public long getUserinfoid(); // users表里的key

	public void setUserid(int _v_); // 游戏里用的userid
	public void setUserinfoid(long _v_); // users表里的key
}
