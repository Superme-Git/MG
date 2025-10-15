
package xbean;

public interface RoleTradingRecord extends mkdb.Bean {
	public RoleTradingRecord copy(); // deep clone
	public RoleTradingRecord toData(); // a Data instance
	public RoleTradingRecord toBean(); // a Bean instance
	public RoleTradingRecord toDataIf(); // a Data instance If need. else return this
	public RoleTradingRecord toBeanIf(); // a Bean instance If need. else return this

	public String getTradingid(); // 挂单号
	public com.locojoy.base.Octets getTradingidOctets(); // 挂单号
	public long getRoleid(); // 角色id
	public int getTradingtype(); // 交易类型  0买入  1出售
	public int getCurnum(); // 当前数量
	public int getAllnum(); // 总数量
	public int getPrice(); // 价格
	public long getCreatetime(); // 挂单时间
	public long getTradingtime(); // 交易时间
	public int getState(); // 状态    1表示已经退回或者交易完

	public void setTradingid(String _v_); // 挂单号
	public void setTradingidOctets(com.locojoy.base.Octets _v_); // 挂单号
	public void setRoleid(long _v_); // 角色id
	public void setTradingtype(int _v_); // 交易类型  0买入  1出售
	public void setCurnum(int _v_); // 当前数量
	public void setAllnum(int _v_); // 总数量
	public void setPrice(int _v_); // 价格
	public void setCreatetime(long _v_); // 挂单时间
	public void setTradingtime(long _v_); // 交易时间
	public void setState(int _v_); // 状态    1表示已经退回或者交易完
}
