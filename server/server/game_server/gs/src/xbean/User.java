
package xbean;

public interface User extends mkdb.Bean {
	public User copy(); // deep clone
	public User toData(); // a Data instance
	public User toBean(); // a Bean instance
	public User toDataIf(); // a Data instance If need. else return this
	public User toBeanIf(); // a Bean instance If need. else return this

	public long getPrevloginroleid(); // 上一次登录的角色
	public java.util.List<Long> getIdlist(); // 用户的角色列表 value是roleid
	public java.util.List<Long> getIdlistAsData(); // 用户的角色列表 value是roleid
	public long getCreatetime(); // 帐号第一次进入游戏的时间
	public int getIsfirst(); // 是否第一次登陆0第一次   1不是第一次

	public void setPrevloginroleid(long _v_); // 上一次登录的角色
	public void setCreatetime(long _v_); // 帐号第一次进入游戏的时间
	public void setIsfirst(int _v_); // 是否第一次登陆0第一次   1不是第一次
}
