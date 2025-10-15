
package xbean;

public interface MenstorAndApprent extends mkdb.Bean {
	public MenstorAndApprent copy(); // deep clone
	public MenstorAndApprent toData(); // a Data instance
	public MenstorAndApprent toBean(); // a Bean instance
	public MenstorAndApprent toDataIf(); // a Data instance If need. else return this
	public MenstorAndApprent toBeanIf(); // a Bean instance If need. else return this

	public int getFlag(); // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
	public int getMenstortitleid(); // 为师时的titleId 表apprents 为自己的徒弟信息表
	public long getFirstbaishitime(); // 第一个出师徒弟的拜师时间
	public long getFirstapprentid(); // 第一个出师徒弟的id
	public long getShidezhi(); // 师傅的师德值
	public long getMenstorid(); // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
	public long getBashitime(); // 罢师时间
	public int getBashiflag(); // 1=师傅7天没登录罢师 2=强行罢师
	public int getEqipmentscore(); // 拜师时候的装备评分
	public java.util.Map<Integer, Integer> getAchievement(); // 每达成一项就要记录一项
	public java.util.Map<Integer, Integer> getAchievementAsData(); // 每达成一项就要记录一项
	public long getBaishitime(); // 拜师时间
	public long getLastgivefushitime(); // 上次获得符石的时间
	public long getFushi(); // 获得符石数量

	public void setFlag(int _v_); // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
	public void setMenstortitleid(int _v_); // 为师时的titleId 表apprents 为自己的徒弟信息表
	public void setFirstbaishitime(long _v_); // 第一个出师徒弟的拜师时间
	public void setFirstapprentid(long _v_); // 第一个出师徒弟的id
	public void setShidezhi(long _v_); // 师傅的师德值
	public void setMenstorid(long _v_); // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
	public void setBashitime(long _v_); // 罢师时间
	public void setBashiflag(int _v_); // 1=师傅7天没登录罢师 2=强行罢师
	public void setEqipmentscore(int _v_); // 拜师时候的装备评分
	public void setBaishitime(long _v_); // 拜师时间
	public void setLastgivefushitime(long _v_); // 上次获得符石的时间
	public void setFushi(long _v_); // 获得符石数量
}
