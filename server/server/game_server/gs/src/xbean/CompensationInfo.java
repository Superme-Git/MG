
package xbean;

public interface CompensationInfo extends mkdb.Bean {
	public CompensationInfo copy(); // deep clone
	public CompensationInfo toData(); // a Data instance
	public CompensationInfo toBean(); // a Bean instance
	public CompensationInfo toDataIf(); // a Data instance If need. else return this
	public CompensationInfo toBeanIf(); // a Bean instance If need. else return this

	public long getReceiveawardtime(); // 领取奖励的时间
	public int getReadflag(); // 是否已读 0=未读 1=已读

	public void setReceiveawardtime(long _v_); // 领取奖励的时间
	public void setReadflag(int _v_); // 是否已读 0=未读 1=已读
}
