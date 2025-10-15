
package xbean;

public interface TradingMap extends mkdb.Bean {
	public TradingMap copy(); // deep clone
	public TradingMap toData(); // a Data instance
	public TradingMap toBean(); // a Bean instance
	public TradingMap toDataIf(); // a Data instance If need. else return this
	public TradingMap toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.TradingPrice> getSelllist(); // 排序好的出售列表
	public java.util.List<xbean.TradingPrice> getSelllistAsData(); // 排序好的出售列表
	public java.util.List<xbean.TradingPrice> getBuylist(); // 排序好的购买列表
	public java.util.List<xbean.TradingPrice> getBuylistAsData(); // 排序好的购买列表

}
