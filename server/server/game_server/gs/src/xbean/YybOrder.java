
package xbean;

public interface YybOrder extends mkdb.Bean {
	public YybOrder copy(); // deep clone
	public YybOrder toData(); // a Data instance
	public YybOrder toBean(); // a Bean instance
	public YybOrder toDataIf(); // a Data instance If need. else return this
	public YybOrder toBeanIf(); // a Bean instance If need. else return this

	public int getUserid(); // 充值的userid
	public long getRoleid(); // 充值的角色id
	public int getStatus(); // 0,新建订单; 1,订单完成; -1,订单错误
	public String getPlatname(); // weixin qq
	public com.locojoy.base.Octets getPlatnameOctets(); // weixin qq
	public int getNum(); // 充值金额
	public int getLocalsaveamt(); // 本地累计充值金额
	public int getLocalbalance(); // 本地充值金额
	public int getLocalgenbalance(); // 本地赠送金额
	public long getCreatetime(); // 
	public int getRetrytimes(); // 超过10次单子被标记为失败
	public int getConsumetype(); // 

	public void setUserid(int _v_); // 充值的userid
	public void setRoleid(long _v_); // 充值的角色id
	public void setStatus(int _v_); // 0,新建订单; 1,订单完成; -1,订单错误
	public void setPlatname(String _v_); // weixin qq
	public void setPlatnameOctets(com.locojoy.base.Octets _v_); // weixin qq
	public void setNum(int _v_); // 充值金额
	public void setLocalsaveamt(int _v_); // 本地累计充值金额
	public void setLocalbalance(int _v_); // 本地充值金额
	public void setLocalgenbalance(int _v_); // 本地赠送金额
	public void setCreatetime(long _v_); // 
	public void setRetrytimes(int _v_); // 超过10次单子被标记为失败
	public void setConsumetype(int _v_); // 
}
