
package xbean;

public interface OnetimeEvent extends mkdb.Bean {
	public OnetimeEvent copy(); // deep clone
	public OnetimeEvent toData(); // a Data instance
	public OnetimeEvent toBean(); // a Bean instance
	public OnetimeEvent toDataIf(); // a Data instance If need. else return this
	public OnetimeEvent toBeanIf(); // a Bean instance If need. else return this

	public fire.pb.WorldEventTab.OnetimeTask getOnetimeevent(); // 

	public void setOnetimeevent(fire.pb.WorldEventTab.OnetimeTask _v_); // 
}
