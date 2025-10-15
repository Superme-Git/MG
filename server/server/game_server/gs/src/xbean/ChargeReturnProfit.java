
package xbean;

public interface ChargeReturnProfit extends mkdb.Bean {
	public ChargeReturnProfit copy(); // deep clone
	public ChargeReturnProfit toData(); // a Data instance
	public ChargeReturnProfit toBean(); // a Bean instance
	public ChargeReturnProfit toDataIf(); // a Data instance If need. else return this
	public ChargeReturnProfit toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 
	public int getValue(); // 
	public int getMaxvalue(); // 
	public int getStatus(); // 0是领取了1是未领取2是未到达 by changhao

	public void setId(int _v_); // 
	public void setValue(int _v_); // 
	public void setMaxvalue(int _v_); // 
	public void setStatus(int _v_); // 0是领取了1是未领取2是未到达 by changhao
}
