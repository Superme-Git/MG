
package xbean;

public interface ClanFightHistroyRankList extends mkdb.Bean {
	public ClanFightHistroyRankList copy(); // deep clone
	public ClanFightHistroyRankList toData(); // a Data instance
	public ClanFightHistroyRankList toBean(); // a Bean instance
	public ClanFightHistroyRankList toDataIf(); // a Data instance If need. else return this
	public ClanFightHistroyRankList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.ClanFightHistroyRankRecord> getRecords(); // 公会战历史记录 by changhao
	public java.util.List<xbean.ClanFightHistroyRankRecord> getRecordsAsData(); // 公会战历史记录 by changhao

}
