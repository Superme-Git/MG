
package xbean;

public interface PvP1QueueRole extends mkdb.Bean {
	public PvP1QueueRole copy(); // deep clone
	public PvP1QueueRole toData(); // a Data instance
	public PvP1QueueRole toBean(); // a Bean instance
	public PvP1QueueRole toDataIf(); // a Data instance If need. else return this
	public PvP1QueueRole toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public long getEnterqueuetime(); // 进入队列的时间

	public void setRoleid(long _v_); // 
	public void setEnterqueuetime(long _v_); // 进入队列的时间
}
