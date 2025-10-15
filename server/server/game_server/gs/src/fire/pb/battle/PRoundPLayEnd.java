package fire.pb.battle;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mkdb.Procedure;
import fire.msp.role.GSetRoleBattleInfo;
import fire.pb.GsClient;
import fire.pb.attr.AttrType;
import fire.pb.battle.ai.BattleAIManager;
import fire.pb.effect.PRefreshRoleAttr;
import fire.pb.event.LeaveTeamSpecialQuestEvent;
import fire.pb.gm.GMInteface;
import fire.pb.pet.PSetFightPetProc;
import fire.pb.state.IState;
import fire.pb.state.State;
import fire.pb.state.StateManager;
import fire.pb.talk.ChannelType;
import fire.pb.talk.STransChatMessage2Client;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PRoundPLayEnd extends Procedure {
	
	private List<Integer> itemtimelist;
	boolean procroundend;
	private final long roleID;
	
	PRoundPLayEnd(final long roleID, List<Integer> itemtimelist, boolean procroundend){
		this.roleID = roleID;
		this.itemtimelist = itemtimelist;
		this.procroundend =procroundend;
	}
	
	public boolean process(){
		
		final Long battleID = xtable.Roleid2battleid.select(roleID);
		if(null == battleID) 
			return false;
		final Long teamId = xtable.Roleid2teamid.select(roleID);
		
		//锁battlelock
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if(null == battle)	
			return false;
		//锁teamlock
		if(teamId != null)
			lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, new Object[]{teamId}));
		
		//锁rolelock
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids().keySet()));
		
		if(battle.getPhase() != xbean.BattleInfo.PHASE_WAITFEEDBACK)
			return false;
		
		if(battle.getFeedbacks().contains(roleID))
			return false;
		
		final Integer rolefid = battle.getRoleids().get(roleID);
		if(null == rolefid)
			return false;
		int ntotal = 0;
		for(int itemtime :itemtimelist)
			ntotal += itemtime;
		
		if (BattleField.logger.isDebugEnabled()) {
			BattleField.logger.debug(new StringBuilder("战斗\t").append(battleID).append("\t收到来自\t" ).append(rolefid).append("\t的结束反馈，ItemTimeList：").append(itemtimelist).append("\t 总时长：").append(ntotal));
		}
		logScriptTime(battleID);
		
		if(BattleField.checkRunAway(battle, rolefid))
			processRoleRunaway(roleID, rolefid, battle);
		
		battle.getFeedbacks().add(roleID);
		List<Long> onlineids = new LinkedList<Long>();
		List<Long> offlineUnbackIds = new LinkedList<Long>();
		for(long rId : battle.getRoleids().keySet())
		{
			//if(StateManager.selectStateIdByRoleId(rId) == fire.pb.state.State.TRUSTEESHIP_STATE)
			if(battle.getOfflineroleids().contains(rId))
			{//有托管中的玩家
				if(battle.getFeedbacks().contains(rId))//如果已经收到反馈就不用统计了
					continue;
				offlineUnbackIds.add(rId);
			}
			else
				onlineids.add(rId);
		}
		psendWhileCommit(onlineids, new SSendRoundPlayEnd(rolefid));
		
		if(!procroundend)
			return true;
		
		/**
		 * 检查战斗的播放战斗脚本后的FeedBack是否已够，然后决定触发回合结束，执行PRoundEnd
		 * 触发回合结束必须满足以下情况：收到所有在线的人（可以不包括战斗托管玩家）的播放完毕（RoundPlayEnd）&& 达到战斗脚本的最短播放时间
		 */
		if(checkFeedBacks(battle, offlineUnbackIds))
		{
			BattleField.logger.info(new StringBuilder("战场号为：").append(battle.getBattleid()).append("的战斗---回合结束"));
			processRoundEnd(battle, offlineUnbackIds);
		}
		
		//确定的等待的回合数
		int curround = battle.getRound();
		int maxRound = battle.getBattlemaxround();
		
		if(maxRound == 0){
			maxRound = xbean.BattleInfo.MAX_ROUNDNUM;
		}
		
		
		//如果是pvp战斗 设置成平局
		if(curround >= maxRound){
			try{
				int  battleResult = BattleField.BATTLE_LOSE;
				if(battle.getBattletype() %10 == 1){
					battleResult = BattleField.BATTLE_DRAW;
				}
				new PBattleEnd(battleID, battleResult).call();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	private void logScriptTime(long battleID)
	{
		if(!BattleField.logger.isDebugEnabled())
			return;
		xbean.DebugFlag debugflag = xtable.Debugflag.get(roleID);
		if(debugflag == null || !debugflag.getDebugs().contains(2))
			return;

		String str = "客户端动作时间：" + itemtimelist;
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.messagetype = ChannelType.CHANNEL_SYSTEM;
		snd.message = str;
		mkdb.Procedure.psend(roleID, snd);
	}
	
	/**
	 * 检查是否收到所有的回馈
	 * @param battle
	 * @param offlineUnbackIds 托管的玩家
	 * @return true收到所有，false有未反馈的
	 */
	private boolean checkFeedBacks(xbean.BattleInfo battle, List<Long> offlineUnbackIds)
	{
		for(long battlerid : battle.getRoleids().keySet())
		{
			if(battle.getFeedbacks().contains(battlerid))
				continue;
			if(offlineUnbackIds.contains(battlerid))
				continue;
			return false;//还有未反馈的战斗玩家
		}
		return true;
	}
	
	private void processRoundEnd(xbean.BattleInfo battle, List<Long> offlineUnbackIds)
	{
		//对未收到反馈的托管中玩家，进行处理
		for(long unbackrId : offlineUnbackIds)
		{
			int unbackfId = battle.getRoleids().get(unbackrId);
			if(BattleField.checkRunAway(battle, unbackfId))
				processRoleRunaway(unbackrId, unbackfId, battle);
		}
		BattleField.cancelTimeOutCheck(battle);
		long scripttime = battle.getScriptplaytime();
		long remaintime = scripttime - (System.currentTimeMillis() - battle.getRoundtime());
		if( remaintime <= 0	|| (battle.getRound() == 0 &&(battle.getRoundresultitem().get(0)!=null
				||battle.getRoundresultitem().get(0).getResultitems()!=null||battle.getRoundresultitem().get(0).getResultitems().size()<=0)))
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("战斗 ").append(battle.getBattleid()).append(" 收到全部反馈，满足最小播放时间，本回合结束"));}
			pexecuteWhileCommit(new PRoundEnd(battle.getBattleid(),battle.getRound()));
		}
		else
		{
			if(remaintime > BattleField.MAX_ROUND_PLAY_MILLI_TIME)
				remaintime = BattleField.MAX_ROUND_PLAY_MILLI_TIME;
			/*if (BattleField.logger.isDebugEnabled()) {*/BattleField.logger.info(new StringBuilder("战斗 ").append(battle.getBattleid()).append(" 收到全部反馈，未满足最小播放时间（").append(scripttime).append(" 毫秒），延时至").append(remaintime).append(" 毫秒后结束回合。"));//}
			battle.setTimeout(mkdb.Mkdb.executor().schedule(
					new FeedbackCheck(battle.getBattleid(),battle.getRound()), remaintime,
					TimeUnit.MILLISECONDS));
		}
	}
	
	/**人物逃跑，需要换队长*/
	protected static void processTeamState(long roleId, xbean.BattleInfo battle) {

		TeamManager.processFailedMemberWhileOutBattle(roleId, battle);
		fire.pb.event.Poster.getPoster().dispatchEvent(new LeaveTeamSpecialQuestEvent(roleId));
		Team team = TeamManager.getTeamByRoleId(roleId);
		if (team == null)
			return;// 没有队伍直接返回
		team.notifyHpMpChange(roleId);//通知队友刷血蓝
	}
	
	/**角色逃跑的处理，放在此处理是为了角色逃跑出战斗后，能立即做其他事情*/
	private static void processRoleRunaway(final long roleId, int roleIndex, xbean.BattleInfo battle)
	{
		try
		{
			xbean.Fighter fighter = battle.getFighters().get(roleIndex);
			if (fighter != null )
			{
				Integer fightPetKey = xtable.Properties.selectFightpetkey(roleId);
				if(fightPetKey.intValue() != fighter.getInipetkey()) {
					boolean on = (fighter.getInipetkey() != -1);
					pexecuteWhileCommit(new PSetFightPetProc(roleId, fighter.getInipetkey(), on));
				}
			}
			
			for (BattleEndHandler handler : battle.getBattleendhandlers())
			{
				Integer petIndex = xtable.Properties.selectFightpetkey(roleId);
				petIndex = petIndex == null?-1:petIndex;
				handler.processOutBattle(roleIndex,petIndex);
				battle.getRoundresult().remove(roleIndex);
			}
			if ( battle.getBattletype() != BattleType.BATTLE_PVP) {
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						Module.getInstance().checkBattleBuff( roleId );
						return true;
					}
				}.call();
			}
			
			if (fighter != null )
			{
				fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(roleId,true);	
				if(checkattrerror(fighter, erole, AttrType.HIT_RATE)
						||checkattrerror(fighter, erole, AttrType.ATTACK)
						||checkattrerror(fighter, erole, AttrType.DEFEND)
						||checkattrerror(fighter, erole, AttrType.MAGIC_ATTACK)
						||checkattrerror(fighter, erole, AttrType.MAGIC_DEF)
						||checkattrerror(fighter, erole, AttrType.SPEED)
						||checkattrerror(fighter, erole, AttrType.DODGE_RATE)
						||checkattrerror(fighter, erole, AttrType.MEDICAL))
				{
					GMInteface.outputattr(roleId);
				}
			}
		} catch (Exception ex)
		{
			BattleField.logger.debug("Runaway handle failed", ex);
		}
		GsClient.pSendWhileCommit(new GSetRoleBattleInfo(roleId,0));
		processTeamState(roleId, battle);
		final SSendBattleEnd snd = new SSendBattleEnd();
		if(battle.getBattleresult() != BattleField.BATTLE_NOT_END)//如果是因为逃跑而整场战斗结束，则给逃跑的人也要发送战斗结束AI
			snd.aiactions = BattleAIManager.getInstance().getActionWhileBattleEnd(battle);
		psendWhileCommit(roleId, snd);
		pexecuteWhileCommit(new PRefreshRoleAttr(roleId));
		for(Iterator<xbean.BattleEndProcedure> it = battle.getEndprocedures().iterator();it.hasNext();)
		{
			xbean.BattleEndProcedure endp = it.next();
			if(endp.getFighterid() == roleIndex)
			{
				endp.getEndprocedure().call();
				it.remove();
			}
		}
		//处理状态的转移
		IState state = StateManager.getStateByRoleId(roleId);
		state.trigger(State.TRIGGER_BATTLE_END);
		
	}
	
	private static boolean checkattrerror(xbean.Fighter fighter, fire.pb.effect.RoleImpl erole, int attrtype) {
		return (fighter.getInitattrs().get(attrtype) != erole.getAttrById(attrtype) && erole.getAttrById(attrtype) < 0);
	}
}
