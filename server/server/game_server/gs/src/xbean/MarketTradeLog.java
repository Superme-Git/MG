
package xbean;

public interface MarketTradeLog extends mkdb.Bean {
	public MarketTradeLog copy(); // deep clone
	public MarketTradeLog toData(); // a Data instance
	public MarketTradeLog toBean(); // a Bean instance
	public MarketTradeLog toDataIf(); // a Data instance If need. else return this
	public MarketTradeLog toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.LogBean> getBuylog(); // 买记录
	public java.util.List<xbean.LogBean> getBuylogAsData(); // 买记录
	public java.util.List<xbean.LogBean> getSalelog(); // 卖记录
	public java.util.List<xbean.LogBean> getSalelogAsData(); // 卖记录

}
