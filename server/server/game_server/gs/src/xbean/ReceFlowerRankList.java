
package xbean;

public interface ReceFlowerRankList extends mkdb.Bean {
	public ReceFlowerRankList copy(); // deep clone
	public ReceFlowerRankList toData(); // a Data instance
	public ReceFlowerRankList toBean(); // a Bean instance
	public ReceFlowerRankList toDataIf(); // a Data instance If need. else return this
	public ReceFlowerRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.ReceFlowerRecord> getRecords(); // 送花记录 by changhao
	public java.util.List<xbean.ReceFlowerRecord> getRecordsAsData(); // 送花记录 by changhao

}
