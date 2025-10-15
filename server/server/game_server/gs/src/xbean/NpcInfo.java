
package xbean;

public interface NpcInfo extends mkdb.Bean {
	public NpcInfo copy(); // deep clone
	public NpcInfo toData(); // a Data instance
	public NpcInfo toBean(); // a Bean instance
	public NpcInfo toDataIf(); // a Data instance If need. else return this
	public NpcInfo toBeanIf(); // a Bean instance If need. else return this

	public int getIsinbattle(); // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除

	public void setIsinbattle(int _v_); // 0表示不在战斗中,1表示在战斗中,2表示在战斗中且已经到期,战斗结束就要删除
}
