package fire.pb.effect;

import java.util.Map;

public interface Role {

	
	
	//
	public int addHp(int v);
	public int addMp(int v);
	public int addSp(int v, int maxsp);//添加怒气（宠怪没有）
	public Map<Integer,Float> addWound(int v);//添加伤（宠怪没有）
//	public void addEnergy(int v); //活力（宠物没有）
//	public void addPhforce(int v); //体力（宠物没有）
	
	
	public int setHp(int hp);//直接设置Hp，不超过当前血上限
	public int setMp(int mp);//直接设置Mp，不超过最大mp上限
	public int setSp(int sp);
	
	public int getLevel();
	
	public int getScore();

	public Map<Integer,Float> fullHp();
	public Map<Integer,Float> fullMp();
	public void fullEnergy();//（宠物没有）
	public void fullPhyforce();//（宠物没有）
	
	
	public Map<Integer,Float> fullHpAndRecoverWound();
	
	//
	public int getHp();
	public int getMp();
	public int getSp();
	public float getHpPercent();//获取当前hp占maxhp的百分比
	public float getMpPercent();//获取当前mp占maxmp的百分比
	public int getWound();
//	public int getEnergy();	// 活力（宠物没有）
//	public int getPhforce(); //体力（宠物没有）
	
	//
	int getMaxHp();
	int getMaxMp();	
	int getUplimithp();
	int getMaxSp();
	public int getCons();//体质
	public int getIq();//智力
	public int getStr();//力量
	public int getEndu();//耐力
	public int getAgi();//敏捷

	public int getHit(); // 命中
	public int getAttack(); // 物理攻击力
	public int getDefend(); // 防御
	public int getSpeed(); // 速度
	public int getDodge(); // 躲避
	
	//public int getSpirit(); // 灵力
//	public int getEnlimit(); //活力上限(宠物没有)
//	public int getPflimit(); //体力上限（宠物没有）
	public int getMagicAttack();
	public int getMagicDef();
	
	public float getAttrById(int attrId);
	public float getAttrByName(String attrname);
	
	public int getAttrLevelStandardValue(int attrId);

	//	
//	public long getSMoney(); // 储备金
//	public void addSmoney(long v); // 储备金

	
	//public xbean.BasicFightProperties getBasicFightProperties();
	long getNextExp();
	
	
	public Map<Integer,Float> addPoints(int cons, int str, int agi , int endu , int iq);

	
	public Map<Integer, Float> resetPoints();

	
	public Map<Integer,Float> changeScheme(int schemeid);
	
	
	public Map<Integer, Float> levelUp();
	
	
	public void attachEffect(int effectType, float value);
	
	public void detachEffect(int effectType, float value);
	
	public void attachEffects(Map<Integer,Float> effects);
	
	public void detachEffects(Map<Integer,Float> effects);
	
	public Map<Integer,Float> updateAllFinalAttrs();
	
	
	public Map<Integer, Float> recovery();
	
	
	public Map<Integer, Float> recoverToMaxHpMpPct(double hppct, double mppct, boolean ulhp2hp);
	
	
	public void clear();
	
	
	public int getBodySize();
	
	public Map<Integer,Float> correctEffects(Map<Integer,Float> buffeffects);
	
	
	public int updateScore();
	
}
