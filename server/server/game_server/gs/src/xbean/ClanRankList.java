
package xbean;

public interface ClanRankList extends mkdb.Bean {
	public ClanRankList copy(); // deep clone
	public ClanRankList toData(); // a Data instance
	public ClanRankList toBean(); // a Bean instance
	public ClanRankList toDataIf(); // a Data instance If need. else return this
	public ClanRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.ClanRankRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.ClanRankRecord> getRecordsAsData(); // 所有的记录

}
