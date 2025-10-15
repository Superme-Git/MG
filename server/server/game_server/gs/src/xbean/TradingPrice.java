
package xbean;

public interface TradingPrice extends mkdb.Bean {
	public TradingPrice copy(); // deep clone
	public TradingPrice toData(); // a Data instance
	public TradingPrice toBean(); // a Bean instance
	public TradingPrice toDataIf(); // a Data instance If need. else return this
	public TradingPrice toBeanIf(); // a Bean instance If need. else return this

	public String getTradingid(); // 订单号
	public com.locojoy.base.Octets getTradingidOctets(); // 订单号
	public int getPrice(); // 价格
	public int getNum(); // 数量
	public long getRoleid(); // 角色id
	public long getCreatetime(); // 挂单时间

	public void setTradingid(String _v_); // 订单号
	public void setTradingidOctets(com.locojoy.base.Octets _v_); // 订单号
	public void setPrice(int _v_); // 价格
	public void setNum(int _v_); // 数量
	public void setRoleid(long _v_); // 角色id
	public void setCreatetime(long _v_); // 挂单时间
}
