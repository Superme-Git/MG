
package xbean;

public interface LocojoyPlatOrderInfo extends mkdb.Bean {
	public LocojoyPlatOrderInfo copy(); // deep clone
	public LocojoyPlatOrderInfo toData(); // a Data instance
	public LocojoyPlatOrderInfo toBean(); // a Bean instance
	public LocojoyPlatOrderInfo toDataIf(); // a Data instance If need. else return this
	public LocojoyPlatOrderInfo toBeanIf(); // a Bean instance If need. else return this

	public long getCreatetime(); // 
	public String getOrderserialplat(); // 
	public com.locojoy.base.Octets getOrderserialplatOctets(); // 
	public String getOrderserialgame(); // 
	public com.locojoy.base.Octets getOrderserialgameOctets(); // 
	public String getUsername(); // 
	public com.locojoy.base.Octets getUsernameOctets(); // 
	public <T extends com.locojoy.base.Marshal.Marshal> T getVars(T _v_); // 
	public boolean isVarsEmpty(); // 
	public byte[] getVarsCopy(); // 

	public void setCreatetime(long _v_); // 
	public void setOrderserialplat(String _v_); // 
	public void setOrderserialplatOctets(com.locojoy.base.Octets _v_); // 
	public void setOrderserialgame(String _v_); // 
	public void setOrderserialgameOctets(com.locojoy.base.Octets _v_); // 
	public void setUsername(String _v_); // 
	public void setUsernameOctets(com.locojoy.base.Octets _v_); // 
	public void setVars(com.locojoy.base.Marshal.Marshal _v_); // 
	public void setVarsCopy(byte[] _v_); // 
}
