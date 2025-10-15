
package xbean;

public interface RegDay extends mkdb.Bean {
	public RegDay copy(); // deep clone
	public RegDay toData(); // a Data instance
	public RegDay toBean(); // a Bean instance
	public RegDay toDataIf(); // a Data instance If need. else return this
	public RegDay toBeanIf(); // a Bean instance If need. else return this

	public int getRewardflag(); // 奖励标志(1-过期 2-已领取 3-未领取)
	public int getDay(); // 第几天
	public int getSuppregflag(); // 补签标志(0-正常签到 1-补签)

	public void setRewardflag(int _v_); // 奖励标志(1-过期 2-已领取 3-未领取)
	public void setDay(int _v_); // 第几天
	public void setSuppregflag(int _v_); // 补签标志(0-正常签到 1-补签)
}
