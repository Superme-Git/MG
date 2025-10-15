
package xbean;

public interface PvP3ScoreRankList extends mkdb.Bean {
	public PvP3ScoreRankList copy(); // deep clone
	public PvP3ScoreRankList toData(); // a Data instance
	public PvP3ScoreRankList toBean(); // a Bean instance
	public PvP3ScoreRankList toDataIf(); // a Data instance If need. else return this
	public PvP3ScoreRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.PvP3ScoreRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.PvP3ScoreRecord> getRecordsAsData(); // 所有的记录

}
