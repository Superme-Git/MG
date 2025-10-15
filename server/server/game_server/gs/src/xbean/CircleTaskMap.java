
package xbean;

public interface CircleTaskMap extends mkdb.Bean {
	public CircleTaskMap copy(); // deep clone
	public CircleTaskMap toData(); // a Data instance
	public CircleTaskMap toBean(); // a Bean instance
	public CircleTaskMap toDataIf(); // a Data instance If need. else return this
	public CircleTaskMap toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmap(); // 
	public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmapAsData(); // 

}
