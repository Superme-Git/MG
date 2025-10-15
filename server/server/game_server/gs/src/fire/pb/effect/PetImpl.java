package fire.pb.effect;

import java.util.HashMap;
import java.util.Map;

import xbean.BasicFightProperties;
import fire.pb.DataInit;
import fire.pb.attr.AttrType;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttrModData;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;

public class PetImpl extends AttrCalcRole
{
	private final long roleId;

	private final int petkey;
	private Pet pet;

	
	static fire.pb.pet.PetNextExp getNextExpTable(Integer curlevel){
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.pet.PetNextExp.class).get(curlevel);
	}

	
	public static fire.pb.pet.PetAttrModData getAttrModData(Integer id)
	{
		return  fire.pb.main.ConfigManager.getInstance().getConf(
				PetAttrModData.class).get(id);
	}

	
	public PetImpl(long roleId,int petkey)
	{
		this(roleId,petkey ,false);
	}

	
	public PetImpl(long roleId,int petkey, boolean readonly)
	{
		this.roleId = roleId;
		this.petkey = petkey;
		PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, readonly);
		pet = petColumn.getPet(petkey);
		if (pet == null)
			throw new IllegalArgumentException("错误的petkey：" + petkey);
	}
	
	public PetImpl(xbean.PetInfo petInfo)
	{
		pet = Pet.getPet(petInfo);
		this.roleId = -1;
		this.petkey = -1;
		updateAllFinalAttrs();
	}

	@Override
	public int getLevel()
	{
		return pet.getLevel();
	}

	@Override
	public Map<Integer,Float> addWound(int v)
	{
		return new HashMap<Integer, Float>();//宠物没有伤
	}

	public void addBornattackApt( final int v ) {
		xbean.PetInfo pi = pet.getPetInfo();
		final int newV =  pi.getBornattackapt() + v;
		pi.setBornattackapt( newV > 100 ? newV : 100 );
	}
	public int getAttackApt() {
		return pet.getAttackapt();
	}
	public void addBorndefendApt( final int v ) {
		xbean.PetInfo pi = pet.getPetInfo();
		final int newV =  pi.getBorndefendapt() + v;
		pi.setBorndefendapt( newV > 100 ? newV : 100 );
	}
	public int getDefendApt() {
		return pet.getDefendapt();
	}
	public void addBorndodgeApt( final int v ) {
		xbean.PetInfo pi = pet.getPetInfo();
		final int newV = pi.getBorndodgeapt() + v;
		pi.setBorndodgeapt( newV > 100 ? newV : 100 );
	}
	public int getDodgeApt() {
		return pet.getDodgeapt();
	}
	public void addBornmagicApt( final int v ) {
		xbean.PetInfo pi = pet.getPetInfo();
		final int newV =  pi.getBornmagicapt() + v;
		pi.setBornmagicapt( newV > 100 ? newV : 100 );
	}
	public int getMagicApt() {
		return pet.getMagicapt();
	}
	public void addBornphyforceApt( final int v ) {
		xbean.PetInfo pi = pet.getPetInfo();
		final int newV =  pi.getBornphyforceapt() + v;
		pi.setBornphyforceapt( newV > 100 ? newV : 100 );
	}
	public int getPhyforceApt() {
		return pet.getPhyforceapt();
	}
	public void addBornspeedApt( final int v ) {
		xbean.PetInfo pi = pet.getPetInfo();
		final int newV =  pi.getBornspeedapt() + v;
		pi.setBornspeedapt( newV > 100 ? newV : 100 );
	}
	public int getSpeedApt() {
		return pet.getSpeedapt();
	}
	@Override
	public Map<Integer,Float> fullHp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int ulhp = this.getUplimithp();
		pet.setHp(ulhp);
		changedAttrs.put(AttrType.HP, (float)ulhp);
		return changedAttrs;
		
	}
	@Override
	public Map<Integer,Float> fullMp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int maxmp = this.getMaxMp();
		pet.setMp(maxmp);
		changedAttrs.put(AttrType.MP, (float)maxmp);
		return changedAttrs;
	}

	@Override
	public Map<Integer, Float> fullHpAndRecoverWound()
	{
		Map<Integer, Float> changedAttrs = new HashMap<Integer, Float>();
		int maxhp = this.getMaxHp();
		if (pet.getHp() == maxhp)
		{
			pet.setHp(maxhp);
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

	@Override
	public int getHp()
	{
		return pet.getHp();
	}

	@Override
	public int getMp()
	{
		return pet.getMp();
	}
	
	
	@Override
	public int getWound()
	{
		return 0;
	}
	
	public int getEnlimit()
	{
		return 50 + pet.getLevel() * 5;
	}

	@Override
	public long getNextExp()
	{
		return getNextExpTable((int)pet.getLevel()).exp;
	}

	@Override
	public Map<Integer, Float> addPoints(int cons, int str, int agi, int endu, int iq)
	{
		final int curpoint = pet.getPoint();
		final long addpoints = agi + cons + endu + iq +str;
		if (addpoints > curpoint)
			return null;
		if (fire.pb.pet.Helper.isPetInBattle(roleId, petkey))
			return null; // 战斗中的参战宠不能执行当前操作

		pet.setPoint(curpoint - (int)addpoints);
		xbean.BasicFightProperties bfp = pet.getPetInfo().getBfp();
		bfp.setAgi(bfp.getAgi() + agi);
		bfp.setCons(bfp.getCons() + cons);
		bfp.setEndu(bfp.getEndu() + endu);
		bfp.setIq(bfp.getIq() + iq);
		bfp.setStr(bfp.getStr() + str);
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		if(cons > 0)
			changedAttrs.putAll(fullHp());
		if(iq > 0)
			changedAttrs.putAll(fullMp());
		changedAttrs.put(AttrType.POINT, (float)pet.getPoint());
		return changedAttrs;
	}

	@Override
	public Map<Integer, Float> resetPoints()
	{
		if (fire.pb.pet.Helper.isPetInBattle(roleId, petkey))
			return null; // 战斗中的参战宠不能执行当前操作

		int level = pet.getLevel();

		// 获取已加点数
		int point = 0;
		point += pet.getBfp().getCons() - pet.getInitBfp().getCons() - level;
		point += pet.getBfp().getIq()   - pet.getInitBfp().getIq()   - level;
		point += pet.getBfp().getStr()  - pet.getInitBfp().getStr()  - level;
		point += pet.getBfp().getEndu() - pet.getInitBfp().getEndu() - level;
		point += pet.getBfp().getAgi()  - pet.getInitBfp().getAgi()  - level;

		// 重置点数
		pet.getBfp().setCons(pet.getInitBfp().getCons() + level);
		pet.getBfp().setIq(pet.getInitBfp().getIq()     + level);
		pet.getBfp().setStr(pet.getInitBfp().getStr()   + level);
		pet.getBfp().setEndu(pet.getInitBfp().getEndu() + level);
		pet.getBfp().setAgi(pet.getInitBfp().getAgi()   + level);

		// 恢复潜能点
		pet.setPoint(pet.getPoint() + point);

		// 加满血蓝
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		changedAttrs.putAll(fullHp());
		changedAttrs.putAll(fullMp());
		changedAttrs.put(AttrType.POINT, (float)pet.getPoint());
		return changedAttrs;
	}

	@Override
	public HashMap<Integer, Float> changeScheme(int schemeid)
	{
		return null;
	}
	
	@Override
	public Map<Integer, Float> updateAllFinalAttrs()
	{
		Map<Integer, Float> changedattrs = super.updateAllFinalAttrs();
		return changedattrs;
	}

	// ********************************计算*****************************************//
	// ********************************计算*****************************************//
	// ********************************计算*****************************************//

	protected float calcL2Attr(int attrType)
	{
		PetAttrModData attrmod = getAttrModData(attrType);
		float v = (float)(getCons()*attrmod.getConsfactor() + getIq()*attrmod.getIqfactor() + getStr()*attrmod.getStrfactor() + getAgi()*attrmod.getAgifactor() + getEndu()*attrmod.getEndufactor());
		return v;
	}

	
	protected float calcMaxHp(){
//		float maxhp = (float) 2*(calcCons() * pet.getGrowrate() * 2.6f + pet.getPhyforceapt() *11 *pet.getLevel())/1000f;
		float maxhp = (float)(calcL2Attr(AttrType.MAX_HP)*pet.getGrowrate()/1000f + pet.getPhyforceapt()/360f*pet.getLevel());
		maxhp = calcEffectBonus(AttrType.MAX_HP, maxhp);

		return maxhp;
	}

	
	protected float calcMaxMp()
	{
//		float maxmp = (float) 2*(calcIq() * pet.getGrowrate()*1.2f+ pet.getMagicapt()*pet.getLevel()*5)/1000f;
		float maxmp = (float)(calcL2Attr(AttrType.MAX_MP)*pet.getGrowrate()/1000f + pet.getMagicapt()/400f*pet.getLevel());
		maxmp = calcEffectBonus(AttrType.MAX_MP, maxmp);

		return maxmp;
	}
	
	
	protected float calcHit(){
		float hitrate = (float)2*((pet.getLevel() * 12 + calcStr() * 10)* pet.getGrowrate() + (4 * pet.getAttackapt() - 2200)* pet.getLevel()*10)/10000f;
		return calcEffectBonus(AttrType.HIT_RATE, hitrate);
	}

	
	protected float calcAttack(){
//		float attack = (float) 2*((pet.getLevel() * 3 + calcStr() * 6.8f)* pet.getGrowrate() + pet.getAttackapt()* pet.getLevel()*50)/10000f;
		float attack = (float)(calcL2Attr(AttrType.ATTACK)*pet.getGrowrate()/1000f + pet.getAttackapt()/400f*pet.getLevel());
		return calcEffectBonus(AttrType.ATTACK, attack);
	}
	
	
	protected float calcDefend(){
		//float defend = (float)(2*(pet.getLevel()*pet.getDefendapt()*(9.5f + pet.getGrowrate()*19f/1000f)/9000f + pet.getGrowrate() * 1.3f /1000f* calcEndu()));
		float defend = (float)(calcL2Attr(AttrType.DEFEND)*pet.getGrowrate()/1000f + pet.getDefendapt()/300f*pet.getLevel());
		return calcEffectBonus(AttrType.DEFEND, defend);
	}
	
	
	protected float calcSpeed(){
		//float speed = (float) (calcAgi()*pet.getSpeedapt()*0.7f/1000f + 0.5f*pet.getLevel()*pet.getGrowrate()/1000f);
		float speed = (float)(calcL2Attr(AttrType.SPEED)*pet.getGrowrate()/1000f + pet.getSpeedapt()/480f*pet.getLevel());
		return calcEffectBonus(AttrType.SPEED, speed);
	}
	
	
	protected float calcDodge(){
		float dodge =  (float) 3*calcAgi() * pet.getDodgeapt()/1000f;
		return calcEffectBonus(AttrType.DODGE_RATE, dodge);
	}
	
	
	protected float calcMagicAttack(){
		//float magicattack = (float)(2*((pet.getLevel()*2.5f+calcIq()*5.3f)*pet.getGrowrate()+pet.getMagicapt()*pet.getLevel()*36)/10000f);
		float magicattack = (float)(calcL2Attr(AttrType.MAGIC_ATTACK)*pet.getGrowrate()/1000f + pet.getMagicapt()/1000f*pet.getLevel());
		return calcEffectBonus(AttrType.MAGIC_ATTACK, magicattack);
	}
	
	
	protected float calcMagicDef(){
		//float magicdef = (((pet.getDefendapt()*15+pet.getGrowrate()*4.3f)*pet.getLevel()+(calcIq()*7+calcCons()*3+calcEndu()*2)*200f)/5000f)*2;
		float magicdef = (float)(calcL2Attr(AttrType.MAGIC_DEF)*pet.getGrowrate()/1000f + pet.getMagicapt()/1000f*pet.getLevel());
		return calcEffectBonus(AttrType.MAGIC_DEF, magicdef);
	}

	
	@Override
	public Map<Integer, Float> levelUp()
	{
		final long nexp = this.getNextExp();
		if (pet.getExp() < nexp)
			return null;
		if (pet.getLevel() >= DataInit.PET_LEVEL_MAX)
			return null;
		Integer rolelevel = xtable.Properties.selectLevel(roleId);
		if (rolelevel == null)
			return null;
		if (pet.getLevel() >= rolelevel + DataInit.ROLE_PET_LEVEL_SPACE_OFEXPITEM)
			return null;
		if (fire.pb.pet.Helper.isPetInBattle(roleId, petkey))
			return null; // 战斗中的参战宠不能执行当前操作

		int oldsize = pet.getSize();
		pet.setExp(pet.getExp()-nexp);
		pet.setLevel((short)(pet.getLevel() + 1));
		pet.setPoint(pet.getPoint() + DataInit.PET_UP_LEVEL_ADD_POINT);
			
		pet.getBfp().setAgi(pet.getBfp().getAgi() + 1);
		pet.getBfp().setCons(pet.getBfp().getCons() + 1);
		pet.getBfp().setEndu(pet.getBfp().getEndu() + 1);
		pet.getBfp().setStr(pet.getBfp().getStr() + 1);
		pet.getBfp().setIq(pet.getBfp().getIq() + 1);
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		fullHp();
		fullMp();
		changedAttrs.put(AttrType.HP, (float)getHp());
		changedAttrs.put(AttrType.MP, (float)getMp());
		changedAttrs.put(AttrType.LEVEL, (float)pet.getLevel());
		changedAttrs.put(AttrType.POINT, (float)pet.getPoint());
		int newsize = pet.getSize();
		if(oldsize != newsize)
			changedAttrs.put(AttrType.PET_SCALE, (float)newsize);
		
		
		// 自动加点并计算加点后的属性变化
		int cons = pet.getPetInfo().getAutoaddcons();
		int iq = pet.getPetInfo().getAutoaddiq();
		int str = pet.getPetInfo().getAutoaddstr();
		int agi = pet.getPetInfo().getAutoaddagi();
		int endu = pet.getPetInfo().getAutoaddendu();
		if (Integer.MAX_VALUE - cons - iq -str -agi -endu > 0){
			Map<Integer, Float> attrs = addPoints(cons, str, agi, endu, iq);
			if (attrs != null) {
				changedAttrs.putAll(attrs);
			}
		}
		
		// 如果升级的宠物是正在展示的宠物，通知地图模块展示宠物
		xbean.Properties prop = xtable.Properties.get(roleId);
		if (prop != null && prop.getShowpetkey() == petkey){
			fire.msp.move.GNotifyMapPetInfo send = new fire.msp.move.GNotifyMapPetInfo(roleId, pet.getShowPetInfo());
			fire.pb.GsClient.pSendWhileCommit(send);
		}
		return changedAttrs;
	}

	public Pet getPet()
	{
		return pet;
	}
	
	public xbean.PetInfo getPetinfo()
	{
		return pet.getPetInfo();
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
	
	@Override
	public int updateScore() {
		return 0;
	}

	@Override
	public void clear()
	{
		pet.getEffects().clear();
		pet.getFinalAttrs().clear();
	}
	
	@Override
	public int setHp(int hp)
	{
		int oldvalue = this.getHp();
		pet.setHp(Math.max(0, (int) Math.min((long) this.getUplimithp(), hp)));
		return pet.getHp() - oldvalue;
	}
	
	@Override
	public int setMp(int mp)
	{
		int oldvalue = this.getMp();
		pet.setMp(Math.max(0, (int) Math.min((long) this.getMaxMp(), mp)));
		return pet.getMp() - oldvalue;
	}
	
	@Override
	public int setSp(int sp)
	{
		return 0;
	}

	@Override
	public int addSp(int v, int maxsp)
	{
		return 0;
	}

	@Override
	public int getSp()
	{
		return 0;
	}

	@Override
	public int getMaxSp()
	{
		return 0;
	}
	
	public long getRoleId()
	{
		return roleId;
	}

	public int getPetkey()
	{
		return petkey;
	}

	@Override
	public int getBodySize()
	{
		return pet.getSize();
	}

	@Override
	public Map<Integer,Float> correctEffects(Map<Integer, Float> buffeffects)
	{
		return null;
	}

	@Override
	protected Map<Integer, Float> getFinalAttrs() {
		return pet.getFinalAttrs();
	}

	@Override
	protected Map<Integer, Float> getEffects() {
		return pet.getEffects();
	}

	@Override
	protected BasicFightProperties getBFP() {
		return pet.getBfp();
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
		float value = (float) (getLevel() * pet.getPetAttr().getHealgrow());
		return value;
	}

	@Override
	protected float calcSeal() {
		float value = (float) (getLevel() * pet.getPetAttr().getCtrlhitgrow());
		return value;
	}

	@Override
	protected float calcUnseal() {
		float value = (float) (getLevel() * pet.getPetAttr().getCtrlresistgrow());
		return value;
	}

	@Override
	public int getScore() {
		return pet.getPetInfo().getPetscore();
	}
}
