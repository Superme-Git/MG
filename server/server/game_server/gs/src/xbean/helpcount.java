
package xbean;

public interface helpcount extends mkdb.Bean {
	public helpcount copy(); // deep clone
	public helpcount toData(); // a Data instance
	public helpcount toBean(); // a Bean instance
	public helpcount toDataIf(); // a Data instance If need. else return this
	public helpcount toBeanIf(); // a Bean instance If need. else return this

	public long getExpvalue(); // 经验(援助战斗次数)
	public int getShengwangvalue(); // 声望
	public int getClanvalue(); // 帮贡
	public int getClanhishelpnum(); // 公会历史援助次数
	public int getHelpgiveitemnum(); // 援助物品次数
	public int getHelpitemnum(); // 求助物品次数

	public void setExpvalue(long _v_); // 经验(援助战斗次数)
	public void setShengwangvalue(int _v_); // 声望
	public void setClanvalue(int _v_); // 帮贡
	public void setClanhishelpnum(int _v_); // 公会历史援助次数
	public void setHelpgiveitemnum(int _v_); // 援助物品次数
	public void setHelpitemnum(int _v_); // 求助物品次数
}
