
package xbean;

public interface PetItem extends mkdb.Bean {
	public PetItem copy(); // deep clone
	public PetItem toData(); // a Data instance
	public PetItem toBean(); // a Bean instance
	public PetItem toDataIf(); // a Data instance If need. else return this
	public PetItem toBeanIf(); // a Bean instance If need. else return this

	public long getId(); // 主键id
	public int getFirstno(); // 一级菜单
	public int getTwono(); // 二级菜单
	public int getThreeno(); // 三级菜单
	public long getUniquid(); // 道具唯一id
	public int getKey(); // 背包中key
	public int getItemid(); // 道具id
	public long getExtid(); // 道具属性扩展id
	public String getName(); // 道具名称
	public com.locojoy.base.Octets getNameOctets(); // 道具名称
	public long getRoleid(); // 拥有者id
	public int getLevel(); // 道具等级
	public int getNumber(); // 道具数量
	public int getPrice(); // 道具价格
	public int getAttentionnumber(); // 关注数量
	public long getShowtime(); // 公示时间
	public long getExpiretime(); // 到期时间
	public int getAttack(); // 攻击
	public int getDefend(); // 防御
	public int getSpeed(); // 速度
	public int getMagicattack(); // 法术攻击
	public int getMagicdef(); // 法术防御
	public int getMaxhp(); // 最大生命
	public int getAttackapt(); // 攻击资质
	public int getDefendapt(); // 防御资质
	public int getPhyforceapt(); // 体力资质
	public int getMagicapt(); // 法力资质
	public int getSpeedapt(); // 速度资质
	public int getDodgeapt(); // 躲闪资质
	public float getGrowrate(); // 成长率
	public java.util.List<Integer> getSkills(); // 技能
	public java.util.List<Integer> getSkillsAsData(); // 技能
	public int getSkillnumber(); // 技能数量
	public int getPetscore(); // 宠物评分

	public void setId(long _v_); // 主键id
	public void setFirstno(int _v_); // 一级菜单
	public void setTwono(int _v_); // 二级菜单
	public void setThreeno(int _v_); // 三级菜单
	public void setUniquid(long _v_); // 道具唯一id
	public void setKey(int _v_); // 背包中key
	public void setItemid(int _v_); // 道具id
	public void setExtid(long _v_); // 道具属性扩展id
	public void setName(String _v_); // 道具名称
	public void setNameOctets(com.locojoy.base.Octets _v_); // 道具名称
	public void setRoleid(long _v_); // 拥有者id
	public void setLevel(int _v_); // 道具等级
	public void setNumber(int _v_); // 道具数量
	public void setPrice(int _v_); // 道具价格
	public void setAttentionnumber(int _v_); // 关注数量
	public void setShowtime(long _v_); // 公示时间
	public void setExpiretime(long _v_); // 到期时间
	public void setAttack(int _v_); // 攻击
	public void setDefend(int _v_); // 防御
	public void setSpeed(int _v_); // 速度
	public void setMagicattack(int _v_); // 法术攻击
	public void setMagicdef(int _v_); // 法术防御
	public void setMaxhp(int _v_); // 最大生命
	public void setAttackapt(int _v_); // 攻击资质
	public void setDefendapt(int _v_); // 防御资质
	public void setPhyforceapt(int _v_); // 体力资质
	public void setMagicapt(int _v_); // 法力资质
	public void setSpeedapt(int _v_); // 速度资质
	public void setDodgeapt(int _v_); // 躲闪资质
	public void setGrowrate(float _v_); // 成长率
	public void setSkillnumber(int _v_); // 技能数量
	public void setPetscore(int _v_); // 宠物评分
}
