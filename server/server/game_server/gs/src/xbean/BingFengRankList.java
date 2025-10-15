
package xbean;

public interface BingFengRankList extends mkdb.Bean {
	public BingFengRankList copy(); // deep clone
	public BingFengRankList toData(); // a Data instance
	public BingFengRankList toBean(); // a Bean instance
	public BingFengRankList toDataIf(); // a Data instance If need. else return this
	public BingFengRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.BingFengListRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.BingFengListRecord> getRecordsAsData(); // 所有的记录

}
