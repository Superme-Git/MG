
package xbean;

public interface ChargeHistory extends mkdb.Bean {
	public ChargeHistory copy(); // deep clone
	public ChargeHistory toData(); // a Data instance
	public ChargeHistory toBean(); // a Bean instance
	public ChargeHistory toDataIf(); // a Data instance If need. else return this
	public ChargeHistory toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.ChargeOrder> getCharges(); // 
	public java.util.Map<Long, xbean.ChargeOrder> getChargesAsData(); // 

}
