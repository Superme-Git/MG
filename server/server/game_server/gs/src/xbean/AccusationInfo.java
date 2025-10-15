
package xbean;

public interface AccusationInfo extends mkdb.Bean {
	public AccusationInfo copy(); // deep clone
	public AccusationInfo toData(); // a Data instance
	public AccusationInfo toBean(); // a Bean instance
	public AccusationInfo toDataIf(); // a Data instance If need. else return this
	public AccusationInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 被举报人ID
	public java.util.List<Long> getAccusationedroleids(); // 举报人IDs
	public java.util.List<Long> getAccusationedroleidsAsData(); // 举报人IDs
	public long getLasttime(); // 最后一次被举报时间

	public void setRoleid(long _v_); // 被举报人ID
	public void setLasttime(long _v_); // 最后一次被举报时间
}
