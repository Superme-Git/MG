
package xbean;

public interface TaskRecourse extends mkdb.Bean {
	public TaskRecourse copy(); // deep clone
	public TaskRecourse toData(); // a Data instance
	public TaskRecourse toBean(); // a Bean instance
	public TaskRecourse toDataIf(); // a Data instance If need. else return this
	public TaskRecourse toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getChannellist(); // 频道id 的list
	public java.util.List<Integer> getChannellistAsData(); // 频道id 的list

}
