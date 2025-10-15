
package xbean;

public interface NameState extends xdb.Bean {
	public NameState copy(); // deep clone
	public NameState toData(); // a Data instance
	public NameState toBean(); // a Bean instance
	public NameState toDataIf(); // a Data instance If need. else return this
	public NameState toBeanIf(); // a Bean instance If need. else return this

	public final static int STATE_ALLOCATE = 0; // 
	public final static int STATE_CONFIRM = 1; // 

	public int getState(); // 名字分配状态
	public int getLocalid(); // 名字分配的远端分组编号
	public String getPeerip(); // 名字分配的远端服务器地址
	public com.goldhuman.Common.Octets getPeeripOctets(); // 名字分配的远端服务器地址
	public long getTime(); // 名字分配的时间

	public void setState(int _v_); // 名字分配状态
	public void setLocalid(int _v_); // 名字分配的远端分组编号
	public void setPeerip(String _v_); // 名字分配的远端服务器地址
	public void setPeeripOctets(com.goldhuman.Common.Octets _v_); // 名字分配的远端服务器地址
	public void setTime(long _v_); // 名字分配的时间
}
