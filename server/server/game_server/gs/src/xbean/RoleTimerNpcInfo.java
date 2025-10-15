
package xbean;

public interface RoleTimerNpcInfo extends mkdb.Bean {
	public RoleTimerNpcInfo copy(); // deep clone
	public RoleTimerNpcInfo toData(); // a Data instance
	public RoleTimerNpcInfo toBean(); // a Bean instance
	public RoleTimerNpcInfo toDataIf(); // a Data instance If need. else return this
	public RoleTimerNpcInfo toBeanIf(); // a Bean instance If need. else return this

	public int getAwardtimes(); // //领取奖励次数
	public long getLastawardtime(); // //上次领取奖励时间

	public void setAwardtimes(int _v_); // //领取奖励次数
	public void setLastawardtime(long _v_); // //上次领取奖励时间
}
