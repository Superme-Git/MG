package fire.pb.http.beans;

import java.util.ArrayList;
import java.util.List;

public class BagItem {
	private int key;//装备key
	private String itemname;//装备名称
	private int num;//物品数量
	private int equiptype;//  装备类型	
	private int color;//	装备品级
	private int equiplevel;//	装备等级	
	private List<AttrBean> baseattr=new ArrayList<AttrBean>();//	基础属性
	private List<Integer> diamonds=new ArrayList<Integer>();//	宝石属性	
	private List<AttrBean> appendattr=new ArrayList<AttrBean>();//	附加属性
	private int effect;//	装备特效	
	private int endure;//	装备耐久
	private int equipscore;//	装备评分	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public int getEquiptype() {
		return equiptype;
	}
	public void setEquiptype(int equiptype) {
		this.equiptype = equiptype;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getEquiplevel() {
		return equiplevel;
	}
	public void setEquiplevel(int equiplevel) {
		this.equiplevel = equiplevel;
	}
	
	public List<AttrBean> getBaseattr() {
		return baseattr;
	}
	public void setBaseattr(List<AttrBean> baseattr) {
		this.baseattr = baseattr;
	}
	public List<Integer> getDiamonds() {
		return diamonds;
	}
	public void setDiamonds(List<Integer> diamonds) {
		this.diamonds = diamonds;
	}
	public List<AttrBean> getAppendattr() {
		return appendattr;
	}
	public void setAppendattr(List<AttrBean> appendattr) {
		this.appendattr = appendattr;
	}
	public int getEffect() {
		return effect;
	}
	public void setEffect(int effect) {
		this.effect = effect;
	}
	public int getEndure() {
		return endure;
	}
	public void setEndure(int endure) {
		this.endure = endure;
	}
	public int getEquipscore() {
		return equipscore;
	}
	public void setEquipscore(int equipscore) {
		this.equipscore = equipscore;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
	
	
	
	
//	public int getEquiplevel(); // 装备等级(目的就是用于离线快照需要过滤级别)
//	public int getForginglevel(); // 锻炼等级
//	public int getBlesslevel(); // 祝福等级
//	public int getRepairtimes(); // 修理失败次数
//	public int getEndure(); // 耐久
//	public int getCurmaxendure(); // 当前最大耐久
//	public String getProducer(); // 制造者
//	public com.locojoy.base.Octets getProducerOctets(); // 制造者
//	public java.util.Map<Integer, Integer> getGrowattr(); // 装备增强属性
//	public java.util.Map<Integer, Integer> getGrowattrAsData(); // 装备增强属性
//	public int getExtendure(); // 当值为100时,减少1
//	public int getMaxendure(); // 
//	public int getSkill(); // 技能
//	public int getEffect(); // 特效
//	public int getSpecialeffect(); // 特殊效果
//	public int getPrefixtype(); // 前缀类型
//	public int getIsidentified(); // 是否鉴定过
//	public int getTreasure(); // 0表示不是珍品;1表示是珍品
//	public java.util.Map<Integer, Integer> getAttr(); // 基础属性
//	public java.util.Map<Integer, Integer> getAttrAsData(); // 基础属性
//	public java.util.Map<Integer, Integer> getAddattr(); // 附加属性
//	public java.util.Map<Integer, Integer> getAddattrAsData(); // 附加属性
//	public java.util.List<xbean.AppendAttr> getAppendattr(); // 附加属性 TODO 准备去掉
//	public java.util.List<xbean.AppendAttr> getAppendattrAsData(); // 附加属性 TODO 准备去掉
//	public java.util.Map<Integer, xbean.EnhancementData> getEnhancement(); // 附魔属性 by changhao
//	public java.util.Map<Integer, xbean.EnhancementData> getEnhancementAsData(); // 附魔属性 by changhao
//	public int getEquipscore(); // 装备评分
//	public java.util.List<Integer> getDiamonds(); // 钻石的列表,key为第几个洞,value为钻石的id
//	public java.util.List<Integer> getDiamondsAsData(); // 钻石的列表,key为第几个洞,value为钻石的id
//	public int getRefinelevel(); // 淬炼等级
//	public int getRefineprogress(); // 冲星进度
//	public int getGenway(); // 产出途径
	




}
