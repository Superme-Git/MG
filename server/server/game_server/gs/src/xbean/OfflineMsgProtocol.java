
package xbean;

public interface OfflineMsgProtocol extends mkdb.Bean {
	public OfflineMsgProtocol copy(); // deep clone
	public OfflineMsgProtocol toData(); // a Data instance
	public OfflineMsgProtocol toBean(); // a Bean instance
	public OfflineMsgProtocol toDataIf(); // a Data instance If need. else return this
	public OfflineMsgProtocol toBeanIf(); // a Bean instance If need. else return this

	public int getProtype(); // 
	public <T extends com.locojoy.base.Marshal.Marshal> T getContent(T _v_); // 
	public boolean isContentEmpty(); // 
	public byte[] getContentCopy(); // 
	public String getProclassname(); // 
	public com.locojoy.base.Octets getProclassnameOctets(); // 
	public long getTick(); // 插入到离线协议的时间

	public void setProtype(int _v_); // 
	public void setContent(com.locojoy.base.Marshal.Marshal _v_); // 
	public void setContentCopy(byte[] _v_); // 
	public void setProclassname(String _v_); // 
	public void setProclassnameOctets(com.locojoy.base.Octets _v_); // 
	public void setTick(long _v_); // 插入到离线协议的时间
}
