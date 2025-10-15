
package xbean;

public interface EventInfo extends mkdb.Bean {
	public EventInfo copy(); // deep clone
	public EventInfo toData(); // a Data instance
	public EventInfo toBean(); // a Bean instance
	public EventInfo toDataIf(); // a Data instance If need. else return this
	public EventInfo toBeanIf(); // a Bean instance If need. else return this

	public fire.pb.WorldEventTab.CrontabTask getEvent(); // 

	public void setEvent(fire.pb.WorldEventTab.CrontabTask _v_); // 
}
