
package xbean;

public interface Track extends mkdb.Bean {
	public Track copy(); // deep clone
	public Track toData(); // a Data instance
	public Track toBean(); // a Bean instance
	public Track toDataIf(); // a Data instance If need. else return this
	public Track toBeanIf(); // a Bean instance If need. else return this

	public long getDate(); // 接受任务

	public void setDate(long _v_); // 接受任务
}
