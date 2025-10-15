
package xbean;

public interface HookData extends mkdb.Bean {
	public HookData copy(); // deep clone
	public HookData toData(); // a Data instance
	public HookData toBean(); // a Bean instance
	public HookData toDataIf(); // a Data instance If need. else return this
	public HookData toBeanIf(); // a Bean instance If need. else return this

	public short getCangetdpoint(); // 可领取双倍点数
	public short getGetdpoint(); // 已领取双倍点数
	public boolean getIsautobattle(); // 是否自动战斗
	public short getCharoptype(); // 人物操作类型
	public int getCharopid(); // 人物操作id
	public short getPetoptype(); // 宠物操作类型
	public int getPetopid(); // 宠物操作id
	public long getOfflineexp(); // 离线经验
	public long getLastgettime(); // 上次系统发放双倍点数时间

	public void setCangetdpoint(short _v_); // 可领取双倍点数
	public void setGetdpoint(short _v_); // 已领取双倍点数
	public void setIsautobattle(boolean _v_); // 是否自动战斗
	public void setCharoptype(short _v_); // 人物操作类型
	public void setCharopid(int _v_); // 人物操作id
	public void setPetoptype(short _v_); // 宠物操作类型
	public void setPetopid(int _v_); // 宠物操作id
	public void setOfflineexp(long _v_); // 离线经验
	public void setLastgettime(long _v_); // 上次系统发放双倍点数时间
}
