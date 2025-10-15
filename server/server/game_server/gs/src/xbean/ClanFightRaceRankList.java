
package xbean;

public interface ClanFightRaceRankList extends mkdb.Bean {
	public ClanFightRaceRankList copy(); // deep clone
	public ClanFightRaceRankList toData(); // a Data instance
	public ClanFightRaceRankList toBean(); // a Bean instance
	public ClanFightRaceRankList toDataIf(); // a Data instance If need. else return this
	public ClanFightRaceRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.ClanFightRaceRankRecord> getRecords(); // 送花记录 by changhao
	public java.util.List<xbean.ClanFightRaceRankRecord> getRecordsAsData(); // 送花记录 by changhao

}
