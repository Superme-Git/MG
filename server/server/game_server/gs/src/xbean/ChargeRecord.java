
package xbean;

public interface ChargeRecord extends mkdb.Bean {
	public ChargeRecord copy(); // deep clone
	public ChargeRecord toData(); // a Data instance
	public ChargeRecord toBean(); // a Bean instance
	public ChargeRecord toDataIf(); // a Data instance If need. else return this
	public ChargeRecord toBeanIf(); // a Bean instance If need. else return this

	public int getChargecount(); // 已经充值次数
	public long getChargetime(); // 上次的时间

	public void setChargecount(int _v_); // 已经充值次数
	public void setChargetime(long _v_); // 上次的时间
}
