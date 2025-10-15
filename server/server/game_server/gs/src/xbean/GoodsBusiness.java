
package xbean;

public interface GoodsBusiness extends mkdb.Bean {
	public GoodsBusiness copy(); // deep clone
	public GoodsBusiness toData(); // a Data instance
	public GoodsBusiness toBean(); // a Bean instance
	public GoodsBusiness toDataIf(); // a Data instance If need. else return this
	public GoodsBusiness toBeanIf(); // a Bean instance If need. else return this

	public long getBuynum(); // 本期购买的数量
	public long getSalenum(); // 本期售卖的数量
	public long getLastnum(); // 上期成交量
	public int getPriorperiodprice(); // 上期价格
	public int getPrice(); // 商品价格
	public int getCount(); // 周期成交量小于等于零的次数,降价后次数清零重新计数

	public void setBuynum(long _v_); // 本期购买的数量
	public void setSalenum(long _v_); // 本期售卖的数量
	public void setLastnum(long _v_); // 上期成交量
	public void setPriorperiodprice(int _v_); // 上期价格
	public void setPrice(int _v_); // 商品价格
	public void setCount(int _v_); // 周期成交量小于等于零的次数,降价后次数清零重新计数
}
