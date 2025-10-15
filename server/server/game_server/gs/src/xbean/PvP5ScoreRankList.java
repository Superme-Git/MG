
package xbean;

public interface PvP5ScoreRankList extends mkdb.Bean {
	public PvP5ScoreRankList copy(); // deep clone
	public PvP5ScoreRankList toData(); // a Data instance
	public PvP5ScoreRankList toBean(); // a Bean instance
	public PvP5ScoreRankList toDataIf(); // a Data instance If need. else return this
	public PvP5ScoreRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.PvP5ScoreRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.PvP5ScoreRecord> getRecordsAsData(); // 所有的记录

}
