
package xbean;

public interface PetInfo extends mkdb.Bean {
	public PetInfo copy(); // deep clone
	public PetInfo toData(); // a Data instance
	public PetInfo toBean(); // a Bean instance
	public PetInfo toDataIf(); // a Data instance If need. else return this
	public PetInfo toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 宠物ID
	public int getKey(); // 在宠物栏、仓库、商会柜台里的key
	public long getUniqid(); // 宠物唯一ID
	public String getName(); // 名称
	public com.locojoy.base.Octets getNameOctets(); // 名称
	public int getLevel(); // 等级    	*short够了
	public xbean.BasicFightProperties getInitbfp(); // 初始的基础战斗属性
	public xbean.BasicFightProperties getBfp(); // 基础战斗属性
	public int getHp(); // 当前气血
	public int getMp(); // 当前法力
	public int getGrowrate(); // 成长率 		*short够了
	public int getLife(); // 寿命
	public long getExp(); // 当前经验
	public int getBornattackapt(); // 出生时攻击资质	*short够了
	public int getBorndefendapt(); // 出生时防御资质	*short够了
	public int getBornphyforceapt(); // 出生时体力资质	*short够了
	public int getBornmagicapt(); // 出生时法力资质	*short够了
	public int getBornspeedapt(); // 出生时速度资质	*short够了
	public int getBorndodgeapt(); // 出生时躲闪资质	*short够了
	public int getKind(); // 宠物类型 野生、宝宝	*byte够了
	public java.util.Map<Integer, Float> getEffects(); // key = effect type id
	public java.util.Map<Integer, Float> getEffectsAsData(); // key = effect type id
	public java.util.Map<Integer, Float> getFinalattrs(); // key = attr type
	public java.util.Map<Integer, Float> getFinalattrsAsData(); // key = attr type
	public xbean.BuffAgent getBuffpet(); // buff
	public boolean getIslocked(); // 是否锁定
	public boolean getIsbinded(); // 是否绑定
	public long getTimeout(); // 时间锁的到期时间，为0则只是普通锁
	public int getPetscore(); // 宠物评分
	public int getPetbasescore(); // 宠物一级属性评分
	public long getOwnerid(); // 宠物的主人
	public java.util.List<xbean.PetSkill> getSkills(); // 宠物拥有的战斗技能
	public java.util.List<xbean.PetSkill> getSkillsAsData(); // 宠物拥有的战斗技能
	public int getPoint(); // 潜能点
	public int getAutoaddcons(); // 加点方案-升级自动加体质
	public int getAutoaddiq(); // 加点方案-升级自动加智力
	public int getAutoaddstr(); // 加点方案-升级自动加力量
	public int getAutoaddendu(); // 加点方案-升级自动加耐力
	public int getAutoaddagi(); // 加点方案-升级自动加敏捷
	public int getPointresetcount(); // 宠物加点的重置次数 *short够了
	public int getAptaddcount(); // 资质培养次数 *byte够了
	public int getGrowrateaddcount(); // 成长率培养次数 *byte够了
	public int getWashcount(); // 洗练次数 *short够了
	public int getTreasure(); // 0表示不是珍品;1表示是珍品
	public int getPetdye1(); // 宠物染色信息部位1
	public int getPetdye2(); // 宠物染色信息部位2
	public int getShenshouinccount(); // 神兽养成次数
	public long getMarketfreezeexpire(); // 摆摊冻结截止时间,默认0不冻结

	public void setId(int _v_); // 宠物ID
	public void setKey(int _v_); // 在宠物栏、仓库、商会柜台里的key
	public void setUniqid(long _v_); // 宠物唯一ID
	public void setName(String _v_); // 名称
	public void setNameOctets(com.locojoy.base.Octets _v_); // 名称
	public void setLevel(int _v_); // 等级    	*short够了
	public void setHp(int _v_); // 当前气血
	public void setMp(int _v_); // 当前法力
	public void setGrowrate(int _v_); // 成长率 		*short够了
	public void setLife(int _v_); // 寿命
	public void setExp(long _v_); // 当前经验
	public void setBornattackapt(int _v_); // 出生时攻击资质	*short够了
	public void setBorndefendapt(int _v_); // 出生时防御资质	*short够了
	public void setBornphyforceapt(int _v_); // 出生时体力资质	*short够了
	public void setBornmagicapt(int _v_); // 出生时法力资质	*short够了
	public void setBornspeedapt(int _v_); // 出生时速度资质	*short够了
	public void setBorndodgeapt(int _v_); // 出生时躲闪资质	*short够了
	public void setKind(int _v_); // 宠物类型 野生、宝宝	*byte够了
	public void setIslocked(boolean _v_); // 是否锁定
	public void setIsbinded(boolean _v_); // 是否绑定
	public void setTimeout(long _v_); // 时间锁的到期时间，为0则只是普通锁
	public void setPetscore(int _v_); // 宠物评分
	public void setPetbasescore(int _v_); // 宠物一级属性评分
	public void setOwnerid(long _v_); // 宠物的主人
	public void setPoint(int _v_); // 潜能点
	public void setAutoaddcons(int _v_); // 加点方案-升级自动加体质
	public void setAutoaddiq(int _v_); // 加点方案-升级自动加智力
	public void setAutoaddstr(int _v_); // 加点方案-升级自动加力量
	public void setAutoaddendu(int _v_); // 加点方案-升级自动加耐力
	public void setAutoaddagi(int _v_); // 加点方案-升级自动加敏捷
	public void setPointresetcount(int _v_); // 宠物加点的重置次数 *short够了
	public void setAptaddcount(int _v_); // 资质培养次数 *byte够了
	public void setGrowrateaddcount(int _v_); // 成长率培养次数 *byte够了
	public void setWashcount(int _v_); // 洗练次数 *short够了
	public void setTreasure(int _v_); // 0表示不是珍品;1表示是珍品
	public void setPetdye1(int _v_); // 宠物染色信息部位1
	public void setPetdye2(int _v_); // 宠物染色信息部位2
	public void setShenshouinccount(int _v_); // 神兽养成次数
	public void setMarketfreezeexpire(long _v_); // 摆摊冻结截止时间,默认0不冻结
}
