
package xbean;

public interface TriggerRole extends mkdb.Bean {
	public TriggerRole copy(); // deep clone
	public TriggerRole toData(); // a Data instance
	public TriggerRole toBean(); // a Bean instance
	public TriggerRole toDataIf(); // a Data instance If need. else return this
	public TriggerRole toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getTriggeredids(); // 
	public java.util.List<Integer> getTriggeredidsAsData(); // 

}
