
package xbean;

public interface RolenpcjiangliInfo extends mkdb.Bean {
	public RolenpcjiangliInfo copy(); // deep clone
	public RolenpcjiangliInfo toData(); // a Data instance
	public RolenpcjiangliInfo toBean(); // a Bean instance
	public RolenpcjiangliInfo toDataIf(); // a Data instance If need. else return this
	public RolenpcjiangliInfo toBeanIf(); // a Bean instance If need. else return this

	public int getAwardtimes(); // //领取奖励次数
	public long getLastawardtime(); // //上次领取奖励时间

	public void setAwardtimes(int _v_); // //领取奖励次数
	public void setLastawardtime(long _v_); // //上次领取奖励时间
}
