package fire.pb.shop.srv.market.pojo;

import java.util.ArrayList;
import java.util.List;

public class PetDaoBean extends DaoBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3283202437650712801L;

	private int attack; // 攻击
	private int magicattack; // 法术攻击
	private int maxhp; // 最大生命
	private int defend; // 防御
	private int magicdef; // 法术防御
	private int speed; // 速度
	
	private int attackapt; // 攻击资质
	private int defendapt; // 防御资质
	private int phyforceapt; // 体力资质
	private int magicapt; // 法力资质
	private int speedapt; // 速度资质
	private float growrate; // 成长率
	
	private int dodgeapt; // 躲闪资质
	
	private List<Integer> skills = new ArrayList<>(5); // 宠物技能
	private int skillNumber; // 技能数量
	private int petscore; // 宠物评分
	

	public int getDefend() {
		return defend;
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}

	public int getMagicdef() {
		return magicdef;
	}

	public void setMagicdef(int magicdef) {
		this.magicdef = magicdef;
	}

	public int getMagicapt() {
		return magicapt;
	}

	public void setMagicapt(int magicapt) {
		this.magicapt = magicapt;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getMagicattack() {
		return magicattack;
	}

	public void setMagicattack(int magicattack) {
		this.magicattack = magicattack;
	}

	public int getAttackapt() {
		return attackapt;
	}

	public void setAttackapt(int attackapt) {
		this.attackapt = attackapt;
	}
	
	public List<Integer> getSkills() {
		return skills;
	}

	public void setSkills(List<Integer> skills) {
		this.skills = skills;
	}

	public int getPetscore() {
		return petscore;
	}

	public void setPetscore(int petscore) {
		this.petscore = petscore;
	}
	
	public int getDefendapt() {
		return defendapt;
	}

	public void setDefendapt(int defendapt) {
		this.defendapt = defendapt;
	}
	
	public int getPhyforceapt() {
		return phyforceapt;
	}

	public void setPhyforceapt(int phyforceapt) {
		this.phyforceapt = phyforceapt;
	}
	
	public int getSpeedapt() {
		return speedapt;
	}

	public void setSpeedapt(int speedapt) {
		this.speedapt = speedapt;
	}
	
	public int getDodgeapt() {
		return dodgeapt;
	}

	public void setDodgeapt(int dodgeapt) {
		this.dodgeapt = dodgeapt;
	}

	public float getGrowrate() {
		return growrate;
	}

	public void setGrowrate(float growrate) {
		this.growrate = growrate;
	}

	public int getMaxhp() {
		return maxhp;
	}

	public void setMaxhp(int maxhp) {
		this.maxhp = maxhp;
	}
	
	public int getSkillNumber() {
		return skillNumber;
	}

	public void setSkillNumber(int skillNumber) {
		this.skillNumber = skillNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PetDaoBean [attack=");
		builder.append(attack);
		builder.append(", magicattack=");
		builder.append(magicattack);
		builder.append(", maxhp=");
		builder.append(maxhp);
		builder.append(", defend=");
		builder.append(defend);
		builder.append(", magicdef=");
		builder.append(magicdef);
		builder.append(", speed=");
		builder.append(speed);
		builder.append(", attackapt=");
		builder.append(attackapt);
		builder.append(", defendapt=");
		builder.append(defendapt);
		builder.append(", phyforceapt=");
		builder.append(phyforceapt);
		builder.append(", magicapt=");
		builder.append(magicapt);
		builder.append(", speedapt=");
		builder.append(speedapt);
		builder.append(", growrate=");
		builder.append(growrate);
		builder.append(", dodgeapt=");
		builder.append(dodgeapt);
		builder.append(", skills=");
		builder.append(skills);
		builder.append(", skillNumber=");
		builder.append(skillNumber);
		builder.append(", petscore=");
		builder.append(petscore);
		builder.append("]");
		return builder.toString();
	}

}
