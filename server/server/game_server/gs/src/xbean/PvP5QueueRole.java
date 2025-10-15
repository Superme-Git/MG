
package xbean;

public interface PvP5QueueRole extends mkdb.Bean {
	public PvP5QueueRole copy(); // deep clone
	public PvP5QueueRole toData(); // a Data instance
	public PvP5QueueRole toBean(); // a Bean instance
	public PvP5QueueRole toDataIf(); // a Data instance If need. else return this
	public PvP5QueueRole toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public long getEnterqueuetime(); // 进入队列的时间

	public void setRoleid(long _v_); // 
	public void setEnterqueuetime(long _v_); // 进入队列的时间
}
