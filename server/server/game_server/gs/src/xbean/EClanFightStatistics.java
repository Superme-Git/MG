
package xbean;

public interface EClanFightStatistics extends mkdb.Bean {
	public EClanFightStatistics copy(); // deep clone
	public EClanFightStatistics toData(); // a Data instance
	public EClanFightStatistics toBean(); // a Bean instance
	public EClanFightStatistics toDataIf(); // a Data instance If need. else return this
	public EClanFightStatistics toBeanIf(); // a Bean instance If need. else return this

	public int getAct(); // 行动力 by changhao
	public int getScore(); // 积分 by changhao
	public long getEntertime(); // 进入战场时间 by changhao
	public int getWinneritemnum(); // 获取胜利宝箱的数量 by changhao
	public int getClancelebrateitemnum(); // 获取公会庆祝宝箱的数量 by changhao
	public int getWinnum(); // 连续胜利的常数 by changhao
	public long getLastcbattlefieldrankliststamp(); // 上次请求战场积分时间戳 by changhao
	public long getLastlosestamp(); // 上次战败时间戳 by changhao

	public void setAct(int _v_); // 行动力 by changhao
	public void setScore(int _v_); // 积分 by changhao
	public void setEntertime(long _v_); // 进入战场时间 by changhao
	public void setWinneritemnum(int _v_); // 获取胜利宝箱的数量 by changhao
	public void setClancelebrateitemnum(int _v_); // 获取公会庆祝宝箱的数量 by changhao
	public void setWinnum(int _v_); // 连续胜利的常数 by changhao
	public void setLastcbattlefieldrankliststamp(long _v_); // 上次请求战场积分时间戳 by changhao
	public void setLastlosestamp(long _v_); // 上次战败时间戳 by changhao
}
