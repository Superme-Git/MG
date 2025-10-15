
package xbean;

public interface EChargeReturnProfit extends mkdb.Bean {
	public EChargeReturnProfit copy(); // deep clone
	public EChargeReturnProfit toData(); // a Data instance
	public EChargeReturnProfit toBean(); // a Bean instance
	public EChargeReturnProfit toDataIf(); // a Data instance If need. else return this
	public EChargeReturnProfit toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmap(); // 
	public java.util.Map<Integer, xbean.ChargeReturnProfit> getReturnprofitmapAsData(); // 

}
