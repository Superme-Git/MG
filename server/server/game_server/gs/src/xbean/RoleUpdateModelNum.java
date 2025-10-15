
package xbean;

public interface RoleUpdateModelNum extends mkdb.Bean {
	public RoleUpdateModelNum copy(); // deep clone
	public RoleUpdateModelNum toData(); // a Data instance
	public RoleUpdateModelNum toBean(); // a Bean instance
	public RoleUpdateModelNum toDataIf(); // a Data instance If need. else return this
	public RoleUpdateModelNum toBeanIf(); // a Bean instance If need. else return this

	public int getTradingbuyfushinum(); // 每日交易所购买符石数量

	public void setTradingbuyfushinum(int _v_); // 每日交易所购买符石数量
}
