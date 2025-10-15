package fire.pb.shop.srv.market.pojo;

import java.util.HashMap;
import java.util.Map;

public class EquipDaoBean extends DaoBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6516684772077677765L;
	
	private int effect; // 特效
	private int skill; // 技能
	private int color; // 品质
	
	private Map<Integer, Integer> attr = new HashMap<>(); // 基础属性
	private Map<Integer, Integer> addattr = new HashMap<>(); // 附加属性
	
	private int totalattr;// 附加属性总和
	private int equipscore; // 评分
	

	public Map<Integer, Integer> getAttr() {
		return attr;
	}

	public void setAttr(Map<Integer, Integer> attr) {
		this.attr = attr;
	}

	public Map<Integer, Integer> getAddattr() {
		return addattr;
	}

	public void setAddattr(Map<Integer, Integer> addattr) {
		this.addattr = addattr;
	}
	
	public int getEquipscore() {
		return equipscore;
	}

	public void setEquipscore(int equipscore) {
		this.equipscore = equipscore;
	}
	
	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getTotalattr() {
		return totalattr;
	}

	public void setTotalattr(int totalattr) {
		this.totalattr = totalattr;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EquipDaoBean [effect=");
		builder.append(effect);
		builder.append(", skill=");
		builder.append(skill);
		builder.append(", color=");
		builder.append(color);
		builder.append(", attr=");
		builder.append(attr);
		builder.append(", addattr=");
		builder.append(addattr);
		builder.append(", totalattr=");
		builder.append(totalattr);
		builder.append(", equipscore=");
		builder.append(equipscore);
		builder.append("]");
		return builder.toString();
	}

}
