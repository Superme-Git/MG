
package xbean;

public interface NewPlayActiveWeek extends mkdb.Bean {
	public NewPlayActiveWeek copy(); // deep clone
	public NewPlayActiveWeek toData(); // a Data instance
	public NewPlayActiveWeek toBean(); // a Bean instance
	public NewPlayActiveWeek toDataIf(); // a Data instance If need. else return this
	public NewPlayActiveWeek toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 活动记录时间
	public java.util.Map<Integer, xbean.PlayActiveData> getActives(); // key为活动id,一周的计次数据
	public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData(); // key为活动id,一周的计次数据

	public void setTime(long _v_); // 活动记录时间
}
