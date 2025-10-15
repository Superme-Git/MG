
package xbean;

public interface PvP3QueueRole extends mkdb.Bean {
	public PvP3QueueRole copy(); // deep clone
	public PvP3QueueRole toData(); // a Data instance
	public PvP3QueueRole toBean(); // a Bean instance
	public PvP3QueueRole toDataIf(); // a Data instance If need. else return this
	public PvP3QueueRole toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public long getEnterqueuetime(); // 进入队列的时间

	public void setRoleid(long _v_); // 
	public void setEnterqueuetime(long _v_); // 进入队列的时间
}
