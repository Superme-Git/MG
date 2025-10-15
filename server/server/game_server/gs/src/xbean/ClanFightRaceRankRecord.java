
package xbean;

public interface ClanFightRaceRankRecord extends mkdb.Bean {
	public ClanFightRaceRankRecord copy(); // deep clone
	public ClanFightRaceRankRecord toData(); // a Data instance
	public ClanFightRaceRankRecord toBean(); // a Bean instance
	public ClanFightRaceRankRecord toDataIf(); // a Data instance If need. else return this
	public ClanFightRaceRankRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTriggertime(); // 
	public xbean.MarshalClanFightRaceRankRecord getMarshaldata(); // 

	public void setTriggertime(long _v_); // 
}
