
package xbean;

public interface TaskEventInfo extends mkdb.Bean {
	public TaskEventInfo copy(); // deep clone
	public TaskEventInfo toData(); // a Data instance
	public TaskEventInfo toBean(); // a Bean instance
	public TaskEventInfo toDataIf(); // a Data instance If need. else return this
	public TaskEventInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getEventindexs(); // key为taskid,value为eventidx
	public java.util.Map<Integer, Long> getEventindexsAsData(); // key为taskid,value为eventidx

}
