
package xbean;

public interface BuffRole extends mkdb.Bean {
	public BuffRole copy(); // deep clone
	public BuffRole toData(); // a Data instance
	public BuffRole toBean(); // a Bean instance
	public BuffRole toDataIf(); // a Data instance If need. else return this
	public BuffRole toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public xbean.BuffAgent getBuffagent(); // 

	public void setRoleid(long _v_); // 
}
