
package xbean;

public interface TaskInfos extends mkdb.Bean {
	public TaskInfos copy(); // deep clone
	public TaskInfos toData(); // a Data instance
	public TaskInfos toBean(); // a Bean instance
	public TaskInfos toDataIf(); // a Data instance If need. else return this
	public TaskInfos toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmap(); // 
	public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmapAsData(); // 

}
