
package xbean;

public interface RoleRankList extends mkdb.Bean {
	public RoleRankList copy(); // deep clone
	public RoleRankList toData(); // a Data instance
	public RoleRankList toBean(); // a Bean instance
	public RoleRankList toDataIf(); // a Data instance If need. else return this
	public RoleRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.RoleRankRecord> getRecords(); // 
	public java.util.List<xbean.RoleRankRecord> getRecordsAsData(); // 

}
