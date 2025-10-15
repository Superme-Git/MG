
package xbean;

public interface ActiveUserInfo extends mkdb.Bean {
	public ActiveUserInfo copy(); // deep clone
	public ActiveUserInfo toData(); // a Data instance
	public ActiveUserInfo toBean(); // a Bean instance
	public ActiveUserInfo toDataIf(); // a Data instance If need. else return this
	public ActiveUserInfo toBeanIf(); // a Bean instance If need. else return this

	public int getFirsttimeenter(); // 第一次进入服务器 当前天
	public int getLasttimeenter(); // 最近一次进入服务器 当前天
	public int getLasttimeactiveday1(); // 最近两次活跃的 当前天 day1比day2新,下同
	public int getLasttimeactiveday2(); // 
	public int getLasttimeactiveweek1(); // 最近两次活跃的 当前周 week1比week2新,下同
	public int getLasttimeactiveweek2(); // 
	public int getFirsttimeactiveday(); // 第一次活跃的 当前天
	public int getFirsttimeactiveweek(); // 第一次活跃的 当前周
	public int getLasttimechargeday1(); // 最近2次充值的当前天
	public int getLasttimechargeday2(); // 最近2次充值的当前天
	public int getLasttimechargeweek1(); // 最近2次充值的当前周
	public int getLasttimechargeweek2(); // 最近2次充值的当前周
	public int getFirsttimechargeday(); // 第一次充值 当前天
	public int getFirsttimechargeweek(); // 第一次充值 当前周
	public int getLasttimechargeday(); // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
	public int getLasttimechargedaytotalamount(); // 最近一天充值的总额 当前天
	public int getLasttimechargeweek(); // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
	public int getLasttimechargeweektotalamount(); // 最近一周充值的总额 当前周
	public int getChargetotalamount(); // 充值总额
	public int getChargetotalrmb(); // 充值rmb总额
	public long getChargetotalamountlong(); // 充值总额
	public long getChargetotalrmblong(); // 充值rmb总额
	public int getChargetotalrmbuntillastday(); // 截止昨天的充值总额，方便计算当日充值多少
	public long getChargetotalrmbuntillastdaylong(); // 截止昨天的充值总额，方便计算当日充值多少
	public int getConvertchargetotalamouttolong(); // 是否已经把充值总额转变成了long型
	public long getFushiconsumetotalamount(); // 符石消耗的总量
	public long getCashfushiconsumetotalamount(); // 现金充值符石消耗的总量
	public int getLasttimefushiconsumeday(); // 最近天 消耗符石,记录的是天数
	public int getLasttimefushiconsumedaytotalamount(); // 最近天 消耗的符石数量
	public int getLasttimefushiconsumeweek(); // 最近周 消耗符石,记录的是周数
	public int getLasttimefushiconsumeweektotalamount(); // 最近周 消耗的符石数量
	public long getFushibuytotalamount(); // 符石购买的总量,给log用,不影响活跃
	public long getFushiselltotalamount(); // 符石出售的总量,给log用
	public int getCashfushi(); // 剩余流通符石
	public int getBindfushi(); // 剩余绑定rmb符石
	public int getSysfushi(); // 剩余系统赠送符石
	public int getFushiinplatform(); // 在交易平台里面的挂单符石

	public void setFirsttimeenter(int _v_); // 第一次进入服务器 当前天
	public void setLasttimeenter(int _v_); // 最近一次进入服务器 当前天
	public void setLasttimeactiveday1(int _v_); // 最近两次活跃的 当前天 day1比day2新,下同
	public void setLasttimeactiveday2(int _v_); // 
	public void setLasttimeactiveweek1(int _v_); // 最近两次活跃的 当前周 week1比week2新,下同
	public void setLasttimeactiveweek2(int _v_); // 
	public void setFirsttimeactiveday(int _v_); // 第一次活跃的 当前天
	public void setFirsttimeactiveweek(int _v_); // 第一次活跃的 当前周
	public void setLasttimechargeday1(int _v_); // 最近2次充值的当前天
	public void setLasttimechargeday2(int _v_); // 最近2次充值的当前天
	public void setLasttimechargeweek1(int _v_); // 最近2次充值的当前周
	public void setLasttimechargeweek2(int _v_); // 最近2次充值的当前周
	public void setFirsttimechargeday(int _v_); // 第一次充值 当前天
	public void setFirsttimechargeweek(int _v_); // 第一次充值 当前周
	public void setLasttimechargeday(int _v_); // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
	public void setLasttimechargedaytotalamount(int _v_); // 最近一天充值的总额 当前天
	public void setLasttimechargeweek(int _v_); // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
	public void setLasttimechargeweektotalamount(int _v_); // 最近一周充值的总额 当前周
	public void setChargetotalamount(int _v_); // 充值总额
	public void setChargetotalrmb(int _v_); // 充值rmb总额
	public void setChargetotalamountlong(long _v_); // 充值总额
	public void setChargetotalrmblong(long _v_); // 充值rmb总额
	public void setChargetotalrmbuntillastday(int _v_); // 截止昨天的充值总额，方便计算当日充值多少
	public void setChargetotalrmbuntillastdaylong(long _v_); // 截止昨天的充值总额，方便计算当日充值多少
	public void setConvertchargetotalamouttolong(int _v_); // 是否已经把充值总额转变成了long型
	public void setFushiconsumetotalamount(long _v_); // 符石消耗的总量
	public void setCashfushiconsumetotalamount(long _v_); // 现金充值符石消耗的总量
	public void setLasttimefushiconsumeday(int _v_); // 最近天 消耗符石,记录的是天数
	public void setLasttimefushiconsumedaytotalamount(int _v_); // 最近天 消耗的符石数量
	public void setLasttimefushiconsumeweek(int _v_); // 最近周 消耗符石,记录的是周数
	public void setLasttimefushiconsumeweektotalamount(int _v_); // 最近周 消耗的符石数量
	public void setFushibuytotalamount(long _v_); // 符石购买的总量,给log用,不影响活跃
	public void setFushiselltotalamount(long _v_); // 符石出售的总量,给log用
	public void setCashfushi(int _v_); // 剩余流通符石
	public void setBindfushi(int _v_); // 剩余绑定rmb符石
	public void setSysfushi(int _v_); // 剩余系统赠送符石
	public void setFushiinplatform(int _v_); // 在交易平台里面的挂单符石
}
