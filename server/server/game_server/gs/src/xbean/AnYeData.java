
package xbean;

public interface AnYeData extends mkdb.Bean {
	public AnYeData copy(); // deep clone
	public AnYeData toData(); // a Data instance
	public AnYeData toBean(); // a Bean instance
	public AnYeData toDataIf(); // a Data instance If need. else return this
	public AnYeData toBeanIf(); // a Bean instance If need. else return this

	public long getJointime(); // 首次参加时间
	public long getRandomtime(); // 上次随机时间(已经淘汰)
	public long getRefreshtime(); // 上次清空时间(已经淘汰)
	public int getTimes(); // 任务累计次数
	public int getTodaynum(); // 今天次数(已经淘汰)
	public long getToday(); // 今天时间(已经淘汰)
	public int getRenxins(); // 任性次数
	public int getReqhelptimes(); // 请求援助物品次数
	public int getHelptimes(); // 援助别人物品次数
	public int getLegendtask(); // 当前传说任务pos
	public java.util.Map<Integer, xbean.AnYeTask> getAnyetasks(); // key为任务位置id
	public java.util.Map<Integer, xbean.AnYeTask> getAnyetasksAsData(); // key为任务位置id

	public void setJointime(long _v_); // 首次参加时间
	public void setRandomtime(long _v_); // 上次随机时间(已经淘汰)
	public void setRefreshtime(long _v_); // 上次清空时间(已经淘汰)
	public void setTimes(int _v_); // 任务累计次数
	public void setTodaynum(int _v_); // 今天次数(已经淘汰)
	public void setToday(long _v_); // 今天时间(已经淘汰)
	public void setRenxins(int _v_); // 任性次数
	public void setReqhelptimes(int _v_); // 请求援助物品次数
	public void setHelptimes(int _v_); // 援助别人物品次数
	public void setLegendtask(int _v_); // 当前传说任务pos
}
