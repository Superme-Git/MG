
package xbean;

public interface ClanZongHeRankList extends mkdb.Bean {
	public ClanZongHeRankList copy(); // deep clone
	public ClanZongHeRankList toData(); // a Data instance
	public ClanZongHeRankList toBean(); // a Bean instance
	public ClanZongHeRankList toDataIf(); // a Data instance If need. else return this
	public ClanZongHeRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.ClanZongHeRankRecord> getRecords(); // 
	public java.util.List<xbean.ClanZongHeRankRecord> getRecordsAsData(); // 

}
