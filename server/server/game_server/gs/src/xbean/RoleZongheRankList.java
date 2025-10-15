
package xbean;

public interface RoleZongheRankList extends mkdb.Bean {
	public RoleZongheRankList copy(); // deep clone
	public RoleZongheRankList toData(); // a Data instance
	public RoleZongheRankList toBean(); // a Bean instance
	public RoleZongheRankList toDataIf(); // a Data instance If need. else return this
	public RoleZongheRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.RoleZongheRankRecord> getRecords(); // 所有的记录
	public java.util.List<xbean.RoleZongheRankRecord> getRecordsAsData(); // 所有的记录

}
