
package xbean;

public interface RoleBlackMarket extends mkdb.Bean {
	public RoleBlackMarket copy(); // deep clone
	public RoleBlackMarket toData(); // a Data instance
	public RoleBlackMarket toBean(); // a Bean instance
	public RoleBlackMarket toDataIf(); // a Data instance If need. else return this
	public RoleBlackMarket toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.GoldOrder> getGoldordersale(); // 出售列表
	public java.util.Map<Long, xbean.GoldOrder> getGoldordersaleAsData(); // 出售列表
	public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuy(); // 购买列表
	public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuyAsData(); // 购买列表

}
