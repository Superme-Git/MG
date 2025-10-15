
package xbean;

public interface ClanFights extends mkdb.Bean {
	public ClanFights copy(); // deep clone
	public ClanFights toData(); // a Data instance
	public ClanFights toBean(); // a Bean instance
	public ClanFights toDataIf(); // a Data instance If need. else return this
	public ClanFights toBeanIf(); // a Bean instance If need. else return this

	public long getCreatetime(); // 创建时间 by changhao
	public java.util.Map<Long, xbean.ClanFight> getClan1vschan2(); // 配对的公会战 by changhao
	public java.util.Map<Long, xbean.ClanFight> getClan1vschan2AsData(); // 配对的公会战 by changhao

	public void setCreatetime(long _v_); // 创建时间 by changhao
}
