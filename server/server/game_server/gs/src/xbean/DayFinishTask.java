
package xbean;

public interface DayFinishTask extends mkdb.Bean {
	public DayFinishTask copy(); // deep clone
	public DayFinishTask toData(); // a Data instance
	public DayFinishTask toBean(); // a Bean instance
	public DayFinishTask toDataIf(); // a Data instance If need. else return this
	public DayFinishTask toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getTasklist(); // 
	public java.util.List<Integer> getTasklistAsData(); // 

}
