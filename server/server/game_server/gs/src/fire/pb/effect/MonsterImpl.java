package fire.pb.effect;

import java.util.HashMap;
import java.util.Map;

import fire.pb.attr.AttrType;
import fire.pb.battle.Monster;
import fire.pb.npc.SWildMonsterConfig;
import xbean.BasicFightProperties;

public class MonsterImpl extends AttrCalcRole
{
	

	final private xbean.Monster monsterBean;
	final private SWildMonsterConfig monsterConfig;

	public MonsterImpl(xbean.Monster monsterBean)
	{
		if(monsterBean == null)
			throw new IllegalArgumentException("ERROR: 构造EMonster时，xbean.Monster == null");
		this.monsterBean = monsterBean;
		if(Monster.isNpcMonster(monsterBean.getMonsterid()))
			monsterConfig = new SWildMonsterConfig();//设置一个假的config，里面的属性都是0
		else
			monsterConfig = Monster.getSWildMonsterConfig(monsterBean.getMonsterid());
	}
	
	@Override
	public Map<Integer,Float> addWound(int v)
	{
		return new HashMap<Integer, Float>();
	}

	@Override
	public Map<Integer,Float> fullHp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int maxhp = this.getMaxHp();
		monsterBean.setHp(maxhp);
		changedAttrs.put(AttrType.HP, (float)maxhp);
		return changedAttrs;
		
	}
	@Override
	public Map<Integer,Float> fullMp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int maxmp = this.getMaxMp();
		monsterBean.setMp(maxmp);
		changedAttrs.put(AttrType.MP, (float)maxmp);
		return changedAttrs;
	}
	
	@Override
	public Map<Integer, Float> fullHpAndRecoverWound()
	{
		Map<Integer, Float> changedAttrs = new HashMap<Integer, Float>();
		int maxhp = this.getMaxHp();
		if (monsterBean.getHp() == this.getMaxHp())
		{
			monsterBean.setHp(maxhp);
			changedAttrs.put(AttrType.HP, (float) maxhp);
		}
		return changedAttrs;
	}

	@Override
	public void fullEnergy()
	{
	}

	@Override
	public void fullPhyforce()
	{
	}

	// ********************************GET*****************************************//
	// ********************************GET*****************************************//
	// ********************************GET*****************************************//

	@Override
	public int getLevel()
	{
		return monsterBean.getLevel();
	}
	
	@Override
	public int getHp()
	{
		return monsterBean.getHp();
	}

	@Override
	public int getMp()
	{
		return monsterBean.getMp();
	}
	@Override
	public int getWound()
	{
		return 0;
	}

	public int getEnlimit()
	{
		return 50 + monsterBean.getLevel() * 5;
	}

	@Override
	public long getNextExp()
	{
		return 0L;
	}
	

	@Override
	public Map<Integer, Float> addPoints(int cons, int str, int agi, int endu, int iq)
	{
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		return changedAttrs;
	}
	
	@Override
	public Map<Integer, Float> resetPoints()
	{
		return null;
	}
	
	@Override
	public HashMap<Integer, Float> changeScheme(int schemeid)
	{
		return null;
	}

	// ********************************计算*****************************************//
	// ********************************计算*****************************************//
	// ********************************计算*****************************************//

	protected float calcL2Attr(int attrType)
	{
		fire.pb.pet.PetAttrModData attrmod = PetImpl.getAttrModData(attrType);
		float v = (float)(getCons()*attrmod.getConsfactor() + getIq()*attrmod.getIqfactor() + getStr()*attrmod.getStrfactor() + getAgi()*attrmod.getAgifactor() + getEndu()*attrmod.getEndufactor());
		return v;
	}

	protected float calcMaxHp()
	{
//		float maxhp = (float) 2*(calcCons() * monsterConfig.getGrowrate() * 6 + ((float)monsterConfig.getPhyforceapt() *10)/3 *getLevel())/1000;
		float maxhp = (float)(calcL2Attr(AttrType.MAX_HP)*monsterConfig.getGrowrate()/1000f + monsterConfig.getPhyforceapt()/200f*getLevel());
		maxhp = calcEffectBonus(AttrType.MAX_HP, maxhp);

		return maxhp;
	}

	protected float calcMaxMp()
	{
//		float maxmp = (float) 2*((calcIq() * monsterConfig.getGrowrate()*3+ monsterConfig.getMagicapt()*2*getLevel()*2)/1000);
		float maxmp = (float)(calcL2Attr(AttrType.MAX_MP)*monsterConfig.getGrowrate()/1000f + monsterConfig.getMagicapt()/200f*getLevel());
		maxmp = calcEffectBonus(AttrType.MAX_MP, maxmp);

		return maxmp;
	}
	
	
	protected float calcHit()
	{
		//float hitrate = (calcStr() * monsterConfig.getAttackapt() /1000)*2;//命中改为与攻击力同样公式
		float hitrate = (float) (((monsterBean.getLevel() * 12 + calcStr() * 10)* monsterConfig.getGrowrate()
				+ (4 * monsterConfig.getAttackapt() - 2200)* monsterBean.getLevel()*10)/10000)*2;
		return calcEffectBonus(AttrType.HIT_RATE, hitrate);
	}

	
	protected float calcAttack()
	{
//		float attack = (float) (((monsterBean.getLevel() * 12 + calcStr() * 10)* monsterConfig.getGrowrate()
//				+ (4 * monsterConfig.getAttackapt() - 2200)* monsterBean.getLevel()*10)/10000)*2;
		float attack = (float)(calcL2Attr(AttrType.ATTACK)*monsterConfig.getGrowrate()/1000f + monsterConfig.getAttackapt()/200f*getLevel());
		return calcEffectBonus(AttrType.ATTACK, attack);
	}
	
	protected float calcDefend()
	{
//		long sum = (long)monsterBean.getLevel() * (long)monsterConfig.getDefendapt() * (28200 + 19 * (long)monsterConfig.getGrowrate())+ (long)monsterConfig.getGrowrate() * (long)calcEndu() * 4 * 7500;
//		float defend = (float)(sum/(7500 * 3000)) *2;
		float defend = (float)(calcL2Attr(AttrType.DEFEND)*monsterConfig.getGrowrate()/1000f + monsterConfig.getDefendapt()/200f*getLevel());
		return calcEffectBonus(AttrType.DEFEND, defend);
	}
	
	protected float calcSpeed()
	{
//		float speed = (float) calcAgi() * monsterConfig.getSpeedapt()/1000;
		float speed = (float)(calcL2Attr(AttrType.SPEED)*monsterConfig.getGrowrate()/1000f + monsterConfig.getSpeedapt()/300f*getLevel());
		return calcEffectBonus(AttrType.SPEED, speed);
	}
	
	protected float calcDodge()
	{
		float dodge =  (calcAgi() * monsterConfig.getDodgeapt() /1000)*2;
		return calcEffectBonus(AttrType.DODGE_RATE, dodge);
	}
	
	protected float calcMagicAttack()
	{
//		float magicattack = (((monsterConfig.getMagicapt()*2 + monsterConfig.getGrowrate() * 3) * getLevel()+(calcIq() * 7 + calcStr()
//				* 4 + calcCons() * 3 + calcEndu() * 2) * 500) / 5000) * 2;
		float magicattack = (float)(calcL2Attr(AttrType.MAGIC_ATTACK)*monsterConfig.getGrowrate()/1000f + monsterConfig.getMagicapt()/600f*getLevel());
		return calcEffectBonus(AttrType.MAGIC_ATTACK, magicattack);
	}
	
	protected float calcMagicDef()
	{
//		float magicdef = (((monsterConfig.getMagicapt()*2 + monsterConfig.getGrowrate() * 3) * getLevel() +(calcIq() * 7 + calcStr()
//				* 4 + calcCons() * 3 + calcEndu() * 2) * 500) / 5000) * 2;
		float magicdef = (float)(calcL2Attr(AttrType.MAGIC_DEF)*monsterConfig.getGrowrate()/1000f + monsterConfig.getMagicapt()/900f*getLevel());
		return calcEffectBonus(AttrType.MAGIC_DEF, magicdef);
	}
	

	@Override
	public HashMap<Integer, Float> levelUp()
	{
		return null;
	}

	@Override
	public Map<Integer, Float> recovery() {
		return new HashMap<Integer, Float>();
	}
	
	
	@Override
	public Map<Integer, Float> recoverToMaxHpMpPct(double hppct, double mppct, boolean ulhp2hp)
	{
		Map<Integer, Float> changedAttrs = new HashMap<Integer, Float>();
		int dsthp = (int)(getMaxHp() * hppct);
		setHp(dsthp);
		changedAttrs.put(AttrType.HP, (float)dsthp);
		int dstmp = (int)(getMaxMp() * mppct);
		setMp(dstmp);
		changedAttrs.put(AttrType.MP, (float)dstmp);
		return changedAttrs;
	}
	
	
	
	public void clear()
	{
		monsterBean.getEffects().clear();
		monsterBean.getFinalattrs().clear();
	}

	@Override
	public int setHp(int hp)
	{
		int oldvalue = this.getHp();
		monsterBean.setHp(Math.max(0, (int) Math.min((long) this.getUplimithp(), hp)));
		return monsterBean.getHp() - oldvalue;
	}

	@Override
	public int setMp(int mp)
	{
		int oldvalue = this.getMp();
		monsterBean.setMp(Math.max(0, (int) Math.min((long) this.getMaxMp(), mp)));
		return monsterBean.getMp() - oldvalue;
	}
	
	@Override
	public int setSp(int sp) {		
		int oldvalue = this.getSp();
		monsterBean.setSp(Math.max(0, (int) Math.min((long) this.getMaxSp(), sp)));
		return monsterBean.getSp() - oldvalue;
	}
	
	@Override
	public int addSp(int v, int maxsp)
	{
		return 0;
	}

	@Override
	public int getSp()
	{
		return monsterBean.getSp();
	}

	@Override
	public int getBodySize()
	{
		return (int)monsterBean.getBodysize();
	}

	@Override
	public Map<Integer, Float> correctEffects(Map<Integer, Float> buffeffects)
	{
		return null;
	}

	@Override
	protected Map<Integer, Float> getFinalAttrs() {
		return monsterBean.getFinalattrs();
	}

	@Override
	protected Map<Integer, Float> getEffects() {
		return monsterBean.getEffects();
	}

	@Override
	protected BasicFightProperties getBFP() {
		return monsterBean.getBfp();
	}	

	@Override
	protected float calcPflimit() {
		return 0;
	}

	@Override
	protected float calcEnlimit() {
		return 0;
	}

	@Override
	protected float calcMedical() {
		float value = 0.0f;
		if (Monster.isNpcMonster(monsterBean.getMonsterid())) {
			value = calcEffectBonus(AttrType.MEDICAL, 0.0f);
		} else {
			value = getLevel() * monsterConfig.getHealgrow();
		}
		return value;
	}

	@Override
	protected float calcSeal() {
		float value = 0.0f;
		if (Monster.isNpcMonster(monsterBean.getMonsterid())) {
			value = calcEffectBonus(AttrType.SEAL, 0.0f);
		} else {
			value = getLevel() * monsterConfig.getCtrlhitgrow();
		}
		return value;
	}

	@Override
	protected float calcUnseal() {
		float value = 0.0f;
		if (Monster.isNpcMonster(monsterBean.getMonsterid())) {
			value = calcEffectBonus(AttrType.UNSEAL, 0.0f);
		} else {
			value = getLevel() * monsterConfig.getCtrlresistgrow();
		}
		return value;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}	
}
