
package xbean;

public interface RoleLevelRankList extends mkdb.Bean {
	public RoleLevelRankList copy(); // deep clone
	public RoleLevelRankList toData(); // a Data instance
	public RoleLevelRankList toBean(); // a Bean instance
	public RoleLevelRankList toDataIf(); // a Data instance If need. else return this
	public RoleLevelRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.RoleLevelListRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.RoleLevelListRecord> getRecordsAsData(); // 所有的记录

}
