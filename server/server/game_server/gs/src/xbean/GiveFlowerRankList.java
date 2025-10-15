
package xbean;

public interface GiveFlowerRankList extends mkdb.Bean {
	public GiveFlowerRankList copy(); // deep clone
	public GiveFlowerRankList toData(); // a Data instance
	public GiveFlowerRankList toBean(); // a Bean instance
	public GiveFlowerRankList toDataIf(); // a Data instance If need. else return this
	public GiveFlowerRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.GiveFlowerRecord> getRecords(); // 送花记录 by changhao
	public java.util.List<xbean.GiveFlowerRecord> getRecordsAsData(); // 送花记录 by changhao

}
