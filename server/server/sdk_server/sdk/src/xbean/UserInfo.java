
package xbean;

public interface UserInfo extends mkdb.Bean {
	public UserInfo copy(); // deep clone
	public UserInfo toData(); // a Data instance
	public UserInfo toBean(); // a Bean instance
	public UserInfo toDataIf(); // a Data instance If need. else return this
	public UserInfo toBeanIf(); // a Bean instance If need. else return this

	public String getPlat(); // 
	public com.locojoy.base.Octets getPlatOctets(); // 
	public String getUid(); // 实际就是username，account，platformUID
	public com.locojoy.base.Octets getUidOctets(); // 实际就是username，account，platformUID

	public void setPlat(String _v_); // 
	public void setPlatOctets(com.locojoy.base.Octets _v_); // 
	public void setUid(String _v_); // 实际就是username，account，platformUID
	public void setUidOctets(com.locojoy.base.Octets _v_); // 实际就是username，account，platformUID
}
