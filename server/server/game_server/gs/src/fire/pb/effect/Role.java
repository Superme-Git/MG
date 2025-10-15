package fire.pb.effect;

import java.util.Map;

public interface Role {
	public int addHp(int v);
	public int addMp(int v);
	public int addSp(int v, int maxsp);
	public Map<Integer,Float> addWound(int v);
	
	public int setHp(int hp);
	public int setMp(int mp);
	public int setSp(int sp);
	
	public int getLevel();
	
	public int getScore();

	public Map<Integer,Float> fullHp();
	public Map<Integer,Float> fullMp();
	public void fullEnergy();
	public void fullPhyforce();
	
	public Map<Integer,Float> fullHpAndRecoverWound();
	
	public int getHp();
	public int getMp();
	public int getSp();
	public float getHpPercent();
	public float getMpPercent();
	public int getWound();
	
	int getMaxHp();
	int getMaxMp();	
	int getUplimithp();
	int getMaxSp();
	public int getCons();
	public int getIq();
	public int getStr();
	public int getEndu();
	public int getAgi();

	public int getHit();
	public int getAttack();
	public int getDefend();
	public int getSpeed();
	public int getDodge();
	
	public int getMagicAttack();
	public int getMagicDef();
	public float getAttrById(int attrId);
	public float getAttrByName(String attrname);
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
