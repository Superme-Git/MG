
package xbean;

public interface RedPackRankList extends mkdb.Bean {
	public RedPackRankList copy(); // deep clone
	public RedPackRankList toData(); // a Data instance
	public RedPackRankList toBean(); // a Bean instance
	public RedPackRankList toDataIf(); // a Data instance If need. else return this
	public RedPackRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.RedPackRecord> getRecords(); // 红包所有的记录 by changhao
	public java.util.List<xbean.RedPackRecord> getRecordsAsData(); // 红包所有的记录 by changhao

}
