
package xbean;

public interface RoleChargeLimit extends mkdb.Bean {
	public RoleChargeLimit copy(); // deep clone
	public RoleChargeLimit toData(); // a Data instance
	public RoleChargeLimit toBean(); // a Bean instance
	public RoleChargeLimit toDataIf(); // a Data instance If need. else return this
	public RoleChargeLimit toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimit(); // key为商品id,value为充值次数记录
	public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimitAsData(); // key为商品id,value为充值次数记录

}
