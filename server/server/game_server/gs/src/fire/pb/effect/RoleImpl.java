package fire.pb.effect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fire.msp.role.GUpdateRoleLevel;
import fire.pb.DataInit;
import fire.pb.GsClient;
import fire.pb.PAddExpProc;
import fire.pb.RoleConfigManager;
import fire.pb.SRefreshUserExp;
import fire.pb.attr.AttrType;
import fire.pb.common.SCommon;
import fire.pb.item.Pack;
import fire.pb.product.Commontext;
import fire.pb.product.SErrorCode;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.role.AttrModData;
import fire.pb.role.NextExpAndResMoney;
import fire.pb.role.Saddpointchange;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.state.State;
import fire.pb.state.StateManager;
import fire.pb.util.DateValidate;
import mkdb.Procedure;
import xbean.BasicFightProperties;
import xbean.RoleAddPointProperties;


public class RoleImpl extends AttrCalcRole
{
	private final long roleid;
	private final xbean.Properties prop;
	private xbean.ERole eRole;
	private boolean readonly;
	private final fire.pb.role.RoleModData moddata;
	
	private static fire.pb.role.RoleModData getRoleModData(Integer id)
	{
		return fire.pb.main.ConfigManager.getInstance().getConf(
				fire.pb.role.RoleModData.class).get(id);
	}
	
	public static fire.pb.role.AttrModData getAttrModData(Integer id)
	{
		return  fire.pb.main.ConfigManager.getInstance().getConf(
				AttrModData.class).get(id);
	}

	public static NextExpAndResMoney getLevelConfig(Integer id)
	{
		return  fire.pb.main.ConfigManager.getInstance().getConf(
				NextExpAndResMoney.class).get(id);

	}
	
	public long getRoleId()
	{
		return roleid;
	}
	
	public NextExpAndResMoney getLevelConfig()
	{
		return getLevelConfig(getLevel());
	}

	public RoleImpl(long roleid)
	{
		this(roleid, false);
	}

	public RoleImpl(long roleid, boolean readonly)
	{
		this.readonly = readonly;
		if(StateManager.selectStateIdByRoleId(roleid) == State.UNENTRY_STATE)
		{
			Module.logger.error("在角色"+roleid+"下线的情况下，使用了该角色的ERole。因为ERole是内存表，数据可能没有  ，请根据堆栈内容确认此使用是否正确。");
			Module.logger.error(fire.pb.util.Parser.convertStackTrace2String(Thread.currentThread().getStackTrace()));
		}
		if (!readonly)
		{
			this.prop = xtable.Properties.get(roleid);
			if(prop == null)
				throw new IllegalArgumentException("错误的roleId：" + roleid);
			eRole = xtable.Erole.get(roleid);
		} else
		{
			this.prop = xtable.Properties.select(roleid);
			if(prop == null)
				throw new IllegalArgumentException("错误的roleId：" + roleid);
			eRole = xtable.Erole.select(roleid);
		}

		if (eRole == null)
		{
			if(readonly)
				eRole = xbean.Pod.newERoleData();
			else
			{
				eRole = xbean.Pod.newERole();
				xtable.Erole.insert(roleid, eRole);
			}
		}
		this.roleid = roleid;
		moddata = getRoleModData(prop.getRace());
		//standardattr = getStandardAttr(prop.getLevel());
	}

	@Override
	public int getLevel()
	{
		return prop.getLevel();
	}

	@Override
	public Map<Integer,Float> fullHp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int ulhp = this.getUplimithp();
		prop.setHp(ulhp);
		changedAttrs.put(AttrType.HP, (float)ulhp);
		return changedAttrs;
	}

	@Override
	public Map<Integer,Float> fullHpAndRecoverWound()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int maxhp = this.getMaxHp();
		changedAttrs.put(AttrType.UP_LIMITED_HP, (float)maxhp);
		prop.setHp(maxhp);
		changedAttrs.put(AttrType.HP, (float)maxhp);
		return changedAttrs;
	}
	
	public Map<Integer,Float> recoverWound(){
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int maxhp = this.getMaxHp();
		if(prop.getWound() > 0){
			prop.setWound(0);
			changedAttrs.put(AttrType.UP_LIMITED_HP, (float)maxhp);
		}
		return changedAttrs;
	}
	

	@Override
	public Map<Integer,Float> fullMp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int maxmp = this.getMaxMp();
		prop.setMp(maxmp);
		changedAttrs.put(AttrType.MP, (float)maxmp);
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
	
	@Override
	public Map<Integer, Float> recovery() {
		HashMap<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		
		if(getWound() != 0)
		{
			prop.setWound(0);//恢复伤
			changedAttrs.put(AttrType.UP_LIMITED_HP, (float)this.getUplimithp());
		}
		
		int maxhp = this.getMaxHp();
		if ( this.getHp() != maxhp ) {
			this.setHp( maxhp );
			changedAttrs.put( AttrType.HP, (float)maxhp );
		}
		int maxmp = this.getMaxMp();
		if ( this.getMp() != maxmp ) {
			this.setMp( maxmp );
			changedAttrs.put( AttrType.MP, (float)maxmp );
		}
		
		return changedAttrs;
	}
	
	@Override
	public Map<Integer, Float> recoverToMaxHpMpPct(double hppct, double mppct, boolean ulhp2hp)
	{
		Map<Integer, Float> changedAttrs = new HashMap<Integer, Float>();
		int dsthp = (int)(getMaxHp() * hppct);
		//伤要回到至少dsthp的程度
		int curulhp = this.getUplimithp();
		if( curulhp< dsthp)//血上限小于血量，回复到血量值
			changedAttrs.putAll(addWound(curulhp - dsthp));
		if( curulhp > dsthp && ulhp2hp)//血上限大于血量，并且要强制回复到与血相等
			changedAttrs.putAll(addWound(curulhp - dsthp));
		setHp(dsthp);
		changedAttrs.put(AttrType.HP, (float)dsthp);
		int dstmp = (int)(getMaxMp() * mppct);
		setMp(dstmp);
		changedAttrs.put(AttrType.MP, (float)dstmp);
		return changedAttrs;
	}

	@Override
	public int addSp(int v, int maxsp)
	{
		if(v == 0)
			return 0;
		int oldvalue = this.getSp();
		prop.setSp(Math.max(0, (int) Math.min(maxsp>0?maxsp:(long) this.getMaxSp(), oldvalue + (long) v)));
		return prop.getSp() - oldvalue;
	}
	
	@Override
	public Map<Integer,Float> addWound(int v)
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int oldvalue = prop.getWound();
		prop.setWound(Math.max(0,(int)Math.min((long) this.getMaxHp()-1, oldvalue + (long) v)));//当前气血上限最小为1
		
		int uplimithp =  getUplimithp();
		changedAttrs.put(AttrType.UP_LIMITED_HP, (float)uplimithp);
		if(prop.getHp() > uplimithp)
		{
			prop.setHp((int)uplimithp);
			changedAttrs.put(AttrType.HP, (float)prop.getHp());
		}		
		return changedAttrs;
	}
	
	public Map<Integer, Float> addPoints(int cons, int str, int agi, int endu, int iq)
	{
		if(readonly)
			return null;
		if(fire.pb.buff.Module.existState(roleid, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return null;
		final int curpoint = prop.getPoint().get(prop.getScheme());
		final int addpoints = agi + cons + endu + iq + str;
		if (addpoints > curpoint)
			return null;
		prop.getPoint().put(prop.getScheme(), (curpoint - addpoints));		
		RoleAddPointProperties addfp = prop.getAddpointfp();
		
		xbean.BasicFightProperties bfp = prop.getBfp();
		bfp.setAgi((short) (bfp.getAgi() + agi));
		bfp.setCons((short) (bfp.getCons() + cons));
		bfp.setEndu((short) (bfp.getEndu() + endu));
		bfp.setIq((short) (bfp.getIq() + iq));
		bfp.setStr((short) (bfp.getStr() + str));
		
		addfp.getAgi_save().put(prop.getScheme(), addfp.getAgi_save().get(prop.getScheme()) + agi);
		addfp.getCons_save().put(prop.getScheme(), addfp.getCons_save().get(prop.getScheme()) + cons);
		addfp.getEndu_save().put(prop.getScheme(), addfp.getEndu_save().get(prop.getScheme()) + endu);
		addfp.getIq_save().put(prop.getScheme(), addfp.getIq_save().get(prop.getScheme()) + iq);
		addfp.getStr_save().put(prop.getScheme(), addfp.getStr_save().get(prop.getScheme()) + str);		
		
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		if(cons > 0)
			changedAttrs.putAll(fullHpAndRecoverWound());
		if(iq > 0)
			changedAttrs.putAll(fullMp());
		//changedAttrs.put(AttrType.HP, (float)getHp());
		//changedAttrs.put(AttrType.MP, (float)getMp());
		changedAttrs.put(AttrType.POINT, (float)prop.getPoint().get(prop.getScheme()));
		//更新人物评分
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleid));
		return changedAttrs;
	}

	public Map<Integer, Float> gmAddPoints(int cons, int str, int agi, int endu, int iq)
	{
		if(fire.pb.buff.Module.existState(roleid, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return null;
		
		RoleAddPointProperties addfp = prop.getAddpointfp();
		xbean.BasicFightProperties bfp = prop.getBfp();
		
		if (bfp.getAgi() >= 25000){
			return null;
		}
		bfp.setAgi((short) (bfp.getAgi() + agi));
		bfp.setCons((short) (bfp.getCons() + cons));
		bfp.setEndu((short) (bfp.getEndu() + endu));
		bfp.setIq((short) (bfp.getIq() + iq));
		bfp.setStr((short) (bfp.getStr() + str));
		
		addfp.getAgi_save().put(prop.getScheme(), addfp.getAgi_save().get(prop.getScheme()) + agi);
		addfp.getCons_save().put(prop.getScheme(), addfp.getCons_save().get(prop.getScheme()) + cons);
		addfp.getEndu_save().put(prop.getScheme(), addfp.getEndu_save().get(prop.getScheme()) + endu);
		addfp.getIq_save().put(prop.getScheme(), addfp.getIq_save().get(prop.getScheme()) + iq);
		addfp.getStr_save().put(prop.getScheme(), addfp.getStr_save().get(prop.getScheme()) + str);		
		
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		if(cons > 0)
			changedAttrs.putAll(fullHpAndRecoverWound());
		if(iq > 0)
			changedAttrs.putAll(fullMp());
		changedAttrs.put(AttrType.POINT, (float)prop.getPoint().get(prop.getScheme()));
		//更新人物评分
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleid));
		return changedAttrs;
	}
	
	@Override
	public Map<Integer, Float> resetPoints()
	{
		return null;
	}

	public Map<Integer, Float> changeScheme(int schemeid)
	{
		if (readonly)
			return null;
		
		final SErrorCode sErrorCode = new SErrorCode();
		
		Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(
				roleid, fire.pb.item.BagTypes.BAG, false);
		
		long currentTime = System.currentTimeMillis();
		if (!DateValidate.inTheSameDay(prop.getSchemechangetimes(), currentTime)) {
			prop.setSchemechangetimes(currentTime);
			prop.setSchemechanges(0);
		}
		
		int n = prop.getSchemechanges();
		
		Map<Integer, Saddpointchange> changeMap = fire.pb.main.ConfigManager.getInstance().getConf(Saddpointchange.class);		
		Saddpointchange pointChange = changeMap.get(n + 1);
		if (pointChange == null) {
			pointChange = changeMap.get(changeMap.size());  //得到最后一个配置
		}
		
		int money = pointChange.consume;
		if (money > bag.getMoney()){
			sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleid, sErrorCode);
			return null;
		}
		
		if(bag.subMoney(-money, Commontext.REASON_STRING,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_switchpoint, 0) != -money){
			
			return null;
		}
		
		//切换点数方案
		int curr = prop.getScheme();
		BasicFightProperties bfp = prop.getBfp();		
		
		bfp.setAgi(bfp.getAgi() - prop.getAddpointfp().getAgi_save().get(curr));
		bfp.setCons(bfp.getCons() - prop.getAddpointfp().getCons_save().get(curr));
		bfp.setEndu(bfp.getEndu() - prop.getAddpointfp().getEndu_save().get(curr));
		bfp.setIq(bfp.getIq() - prop.getAddpointfp().getIq_save().get(curr));
		bfp.setStr(bfp.getStr() - prop.getAddpointfp().getStr_save().get(curr));		
		
		bfp.setAgi(bfp.getAgi() + prop.getAddpointfp().getAgi_save().get(schemeid));
		bfp.setCons(bfp.getCons() + prop.getAddpointfp().getCons_save().get(schemeid));
		bfp.setEndu(bfp.getEndu() + prop.getAddpointfp().getEndu_save().get(schemeid));
		bfp.setIq(bfp.getIq() + prop.getAddpointfp().getIq_save().get(schemeid));
		bfp.setStr(bfp.getStr() + prop.getAddpointfp().getStr_save().get(schemeid));
		
		prop.setSchemechanges(n + 1);		
		prop.setScheme(schemeid);
		
		
		
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		
		changedAttrs.putAll(fullHpAndRecoverWound());
		changedAttrs.putAll(fullMp());
		
		return changedAttrs;
	}
	
	
	public Map<Integer, Float> levelUp(){
		final long nexp = this.getNextExp();
		if (prop.getExp() < nexp)
			return null;
		if (prop.getLevel() >= DataInit.ROLE_LEVEL_MAX)
			return null;
		new PAddExpProc(roleid, -nexp, false,PAddExpProc.LEVEL_UP,"").call();
		//prop.setExp(prop.getExp()-nexp);
		prop.setLevel(prop.getLevel() + 1);
		//prop.setPoint(prop.getPoint() + DataInit.ROLE_UP_POINT);
		
		final fire.pb.role.RoleConfig rc = RoleConfigManager.getRoleConfigBySchoolID(prop.getSchool());
		//xbean.BasicFightProperties bfp = prop.getBfp();
		BasicFightProperties bfp = prop.getBfp();
		RoleAddPointProperties addfp = prop.getAddpointfp();
		
		int nlevel = prop.getLevel();
		final SCommon cc = RoleConfigManager.getRoleCommonConfig(DataInit.COMMEN_ROLE_ADDPOINT);
		if (nlevel < Integer.parseInt(cc.getValue())) {
			ArrayList<Integer> points = null;
			switch (rc.defaultscheme)
			{
			case 1:
				points = rc.addpoint;
				break;
			case 2:
				points = rc.addpoint2;
				break;
			case 3:
				points = rc.addpoint3;
				break;
			default:
				points = rc.addpoint;
			}
			
			//points 体 智 力 耐 敏
			bfp.setAgi((short) (bfp.getAgi() + 1 + points.get(4)));
			bfp.setCons((short) (bfp.getCons() + 1 + points.get(0)));
			bfp.setEndu((short) (bfp.getEndu() + 1 + points.get(3)));
			bfp.setIq((short) (bfp.getIq() + 1 + points.get(1)));
			bfp.setStr((short) (bfp.getStr() + 1 + points.get(2)));
			
			addfp.getAgi_save().put(prop.getScheme(), addfp.getAgi_save().get(prop.getScheme()) + points.get(4));
			addfp.getCons_save().put(prop.getScheme(), addfp.getCons_save().get(prop.getScheme()) + points.get(0));
			addfp.getEndu_save().put(prop.getScheme(), addfp.getEndu_save().get(prop.getScheme()) + points.get(3));
			addfp.getIq_save().put(prop.getScheme(), addfp.getIq_save().get(prop.getScheme()) + points.get(1));
			addfp.getStr_save().put(prop.getScheme(), addfp.getStr_save().get(prop.getScheme()) + points.get(2));
			
			prop.getPoint().put(2, prop.getPoint().get(2) + DataInit.ROLE_UP_POINT);
			prop.getPoint().put(3, prop.getPoint().get(3) + DataInit.ROLE_UP_POINT);
		}
		else
		{
			bfp.setAgi((short) (bfp.getAgi() + 1));
			bfp.setCons((short) (bfp.getCons() + 1));
			bfp.setEndu((short) (bfp.getEndu() + 1));
			bfp.setIq((short) (bfp.getIq() + 1));
			bfp.setStr((short) (bfp.getStr() + 1));
			
			for (int i = 1; i < 4; i ++) {
				prop.getPoint().put(i, prop.getPoint().get(i) + DataInit.ROLE_UP_POINT);
			}
		}
		
		//standardattr = getStandardAttr(prop.getLevel());//更新标准值
		
		
		Map<Integer, Float> changedAttrs = updateAllFinalAttrs();
		fullHpAndRecoverWound();
		fullMp();
		changedAttrs.put(AttrType.HP, (float)getHp());
		changedAttrs.put(AttrType.UP_LIMITED_HP, (float)getUplimithp());
		changedAttrs.put(AttrType.MP, (float)getMp());
		changedAttrs.put(AttrType.LEVEL, (float)prop.getLevel());
		
		changedAttrs.put(AttrType.POINT, (float)prop.getPoint().get(prop.getScheme()));
		changedAttrs.put(AttrType.ENLIMIT, (float)LiveSkillManager.getInstance().CalcEnergyMaxLimit(prop.getLevel())); //活力上限提高了 by changhao
		//修改给客户端发经验的方式
		mkdb.Procedure.psendWhileCommit(roleid, new SRefreshUserExp(prop.getExp()));
		// 通知一下师徒模块和任务模块
	//	fire.pb.master.Module.getInstance().onLevelUp(roleid, prop.getLevel());
	//	fire.pb.mission.Module.getInstance().onLevelUp( roleid, prop.getLevel() );
//		fire.pb.event.Poster.getPoster().dispatchEvent( 
//				new fire.pb.event.LevelupEvent(roleid,prop.getLevel()) );
		//向地图更新等级
		
		GUpdateRoleLevel gUpdateRoleLevel = new GUpdateRoleLevel(roleid,prop.getLevel());
		GsClient.pSendWhileCommit(gUpdateRoleLevel);
		return changedAttrs;
	}

	// ********************************GET*****************************************//
	// ********************************GET*****************************************//
	// ********************************GET*****************************************//

	@Override
	public int getHp()
	{
		return prop.getHp();
	}

	@Override
	public int getMp()
	{
		return prop.getMp();
	}

	@Override
	public int getSp()
	{
		return prop.getSp();
	}
	
	@Override
	public int getWound()
	{
		return prop.getWound();
	}

	@Override
	public long getNextExp()
	{
		return getLevelConfig().exp;
	}
	

	// ********************************计算*****************************************//
	// ********************************计算*****************************************//
	// ********************************计算*****************************************//


	protected float calcL2Attr(int attrType)
	{
		AttrModData attrmod = getAttrModData(attrType);
		float v = (float)(getCons()* attrmod.getConsfactor() +  getIq() * attrmod.getIqfactor() + getStr() *attrmod.getStrfactor() + getAgi() * attrmod.getAgifactor() + getEndu() * attrmod.getEndufactor());
		return v;
	}
	
	protected float calcMaxHp()
	{
		float maxhp = (float) (calcL2Attr(AttrType.MAX_HP) + moddata.hpinit);
		maxhp = calcEffectBonus(AttrType.MAX_HP, maxhp);
		return maxhp;
	}


	protected float calcMaxMp()
	{
		float maxmp = (float) (calcL2Attr(AttrType.MAX_MP) + moddata.mpinit);
		maxmp = calcEffectBonus(AttrType.MAX_MP, maxmp);
		return maxmp;
	}
	

	protected float calcHit()
	{
		float hitrate = (float) (calcL2Attr(AttrType.HIT_RATE) + moddata.hitinit);
		return calcEffectBonus(AttrType.HIT_RATE, hitrate);
	}

	protected float calcAttack()
	{
		float attack = (float) (calcL2Attr(AttrType.ATTACK) + moddata.damageinit);
		return calcEffectBonus(AttrType.ATTACK, attack);
	}

	protected float calcDefend()
	{
		float defend = (float) (calcL2Attr(AttrType.DEFEND) + moddata.defendinit);
		return calcEffectBonus(AttrType.DEFEND, defend);
	}

	protected float calcSpeed()
	{
		float speed = (float) (calcL2Attr(AttrType.SPEED) + moddata.speedinit);
		float speed2 = calcEffectBonus(AttrType.SPEED, speed);
		if(speed2<-50||speed2>2000)
		{
			Module.logger.error("用户速度属性超出正常值范围roleid:"+roleid+" 速度值："+speed2);
			Module.logger.error(fire.pb.util.Parser.convertStackTrace2String(Thread.currentThread().getStackTrace()));
		}/**/
		return speed2;
	}

	protected float calcDodge()
	{
		float dodge = (float) (calcL2Attr(AttrType.DODGE_RATE) + moddata.dodgeinit);
		return calcEffectBonus(AttrType.DODGE_RATE, dodge);
	}
	
	protected float calcMagicAttack()
	{
		float magicattack = calcL2Attr(AttrType.MAGIC_ATTACK) + moddata.magicattackinit;
		return calcEffectBonus(AttrType.MAGIC_ATTACK, magicattack);
	}
	
	protected float calcMagicDef()
	{
		float magicdef = calcL2Attr(AttrType.MAGIC_DEF)+ moddata.magicdefendinit;
		return calcEffectBonus(AttrType.MAGIC_DEF, magicdef);
	}
	
	protected float calcEnlimit(){
		float enlimit = 50 + prop.getLevel() * 5;
		return calcEffectBonus(AttrType.ENLIMIT, enlimit);
	}
	
	protected float calcPflimit(){
		float pflimit = 50 + prop.getLevel() * 5;
		return calcEffectBonus(AttrType.PFLIMIT, pflimit);
	}
	
	@Override
	public Map<Integer, Float> updateAllFinalAttrs()
	{
		Map<Integer, Float> changedattrs = super.updateAllFinalAttrs();
		if(changedattrs.containsKey(AttrType.MAX_HP) || changedattrs.containsKey(AttrType.MAX_MP))
			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.team.PUpdateMemberMaxHpMp(roleid,getMaxHp(),getMaxMp()));
		return changedattrs;
	}
	
	public void clear()
	{
		eRole.getEffects().clear();
		eRole.getFinalattrs().clear();
	}
	
	@Override
	public int setHp(int hp)
	{
		int oldvalue = this.getHp();
		prop.setHp(Math.max(0, (int) Math.min((long) this.getUplimithp(), hp)));
		return prop.getHp() - oldvalue;
	}

	@Override
	public int setMp(int mp)
	{
		int oldvalue = this.getMp();
		prop.setMp(Math.max(0, (int) Math.min((long) this.getMaxMp(), mp)));
		return prop.getMp() - oldvalue;
	}
	
	public int setSp(int sp)
	{
		int oldvalue = this.getSp();
		prop.setSp(Math.max(0, (int) Math.min((long) this.getMaxSp(), sp)));
		return prop.getSp() - oldvalue;
	}
	
	public int setScheme(int scheme)
	{
		prop.setScheme(scheme);
		return 0;
	}
	
	
	public xbean.Properties getProperties()
	{
		return prop;
	}
	@Override
	public int getBodySize()
	{
		return 0;
	}
	@Override
	public Map<Integer,Float> correctEffects(Map<Integer, Float> buffeffects)
	{
		StringBuffer sb = new StringBuffer("Role").append(roleid).append("Effects不符：");
		boolean correct = true;
		for(Map.Entry<Integer,Float> entry : buffeffects.entrySet())
		{
			Float value = eRole.getEffects().get(entry.getKey());
			if(value == null)
				value = 0f;
			float minus = value.intValue() - entry.getValue().intValue();
			if(minus < -1 && minus > 1)
			{
				sb.append(entry.getKey()).append(",").append(entry.getValue()).append(",").append(value).append(";");
				eRole.getEffects().put(entry.getKey(), entry.getValue());
				correct = false;
			}
		}
		if(correct)
			return null;
		else
		{
			Module.logger.error(sb);
			return updateAllFinalAttrs();
		}
	}
	@Override
	protected Map<Integer, Float> getFinalAttrs() {
		return eRole.getFinalattrs();
	}
	@Override
	protected Map<Integer, Float> getEffects() {
		return eRole.getEffects();
	}
	
	@Override
	protected BasicFightProperties getBFP() {		
		BasicFightProperties bfp = prop.getBfp();
		return bfp;
	}
	
	public String getAttrStr(){
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<Integer, Float>> effects = eRole.getEffects().entrySet().iterator();
		while(effects.hasNext()){
			Entry<Integer, Float> current = effects.next();
			sb.append(current.getKey()).append("=").append(current.getValue()).append(";");
		}
		return sb.toString();
	}
	
	@Override
	protected float calcMedical() {
		float medical = (float) (moddata.medical);
		return calcEffectBonus(AttrType.MEDICAL, medical);
	}
	@Override
	protected float calcSeal() {
		float seal = (float) ( moddata.sealhit);
		return calcEffectBonus(AttrType.SEAL, seal);
	}
	@Override
	protected float calcUnseal() {
		float unseal = (float) ( moddata.unseal);
		return calcEffectBonus(AttrType.UNSEAL, unseal);
	}
	@Override
	public int updateScore() {
		int score = super.updateScore();
		return score;
	}
	@Override
	public int getScore() {
		return prop.getLevelscore();
	}
}
