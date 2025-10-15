
package xbean;

public interface InstanceTaskInfo extends mkdb.Bean {
	public InstanceTaskInfo copy(); // deep clone
	public InstanceTaskInfo toData(); // a Data instance
	public InstanceTaskInfo toBean(); // a Bean instance
	public InstanceTaskInfo toDataIf(); // a Data instance If need. else return this
	public InstanceTaskInfo toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // excel表唯一id
	public long getInstanceonlyid(); // instancetask 表里的唯一id
	public int getInstanceflag(); // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
	public int getCounts(); // 今天做了多少次
	public int getDefaulttimes(); // 一天默认做多少次
	public int getCurrentstep(); // 当前做到哪一阶段
	public int getCurrenttaskid(); // 当前做到哪一步了
	public long getAccepttime(); // 最后接任务时间
	public int getTotalexp(); // 经验
	public int getState(); // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
	public java.util.Map<Integer, Integer> getStepawardtimes(); // key stepId, value 该step给的奖励次数
	public java.util.Map<Integer, Integer> getStepawardtimesAsData(); // key stepId, value 该step给的奖励次数
	public int getAwarding(); // 是否还能再拿奖励,0 不能, 1 可以拿

	public void setId(int _v_); // excel表唯一id
	public void setInstanceonlyid(long _v_); // instancetask 表里的唯一id
	public void setInstanceflag(int _v_); // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
	public void setCounts(int _v_); // 今天做了多少次
	public void setDefaulttimes(int _v_); // 一天默认做多少次
	public void setCurrentstep(int _v_); // 当前做到哪一阶段
	public void setCurrenttaskid(int _v_); // 当前做到哪一步了
	public void setAccepttime(long _v_); // 最后接任务时间
	public void setTotalexp(int _v_); // 经验
	public void setState(int _v_); // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
	public void setAwarding(int _v_); // 是否还能再拿奖励,0 不能, 1 可以拿
}
