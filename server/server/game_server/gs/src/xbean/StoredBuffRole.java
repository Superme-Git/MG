
package xbean;

public interface StoredBuffRole extends mkdb.Bean {
	public StoredBuffRole copy(); // deep clone
	public StoredBuffRole toData(); // a Data instance
	public StoredBuffRole toBean(); // a Bean instance
	public StoredBuffRole toDataIf(); // a Data instance If need. else return this
	public StoredBuffRole toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.Buff> getBuffs(); // 
	public java.util.Map<Integer, xbean.Buff> getBuffsAsData(); // 

}
