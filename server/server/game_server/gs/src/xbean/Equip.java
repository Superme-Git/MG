
package xbean;

public interface Equip extends mkdb.Bean {
	public Equip copy(); // deep clone
	public Equip toData(); // a Data instance
	public Equip toBean(); // a Bean instance
	public Equip toDataIf(); // a Data instance If need. else return this
	public Equip toBeanIf(); // a Bean instance If need. else return this

	public int getEquiplevel(); // 装备等级
	public int getRepairtimes(); // 修理失败次数
	public int getEndure(); // 耐久
	public int getCurmaxendure(); // 当前最大耐久
	public String getProducer(); // 制造者
	public com.locojoy.base.Octets getProducerOctets(); // 制造者
	public int getExtendure(); // 当值为100时,减少1
	public int getMaxendure(); // 
	public int getSkill(); // 技能
	public int getEffect(); // 特效
	public int getTreasure(); // 0表示不是珍品;1表示是珍品
	public java.util.Map<Integer, Integer> getAttr(); // 基础属性
	public java.util.Map<Integer, Integer> getAttrAsData(); // 基础属性
	public java.util.Map<Integer, Integer> getAddattr(); // 附加属性
	public java.util.Map<Integer, Integer> getAddattrAsData(); // 附加属性
	public java.util.Map<Integer, xbean.EnhancementData> getEnhancement(); // 附魔属性 by changhao
	public java.util.Map<Integer, xbean.EnhancementData> getEnhancementAsData(); // 附魔属性 by changhao
	public int getEquipscore(); // 装备评分
	public java.util.List<Integer> getDiamonds(); // 宝石列表
	public java.util.List<Integer> getDiamondsAsData(); // 宝石列表

	public void setEquiplevel(int _v_); // 装备等级
	public void setRepairtimes(int _v_); // 修理失败次数
	public void setEndure(int _v_); // 耐久
	public void setCurmaxendure(int _v_); // 当前最大耐久
	public void setProducer(String _v_); // 制造者
	public void setProducerOctets(com.locojoy.base.Octets _v_); // 制造者
	public void setExtendure(int _v_); // 当值为100时,减少1
	public void setMaxendure(int _v_); // 
	public void setSkill(int _v_); // 技能
	public void setEffect(int _v_); // 特效
	public void setTreasure(int _v_); // 0表示不是珍品;1表示是珍品
	public void setEquipscore(int _v_); // 装备评分
}
