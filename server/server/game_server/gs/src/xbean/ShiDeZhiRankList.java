
package xbean;

public interface ShiDeZhiRankList extends mkdb.Bean {
	public ShiDeZhiRankList copy(); // deep clone
	public ShiDeZhiRankList toData(); // a Data instance
	public ShiDeZhiRankList toBean(); // a Bean instance
	public ShiDeZhiRankList toDataIf(); // a Data instance If need. else return this
	public ShiDeZhiRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.MasterRankRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.MasterRankRecord> getRecordsAsData(); // 所有的记录

}
