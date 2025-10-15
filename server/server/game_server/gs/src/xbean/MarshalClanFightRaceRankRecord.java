
package xbean;

public interface MarshalClanFightRaceRankRecord extends mkdb.Bean {
	public MarshalClanFightRaceRankRecord copy(); // deep clone
	public MarshalClanFightRaceRankRecord toData(); // a Data instance
	public MarshalClanFightRaceRankRecord toBean(); // a Bean instance
	public MarshalClanFightRaceRankRecord toDataIf(); // a Data instance If need. else return this
	public MarshalClanFightRaceRankRecord toBeanIf(); // a Bean instance If need. else return this

	public long getClanid(); // 公会id by changhao
	public String getClanname(); // 公会名字 by changhao
	public com.locojoy.base.Octets getClannameOctets(); // 公会名字 by changhao
	public int getClanlevel(); // 公会等级 by changhao
	public int getFightcount(); // 战斗次数 by changhao
	public int getWincount(); // 胜利次数 by changhao
	public int getScroe(); // 积分 by changhao

	public void setClanid(long _v_); // 公会id by changhao
	public void setClanname(String _v_); // 公会名字 by changhao
	public void setClannameOctets(com.locojoy.base.Octets _v_); // 公会名字 by changhao
	public void setClanlevel(int _v_); // 公会等级 by changhao
	public void setFightcount(int _v_); // 战斗次数 by changhao
	public void setWincount(int _v_); // 胜利次数 by changhao
	public void setScroe(int _v_); // 积分 by changhao
}
