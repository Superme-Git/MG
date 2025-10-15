
package xbean;

public interface ClanProgressRankList extends mkdb.Bean {
	public ClanProgressRankList copy(); // deep clone
	public ClanProgressRankList toData(); // a Data instance
	public ClanProgressRankList toBean(); // a Bean instance
	public ClanProgressRankList toDataIf(); // a Data instance If need. else return this
	public ClanProgressRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.ClanProgressRankRecord> getRecords(); // 
	public java.util.List<xbean.ClanProgressRankRecord> getRecordsAsData(); // 

}
