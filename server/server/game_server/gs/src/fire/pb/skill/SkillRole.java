package fire.pb.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fire.log.RemoteLogParam;
import fire.log.beans.RoleSkillUpBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.DataInit;
import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.SRefreshUserExp;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.attr.ScoreType;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.game.MoneyType;
import fire.pb.item.BagTypes;
import fire.pb.item.Depot;
import fire.pb.item.Pack;
import fire.pb.item.SRefreshCurrency;
import fire.pb.main.ConfigManager;
import fire.pb.skill.SkillConstant.BasicSkillBelongs;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.talk.MessageMgr;
import fire.script.FightJSEngine;
import fire.script.JavaScript;
import mkdb.Transaction;
import xbean.BattleInfo;
import xbean.PracticeSkill;

public class SkillRole extends SkillAgent
{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("SKILL");
	public static final long A_DAY_TimeMillis = 24*60*60*1000;
	private long roleId;
	private xbean.SkillRole srole;
	private boolean readonly;
	
	xbean.SkillRole getData() {
		return srole;
	}
	
	public SkillRole(long roleId)
	{
		this(roleId,false);
	}
	
	public long getRoleid() {
		return roleId;
	}
	
	public SkillRole(long roleId,boolean readonly)
	{
		this.roleId = roleId;
		this.readonly = readonly;
		if(this.readonly)
			srole = xtable.Skillroles.select(roleId);
		else
			srole = xtable.Skillroles.get(roleId);
		if(srole == null)
		{
			if(readonly)
				srole = xbean.Pod.newSkillRoleData();
			else
			{
				srole = xbean.Pod.newSkillRole();
				xtable.Skillroles.insert(roleId, srole);
			}
		}
	}
	
	public Integer getInbornLevel(int inbornId)
	{
		return srole.getInborns().get(inbornId);
	}

	//升级辅助技能	
	public xbean.AssistSkill updateAsssitSkillLevel(int skillId)
	{
		xbean.AssistSkill skill = srole.getAssistskills().get(skillId);
		if(skill == null)
		{
			skill = xbean.Pod.newAssistSkill();
			skill.setLevel(1);
			skill.setExp(0);
			srole.getAssistskills().put(skillId, skill);
		}
		else
		{
			skill.setLevel(skill.getLevel() + 1);
		}
		return skill;
	}
	
	//技能升级,包括合法性验证和发送协议
	public boolean updateInbornWithValid(int inbornId)
	{
		//校验技能升级状态
		BuffAgent buffagent = new BuffRoleImpl(roleId);
		if(!buffagent.canAddBuff(OperateType.UPDATE_INBORN))
		{
			MessageMgr.psendMsgNotify(roleId, 150062, null);
			return false;
		}
		
		PropRole prole = new PropRole(roleId,false);
		
		if(fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return false;
		
		SInbornInfo sinborninfo = Module.getSInbornInfo(inbornId);
		InbornConfig sinbornconfig = Module.getInbornConfig(inbornId);
		if(sinborninfo == null||sinbornconfig == null)
			return false;
		
		//验证职业合法
		int schoolid = sinborninfo.getSchool();
		if(prole.getSchool()!=schoolid)
			return false;
		
		//验证技能是否冲满
		Integer curLevel = srole.getInborns().get(inbornId);
		if(curLevel == null) curLevel = 0;
		
		if(curLevel == sinborninfo.getMaxLevel())
		{
			MessageMgr.psendMsgNotify(roleId, 141229, null);//冲满了不能再冲了
			return false;
		}
		
		//人物级别验证
		fire.pb.skill.SInbornMaxLevel sInbornMaxLevel = Module.getSInbornMaxLevel(prole.getLevel());
		
		if(sInbornMaxLevel==null)
			return false;
		
		//技能id的后两位，前两位标示职业
		int subInbornId = inbornId % 100;
		int seq = -1;
		for(int i = 0;i<Module.inbornSeq.length;i++)
		{
			if(Module.inbornSeq[i] == subInbornId)
			{	
				seq = i;
				break;
			}
		}
		if(seq == -1)
			return false;
		Integer maxLevel = sInbornMaxLevel.getInbornMaxLevel().get(seq);
		if(maxLevel == null)
			return false;
		
		if(curLevel + 1 > maxLevel)
		{
			int nextrolelevel = prole.getLevel() + 1;
			for(;nextrolelevel <= DataInit.ROLE_LEVEL_MAX;nextrolelevel++)
			{
				fire.pb.skill.SInbornMaxLevel sacMaxLevel = Module.getSInbornMaxLevel(nextrolelevel);
				if(sacMaxLevel == null)
					continue;
				Integer tmpmaxlv = sacMaxLevel.getInbornMaxLevel().get(seq);
				if(tmpmaxlv == null)
					continue;
				if(tmpmaxlv >= curLevel + 1)
					break;
			}
			MessageMgr.psendMsgNotify(roleId, 141230, MessageMgr.getStringList(nextrolelevel));
			return false;
		}
		
		//6.验证是否要超过主职业技能等级
		int mainInbornId = (inbornId/10) * 10 + 1;
		if(mainInbornId != inbornId)
		{
			Integer mainaplv = srole.getInborns().get(mainInbornId);
			if(mainaplv == null || curLevel >= mainaplv)
			{
				SInbornInfo mainapinfo = Module.getSInbornInfo(mainInbornId);
				MessageMgr.psendMsgNotify(roleId, 141231, MessageMgr.getStringList(mainapinfo.skillname,mainapinfo.skillname));
				return false;//超过主职业技能等级
			}
		}
		//技能依赖检查		
		if(curLevel == 0 && sinbornconfig.getDependInborns().size() > 0)
		{
			for(int dependInbornid : sinbornconfig.getDependInborns())
			{
				Integer lv = srole.getInborns().get(dependInbornid);
				if(lv == null || lv < sinborninfo.getDependLevel())
				{
					List<String> parameters = new ArrayList<String>();
					SInbornInfo depapinfo = Module.getSInbornInfo(sinbornconfig.getDependInborns().get(0));
					parameters.add(depapinfo.getSkillname());
					parameters.add(String.valueOf(sinborninfo.getDependLevel()));
					MessageMgr.psendMsgNotify(roleId, 141232, parameters);
					return false;
				}
			}
		}
		
		//当前经验是否足够
		SInbornConsume consumes = Module.getSInbornConsume(curLevel + 1);
		if(consumes == null)
			return false;
		InbornConsume consume = consumes.consumes.get(sinborninfo.getConsumerule()-1);
		if(consume == null)
			return false;
		
		if(consume.getExp() > prole.getCurExp()){
			logger.debug("ERROR:当前经验不足，不能学习,inborn: "+inbornId);
			MessageMgr.psendMsgNotify(roleId, 141228, null);// 经验不够
			return false;
		}
		
		//当前银币是否足够
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId,false);
		fire.pb.item.Depot depot = new fire.pb.item.Depot(roleId,false);
		long moneycost = consume.getMoney();
		if(moneycost > (bag.getMoney() + depot.getMoney()))
		{
			logger.debug("ERROR:当前金钱不足，不能学习,inborn: "+inbornId);
			MessageMgr.psendMsgNotify(roleId, 141227, null);// 金钱不够
			return false;
		}
		
		if(!updateInbornWithSP(inbornId))
			return false;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(RemoteLogParam.INBORNID,inbornId);
		param.put(RemoteLogParam.INBORNLEV,getInbornLevel(inbornId));
		//学习扣除
		//扣除银币
		costMoneyWithSP(prole, bag, depot, moneycost,param,fire.log.enums.YYLoggerTuJingEnum.tujing_Value_skillLevelup, 0 );
		//扣除经验
		new PAddExpProc(roleId, -consume.getExp(),false,PAddExpProc.UPDATE_AP,"").call();
		param.put(RemoteLogParam.EXPCHANGED,-consume.getExp());
		//修改给客户端发经验的方式
		mkdb.Procedure.psendWhileCommit(roleId, new SRefreshUserExp(prole.getCurExp()));
		
		fire.log.YYLogger.roleSkillUPLog(roleId, new RoleSkillUpBean(inbornId, getInbornLevel(inbornId)));

		fire.pb.event.Poster.getPoster().dispatchEvent( new fire.pb.event.UpdateInbornLevel( roleId, inbornId ) );
		for (int skillindexY = 0; skillindexY < SkillConstant.MAX_INBORN_SKILL_COUNT; skillindexY ++)
		{
			int apIdY = prole.getSchool() * 100 + fire.pb.skill.Module.inbornSeq[skillindexY];
			if (inbornId == apIdY)
				continue;
			
			Integer curlevelY = getInbornLevel(apIdY);
			if (curlevelY != null)
				continue;
			
			InbornConfig sinbornconfigY = Module.getInbornConfig(apIdY);
			SInbornInfo sinborninfoY = Module.getSInbornInfo(apIdY);
			if (sinbornconfigY == null)
				continue;
			
			if (sinbornconfigY.getDependInborns().size() == 0)
				continue;
			
			if (!sinbornconfigY.getDependInborns().contains(inbornId))
				continue;
			
			Integer depLevel = sinborninfoY.getDependLevel();
			if (depLevel == null || depLevel == 0)
				continue;
			
			if (curLevel + 1 >= depLevel) {
				if (!getSRoleBean().getInborns().containsKey(apIdY))
					getSRoleBean().getInborns().put(apIdY, 0);
			}
		}
		
		int apminlevel = getInbornMinLevel();
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.SKILL_LEVELUP_COURSE, apminlevel);

		//重新校验绝技
		boolean nbskilllvup = DealNBSkill();
		if(nbskilllvup)
		{
			int nnbskillid = getNBSkillid();
			SUpdateInborn sUpdateInborn = new SUpdateInborn();
			sUpdateInborn.flag=0;
			sUpdateInborn.inborns.put(nnbskillid, getLevel(nnbskillid));
			Transaction.tsendWhileCommit(roleId, sUpdateInborn);
			
			fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.NB_SKILL_COURSE, nnbskillid, 0, getLevel(nnbskillid));
			
			List<String> parameters = new ArrayList<String>();
			SInbornInfo depapinfo = Module.getSInbornInfo(nnbskillid);
			parameters.add(depapinfo.getSkillname());
			parameters.add(String.valueOf(getLevel(nnbskillid)));
			MessageMgr.psendMsgNotify(roleId, 150532, parameters);//绝技$parameter1$升到$parameter2$级
		}		

		return true;
	}
	
	public void updateRoleSkillChange(int skillId)
	{
		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.RoleSkillChangeEvent(roleId, skillId));
	}
	
	//获取自动技能ID
	public int getAutoSkillId() {
		for (Map.Entry<Integer, Integer> entry : srole.getInborns().entrySet())
		{
			//如果等级为0,则跳过
			if (entry.getValue() == 0)
				continue;
			
			SInbornInfo sapInfo = Module.getSInbornInfo(entry.getKey());
			if(sapInfo != null)
			{
				for(int skillid : sapInfo.getPointToSkillList())
				{
					if(skillid > 0 )
					{
						FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillid);
						if (sconf == null)
							continue ;
						if (sconf.getAddautoSkill() == 1) {
							//添加到自动技能
							return skillid;
						}
					}
				}
			}
		}
		return -1;
	}

	//获得绝技id
	public int getNBSkillid()
	{
		PropRole prole = new PropRole(roleId, false);
		int nNBSkillInborn = -1; 
		// 找到绝技id
		for(int skillindex = 0; skillindex < SkillConstant.MAX_INBORN_SKILL_COUNT;skillindex++)
		{
			int inbornId = prole.getSchool() * 100 + fire.pb.skill.Module.inbornSeq[skillindex];
			SInbornInfo sinborninfo = Module.getSInbornInfo(inbornId);
			if(sinborninfo != null && sinborninfo.getIsnbskill())				
			{
				nNBSkillInborn = sinborninfo.id;
				break;
			}
		}		
		return nNBSkillInborn;
	}
	
	//处理绝技--普通技能升级完后。查看有没有绝技符合条件
	public boolean DealNBSkill()
	{	
		PropRole prole = new PropRole(roleId, false);
		int nNBSkillInborn = 0; 
		int nMaxNBSkilllv = 0;
		// 找到绝技id
		for(int skillindex = 0; skillindex < SkillConstant.MAX_INBORN_SKILL_COUNT;skillindex++)
		{
			int inbornId = prole.getSchool() * 100 + fire.pb.skill.Module.inbornSeq[skillindex];
			SInbornInfo sinborninfo = Module.getSInbornInfo(inbornId);
			if(sinborninfo != null && sinborninfo.getIsnbskill())				
			{
				nNBSkillInborn = sinborninfo.id;
				nMaxNBSkilllv = sinborninfo.maxLevel;
				break;
			}
		}			
		
		if(nNBSkillInborn>0)
		{			
			InbornConfig sinbornconfig = Module.getInbornConfig(nNBSkillInborn);
			if(sinbornconfig == null||sinbornconfig.getNbskilldependLevels().size() < 1)
				return false;
			//初始最小等级
			int nMinLevel = sinbornconfig.getNbskilldependLevels().get(sinbornconfig.getNbskilldependLevels().size()-1);
			//需要的最小等级
			int nMinneedLevel = sinbornconfig.getNbskilldependLevels().get(0);			
			if(sinbornconfig.getDependInborns().size() > 0)
			{
				// 遍历所有依赖技能，找其中等级最低的技能。
				for(int inbornid :sinbornconfig.getDependInborns())
				{
					Integer dependlevel = srole.getInborns().get(inbornid);
					if(dependlevel==null||dependlevel < nMinneedLevel )//如果比所需最小等级都低。后面啥判断都不需要了
						return false;
					if(dependlevel < nMinLevel)
						nMinLevel  = dependlevel;
				}
				int nbskillleve = 0;
				for(int dependLevel :sinbornconfig.getNbskilldependLevels())
				{
					if(nMinLevel >= dependLevel&& nbskillleve < nMaxNBSkilllv)
						nbskillleve++;
				}
				if(nbskillleve > 0)
				{
					int oldlevel = 0;					
					if(srole.getInborns().get(nNBSkillInborn) != null)
						oldlevel = srole.getInborns().get(nNBSkillInborn).intValue();
					if(nbskillleve > oldlevel)
					{
						updateInbornWithOutSP(nNBSkillInborn, nbskillleve - oldlevel);
						fire.log.YYLogger.roleSkillUPLog(roleId, new RoleSkillUpBean(nNBSkillInborn, nbskillleve));
						return true;
					}
				}
			}
		}
		return false;
	}	
	
	//技能升级,包括发送协议
	public boolean updateInbornWithSP(int inbornId)
	{	
		//升级职业技能，刷新发消息
		Integer oldlevel = srole.getInborns().get(inbornId);
		if(oldlevel == null)
			oldlevel = 0;
		int newlevel = oldlevel + 1;
		srole.getInborns().put(inbornId, newlevel);
		
		SInbornInfo sinborninfo = Module.getSInbornInfo(inbornId);
		if(sinborninfo != null)
		{
			for(int skillid : sinborninfo.getPointToSkillList())
			{
				if(skillid > 0 )
				{
					FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillid);
					if (sconf == null)
						continue ;
					if (sconf.getAddautoSkill() == 1) {
						updateRoleSkillChange(skillid);
					}
				}
			}
		}			

		SUpdateInborn sUpdateInborn = new SUpdateInborn();
		sUpdateInborn.flag=0;
		sUpdateInborn.inborns.put(inbornId, newlevel);
		Transaction.tsendWhileCommit(roleId, sUpdateInborn);
		
		Result result = addSkillBaseBuff();
		if(result.isSuccess())//通知属性改动
			Transaction.tsendWhileCommit(roleId,new SRefreshRoleData((HashMap<Integer, Float>)result.getChangedAttrs()));
		return true;
	}
	
	//技能升级
	public Result updateInbornWithOutSP(int inbornId, int naddlv){
		//升级职业技能，刷新发消息
		Integer oldlevel = srole.getInborns().get(inbornId);
		if(oldlevel == null)
			oldlevel = 0;
		int newlevel = oldlevel + naddlv;
		srole.getInborns().put(inbornId, newlevel);
		if(newlevel == 1)
		{
			SInbornInfo sinborninfo = Module.getSInbornInfo(inbornId);
			if(sinborninfo != null)
			{
				for(int skillid : sinborninfo.getPointToSkillList())
				{
					if(skillid > 0 )
					{
						FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillid);
						if (sconf == null)
							continue ;
						if (sconf.getAddautoSkill() == 1) {
							//添加到自动技能
							updateRoleSkillChange(skillid);
						}
					}
				}						
			}
		}
		Result result = addSkillBaseBuff();
		return result;
	}
	
	//增加人物技能的熟练度，只对辅助技能有效
	public long addExp(int skillId, long exp)
	{
		xbean.AssistSkill assistSkill = srole.getAssistskills().get(skillId);
		if(assistSkill != null)
		{
			assistSkill.setExp(assistSkill.getExp() + exp);
			logger.debug("Role (id="+roleId +") added assist skill(id="+ skillId+") exp("+exp +").");
			SUpdateAssistSkill supdate = new SUpdateAssistSkill();
			supdate.assistskill.id = skillId;
			supdate.assistskill.level = assistSkill.getLevel();
			supdate.assistskill.exp = (int)assistSkill.getExp();
			mkdb.Procedure.psendWhileCommit(roleId, supdate);
			return assistSkill.getExp();
		}
		else
			return 0;
	}
	
	//添加特殊技能
	public void addSpecialSkillWithSP(int equiptype, List<Integer> skillIds)
	{
		//先移除以前该装备拥有的特技特效
		if (skillIds != null)
		{
			// 再加入新的特技特效
			for (int skillId : skillIds)
			{
					xbean.SpecialSkill speskill = xbean.Pod.newSpecialSkill();
					speskill.setEquiptype(equiptype);
					speskill.setSkillid(skillId);
					srole.getSpecialskills().add(speskill);
			}
		}
		sendSpecialSkills();
	}
	
	//删除特殊技能
	public void removeSpecialSkillWithSP(int equiptype)
	{
		if(removeSpecialSkill(equiptype))
			sendSpecialSkills();
	}
	
	//删除特殊技能
	public boolean removeSpecialSkill(int equiptype)
	{
		boolean removed = false;
		BuffAgent agent = new BuffRoleImpl(roleId);
		//先移除以前该装备拥有的特技特效
		for(Iterator<xbean.SpecialSkill> it = srole.getSpecialskills().iterator();it.hasNext();)
		{	
			xbean.SpecialSkill speskill = it.next();
			if(speskill.getEquiptype() == equiptype)
			{
				it.remove();				
				List<BuffUnit> buffArgs = null;
				try {
					FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(speskill.getSkillid());
					if (sconf != null) {
						if (sconf.isUseInBattle()&& sconf.getSubSkills()[0] != null&& sconf.getSubSkills()[0].getBuffUnits()[0] != null) {
							continue;
						}
					}
					
					buffArgs = fire.pb.item.Module.getItemBuffs(speskill.getSkillid());
					if (buffArgs == null || buffArgs.size() < 1)
						continue;
					
					for (BuffUnit buffarg : buffArgs) {
						agent.removeCBuffWithSP(buffarg.buffIndex);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				removed = true;
			}
		}
		return removed;
	}
	
	public void sendSpecialSkills()
	{
		//先取出所有特技列表，过滤掉重复的
		List<Integer> skillList = new ArrayList<Integer>();
		List<Integer> effectList = new ArrayList<Integer>();
		for(xbean.SpecialSkill speskill : srole.getSpecialskills())
		{
			if (!skillList.contains(speskill.getSkillid())) {
				FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(speskill.getSkillid());
				if (sconf == null)
					continue;
				if (sconf.isActiveSkill()) {
					skillList.add(speskill.getSkillid());
				}
				else {
					effectList.add(speskill.getSkillid());
				}
			}
		}
		
		//把特技列表发给客户端
		SSendSpecialSkills sSendSpecialSkills = new SSendSpecialSkills();
		for (Integer skillId : skillList) {
			sSendSpecialSkills.skills.add(skillId);
		}
		
		for (Integer effectid : effectList) {
			//添加特效
			sSendSpecialSkills.effects.add(effectid);
		}
		mkdb.Procedure.psendWhileCommit(roleId, sSendSpecialSkills);
	}
	
	
	//添加扩展技能
	public void addExtSkillWithSP(Map<Integer, Integer> expskillskill)
	{
		//先移除以前该装备拥有的特技特效
		removeExtSkill();
		for (Map.Entry<Integer, Integer> entry : expskillskill.entrySet())
		{
			srole.getExtskills().put(entry.getKey(), entry.getValue());
		}
		sendExtSkills();
	}
	
	//删除扩展技能
	public void removeExtSkillWithSP()
	{
		removeExtSkill();
		sendExtSkills();
	}
	
	
	//删除扩展技能
	public void removeExtSkill()
	{
		srole.getExtskills().clear();
	}
	
	public void sendExtSkills()
	{
		SUpdateExtSkill sextskill = new SUpdateExtSkill();
		for (Map.Entry<Integer, Integer> entry : srole.getExtskills().entrySet())
		{
			sextskill.extskilllists.put(entry.getKey(), entry.getValue());
		}
		mkdb.Procedure.psendWhileCommit(roleId, sextskill);
	}		
	
	//从多个地方扣除学习技能的花费（优先储备金-身上银币-银行银币）
	public static void costMoneyWithSP(PropRole prole ,Pack bag,Depot depot, long moneycost, Map<String, Object> param, YYLoggerTuJingEnum countertype, int xiangguanid )
	{
			if(moneycost <= bag.getMoney())
			{//身上银币够
				bag.subMoney(-moneycost, "Learn Skill", countertype, xiangguanid);
				param.put(RemoteLogParam.MONEYNEED, -moneycost);
				HashMap<Byte, Long> currency = new HashMap<Byte, Long>();
				currency.put((byte)MoneyType.MoneyType_SilverCoin, bag.getMoney());
				currency.put((byte)MoneyType.MoneyType_GoldCoin, bag.getGold());
				mkdb.Procedure.psendWhileCommit(prole.getRoleId(),new SRefreshCurrency(BagTypes.BAG, currency));
			}
			else
			{//身上银币不够
				if (bag.getMoney() != 0) {
					moneycost = moneycost - bag.getMoney();
					long bagmoneycost = bag.getMoney();
					// 刷新身上银币
					bag.subMoney(-bagmoneycost, "Learn Skill", countertype,
							xiangguanid);
					param.put(RemoteLogParam.MONEYNEED, -bagmoneycost);
					HashMap<Byte, Long> currency = new HashMap<Byte, Long>();
					currency.put((byte)MoneyType.MoneyType_SilverCoin, bag.getMoney());
					currency.put((byte)MoneyType.MoneyType_GoldCoin, bag.getGold());
					mkdb.Procedure.psendWhileCommit(
							prole.getRoleId(),
							new SRefreshCurrency(BagTypes.BAG, currency));
				}
				//刷新银行银币
				depot.addMoney(-moneycost, "Learn Skill", 1, YYLoggerTuJingEnum.tujing_Value_skillLevelup);
				param.put(RemoteLogParam.SAVINGNEED, -moneycost);
				HashMap<Byte, Long> currency = new HashMap<Byte, Long>();
				currency.put((byte)MoneyType.MoneyType_SilverCoin, bag.getMoney());
				currency.put((byte)MoneyType.MoneyType_GoldCoin, bag.getGold());
				mkdb.Procedure.psendWhileCommit(prole.getRoleId(),new SRefreshCurrency(BagTypes.DEPOT, currency));
			}
			
		if(param.get(RemoteLogParam.SMONEYNEED) == null)
			param.put(RemoteLogParam.SMONEYNEED, 0);
		if(param.get(RemoteLogParam.MONEYNEED) == null)
			param.put(RemoteLogParam.MONEYNEED, 0);
		if(param.get(RemoteLogParam.SAVINGNEED) == null)
			param.put(RemoteLogParam.SAVINGNEED, 0);
	}
	
	//上线时发送所有技能
	public void sendAllSkillsWhileOnline()
	{
		sendAllInborns();//技能
		sendExtSkills();
	}
	
	//发送所有已学会职业技能及等级
	public void sendAllInborns()
	{
		SSendInborns sSendInborns = new SSendInborns();
		sSendInborns.inborns.putAll(srole.getInborns());
		if(sSendInborns.inborns.size()!=0)
			mkdb.Procedure.psendWhileCommit(roleId,sSendInborns);
	}
	
	//加修炼技能里的被动技能BUFF by changhao	
	public Result addPracticleSkillBuff()
	{	
		Result result = new Result(true);
		FightJSEngine fightJSEngine = null;
		boolean binitJsEngine = false;

		BuffAgent buffpet = new BuffRoleImpl(roleId);
		
		java.util.List<Integer> skilllist = fire.pb.skill.liveskill.LiveSkillManager.getInstance().GetAllPracticeSkillBattleSkill(this, 2);
			
		//找出需要上线加载的buff
		for(int skillId : skilllist)		
		{
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				Module.logger.error("玩家的PracticleSkill为空，技能ID = " + skillId, new NullPointerException());
				continue;
			}
			
			if(!sconf.isActiveSkill() && sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
					&& sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
			{
				for (SubSkillConfig subskill : sconf.getSubSkills())
				{
					for (BuffUnit buffarg : subskill.getBuffUnits())
					{
						if (buffarg == null)
							continue;
						if(buffarg.buffIndex/10000 == 51)
							continue;
						ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);;
						if(binitJsEngine == false)
						{
							fightJSEngine = new FightJSEngine();
							binitJsEngine =true;
						}
						if(fightJSEngine!=null){
							int skilllv = getLevel(skillId);
							Module.logger.info("玩家:" + roleId + "加载PracticleSkill技能buff，ID = " + skillId+" 技能等级:"+skilllv);
							if(skilllv >20)
							{
								Module.logger.error("玩家:" + roleId + "的PracticleSkill技能 ID = " + skillId+" 技能等级:"+skilllv+"已经超出了最大等级限制20级。");
							}
							fightJSEngine.setSkillLevel(skilllv);	
						}
						if(buffarg.effectJavascriptMap.size() > 0)
						{
							for (Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
							{// buff效果
								buff.getEffects().put(entry.getKey(), entry.getValue().eval(fightJSEngine,null,null).floatValue());
							}
						}
						result.updateResult(buffpet.addCBuff(buff));
					}
				}
			}
		}
		return result;
	}
	
	//加修炼技能里的被动技能BUFF by lxh
	public Result addSpecialSkillBuff()
	{	
		Result result = new Result(true);
		FightJSEngine fightJSEngine = null;
		boolean binitJsEngine = false;
		BuffAgent buffpet = new BuffRoleImpl(roleId);		
		//找出需要上线加载的buff
		for(int skillId : getAllSpecialSkills(false,false,false,false))		
		{
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				Module.logger.error("玩家的SpecialSkill为空，技能ID = " + skillId, new NullPointerException());
				continue;
			}			
			if(!sconf.isActiveSkill() && sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
					&& sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
			{
				for (SubSkillConfig subskill : sconf.getSubSkills())
				{
					for (BuffUnit buffarg : subskill.getBuffUnits())
					{
						if (buffarg == null)
							continue;
						if(buffarg.buffIndex/10000 == 51)
							continue;
						ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);;
						if(binitJsEngine == false)
						{
							fightJSEngine = new FightJSEngine();
							binitJsEngine =true;
						}
						if(fightJSEngine!=null){
							int skilllv = getLevel(skillId);
							fightJSEngine.setSkillLevel(skilllv);	
						}
						if(buffarg.effectJavascriptMap.size() > 0)
						{
							for (Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
							{// buff效果
								buff.getEffects().put(entry.getKey(), entry.getValue().eval(fightJSEngine,null,null).floatValue());
							}
						}
						result.updateResult(buffpet.addCBuff(buff));
					}
				}
			}
		}
		return result;
	}
	//加生活技能里的被动技能BUFF by changhao	
	public Result addLiveSkillBuff()
	{
		ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_ASSIST_SKILL_EFFECTS);
		
		SkillRole skillrole = new SkillRole(roleId);
		FightJSEngine engine = null;
		boolean bInitjsEngine = false;
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> e : skillrole.getLiveSkills().entrySet())
		{
			fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(e.getKey());
			
			if (config.skillId != 0) //这是被动技能 by changhao
			{
				int level = e.getValue().getLevel();
				
				if (level <= 0)
				{
					continue;
				}
				
				FightSkillConfig skillconf = Module.getInstance().getFightSkillConfig(config.skillId);
				if (skillconf == null)
				{
					continue;
				}
				
				if(skillconf.isActiveSkill())//是否是被动技能 by changhao
				{
					continue;
				}
				if(bInitjsEngine == false)
				{
					engine = new FightJSEngine();
					bInitjsEngine = true;
				}
				
				if(engine!=null){
					engine.setSkillLevel(level);//被动技能只跟skilllevel相关
				}
				
				try
				{
					for (SubSkillConfig subskill : skillconf.getSubSkills())
					{
						for (BuffUnit buffarg : subskill.getBuffUnits())
						{
							if(buffarg == null) continue;
							if(buffarg.effectJavascriptMap == null) continue;
							for (Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
							{
								buff.getEffects().put(entry.getKey(), entry.getValue().eval(engine,null,null).floatValue());
							}
						}
					}
				} catch (Exception ex)
				{
					logger.error("加载公会被动技能buff出错，技能ID：" + config.skillId, ex);
				}					
			}
		}
		
		if(buff.getEffects().size() != 0)
		{
			BuffAgent agent = new BuffRoleImpl(roleId);
			return agent.addCBuff(buff);
		}
		
		return new Result(false);		
	}
	
	//人物上线时，添加技能中被动技能的buff
	public Result addSkillBaseBuff()
	{
		ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_SKILL_BASE_EFFECTS);
		Integer schoolId = xtable.Properties.selectSchool(roleId);
		List<Integer> skillIds = Module.getInstance().getAllSkillIDsBySchool(schoolId);
		FightJSEngine engine = null;
		boolean bInitjsEngine = false;
		for(int skillId : skillIds)
		{
			FightSkillConfig skillconf = Module.getInstance().getFightSkillConfig(skillId);
			if(skillconf!=null&&!skillconf.isActiveSkill())//被动技能
			{
				
				int skillLevel = getSchoolSkillLevel(skillId);
				if(skillLevel <= 0)
					continue;//还没有学会这个被动技能
				if(bInitjsEngine == false)
				{
					engine = new FightJSEngine();
					bInitjsEngine = true;
				}
				if(engine!=null){
					engine.setSkillLevel(skillLevel);//被动技能只跟skilllevel相关
					engine.setJianshenLevel(getLevel(SkillConstant.ASSIST_SKILL_JIAN_SHEN));
					engine.setTunaLevel(getLevel(SkillConstant.ASSIST_SKILL_TU_NA));
				}
				try
				{
					for (SubSkillConfig subskill : skillconf.getSubSkills())
					{
						for (BuffUnit buffarg : subskill.getBuffUnits())
						{
							if(buffarg == null) continue;
							if(buffarg.effectJavascriptMap == null) continue;
							for (Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
							{
								buff.getEffects().put(entry.getKey(), entry.getValue().eval(engine,null,null).floatValue());
							}
						}
					}
				} catch (Exception ex)
				{
					logger.error("加载职业被动技能buff出错，技能ID：" + skillId, ex);
				}
			}
		}
		
		if(buff.getEffects().size() != 0)
		{
			BuffAgent agent = new BuffRoleImpl(roleId);
			return agent.addCBuff(buff);
		}
		return new Result(false);
	}
	
	//不通知客户端去除buff(如：人物下线时)
	public void removeSkillBuff()
	{
		BuffAgent agent = new BuffRoleImpl(roleId);
		agent.removeCBuff(BuffConstant.CONTINUAL_SKILL_BASE_EFFECTS);
		agent.removeCBuff(BuffConstant.CONTINUAL_ASSIST_SKILL_EFFECTS);
	}

	public xbean.SkillRole getSRoleBean()
	{
		return srole;
	}

	public void setSRoleBean(xbean.SkillRole sbrole)
	{
		this.srole = sbrole;
	}

	//检查生效的技能
	@Override
	public boolean hasEffectSkill(int skillId)
	{
		return hasSkill(skillId);
	}
	//检查所有skillId，将来也包括生活技能
	@Override
	public boolean hasSkill(int skillId)
	{
		int skillLevel = getLevel(skillId);
		if(skillId>=100000 && skillId < 110000)
			return skillLevel > 0;
		if(skillId / 100000 == SkillConstant.TYPE_ROLE_SCHOOL_SKILL)
		{
			SSkillInfoConfig sskilllevelcfg = ConfigManager.getInstance().getConf(SSkillInfoConfig.class).get(skillId);
			if(sskilllevelcfg == null)
				return false;
			int skillRealLevel = 0;
			for(int skilldoorlevel : sskilllevelcfg.getSkillLevelRequireList())
			{
				if(skillLevel >= skilldoorlevel)
					skillRealLevel++;
				else
					break;
			}
			return skillRealLevel > 0;
		}
		else
			return skillLevel > 0;
	}

	//返回指定技能的等级，包括人物职业技能，人物辅助技能
	@Override
	public int getLevel(int skillId)
	{
		Integer extSkillLevel = srole.getExtskills().get(skillId);
		if(extSkillLevel != null)
			return extSkillLevel;
		if(skillId < 10000)
		{
			//技能ID
			Integer level = getInbornLevel(skillId);
			if(level != null)//有可能出现在extskill
				return level;
		}
		else if(skillId>=100000 && skillId < 110000)
		{
			BasicSkillBelongs user = SkillConstant.basicSkillUsers.get(skillId);
			if(user == null)
				return 0;
			if(user.isBelongToRole())
			{
				return 1;
			}
			else 
				return 0;
		}
		else
		{
			//技能ID
			int skillType = skillId / 100000;

			switch (skillType)
			{
			case SkillConstant.TYPE_ROLE_SCHOOL_SKILL:
				return getSchoolSkillLevel(skillId);
			case SkillConstant.TYPE_ROLE_ASSIST_SKILL:
				xbean.AssistSkill assistSkill = getAssistSkill(skillId);
				if (assistSkill != null)
					return assistSkill.getLevel();
				else
					return 0;
			case SkillConstant.TYPE_ROLE_SPECIAL_SKILL:
				if(getSpecialSkill(skillId)!= null)
					return 1;
				else
					return 0;
/*			case SkillConstant.TYPE_ROLE_OTHER_SKILL:
				Integer otherSkillLevel = srole.getOtherskills().get(skillId);
				if(otherSkillLevel != null)
					return otherSkillLevel;
				else
					return 0;*/
			case SkillConstant.TYPE_ROLE_PRACTICE_SKILL://人物修炼技能
				
				int id = LiveSkillManager.getInstance().GetPracticePassivenessSkillLevelBattleSkill(skillId);
				PracticeSkill skill = srole.getPracticeskill().get(id);
				if (skill != null)
				{
					return skill.getLevel();
				}
				else
				{
					return 0;
				}				
			}
		}
		
		return 0;
	}
	
	//获取一个特殊技能或特效
	public xbean.SpecialSkill getSpecialSkill(int skillId)
	{
		for(xbean.SpecialSkill speskill : srole.getSpecialskills())
		{
			if(speskill.getSkillid() == skillId)
				return speskill;
		}
		return null;
	}
	
	//获取辅助技能的熟练度
	public long getExp(int skillId)
	{
		xbean.AssistSkill assistSkill = srole.getAssistskills().get(skillId);
		if (assistSkill != null)
		{
			return assistSkill.getExp();
		} else
			return 0;
	}
	
	//现在只有职业技能，将来可能还会有特技
	@Override
	public List<Integer> getAllBattleSkills()
	{
		List<Integer> skillIds = new ArrayList<Integer>();
		int schoolId = xtable.Properties.selectSchool(roleId);
		List<Integer> allskillIds = Module.getInstance().getAllSkillIDsBySchool(schoolId);
		
		for(int schoolskillId : allskillIds)
		{
			int skillLevel = getSchoolSkillLevel(schoolskillId);
			if(skillLevel >= 1)
				skillIds.add(schoolskillId);
		}
		
		//加入所有的修炼对应的被动技能 by changhao
		skillIds.addAll(fire.pb.skill.liveskill.LiveSkillManager.getInstance().GetAllPracticeSkillBattleSkill(this, 2));

		//加入所有的装备特技 by liuxinhua
		skillIds.addAll(getAllSpecialSkills(false,false,false,false));
		return skillIds;
	}
	
	//返回所有装备特技
	public List<Integer> getAllSpecialSkills(boolean isCheckActive, boolean isActive, boolean isCheckInBattle,boolean isInBattle)
	{
	
		List<Integer> skillList = new ArrayList<Integer>();
		for(xbean.SpecialSkill speskill : srole.getSpecialskills())
		{
			if (!skillList.contains(speskill.getSkillid())) {
				FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(speskill.getSkillid());
				if (sconf == null)
					continue;
				if (isCheckActive == true &&  isActive !=sconf.isActiveSkill())
					continue;
				if (isCheckInBattle == true &&  isInBattle !=sconf.isUseInBattle())
					continue;
				skillList.add(speskill.getSkillid());
			}
		}
		return skillList;
	}
		
	//检查多个技能，当所有都拥有时才返回true
	@Override
	public boolean hasAllSkills(int... skillIds)
	{
		for(int i = 0;i<skillIds.length;i++)
		{
			if(!hasSkill(skillIds[i]))
				return false;
		}
		return true;
	}
	
	//检查多个技能，当拥有一个就返回true
	@Override
	public boolean hasAnySkill(int... skillIds)
	{
		for(int i = 0;i<skillIds.length;i++)
		{
			if(hasSkill(skillIds[i]))
				return true;
		}
		return false;
	}

	public Map<Integer,Integer> getSkillSequence()
	{
		return srole.getSkillsequence();
	}
	
	public void setSkillSequence(Map<Integer,Integer> sequence)
	{
		srole.getSkillsequence().clear();
		srole.getSkillsequence().putAll(sequence);
	}
	
	public xbean.AssistSkill getAssistSkill(int skillId)
	{
		return srole.getAssistskills().get(skillId);
	}
	
	public Map<Integer,xbean.AssistSkill> getAssistSkills()
	{
		return srole.getAssistskills();
	}
	
	//返回某个修炼技能信息 by changhao
	public xbean.PracticeSkill getPracticeSkill(int skillid)
	{
		return srole.getPracticeskill().get(skillid);
	}
	
	//得到所有的修炼技能信息 by changhao
	public Map<Integer,xbean.PracticeSkill> getPracticeSkills()
	{
		return srole.getPracticeskill();
	}
	
	//获取生活技能
	public Map<Integer,xbean.LiveSkill> getLiveSkills()
	{
		return srole.getLiveskill();
	}
	
	//返回职业职业的等级
	public int getSchoolSkillLevel(int skillId)
	{
		SInbornEffectSkill sInbornEffectSkill = Module.getSInbornEffectSkill(skillId);
		if(sInbornEffectSkill == null)
			return 0;
		double skillLevel = sInbornEffectSkill.getInitEffect();
		for(InbornEffect apeffect : sInbornEffectSkill.getInborns())
		{
			Integer aplevel = srole.getInborns().get(apeffect.inbornId);
			if(aplevel != null && aplevel > 0)
				skillLevel += aplevel * apeffect.effect;
		}
		return (int)skillLevel;
	}
	//返回职业职业的ID
	public int getSchoolSkillID(int inbornId)
	{
		SInbornInfo sinborninfo = Module.getSInbornInfo(inbornId);
		if(sinborninfo != null && sinborninfo.getPointToSkillList() != null && sinborninfo.getPointToSkillList().size() > 0)
		{
			return sinborninfo.getPointToSkillList().get(0);
		}
		return 0;
	}	

	@Override
	public Result addSkillBuffWhileBattle(BattleInfo battle, fire.pb.battle.Fighter ofighter)
	{
		BuffAgent buffpet = new BuffRoleImpl(this.roleId);
		Result result = new Result(true);
		List<Integer> skillIds = getAllBattleSkills();
		boolean bregfighter = false;
		for(int skillId : skillIds)
		{
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				if (logger.isDebugEnabled()) {logger.debug("FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
				continue;
			}
			if(!sconf.isActiveSkill() && sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
					&& sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
			{
				if(bregfighter == false)
				{
					battle.getEngine().setOpFighter(ofighter,sconf.getUsedAttrTypea());// 设置技能释放者参数
					bregfighter = true;
				}
				battle.getEngine().setSkillLevel(getLevel(skillId));
				
				for (SubSkillConfig subskill : sconf.getSubSkills())
				{
					for (BuffUnit buffarg : subskill.getBuffUnits())
					{
						if(buffarg==null)
							continue;
						ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);
						int nround = buffarg.roundJavascript.eval(battle.getEngine(),ofighter,null).intValue();
						if(nround != 0 )
							buff.setRound(nround);//被动战斗技能buff只有回合，不设置时间和量
						for(Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
						{
							buff.getEffects().put(entry.getKey(), entry.getValue().eval(battle.getEngine(),ofighter,null).floatValue());
						}
						result.updateResult(buffpet.addCBuff(buff));
					}
				}				
			}				
		}
		return result;
	}
	//添加职业被动技能buff和辅助被动技能buff
	@Override
	public Result addSkillBuffWhileOnline(xbean.BattleInfo battle)
	{
		Result result = new Result(true); 
		result.updateResult(addSkillBaseBuff());
		result.updateResult(addLiveSkillBuff());
		result.updateResult(addSpecialSkillBuff());
		Module.logger.info("玩家:" + roleId + "加载上线时加载PracticleSkill技能");
		result.updateResult(addPracticleSkillBuff());
		
		return result;
	}
	
	//获得最高的辅助技能等级
	public int getHighestAssistSkillLevel()
	{
		int hlevel = 0;
		for (Map.Entry<Integer, xbean.AssistSkill> entry : srole.getAssistskills().entrySet())
		{
			if(entry.getValue().getLevel() > hlevel)
				hlevel = entry.getValue().getLevel();
		}
		return hlevel;
	}
	
	//获取当前所有技能的总等级
	public int getInbornLevelSum()
	{
		int aplevelsum = 0;
		for(int aplevel : getSRoleBean().getInborns().values())
			aplevelsum +=aplevel;
		return aplevelsum;
	}
	
	//获取当前所有技能的最低等级
	public int getInbornMinLevel() {
		int apminlevel = DataInit.ROLE_LEVEL_MAX;
		int nnbskillid = getNBSkillid();
		for (Map.Entry<Integer, Integer> app : getSRoleBean().getInborns().entrySet()) {
			if (app.getKey() == nnbskillid)
				continue;
			if (app.getValue() < apminlevel)
				apminlevel = app.getValue();
		}
		return apminlevel;
	}
	
	//获取职业技能评分
	public int getInbornScore() {
		Double skillScoreXisu = fire.pb.scoremanager.Module.getInstance()
				.getCoe(ScoreType.SCHOOL_SKILL);
		
		Double score = getInbornLevelSum() * skillScoreXisu;
		return score.intValue();
	}
	
	//获取强体和冥想评分
	public int getLiveSkillScore() {
		Double skillScoreXisu = fire.pb.scoremanager.Module.getInstance()
				.getCoe(ScoreType.GUILD_SKILL);
		
		int nLevel = 0;
		if (getSRoleBean().getLiveskill().containsKey(320103)) {
			nLevel += getSRoleBean().getLiveskill().get(320103).getLevel();
		}
		if (getSRoleBean().getLiveskill().containsKey(320104)) {
			nLevel += getSRoleBean().getLiveskill().get(320104).getLevel();
		}
		Double score = skillScoreXisu * nLevel;
		return score.intValue();
	}
	
	//获取公会修炼评分
	public int getPracticeskillScore() {
		Double skillScoreXisu = fire.pb.scoremanager.Module.getInstance()
				.getCoe(ScoreType.GUILD_SHAVE);
		
		int nLevel = 0;
		
		for (java.util.Map.Entry<Integer, xbean.PracticeSkill> e : getSRoleBean().getPracticeskill().entrySet())
		{
			nLevel += e.getValue().getLevel();
		}		
		
		Double score = skillScoreXisu * nLevel;
		return score.intValue();
	}
	
	
	public boolean checkhaveskillcanupgrade()
	{
		//验证职业合法
		PropRole prole = new PropRole(roleId, false);

		for(int skillindex = 0; skillindex < SkillConstant.MAX_INBORN_SKILL_COUNT;skillindex++)
		{
			int inbornId = prole.getSchool() * 100 + fire.pb.skill.Module.inbornSeq[skillindex];
			SInbornInfo sinborninfo = Module.getSInbornInfo(inbornId);
			InbornConfig sinbornconfig = Module.getInbornConfig(inbornId);
			if(sinborninfo == null||sinbornconfig == null || sinborninfo.getIsnbskill())//绝技等普通技能都完鸟后处理。)
				continue;			

			//验证技能是否冲满
			int curLevel = 0;
			Integer curlevel2 = getInbornLevel(inbornId);
			if(curlevel2 != null) {
			   curLevel = curlevel2.intValue();	
			}
			
			if(curLevel == sinborninfo.getMaxLevel()){
				continue;
			}
			
			//人物级别验证
			fire.pb.skill.SInbornMaxLevel sInbornMaxLevel = Module.getSInbornMaxLevel(prole.getLevel());
			if(sInbornMaxLevel==null){
				continue;
			}
			
			int subInbornId = inbornId % 100;//技能ID的后两位，前两位标示职业
			int seq = -1;
			for(int i = 0;i<Module.inbornSeq.length;i++){
				if(Module.inbornSeq[i] == subInbornId){	
					seq = i;
					break;
				}
			}
			
			if(seq == -1){			
				continue;
			}
			
			Integer maxLevel = sInbornMaxLevel.getInbornMaxLevel().get(seq);
			if(maxLevel == null){
				continue;
			}
			
			int mainInbornId = (inbornId/10) * 10 + 1;
			Integer mainaplv = 0;
			if(mainInbornId != inbornId){
				mainaplv = getInbornLevel(mainInbornId);
				if(mainaplv == null || curLevel >= mainaplv){
					continue;//超过主技能等级
				}
			}
			
			if(curLevel + 1 > maxLevel)
			{
				continue;
			}
			
			//技能依赖检查
			if(curLevel == 0 && sinbornconfig.getDependInborns().size() > 0){
				boolean bDependLevelEnough = true;
				for(int dependInbornid : sinbornconfig.getDependInborns())
				{
					Integer lv = getInbornLevel(dependInbornid);
					if(lv == null || lv < sinborninfo.getDependLevel())
					{
						bDependLevelEnough = false;
						break;
					}
				}
				if(bDependLevelEnough == false)
					continue;
			}

			int tempCurrentLevel = curLevel;
			
			int kk = 0;
			while(true && kk<50){
				if(mainInbornId != inbornId){
					if(tempCurrentLevel >= mainaplv){
						break;
					}
				}
				
				//当前技能等级不能大与角色等级
				if(tempCurrentLevel +1> maxLevel){
					break;
				}
				
				SInbornConsume consumes = Module.getSInbornConsume(tempCurrentLevel + 1);
				if(consumes == null){
					break;
				}
				
				tempCurrentLevel++;
				kk++;
			}
			
			if(tempCurrentLevel > curLevel){
				return true;
			}
		}
		return false;
	}	

	/**
	 * 转职改变职业技能
	 */
	public void changeSchool(int v) {
		// 拷贝一份技能信息
		Map<Integer, Integer> copySkillMap = new HashMap<Integer, Integer>();
		copySkillMap.putAll(srole.getInborns());
		srole.getInborns().clear();
		// 修改职业技能ID,保留技能等级
		for (Map.Entry<Integer, Integer> entry : copySkillMap.entrySet()) {
			srole.getInborns().put(entry.getKey() + v, entry.getValue());
		}
	}
}
