
package xbean;

public interface ActivityCalendarStatus extends mkdb.Bean {
	public ActivityCalendarStatus copy(); // deep clone
	public ActivityCalendarStatus toData(); // a Data instance
	public ActivityCalendarStatus toBean(); // a Bean instance
	public ActivityCalendarStatus toDataIf(); // a Data instance If need. else return this
	public ActivityCalendarStatus toBeanIf(); // a Bean instance If need. else return this

	public int getWeek(); // 该记录是今年的第几周的
	public java.util.Map<Integer, xbean.DayFinishTask> getDaystatus(); // 
	public java.util.Map<Integer, xbean.DayFinishTask> getDaystatusAsData(); // 

	public void setWeek(int _v_); // 该记录是今年的第几周的
}
