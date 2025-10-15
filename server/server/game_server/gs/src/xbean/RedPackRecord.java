
package xbean;

public interface RedPackRecord extends mkdb.Bean {
	public RedPackRecord copy(); // deep clone
	public RedPackRecord toData(); // a Data instance
	public RedPackRecord toBean(); // a Bean instance
	public RedPackRecord toDataIf(); // a Data instance If need. else return this
	public RedPackRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTriggertime(); // 
	public xbean.MarshalRedPackRecord getMarshaldata(); // 

	public void setTriggertime(long _v_); // 
}
