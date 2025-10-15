package fire.pb.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.RemoteLogParam;
import fire.log.beans.RoleSkillUpBean;
import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.SRefreshUserExp;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.talk.MessageMgr;

//技能一键升级
public class PInbornWithValidProc extends mkdb.Procedure{
	
	private final long roleId;
	private final int inbornId;
	
	public PInbornWithValidProc(long roleId, int id){
		this.roleId = roleId;
		this.inbornId = id;
	}

	@Override
	protected boolean process() throws Exception {
		SkillRole srole = new SkillRole(roleId);
		//验证技能升级的状态
		BuffAgent buffagent = new BuffRoleImpl(roleId);
		if(!buffagent.canAddBuff(OperateType.UPDATE_INBORN)){
			MessageMgr.psendMsgNotify(roleId, 150062, null);
			return false;
		}
		
		if(fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)){
			return false;
		}
		
		//验证职业
		PropRole prole = new PropRole(roleId, false);
		boolean bvalidated = AutoInborn();
		
		SUpdateInborn sUpdateInborn =  null;
		if(bvalidated == true)
		{
			sUpdateInborn = new SUpdateInborn();
			sUpdateInborn.flag = 1;
		}
		else
		{
			MessageMgr.psendMsgNotify(roleId, 141236, null);
			return false;
		}
		
		for(int skillindex = 0; skillindex < SkillConstant.MAX_INBORN_SKILL_COUNT;skillindex++)
		{
			int tempCurrentLevel = 0;
			int inbornId = prole.getSchool() * 100 + fire.pb.skill.Module.inbornSeq[skillindex];
			SInbornInfo sInbornInfo = Module.getSInbornInfo(inbornId);
			InbornConfig sInbornCfg = Module.getInbornConfig(inbornId);
			if(sInbornInfo == null||sInbornCfg == null || sInbornInfo.getIsnbskill())//绝技等普通技能都完鸟后处理。)
				continue;

			//技能是否已经升满
			Integer curlevel = srole.getInbornLevel(inbornId);
			if(curlevel == null) {
			   curlevel = 0;	
			}
			
			if(curlevel == sInbornInfo.getMaxLevel()){
				continue;
			}
			
			//人物级别验证
			fire.pb.skill.SInbornMaxLevel sInbornMaxLevel = Module.getSInbornMaxLevel(prole.getLevel());
			if(sInbornMaxLevel==null){
				continue;
			}
			
			int inbornSubId = inbornId % 100;//技能的后两位，前两位标示职业
			int seq = -1;
			for(int i = 0;i<Module.inbornSeq.length;i++){
				if(Module.inbornSeq[i] == inbornSubId){	
					seq = i;
					break;
				}
			}
			
			if(seq == -1){			
				continue;
			}
			
			Integer maxlevel = sInbornMaxLevel.getInbornMaxLevel().get(seq);
			if(maxlevel == null){
				continue;
			}
			
			int mainInbornId = (inbornId/10) * 10 + 1;
			Integer mainInbornLv = 0;
			if(mainInbornId != inbornId){
				mainInbornLv = srole.getInbornLevel(mainInbornId);
				if(mainInbornLv == null || curlevel >= mainInbornLv){
					continue;
				}
			}
			
			
			if(curlevel + 1 > maxlevel)
			{
				continue;
			}
			
			//技能依赖校验
			if(curlevel == 0 && sInbornCfg.getDependInborns().size() > 0)
			{
				boolean bDependLevelEnough = true;
				for(int dependInbornid : sInbornCfg.getDependInborns())
				{
					Integer lv = srole.getInbornLevel(dependInbornid);
					if(lv == null || lv < sInbornInfo.getDependLevel())
					{
						bDependLevelEnough = false;
						break;
					}
				}
				if(bDependLevelEnough == false)
					continue;
			}
			
			//经验校验
			SInbornConsume consumesTemp = Module.getSInbornConsume(curlevel + 1);
			if(consumesTemp == null){
				continue;
			}

			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId,false);
			if(sInbornInfo.getConsumerule() > 0)
			{
				InbornConsume consumeTemp = consumesTemp.consumes.get(sInbornInfo.getConsumerule()-1);
				if(consumeTemp != null)
				{
					if(consumeTemp.getExp() > prole.getCurExp()){
						if(bvalidated == false)
						{
							MessageMgr.psendMsgNotify(roleId, 141228, null);// 经验不够
							return false;
						}
						continue;
					}
					
					long moneycost = consumeTemp.getMoney();
					if(moneycost > ( bag.getMoney())){
						if(bvalidated == false)
						{
							MessageMgr.psendMsgNotify(roleId, 141227, null);// 金钱不够
							return false;
						}
						continue;
					}
				}
			}
			

			long totalMoney = bag.getMoney();
			long totalExp = prole.getCurExp();
			tempCurrentLevel = curlevel;
			
			long needMoney = 0;
			long needExp = 0;
			
			while(true)
			{
				if(mainInbornId != inbornId){
					if(tempCurrentLevel >= mainInbornLv){
						break;
					}
				}
				
				//当前学位等级不能大与角色等级
				if(tempCurrentLevel +1> maxlevel){
					break;
				}
				
				SInbornConsume consumes = Module.getSInbornConsume(tempCurrentLevel + 1);
				if(consumes == null){
					break;
				}

				if(sInbornInfo.getConsumerule() > 0)
				{
					InbornConsume consume = consumes.consumes.get(sInbornInfo.getConsumerule()-1);
					if(consume != null)
					{			
						needExp = needExp + consume.getExp();
						if(needExp > totalExp){
							needExp = needExp - consume.getExp();
							break;
						}
						
						needMoney = needMoney + consume.getMoney();
						if(needMoney > totalMoney){
							needExp = needExp - consume.getExp();
							needMoney = needMoney - consume.getMoney();
							break;
						}
					}
				}
				
				tempCurrentLevel++;
			}
			
			Result lastResult = null;
			
			Result result = srole.updateInbornWithOutSP(inbornId,tempCurrentLevel - curlevel);
			if(result != null && result.isSuccess()){
				lastResult = result;
				fire.log.YYLogger.roleSkillUPLog(roleId, new RoleSkillUpBean(inbornId, tempCurrentLevel));
			}

			fire.pb.event.Poster.getPoster().dispatchEvent( new fire.pb.event.UpdateInbornLevel( roleId, inbornId ) );
						
			if(tempCurrentLevel == curlevel.intValue()){
				continue;
			}
			
			if (bvalidated == true) {
				sUpdateInborn.inborns.put(inbornId, tempCurrentLevel);
				
				for (int skillindexY = 0; skillindexY < SkillConstant.MAX_INBORN_SKILL_COUNT; skillindexY ++)
				{
					if (skillindexY == skillindex)
						continue;
					
					int inbornIdY = prole.getSchool() * 100 + fire.pb.skill.Module.inbornSeq[skillindexY];
					Integer curlevelY = srole.getInbornLevel(inbornIdY);
					if (curlevelY != null)
						continue;
					
					InbornConfig sinbornconfigY = Module.getInbornConfig(inbornIdY);
					SInbornInfo sinborninfoY = Module.getSInbornInfo(inbornIdY);
					if (sinbornconfigY == null)
						continue;
					
					if (sinbornconfigY.getDependInborns().size() == 0)
						continue;
					
					if (!sinbornconfigY.getDependInborns().contains(inbornId))
						continue;
					
					Integer depLevel = sinborninfoY.getDependLevel();
					if (depLevel == null || depLevel == 0)
						continue;
					
					if (tempCurrentLevel >= depLevel) {
						if (!srole.getSRoleBean().getInborns().containsKey(inbornIdY))
							srole.getSRoleBean().getInborns().put(inbornIdY, 0);
					}
				}
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(RemoteLogParam.INBORNID,inbornId);
			param.put(RemoteLogParam.INBORNLEV,srole.getInbornLevel(inbornId));
			param.put(RemoteLogParam.EXPCHANGED,-needExp);
			
			//扣除银两
			fire.pb.item.Depot depot = new fire.pb.item.Depot(roleId,false);
			SkillRole.costMoneyWithSP(prole, bag, depot, needMoney, param,  fire.log.enums.YYLoggerTuJingEnum.tujing_Value_skillLevelup, 0 );
			
			//扣除经验
			if(needExp >0){
				new PAddExpProc(roleId, -needExp, false,PAddExpProc.UPDATE_AP,"").call();
			}

			if(lastResult != null &&  lastResult.isSuccess()){
				mkdb.Procedure.psendWhileCommit(roleId,new SRefreshRoleData((HashMap<Integer, Float>)lastResult.getChangedAttrs()));
			}
		}

		boolean nbskilllvup = srole.DealNBSkill();
		if (nbskilllvup) {
			int nnbskillid = srole.getNBSkillid();
			sUpdateInborn.inborns.put(nnbskillid, srole.getLevel(nnbskillid));
		}
		
		if(bvalidated == true)
		{
			if(sUpdateInborn.inborns.size()>0)
				mkdb.Procedure.psendWhileCommit(roleId, sUpdateInborn);
			/**/
			mkdb.Procedure.psendWhileCommit(roleId, new SRefreshUserExp(prole.getCurExp()));
	
			//MessageMgr.psendMsgNotify(roleId, 150531, null);// 一键升级成功
			
			if(nbskilllvup)
			{
				int nnbskillid =srole.getNBSkillid();
				int nnbskilllevel = srole.getLevel(nnbskillid);
				List<String> parameters = new ArrayList<String>();
				SInbornInfo depinborninfo = Module.getSInbornInfo(nnbskillid);
				parameters.add(depinborninfo.getSkillname());
				parameters.add(String.valueOf(nnbskilllevel));
				MessageMgr.psendMsgNotify(roleId, 150532, parameters);//绝技$parameter1$升到$parameter2$级
				
				fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.NB_SKILL_COURSE, nnbskillid, 0, nnbskilllevel);
			}
			//更新玩家综合实力排行榜
			mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(this.roleId));
			final int minLevel = srole.getInbornMinLevel();
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.SKILL_LEVELUP_COURSE, minLevel);
		}
		return true;
	}
	
	boolean AutoInborn()
	{
		
		SkillRole srole = new SkillRole(roleId);
		//职业校验
		PropRole prole = new PropRole(roleId, false);

		for(int skillindex = 0; skillindex < SkillConstant.MAX_INBORN_SKILL_COUNT;skillindex++)
		{
			int inbornId = prole.getSchool() * 100 + fire.pb.skill.Module.inbornSeq[skillindex];
			SInbornInfo sInbornInfo = Module.getSInbornInfo(inbornId);
			InbornConfig sInbornCfg = Module.getInbornConfig(inbornId);
			if(sInbornInfo == null||sInbornCfg == null || sInbornInfo.getIsnbskill())//绝技等普通技能都完鸟后处理。)
				continue;			

			//技能是否学满
			int curlevel = 0;
			Integer curlevel2 = srole.getInbornLevel(inbornId);
			if(curlevel2 != null) {
			   curlevel = curlevel2.intValue();	
			}
			
			if(curlevel == sInbornInfo.getMaxLevel()){
				continue;
			}
			
			//人物级别验证
			fire.pb.skill.SInbornMaxLevel sInbornMaxLevel = Module.getSInbornMaxLevel(prole.getLevel());
			if(sInbornMaxLevel==null){
				continue;
			}
			
			int inbornSubId = inbornId % 100;
			int seq = -1;
			for(int i = 0;i<Module.inbornSeq.length;i++){
				if(Module.inbornSeq[i] == inbornSubId){	
					seq = i;
					break;
				}
			}
			
			if(seq == -1){			
				continue;
			}
			
			Integer maxlevel = sInbornMaxLevel.getInbornMaxLevel().get(seq);
			if(maxlevel == null){
				continue;
			}
			
			int mainInbornId = (inbornId/10) * 10 + 1;
			Integer mainInbornLv = 0;
			if(mainInbornId != inbornId){
				mainInbornLv = srole.getInbornLevel(mainInbornId);
				if(mainInbornLv == null || curlevel >= mainInbornLv){
					continue;
				}
			}
			
			if(curlevel + 1 > maxlevel)
			{
				continue;
			}
			
			//技能依赖校验
			if(curlevel == 0 && sInbornCfg.getDependInborns().size() > 0){
				boolean bDependLevelEnough = true;
				for(int dependInbornid : sInbornCfg.getDependInborns())
				{
					Integer lv = srole.getInbornLevel(dependInbornid);
					if(lv == null || lv < sInbornInfo.getDependLevel())
					{
						bDependLevelEnough = false;
						break;
					}
				}
				if(bDependLevelEnough == false)
					continue;
			}

			//经验校验
			SInbornConsume consumesTemp = Module.getSInbornConsume(curlevel + 1);
			if(consumesTemp == null){
				continue;
			}
			
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId,false);
			if(sInbornInfo.getConsumerule() > 0)
			{
				InbornConsume consumeTemp = consumesTemp.consumes.get(sInbornInfo.getConsumerule()-1);
				if(consumeTemp != null)
				{			
					if(consumeTemp.getExp() > prole.getCurExp()){
						continue;
					}
					
					long moneycost = consumeTemp.getMoney();
					if(moneycost > (bag.getMoney())){
						continue;
					}
				}
			}
			
			long totalMoney = bag.getMoney();
			long totalExp = prole.getCurExp();
			int tempCurrentLevel = curlevel;
			
			long needMoney = 0;
			long needExp = 0;
			
			int kk = 0;
			//Result lastResult = null;
			while(true && kk<50){
				if(mainInbornId != inbornId){
					if(tempCurrentLevel >= mainInbornLv){
						break;
					}
				}
				
				//当前技能等级不能大与角色等级
				if(tempCurrentLevel +1> maxlevel){
					break;
				}
				
				SInbornConsume consumes = Module.getSInbornConsume(tempCurrentLevel + 1);
				if(consumes == null){
					break;
				}
				
				InbornConsume consume = consumes.consumes.get(sInbornInfo.getConsumerule()-1);
				if(consume != null)
				{			
					needExp = needExp + consume.getExp();
					if(needExp > totalExp){
						needExp = needExp - consume.getExp();
						break;
					}
					
					needMoney = needMoney + consume.getMoney();
					if(needMoney > totalMoney){
						needExp = needExp - consume.getExp();
						needMoney = needMoney - consume.getMoney();
						break;
					}
				}				
				
				tempCurrentLevel++;
				kk++;
			}
			
			if(tempCurrentLevel > curlevel){
				return true;
			}
		}
		return false;
	}
}
