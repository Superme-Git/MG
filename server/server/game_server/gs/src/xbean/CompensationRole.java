
package xbean;

public interface CompensationRole extends mkdb.Bean {
	public CompensationRole copy(); // deep clone
	public CompensationRole toData(); // a Data instance
	public CompensationRole toBean(); // a Bean instance
	public CompensationRole toDataIf(); // a Data instance If need. else return this
	public CompensationRole toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Long> getSinglecompensations(); // key = 补偿key, value = 是否已读 0=未读 1=已读
	public java.util.Map<Long, Long> getSinglecompensationsAsData(); // key = 补偿key, value = 是否已读 0=未读 1=已读

}
