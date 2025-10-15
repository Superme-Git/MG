
package xbean;

public interface OfflineMsg extends mkdb.Bean {
	public OfflineMsg copy(); // deep clone
	public OfflineMsg toData(); // a Data instance
	public OfflineMsg toBean(); // a Bean instance
	public OfflineMsg toDataIf(); // a Data instance If need. else return this
	public OfflineMsg toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public String getContent(); // 
	public com.locojoy.base.Octets getContentOctets(); // 
	public java.util.List<byte []> getDetails(); // 展示品信息
	public java.util.List<byte []> getDetailsAsData(); // 展示品信息
	public java.util.List<xbean.ShowInfoBean> getShowinfos(); // 展示品信息
	public java.util.List<xbean.ShowInfoBean> getShowinfosAsData(); // 展示品信息
	public String getSendtime(); // 
	public com.locojoy.base.Octets getSendtimeOctets(); // 

	public void setRoleid(long _v_); // 
	public void setContent(String _v_); // 
	public void setContentOctets(com.locojoy.base.Octets _v_); // 
	public void setSendtime(String _v_); // 
	public void setSendtimeOctets(com.locojoy.base.Octets _v_); // 
}
