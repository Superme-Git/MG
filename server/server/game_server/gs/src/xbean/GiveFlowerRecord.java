
package xbean;

public interface GiveFlowerRecord extends mkdb.Bean {
	public GiveFlowerRecord copy(); // deep clone
	public GiveFlowerRecord toData(); // a Data instance
	public GiveFlowerRecord toBean(); // a Bean instance
	public GiveFlowerRecord toDataIf(); // a Data instance If need. else return this
	public GiveFlowerRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTriggertime(); // 
	public xbean.MarshalGiveFlowerRecord getMarshaldata(); // 

	public void setTriggertime(long _v_); // 
}
