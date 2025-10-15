
package xbean;

public interface EquipItem extends mkdb.Bean {
	public EquipItem copy(); // deep clone
	public EquipItem toData(); // a Data instance
	public EquipItem toBean(); // a Bean instance
	public EquipItem toDataIf(); // a Data instance If need. else return this
	public EquipItem toBeanIf(); // a Bean instance If need. else return this

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
	public int getEffect(); // 特效
	public int getSkill(); // 特技
	public int getColor(); // 品质
	public java.util.Map<Integer, Integer> getAttr(); // 基础属性
	public java.util.Map<Integer, Integer> getAttrAsData(); // 基础属性
	public java.util.Map<Integer, Integer> getAddattr(); // 附加属性
	public java.util.Map<Integer, Integer> getAddattrAsData(); // 附加属性
	public int getTotalattr(); // 属性总和
	public int getEquipscore(); // 装备评分

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
	public void setEffect(int _v_); // 特效
	public void setSkill(int _v_); // 特技
	public void setColor(int _v_); // 品质
	public void setTotalattr(int _v_); // 属性总和
	public void setEquipscore(int _v_); // 装备评分
}
