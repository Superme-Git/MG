
package xbean;

public interface MulDayLoginGift extends mkdb.Bean {
	public MulDayLoginGift copy(); // deep clone
	public MulDayLoginGift toData(); // a Data instance
	public MulDayLoginGift toBean(); // a Bean instance
	public MulDayLoginGift toDataIf(); // a Data instance If need. else return this
	public MulDayLoginGift toBeanIf(); // a Bean instance If need. else return this

	public int getLogindays(); // 累计登录天数
	public long getLogintime(); // 更新时间
	public java.util.Map<Integer, Long> getRewardmap(); // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))
	public java.util.Map<Integer, Long> getRewardmapAsData(); // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))

	public void setLogindays(int _v_); // 累计登录天数
	public void setLogintime(long _v_); // 更新时间
}
