
package xbean;

public interface RoleTradingInfoList extends mkdb.Bean {
	public RoleTradingInfoList copy(); // deep clone
	public RoleTradingInfoList toData(); // a Data instance
	public RoleTradingInfoList toBean(); // a Bean instance
	public RoleTradingInfoList toDataIf(); // a Data instance If need. else return this
	public RoleTradingInfoList toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<String, String> getTradinglist(); // 
	public java.util.Map<String, String> getTradinglistAsData(); // 

}
