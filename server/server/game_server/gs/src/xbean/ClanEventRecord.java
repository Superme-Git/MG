
package xbean;

public interface ClanEventRecord extends mkdb.Bean {
	public ClanEventRecord copy(); // deep clone
	public ClanEventRecord toData(); // a Data instance
	public ClanEventRecord toBean(); // a Bean instance
	public ClanEventRecord toDataIf(); // a Data instance If need. else return this
	public ClanEventRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 时间
	public int getEventtype(); // 时间
	public String getContent(); // 内容
	public com.locojoy.base.Octets getContentOctets(); // 内容
	public long getObjectroleid(); // 内容

	public void setTime(long _v_); // 时间
	public void setEventtype(int _v_); // 时间
	public void setContent(String _v_); // 内容
	public void setContentOctets(com.locojoy.base.Octets _v_); // 内容
	public void setObjectroleid(long _v_); // 内容
}
