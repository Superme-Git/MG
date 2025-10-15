
package xbean;

public interface LocojoyPlatOrderInfo extends xdb.Bean {
	public LocojoyPlatOrderInfo copy(); // deep clone
	public LocojoyPlatOrderInfo toData(); // a Data instance
	public LocojoyPlatOrderInfo toBean(); // a Bean instance
	public LocojoyPlatOrderInfo toDataIf(); // a Data instance If need. else return this
	public LocojoyPlatOrderInfo toBeanIf(); // a Bean instance If need. else return this

	public long getCreatetime(); // 
	public String getOrderserialplat(); // 
	public com.goldhuman.Common.Octets getOrderserialplatOctets(); // 
	public String getOrderserialgame(); // 
	public com.goldhuman.Common.Octets getOrderserialgameOctets(); // 
	public String getUsername(); // 
	public com.goldhuman.Common.Octets getUsernameOctets(); // 
	public <T extends com.goldhuman.Common.Marshal.Marshal> T getVars(T _v_); // 
	public boolean isVarsEmpty(); // 
	public byte[] getVarsCopy(); // 

	public void setCreatetime(long _v_); // 
	public void setOrderserialplat(String _v_); // 
	public void setOrderserialplatOctets(com.goldhuman.Common.Octets _v_); // 
	public void setOrderserialgame(String _v_); // 
	public void setOrderserialgameOctets(com.goldhuman.Common.Octets _v_); // 
	public void setUsername(String _v_); // 
	public void setUsernameOctets(com.goldhuman.Common.Octets _v_); // 
	public void setVars(com.goldhuman.Common.Marshal.Marshal _v_); // 
	public void setVarsCopy(byte[] _v_); // 
}
