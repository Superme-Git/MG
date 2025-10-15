
package xbean;

public interface PetScoreRankList extends mkdb.Bean {
	public PetScoreRankList copy(); // deep clone
	public PetScoreRankList toData(); // a Data instance
	public PetScoreRankList toBean(); // a Bean instance
	public PetScoreRankList toDataIf(); // a Data instance If need. else return this
	public PetScoreRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.PetScoreListRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.PetScoreListRecord> getRecordsAsData(); // 所有的记录

}
