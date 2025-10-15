
package xbean;

public interface ActiveRoleInfo extends mkdb.Bean {
	public ActiveRoleInfo copy(); // deep clone
	public ActiveRoleInfo toData(); // a Data instance
	public ActiveRoleInfo toBean(); // a Bean instance
	public ActiveRoleInfo toDataIf(); // a Data instance If need. else return this
	public ActiveRoleInfo toBeanIf(); // a Bean instance If need. else return this

	public int getCreatetime(); // 创建时间 当前天
	public long getEnterworldtime(); // 最近一次登录游戏的时间
	public int getDayonline(); // 在线时长 当前天 分钟为单位
	public int getDayonlinetime(); // 在线时长 当前天 分钟为单位
	public int getWeekonline(); // 在线时长 当前周 分钟为单位
	public int getWeekonlinetime(); // 在线时长 当前周 分钟为单位
	public int getLasttimeactiveday1(); // 最近两次活跃的 当前天 day1比day2新,下同
	public int getLasttimeactiveday1onlinetime(); // 最近一次日活跃的在线时长
	public int getLasttimeactiveday2(); // 
	public int getLasttimeactiveweek1(); // 最近两次活跃的 当前周 week1比week2新,下同
	public int getLasttimeactiveweek1onlinetime(); // 最近一次周活跃的在线时长
	public int getLasttimeactiveweek2(); // 
	public int getFirsttimeactiveday(); // 第一次活跃的 当前天
	public int getFirsttimeactiveweek(); // 第一次活跃的 当前周
	public long getDealmoneyinplatform(); // 交易平台挂单游戏币
	public long getTmpmoneyinplatform(); // 交易平台暂存游戏币
	public long getMoneyincofc(); // 商会资金

	public void setCreatetime(int _v_); // 创建时间 当前天
	public void setEnterworldtime(long _v_); // 最近一次登录游戏的时间
	public void setDayonline(int _v_); // 在线时长 当前天 分钟为单位
	public void setDayonlinetime(int _v_); // 在线时长 当前天 分钟为单位
	public void setWeekonline(int _v_); // 在线时长 当前周 分钟为单位
	public void setWeekonlinetime(int _v_); // 在线时长 当前周 分钟为单位
	public void setLasttimeactiveday1(int _v_); // 最近两次活跃的 当前天 day1比day2新,下同
	public void setLasttimeactiveday1onlinetime(int _v_); // 最近一次日活跃的在线时长
	public void setLasttimeactiveday2(int _v_); // 
	public void setLasttimeactiveweek1(int _v_); // 最近两次活跃的 当前周 week1比week2新,下同
	public void setLasttimeactiveweek1onlinetime(int _v_); // 最近一次周活跃的在线时长
	public void setLasttimeactiveweek2(int _v_); // 
	public void setFirsttimeactiveday(int _v_); // 第一次活跃的 当前天
	public void setFirsttimeactiveweek(int _v_); // 第一次活跃的 当前周
	public void setDealmoneyinplatform(long _v_); // 交易平台挂单游戏币
	public void setTmpmoneyinplatform(long _v_); // 交易平台暂存游戏币
	public void setMoneyincofc(long _v_); // 商会资金
}
