
package xbean;

public interface ChargeOrder extends mkdb.Bean {
	public ChargeOrder copy(); // deep clone
	public ChargeOrder toData(); // a Data instance
	public ChargeOrder toBean(); // a Bean instance
	public ChargeOrder toDataIf(); // a Data instance If need. else return this
	public ChargeOrder toBeanIf(); // a Bean instance If need. else return this

	public int getStatus(); // 
	public String getPlatformsn(); // 充值的平台的sn
	public com.locojoy.base.Octets getPlatformsnOctets(); // 充值的平台的sn
	public String getPlattype(); // 来自哪个平台的充值
	public com.locojoy.base.Octets getPlattypeOctets(); // 来自哪个平台的充值
	public String getPlatformuid(); // 充值的平台的账号id
	public com.locojoy.base.Octets getPlatformuidOctets(); // 充值的平台的账号id
	public String getReceipt(); // 只有苹果官方平台才有
	public com.locojoy.base.Octets getReceiptOctets(); // 只有苹果官方平台才有
	public String getTranscationid(); // 只有苹果官方平台才有
	public com.locojoy.base.Octets getTranscationidOctets(); // 只有苹果官方平台才有
	public long getRoleid(); // 充值的角色id
	public int getNum(); // 充值金额
	public int getGoodid(); // 
	public int getGoodnum(); // 
	public long getCreatetime(); // 
	public int getUserid(); // 充值的userid
	public int getRetrytimes(); // 超过10次单子被标记为失败

	public void setStatus(int _v_); // 
	public void setPlatformsn(String _v_); // 充值的平台的sn
	public void setPlatformsnOctets(com.locojoy.base.Octets _v_); // 充值的平台的sn
	public void setPlattype(String _v_); // 来自哪个平台的充值
	public void setPlattypeOctets(com.locojoy.base.Octets _v_); // 来自哪个平台的充值
	public void setPlatformuid(String _v_); // 充值的平台的账号id
	public void setPlatformuidOctets(com.locojoy.base.Octets _v_); // 充值的平台的账号id
	public void setReceipt(String _v_); // 只有苹果官方平台才有
	public void setReceiptOctets(com.locojoy.base.Octets _v_); // 只有苹果官方平台才有
	public void setTranscationid(String _v_); // 只有苹果官方平台才有
	public void setTranscationidOctets(com.locojoy.base.Octets _v_); // 只有苹果官方平台才有
	public void setRoleid(long _v_); // 充值的角色id
	public void setNum(int _v_); // 充值金额
	public void setGoodid(int _v_); // 
	public void setGoodnum(int _v_); // 
	public void setCreatetime(long _v_); // 
	public void setUserid(int _v_); // 充值的userid
	public void setRetrytimes(int _v_); // 超过10次单子被标记为失败
}
