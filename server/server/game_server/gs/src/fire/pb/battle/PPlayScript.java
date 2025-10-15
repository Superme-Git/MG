package fire.pb.battle;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.map.MapConfig;
import fire.pb.talk.ChannelType;
import fire.pb.talk.STransChatMessage2Client;
import mkdb.Procedure;
import xbean.BattleInfo;

public class PPlayScript extends Procedure {
	private final long battleID;
	
	public PPlayScript(final long battleID){
		this.battleID = battleID;
	}
	
	/**
	 * 获取本回合剧本
	 * @param battle
	 * @return
	 */
	java.util.List<ResultItem> getScript(final xbean.BattleInfo battle, final List<fire.pb.battle.AIOperation> clientAIActions)
	{
		long nowTime = System.currentTimeMillis();
		BattleField.logger.debug(new StringBuilder("战场号为：").append(battle.getBattleid()).append("的战斗计算开始"));
		java.util.List<ResultItem> script = BattleScriptProcessor.getScript(battle, clientAIActions);
		BattleField.logger.debug(new StringBuilder("战场号为：").append(battle.getBattleid()).append("的战斗计算结束"));
		long nowTime1 = System.currentTimeMillis() - nowTime;
		if(battle.getRound() > 1 && nowTime1>50)
			BattleField.logger.error("战斗计算时间有点超长，花费时间为:"+nowTime1+"毫秒");
		printScript(script,battle);// FIXME print for test only
		printAI(clientAIActions,battle);// FIXME print for test only
		return script;
	}
	
	/**
	 * 打印脚本，测试用
	 */
	private void printScript(java.util.List<ResultItem> script,xbean.BattleInfo battle )
	{
		if(!BattleField.logger.isDebugEnabled())
			return;
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.addAll(battle.getRoleids().keySet());
		for(Iterator<Long> it = roleIds.iterator();it.hasNext();)
		{
			long roleId = it.next();
			xbean.DebugFlag debugflag = xtable.Debugflag.get(roleId);
			if(debugflag == null || !debugflag.getDebugs().contains(xbean.DebugFlag.BATTLE))
				it.remove();
		}
		if(roleIds.isEmpty() && !BattleField.logger.isDebugEnabled())
			return;
		StringBuilder scriptinfo = new StringBuilder("\n 角色：");
		for(long roleId : battle.getRoleids().keySet())
		{
			String name = xtable.Properties.selectRolename(roleId);
			scriptinfo.append(name).append(" ; ");
		}
		scriptinfo.append(" 的战斗脚本如下：\n");
		scriptinfo.append("-------------------------第 ").append(battle.getRound()).append(" 回合战斗脚本 Begin: --------------------------\n");
			
		
		int i = 0;
		for(ResultItem item : script)
		{
			i++;
			scriptinfo.append("********第  ").append(i).append("个出手者脚本:**********: \n");
			scriptinfo.append("---attackerid: ").append(item.execute.attackerid).append("\n");
			if(item.execute.hpconsume != 0)
				scriptinfo.append("---hpconsume: ").append(item.execute.hpconsume).append("\n");
			if(item.execute.mpconsume != 0)
				scriptinfo.append("---mpconsume: ").append(item.execute.mpconsume).append("\n");
			if(item.execute.spconsume != 0)
				scriptinfo.append("---spconsume: ").append(item.execute.spconsume).append("\n");
			scriptinfo.append("---operationid: ").append(item.execute.operationid).append("\n");
			scriptinfo.append("---operationtype: ").append(item.execute.operationtype).append("\n");
			if(item.newfighter.size() != 0)
				scriptinfo.append("---newfighters num: ").append(item.newfighter.size()).append("\n");
			if(item.rolechangedattrs.size() != 0)
				scriptinfo.append("---rolechangedattrs: ").append(item.rolechangedattrs).append("\n");
			if(item.petchangedattrs.size() != 0)
				scriptinfo.append("---petchangedattrs: ").append(item.petchangedattrs).append("\n");
			if(item.execute.demobuffs.size() != 0)
				scriptinfo.append("---demobuffs: ").append(item.execute.demobuffs).append("\n");
			for(SubResultItem subresult : item.subresultlist)
			{
				scriptinfo.append("---subskillid: ").append(subresult.subskillid).append("\n");
				scriptinfo.append("---subskillstarttime: ").append(subresult.subskillstarttime).append("\n");
				scriptinfo.append("======结果列表：======\n");
				for(DemoResult result : subresult.resultlist)
				{
					scriptinfo.append("------resulttype: ").append(result.resulttype).append("\n");
					scriptinfo.append("------targetid: ").append(result.targetid).append("\n");
					scriptinfo.append("------flagtype: ").append(result.flagtype).append("\n");
					if(result.hpchange != 0)
						scriptinfo.append("------hpchange: ").append(result.hpchange).append("\n");
					if(result.uplimithpchange != 0)
						scriptinfo.append("------uplimithpchange: ").append(result.uplimithpchange).append("\n");
					if(result.mpchange != 0)
						scriptinfo.append("------mpchange: ").append(result.mpchange).append("\n");
					if(result.spchange != 0)
						scriptinfo.append("------spchange: ").append(result.spchange).append("\n");
					if(result.epchange != 0)
						scriptinfo.append("------epchange: ").append(result.epchange).append("\n");
					if(result.shapechange != 0)
						scriptinfo.append("------shapechange: ").append(result.shapechange).append("\n");
					if(result.targetresult != 0)
						scriptinfo.append("------targetresult: ").append(result.targetresult).append("\n");
					if(result.returnhurt != 0)
						scriptinfo.append("------returnhurt: ").append(result.returnhurt).append("\n");
					if(result.attackback != 0)
						scriptinfo.append("------attackback: ").append(result.attackback).append("\n");
					if(result.stealhp != 0)
						scriptinfo.append("------stealhp: ").append(result.stealhp).append("\n");
					if(result.godblesshp != 0)
						scriptinfo.append("------godblesshp: ").append(result.godblesshp).append("\n");					
					if(result.attackerresult != 0)
						scriptinfo.append("------attackerResult: ").append(result.attackerresult ).append("\n");
					if(result.attackerulhpchange != 0)
						scriptinfo.append("------attackerulhpchange: ").append(result.attackerulhpchange).append("\n");  
					if(result.protecterid != 0)
						scriptinfo.append("------protecterID: ").append(result.protecterid).append("\n"); 
					if(result.protecterhpchange != 0)
						scriptinfo.append("------protecterhpchange: ").append(result.protecterhpchange).append("\n");  
					if(result.protecterresult != 0)
						scriptinfo.append("------protecterresult: ").append(result.protecterresult).append("\n");  
					if(result.protecterulhpchange != 0)
						scriptinfo.append("------protecterulhpchange: ").append(result.protecterulhpchange).append("\n");  
					if(result.assisterid != 0)
						scriptinfo.append("------assisterID: ").append(result.assisterid).append("\n");
					if(result.demobuffs.size() != 0)
						scriptinfo.append("------addedbuffs: ").append(result.demobuffs ).append("\n");
					scriptinfo.append("===========================\n");
				}
			}
			scriptinfo.append("********第  ").append(i).append("个出手者脚本结束**********: \n");
		}
		scriptinfo.append("----------------------第 ").append(battle.getRound()).append(" 回合战斗脚本 END: ---------------------- \n");
		
		BattleField.logger.debug(scriptinfo);
		

		String[] sendscripts = scriptinfo.toString().split("\n");
		for(String sendscript : sendscripts)
		{
			final STransChatMessage2Client snd = new STransChatMessage2Client();
			snd.messagetype = ChannelType.CHANNEL_SYSTEM;
			snd.message = sendscript;
			psend(roleIds, snd);
		}
	}
	
	private void printAI(List<fire.pb.battle.AIOperation> aiactions,xbean.BattleInfo battle )// FIXME print for test only
	{
		if(!BattleField.logger.isDebugEnabled())
			return;
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.addAll(battle.getRoleids().keySet());
		for(Iterator<Long> it = roleIds.iterator();it.hasNext();)
		{
			long roleId = it.next();
			xbean.DebugFlag debugflag = xtable.Debugflag.get(roleId);
			if(debugflag == null || !debugflag.getDebugs().contains(xbean.DebugFlag.BATTLE))
				it.remove();
		}
		if(roleIds.isEmpty())
			return;
		String aiinfo ="************第 "+battle.getRound()+" 回合AI脚本 Begin: ************\n";
		for(fire.pb.battle.AIOperation action : aiactions)
		{
			aiinfo = aiinfo + "*******************************\n";
			aiinfo = aiinfo + "---actionseq: "+ action.actionseq+ "\n";
			aiinfo = aiinfo + "---actionmoment: "+ action.actionmoment+ "\n";
			aiinfo = aiinfo + "---actionfighterid: "+ action.actionfighterid+ "\n";
			aiinfo = aiinfo + "---actionid: "+ action.actionid+ "\n";
			aiinfo = aiinfo + "*******************************\n";
		}
		aiinfo = aiinfo + "************第 "+battle.getRound()+" 回合AI脚本 END: ************\n";
		BattleField.logger.debug(aiinfo);
		
		String[] sendscripts = aiinfo.split("\n");
		for(String sendscript : sendscripts)
		{
			final STransChatMessage2Client snd = new STransChatMessage2Client();
			snd.messagetype = ChannelType.CHANNEL_CURRENT;
			snd.message = sendscript;
			psend(roleIds, snd);
		}
	}
	
	/**
	 * 向所有人发送剧本
	 * 每个人的剧本长度不同, 如果中途逃离，那么剧本只到自己逃跑就结束了
	 * @param battle
	 */
	private void sendScriptEveryOne(final xbean.BattleInfo battle, final java.util.List<ResultItem> script,final List<fire.pb.battle.AIOperation> aiactions){
		
		final java.util.Set<Long> who = new java.util.HashSet<Long>();
		who.addAll(battle.getRoleids().keySet());
		
		final SSendRoundScript snd = new SSendRoundScript();
		for (Map.Entry<Integer, Fighter> entry : battle.getFighterobjects().entrySet())
		{
			if(entry.getKey() > 0)
			{
				snd.fighterfinallyhps.put(entry.getKey(), entry.getValue().getEffectRole().getHp());
				snd.fighterfinallymps.put(entry.getKey(), entry.getValue().getEffectRole().getMp());
			}
		}
		
		snd.aiactions.addAll(aiactions);
		for(ResultItem item : script)
		{
			snd.playitem.add(BattleField.convertResultItem2New(item));
			
			final int index = item.execute.attackerid;
			if(BattleField.checkRunAway(battle, index) && item.execute.operationtype == OperationType.ACTION_ESCAPE )
			{
				long roleID = BattleField.getRoleIDByIndex(battle, index);
				if(0 != roleID){
					SSendRoundScript scriptcopy = copySSendScriptForRole(battle, snd, index);
					psendWhileCommit(roleID, scriptcopy);//给逃跑的人发送适合他的脚本
					// 逃跑的人,从发送全剧本的角色集合中删除
					who.remove(roleID);
				}
			}
		}
		
		//给每个人发送不同的脚本，包含他们自己的属性变化
		for(long roleId : who)
		{
			int rolefid = battle.getRoleids().get(roleId);
			SSendRoundScript scriptcopy = copySSendScriptForRole(battle, snd, rolefid);
			if(!battle.getOfflineroleids().contains(roleId)||fire.pb.hook.RoleHookManager.getInstance().getLockScreenStateByRoleId(roleId) == true){
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("发送脚本给roleid:").append(roleId));}
				psendWhileCommit(roleId, scriptcopy);
			}else {
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("角色掉线了,没有发送脚本.roleid:").append(roleId));}
			}
		}
		
		Collection<Long> audiences=battle.getAudience().keySet();
		//如果有观战者,那么给观战者也发送剧本
		if(audiences.size()>0){
			psendWhileCommit(audiences, snd);
		}
		
		processTrusteeshipRoles(battle);
		battle.getFighterchangedattrs().clear();
		BattleField.logger.debug("发送脚本完成");
	}
	
	private InstanceZone GetInstanceZone() {
		InstanceZone instzone = null;
		final Integer battletype = xtable.Battle.selectBattletype(battleID);
		if (battletype != null) {
			if (battletype == BattleType.BATTLE_CLANBOSS) {
				Long hostRoleid = xtable.Battle.selectHostroleid(battleID);
				if (hostRoleid == null)
					return instzone;				
				instzone = InstanceZoneFactory.getRoleCurInstanceZone(hostRoleid, true);
			}
		}
		return instzone;
	}
	
	private boolean CheckInstanceZone(InstanceZone instzone) {
		if (instzone == null)
			return false;
		
		if (!(instzone instanceof FactionInstZone))
			return false;
		return true;
	}
	
	public boolean process() {
		InstanceZone instzone = null;
		final Integer battletype = xtable.Battle.selectBattletype(battleID);
		if (battletype != null) {
			instzone = GetInstanceZone();
			if (battletype == BattleType.BATTLE_CLANBOSS) {
				if(!CheckInstanceZone(instzone))
					return false;
				
				if (instzone.getZoneState() == xbean.InstanceZone.STATE_CLOSING) {
					pexecuteWhileCommit(new PBattleEnd(battleID, BattleField.BATTLE_LOSE));
					return true;
				}
			}
		}
		
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if (null == battle)
			return false;
		
		if (battle.getPhase() != BattleInfo.PHASE_WAITOPERATION) {
			return false;
		}
		//锁所有角色的锁	
		long oldhp = 0;
		if (battle.getBattletype() == BattleType.BATTLE_CLANBOSS) {// 公会boss战。同步全局怪物血量
			
			if(!CheckInstanceZone(instzone))
				return false;
			
			if (((FactionInstZone) instzone).checkBattleid(battle.getConfigid())) {
				xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
				xbean.ClanBossInfo bossinfo = clanInst.getBossinfo().get(battle.getConfigid());				
				Fighter boss = battle.getFighterobjects().get(bossinfo.getFighteindx());
				if(boss!= null)
				{
					oldhp = bossinfo.getHp();
					boss.getEffectRole().setHp((int)bossinfo.getHp());
				}
				else
				{
					BattleField.logger.error("公会boss战获取boss对象出错,战斗id为:"+battle.getBattleid()+"    boss战斗站位为:"+bossinfo.getFighteindx() +
							",公会owner:" + instzone.getZoneBean().getOwnerid() + ",instzoneid:" + instzone.getZoneConfig().instZoneId + ",副本名字:" + instzone.getZoneConfig().name);
				}
			}
		}
		//锁队伍
		Set<Long> teamids = new HashSet<Long>(); 
		for(long rid :  battle.getRoleids().keySet())
		{
			Long tid = xtable.Roleid2teamid.select(rid);
			if(tid != null)
				teamids.add(tid);
		}
		lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, teamids));
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK,battle.getRoleids().keySet()));		
		//检查Fighter，主要是检查宠物的主人是否在战斗中
		BattleField.validFighters(battle);
		
		battle.setPhase(BattleInfo.PHASE_WAITFEEDBACK);		
		List<fire.pb.battle.AIOperation> clientAIActions = new LinkedList<fire.pb.battle.AIOperation>();//与战斗脚本匹配的客户端AI
		List<ResultItem> scripts = getScript(battle,clientAIActions);
		
		if (battle.getBattletype() == BattleType.BATTLE_CLANBOSS) {// 公会boss战。血量同步回全局怪物
			//副本正在关闭,结束boss战
			instzone = GetInstanceZone();			
			if(!CheckInstanceZone(instzone))
				return false;
			
			if (instzone.getZoneState() == xbean.InstanceZone.STATE_CLOSING) {
				pexecuteWhileCommit(new PBattleEnd(battleID, BattleField.BATTLE_LOSE));
				return true;
			}
			
			if (((FactionInstZone) instzone).checkBattleid(battle.getConfigid())) {
				xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
				xbean.ClanBossInfo bossinfo = clanInst.getBossinfo().get(battle.getConfigid());
				Fighter boss = battle.getFighterobjects().get(bossinfo.getFighteindx());
				if(boss!= null)
				{
					if (bossinfo.getHp() > 0) {
						long curhp = bossinfo.getHp();
						long changehp = oldhp-boss.getEffectRole().getHp();
						long hp = curhp- changehp;
						if (hp <= 0)
						{
							hp = 0;
							battle.setBattleresult(BattleField.BATTLE_WIN);
						}
						//给场景的角色同步血量
						pexecuteWhileCommit(new PSetBossHp(instzone.getLandkey(), 
								battle.getHostroleid(), 
								battle.getConfigid(), 
								changehp));
						
					} else {
						int result = BattleField.BATTLE_DRAW;
						pexecuteWhileCommit(new PBattleEnd(battleID, result));
						return true;
					}
				}
				else
				{
					BattleField.logger.error("公会boss战获取boss对象出错,战斗id为:"+battle.getBattleid()+"    boss战斗站位为:"+bossinfo.getFighteindx() +
							",公会owner:" + instzone.getZoneBean().getOwnerid() + ",instzoneid:" + instzone.getZoneConfig().instZoneId + ",副本名字:" + instzone.getZoneConfig().name);
					battle.setBattleresult(BattleField.BATTLE_DRAW);
				}
			}
		}
		//设置时间和最小播放时间
		battle.getItemtimelist().clear();
		int scripttime = ScriptTimeCalc.calcScriptTime(scripts,clientAIActions,battle.getItemtimelist());
//		scripttime = scripttime/4;
		if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("战斗\t"+battleID+"\t的脚本计算完毕");}
		logScriptTime(scripttime, battle); 

		List<Long> roleids = new LinkedList<Long>();
		for(Map.Entry<Long, Integer> entry : battle.getRoleids().entrySet())
		{
			long roleId = entry.getKey();
			if(!battle.getOfflineroleids().contains(roleId))
				roleids.add(roleId);
		}
		
		battle.setScriptplaytime((int)((roleids.size()<1?1.75:1)*ScriptTimeCalc.getMinScriptTime(scripttime)*battle.getGmactiontimetimes()/10.0));//+100000
		//if (BattleField.logger.isDebugEnabled()) {
			BattleField.logger.debug("战场号为："+battle.getBattleid()+"的战斗最小播放时长为:"+(int)(ScriptTimeCalc.getMinScriptTime(scripttime)*battle.getGmactiontimetimes()/10.0)+"毫秒");
		//	}"战场号为：").append(battle.getBattleid()
		//if (BattleField.logger.isDebugEnabled()) {
			BattleField.logger.debug("战场号为："+battle.getBattleid()+"的战斗最大播放时长为:"+(int)ScriptTimeCalc.getMaxScriptTime(scripttime)+"毫秒");
		//	}
		battle.setRoundtime(System.currentTimeMillis());
		//发送脚本给所有人
		sendScriptEveryOne(battle, scripts,clientAIActions);
		battle.setTimeout(mkdb.Mkdb.executor().schedule(new FeedbackCheck(battle.getBattleid(),battle.getRound()),
				ScriptTimeCalc.getMaxScriptTime(scripttime), TimeUnit.MILLISECONDS));
		//存入回合的demo
		final xbean.RoundResultItems rounditems = xbean.Pod.newRoundResultItems();
		for(ResultItem item : scripts)
		{
			rounditems.getResultitems().add(BattleField.convertResultItem2New(item));
		}
		rounditems.getAiactions().addAll(clientAIActions);
		for (Map.Entry<Integer, Fighter> entry : battle.getFighterobjects().entrySet())
		{
			if(entry.getKey() > 0)
			{
				rounditems.getFighterfinallyhps().put(entry.getKey(), entry.getValue().getEffectRole().getHp());
				rounditems.getFighterfinallymps().put(entry.getKey(), entry.getValue().getEffectRole().getMp());
			}
		}
		battle.getRoundresultitem().put(battle.getRound(),rounditems);
		
		return true;
	}
	
	private void logScriptTime(int scripttime, xbean.BattleInfo battle)
	{
		if(!BattleField.logger.isDebugEnabled())
			return;
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.addAll(battle.getRoleids().keySet());
		for(Iterator<Long> it = roleIds.iterator();it.hasNext();)
		{
			long roleId = it.next();
			xbean.DebugFlag debugflag = xtable.Debugflag.get(roleId);
			if(debugflag == null || !debugflag.getDebugs().contains(2))
				it.remove();
		}
		if(roleIds.isEmpty())
			return;
		String str = "战斗\t"+battleID+"\t的脚本计算完毕，标准时间" + scripttime+ ",动作时间 :"+ battle.getItemtimelist();
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.messagetype = ChannelType.CHANNEL_CURRENT;
		snd.message = str;
		psend(roleIds, snd);
	}
	
	private void processTrusteeshipRoles(final xbean.BattleInfo battle)
	{
		for(Map.Entry<Long, Integer> entry : battle.getRoleids().entrySet())
		{
			long roleId = entry.getKey();
			if(battle.getOfflineroleids().contains(roleId))
				pexecuteWhileCommit(new PRoundPLayEnd(roleId,new LinkedList<Integer>(),true));
		}
	}
	
	/**
	 * 中度拷贝脚本，并根据不同的角色填充属性变化
	 * 主要是为了半路逃跑播放半路脚本，脚本接收者不同属性变化不同
	 * @param snd
	 * @return
	 */
	private SSendRoundScript copySSendScriptForRole(xbean.BattleInfo battle, SSendRoundScript snd,int rolefid)
	{
		SSendRoundScript copy = new SSendRoundScript();
		copy.aiactions.addAll(snd.aiactions);
		ActionChangedAttrs roleActionAttrs  = battle.getFighterchangedattrs().get(rolefid);
		ActionChangedAttrs petActionAttrs  = battle.getFighterchangedattrs().get(rolefid+5);
		for(int i = 0;i<snd.playitem.size();i++)
		{
			NewResultItem resultItem = snd.playitem.get(i);
			NewResultItem copyitem = new NewResultItem();
			copyitem.execute = resultItem.execute;
			copyitem.newfighter = resultItem.newfighter; 
			for(NewSubResultItem subitem: resultItem.subresultlist)
			{
				NewSubResultItem copysubitem = new NewSubResultItem();
				copysubitem.subskillid = subitem.subskillid; 			
				copysubitem.subskillstarttime = subitem.subskillstarttime; 			
				copysubitem.resultlist = subitem.resultlist; 			
				copyitem.subresultlist.add(copysubitem);
			}
			if(roleActionAttrs!=null)
			{
				Map<Integer,Float> changedAttrs = roleActionAttrs.getChangedAttrs().get(i);
				if(changedAttrs!=null)
				{
					copyitem.rolechangedattrs = (HashMap<Integer,Float>)changedAttrs;
				}
			}
			if(petActionAttrs!=null && petActionAttrs.getChangedAttrs().containsKey(i))
			{
				Map<Integer,Float> changedAttrs = petActionAttrs.getChangedAttrs().get(i);
				if(changedAttrs!=null)
					copyitem.petchangedattrs = (HashMap<Integer,Float>)changedAttrs;
			}
			copy.playitem.add(copyitem);
		}
		//清除累积变化的属性值
		if(roleActionAttrs!=null )
			roleActionAttrs.getChangedAttrs().clear();
		if(petActionAttrs!=null )
			petActionAttrs.getChangedAttrs().clear();
		return copy;
	}
	
	
	public int getSctiptTime(ResultItem item)
	{
		switch(item.execute.operationtype)
		{
		case OperationType.ACTION_ATTACK: // 攻击
		case OperationType.ACTION_SKILL: // 使用技能
		case OperationType.ACTION_UNIQUE_SKILL: // 绝技
		case OperationType.ACTION_USEITEM: // 使用物品
		case OperationType.ACTION_DEFEND: // 防御
		case OperationType.ACTION_PROTECT: // 保护
		case OperationType.ACTION_SUMMON: // 召唤宠物
		case OperationType.ACTION_WITHDRAW: // 召还宠物
		case OperationType.ACTION_CATHCH: // 捕捉
		case OperationType.ACTION_ESCAPE: // 逃跑
		case OperationType.ACTION_REST: // 休息
			return 0;			
		case OperationType.ACTION_SPECIAL_SKILL: // 特殊技能
		case OperationType.ACTION_SUMMON_INSTANT: // 瞬时召唤
		case OperationType.ACTION_ESCAPE_INSTANT: // 瞬时逃跑
		case OperationType.ACTION_FAILURE: // 操作失败
		case OperationType.ACTION_FAILURE_NO_WONDER: // 操作失败
		case OperationType.ACTION_BATTLE_END: // 战斗结束,只有AI怪的AI指令有这个Action
		case OperationType.ACTION_ENVIRONMENTDEMO: // 不带施法者的Demo，attackID填0
		case OperationType.ACTION_ENVIRONMENTCHANGE: // 战场环境改变 operateid填战场环境id
		case OperationType.ACTION_ROUNDENDDEMO: // 回合末结算demo
			
		}
		return 1800;
	}
	
}
