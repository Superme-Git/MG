
package xbean;

public interface RewardData extends mkdb.Bean {
	public RewardData copy(); // deep clone
	public RewardData toData(); // a Data instance
	public RewardData toBean(); // a Bean instance
	public RewardData toDataIf(); // a Data instance If need. else return this
	public RewardData toBeanIf(); // a Bean instance If need. else return this

	public int getRewardid(); // 领了多少个奖励,缺省是0
	public long getLastrewardtime(); // 上次领取奖励的时间
	public long getTimewait(); // 距离下次奖励领取等待时间

	public void setRewardid(int _v_); // 领了多少个奖励,缺省是0
	public void setLastrewardtime(long _v_); // 上次领取奖励的时间
	public void setTimewait(long _v_); // 距离下次奖励领取等待时间
}
