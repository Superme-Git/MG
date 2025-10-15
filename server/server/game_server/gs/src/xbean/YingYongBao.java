
package xbean;

public interface YingYongBao extends mkdb.Bean {
	public YingYongBao copy(); // deep clone
	public YingYongBao toData(); // a Data instance
	public YingYongBao toBean(); // a Bean instance
	public YingYongBao toDataIf(); // a Data instance If need. else return this
	public YingYongBao toBeanIf(); // a Bean instance If need. else return this

	public String getOpenid(); // 
	public com.locojoy.base.Octets getOpenidOctets(); // 
	public String getOpenkey(); // 
	public com.locojoy.base.Octets getOpenkeyOctets(); // 
	public String getPaytoken(); // 
	public com.locojoy.base.Octets getPaytokenOctets(); // 
	public String getPf(); // 
	public com.locojoy.base.Octets getPfOctets(); // 
	public String getPfkey(); // 
	public com.locojoy.base.Octets getPfkeyOctets(); // 
	public String getZoneid(); // 
	public com.locojoy.base.Octets getZoneidOctets(); // 
	public String getPlatformname(); // 手Q用户:qq 微信用户:weixin
	public com.locojoy.base.Octets getPlatformnameOctets(); // 手Q用户:qq 微信用户:weixin

	public void setOpenid(String _v_); // 
	public void setOpenidOctets(com.locojoy.base.Octets _v_); // 
	public void setOpenkey(String _v_); // 
	public void setOpenkeyOctets(com.locojoy.base.Octets _v_); // 
	public void setPaytoken(String _v_); // 
	public void setPaytokenOctets(com.locojoy.base.Octets _v_); // 
	public void setPf(String _v_); // 
	public void setPfOctets(com.locojoy.base.Octets _v_); // 
	public void setPfkey(String _v_); // 
	public void setPfkeyOctets(com.locojoy.base.Octets _v_); // 
	public void setZoneid(String _v_); // 
	public void setZoneidOctets(com.locojoy.base.Octets _v_); // 
	public void setPlatformname(String _v_); // 手Q用户:qq 微信用户:weixin
	public void setPlatformnameOctets(com.locojoy.base.Octets _v_); // 手Q用户:qq 微信用户:weixin
}
