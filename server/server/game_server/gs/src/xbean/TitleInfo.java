
package xbean;

public interface TitleInfo extends mkdb.Bean {
	public TitleInfo copy(); // deep clone
	public TitleInfo toData(); // a Data instance
	public TitleInfo toBean(); // a Bean instance
	public TitleInfo toDataIf(); // a Data instance If need. else return this
	public TitleInfo toBeanIf(); // a Bean instance If need. else return this

	public int getTitleid(); // 称谓id
	public String getTitlename(); // 称谓名
	public com.locojoy.base.Octets getTitlenameOctets(); // 称谓名
	public long getAvailtime(); // 剩余有效时间

	public void setTitleid(int _v_); // 称谓id
	public void setTitlename(String _v_); // 称谓名
	public void setTitlenameOctets(com.locojoy.base.Octets _v_); // 称谓名
	public void setAvailtime(long _v_); // 剩余有效时间
}
