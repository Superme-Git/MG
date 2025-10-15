
package xbean;

public interface RoleQuitStatistics extends mkdb.Bean {
	public RoleQuitStatistics copy(); // deep clone
	public RoleQuitStatistics toData(); // a Data instance
	public RoleQuitStatistics toBean(); // a Bean instance
	public RoleQuitStatistics toDataIf(); // a Data instance If need. else return this
	public RoleQuitStatistics toBeanIf(); // a Bean instance If need. else return this

	public long getMoney(); // 
	public int getExp(); // 
	public java.util.Map<Integer, Long> getCurrency(); // 
	public java.util.Map<Integer, Long> getCurrencyAsData(); // 
	public long getLastcountdate(); // 最近统计钱,储备金,经验的时间
	public int getLastrewardidx(); // 上次领取奖励序号
	public long getRewarddate(); // 上次领取上线奖励

	public void setMoney(long _v_); // 
	public void setExp(int _v_); // 
	public void setLastcountdate(long _v_); // 最近统计钱,储备金,经验的时间
	public void setLastrewardidx(int _v_); // 上次领取奖励序号
	public void setRewarddate(long _v_); // 上次领取上线奖励
}
