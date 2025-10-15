
package xbean;

public interface ClanFightHistroyRankRecord extends mkdb.Bean {
	public ClanFightHistroyRankRecord copy(); // deep clone
	public ClanFightHistroyRankRecord toData(); // a Data instance
	public ClanFightHistroyRankRecord toBean(); // a Bean instance
	public ClanFightHistroyRankRecord toDataIf(); // a Data instance If need. else return this
	public ClanFightHistroyRankRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTriggertime(); // 
	public xbean.MarshalClanFightHistroyRankRecord getMarshaldata(); // 

	public void setTriggertime(long _v_); // 
}
