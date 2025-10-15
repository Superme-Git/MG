
package xbean;

public interface ClanFight extends mkdb.Bean {
	public ClanFight copy(); // deep clone
	public ClanFight toData(); // a Data instance
	public ClanFight toBean(); // a Bean instance
	public ClanFight toDataIf(); // a Data instance If need. else return this
	public ClanFight toBeanIf(); // a Bean instance If need. else return this

	public long getClan1(); // 第一个公会 by changhao
	public int getClanindex1(); // 第一个公会 by changhao
	public String getClanname1(); // 第一个公会名字 by changhao
	public com.locojoy.base.Octets getClanname1Octets(); // 第一个公会名字 by changhao
	public long getClan2(); // 第二个公会 by changhao
	public int getClanindex2(); // 第二个公会 by changhao
	public String getClanname2(); // 第二个公会名字 by changhao
	public com.locojoy.base.Octets getClanname2Octets(); // 第二个公会名字 by changhao
	public int getClanlevel1(); // 第一个公会等级 by changhao
	public int getClanlevel2(); // 第二个公会等级 by changhao
	public int getClan1scroe(); // clan1积分 by changhao
	public int getClan2scroe(); // clan2积分 by changhao
	public int getWinner(); // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao

	public void setClan1(long _v_); // 第一个公会 by changhao
	public void setClanindex1(int _v_); // 第一个公会 by changhao
	public void setClanname1(String _v_); // 第一个公会名字 by changhao
	public void setClanname1Octets(com.locojoy.base.Octets _v_); // 第一个公会名字 by changhao
	public void setClan2(long _v_); // 第二个公会 by changhao
	public void setClanindex2(int _v_); // 第二个公会 by changhao
	public void setClanname2(String _v_); // 第二个公会名字 by changhao
	public void setClanname2Octets(com.locojoy.base.Octets _v_); // 第二个公会名字 by changhao
	public void setClanlevel1(int _v_); // 第一个公会等级 by changhao
	public void setClanlevel2(int _v_); // 第二个公会等级 by changhao
	public void setClan1scroe(int _v_); // clan1积分 by changhao
	public void setClan2scroe(int _v_); // clan2积分 by changhao
	public void setWinner(int _v_); // 胜利者(-1是还没结果0是第一个公会赢1是第2个公会赢) by changhao
}
