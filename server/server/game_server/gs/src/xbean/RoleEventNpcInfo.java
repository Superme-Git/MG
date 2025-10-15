
package xbean;

public interface RoleEventNpcInfo extends mkdb.Bean {
	public RoleEventNpcInfo copy(); // deep clone
	public RoleEventNpcInfo toData(); // a Data instance
	public RoleEventNpcInfo toBean(); // a Bean instance
	public RoleEventNpcInfo toDataIf(); // a Data instance If need. else return this
	public RoleEventNpcInfo toBeanIf(); // a Bean instance If need. else return this

	public int getAwardtimes(); // //领取奖励次数
	public long getLastawardtime(); // //上次领取奖励时间

	public void setAwardtimes(int _v_); // //领取奖励次数
	public void setLastawardtime(long _v_); // //上次领取奖励时间
}
