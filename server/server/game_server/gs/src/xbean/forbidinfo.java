
package xbean;

public interface forbidinfo extends mkdb.Bean {
	public forbidinfo copy(); // deep clone
	public forbidinfo toData(); // a Data instance
	public forbidinfo toBean(); // a Bean instance
	public forbidinfo toDataIf(); // a Data instance If need. else return this
	public forbidinfo toBeanIf(); // a Bean instance If need. else return this

	public long getForbidtime(); // 
	public String getReason(); // 
	public com.locojoy.base.Octets getReasonOctets(); // 

	public void setForbidtime(long _v_); // 
	public void setReason(String _v_); // 
	public void setReasonOctets(com.locojoy.base.Octets _v_); // 
}
