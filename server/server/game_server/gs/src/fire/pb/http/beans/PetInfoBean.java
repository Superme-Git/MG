package fire.pb.http.beans;

import java.util.ArrayList;
import java.util.List;

public class PetInfoBean {
	private int key;//
	private long uniqId;//宠物唯一ID
	private int ownerId;
	private int id;//宠物ID
	private int fightflag;//是否参战
	private String name;//宠物名称
	private int lv;//宠物等级
	private int color;//宠物品级
	private int growRate;//宠物成长
	private int attackApt;//攻击资质
	private int defendApt;//防御资质
	private int phyforceApt;//体力资质
	private int magicApt;//法术资质
	private int speedApt;//速度资质
	private int point;//剩余潜力点
	
	private int cons;//体质
	private int iq;//智力
	private int str;//力量
	private int endu;//耐力
	private int agi;//敏捷

	private int maxhp;//生命
	private int maxmp;//魔法
	private long exp;//经验
	private int atk;//物理攻击
	private int matk;//法术攻击
	private int def;//物理防御
	private int mdef;//法术防御
	private int speed;//速度
	
	private List<Integer> skillid=new ArrayList<Integer>();//技能id
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public long getUniqId() {
		return uniqId;
	}
	public void setUniqId(long uniqId) {
		this.uniqId = uniqId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFightflag() {
		return fightflag;
	}
	public void setFightflag(int fightflag) {
		this.fightflag = fightflag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getGrowRate() {
		return growRate;
	}
	public void setGrowRate(int growRate) {
		this.growRate = growRate;
	}
	public int getAttackApt() {
		return attackApt;
	}
	public void setAttackApt(int attackApt) {
		this.attackApt = attackApt;
	}
	public int getDefendApt() {
		return defendApt;
	}
	public void setDefendApt(int defendApt) {
		this.defendApt = defendApt;
	}
	public int getPhyforceApt() {
		return phyforceApt;
	}
	public void setPhyforceApt(int phyforceApt) {
		this.phyforceApt = phyforceApt;
	}
	public int getMagicApt() {
		return magicApt;
	}
	public void setMagicApt(int magicApt) {
		this.magicApt = magicApt;
	}
	public int getSpeedApt() {
		return speedApt;
	}
	public void setSpeedApt(int speedApt) {
		this.speedApt = speedApt;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getCons() {
		return cons;
	}
	public void setCons(int cons) {
		this.cons = cons;
	}
	public int getIq() {
		return iq;
	}
	public void setIq(int iq) {
		this.iq = iq;
	}
	public int getStr() {
		return str;
	}
	public void setStr(int str) {
		this.str = str;
	}
	public int getEndu() {
		return endu;
	}
	public void setEndu(int endu) {
		this.endu = endu;
	}
	public int getAgi() {
		return agi;
	}
	public void setAgi(int agi) {
		this.agi = agi;
	}
	public int getMaxhp() {
		return maxhp;
	}
	public void setMaxhp(int maxhp) {
		this.maxhp = maxhp;
	}
	public int getMaxmp() {
		return maxmp;
	}
	public void setMaxmp(int maxmp) {
		this.maxmp = maxmp;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getMatk() {
		return matk;
	}
	public void setMatk(int matk) {
		this.matk = matk;
	}
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public int getMdef() {
		return mdef;
	}
	public void setMdef(int mdef) {
		this.mdef = mdef;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public List<Integer> getSkillid() {
		return skillid;
	}
	public void setSkillid(List<Integer> skillid) {
		this.skillid = skillid;
	}
	
	

}
