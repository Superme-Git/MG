
package xbean;

public interface ReceFlowerRecord extends mkdb.Bean {
	public ReceFlowerRecord copy(); // deep clone
	public ReceFlowerRecord toData(); // a Data instance
	public ReceFlowerRecord toBean(); // a Bean instance
	public ReceFlowerRecord toDataIf(); // a Data instance If need. else return this
	public ReceFlowerRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTriggertime(); // 
	public xbean.MarshalReceFlowerRecord getMarshaldata(); // 

	public void setTriggertime(long _v_); // 
}
