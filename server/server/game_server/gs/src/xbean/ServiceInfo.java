
package xbean;

public interface ServiceInfo extends mkdb.Bean {
	public ServiceInfo copy(); // deep clone
	public ServiceInfo toData(); // a Data instance
	public ServiceInfo toBean(); // a Bean instance
	public ServiceInfo toDataIf(); // a Data instance If need. else return this
	public ServiceInfo toBeanIf(); // a Bean instance If need. else return this

	public int getYear(); // 年
	public int getWeeks(); // 当前开服第几周(今年的第几周)
	public int getActiverecommend(); // 当前推荐活动
	public long getUpdatetime(); // 上次更新天数的时间
	public int getDays(); // 累计开服天数

	public void setYear(int _v_); // 年
	public void setWeeks(int _v_); // 当前开服第几周(今年的第几周)
	public void setActiverecommend(int _v_); // 当前推荐活动
	public void setUpdatetime(long _v_); // 上次更新天数的时间
	public void setDays(int _v_); // 累计开服天数
}
