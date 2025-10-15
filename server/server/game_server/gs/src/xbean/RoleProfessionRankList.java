
package xbean;

public interface RoleProfessionRankList extends mkdb.Bean {
	public RoleProfessionRankList copy(); // deep clone
	public RoleProfessionRankList toData(); // a Data instance
	public RoleProfessionRankList toBean(); // a Bean instance
	public RoleProfessionRankList toDataIf(); // a Data instance If need. else return this
	public RoleProfessionRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.RoleProfessionRankRecord> getRecords(); // 
	public java.util.List<xbean.RoleProfessionRankRecord> getRecordsAsData(); // 

}
