package fire.pb.battle;


import java.util.LinkedList;

import xbean.BattleInfo;
import fire.pb.PropConf;
import fire.pb.battle.BattleField;
import fire.pb.battle.Fighter;
import fire.pb.battle.FighterInfo;
import fire.pb.battle.PRoundPLayEnd;
import fire.pb.battle.SSendAddFighters;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendBattlerOperateState;
import fire.pb.battle.SSendPetInitAttrs;
import fire.pb.battle.SSendRoleInitAttrs;
import fire.pb.battle.SSendRoundStart;

/**下线再上线后，当客户端完成进入世界的处理后，发送CAfterEnterWorld*/
public class PEnterBattleAfterEnterWorld extends mkdb.Procedure {

	final private long roleId; 
	
	public PEnterBattleAfterEnterWorld(final long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process()
	{
		// 中断战斗托管状态时需要发的协议
		Long battleId = xtable.Roleid2battleid.select(roleId);
		if(battleId == null)
			return true;
		xbean.BattleInfo battle = xtable.Battle.get(battleId);
		if(battle == null) return true;
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids().keySet()));
			
		Integer fighterId = battle.getRoleids().get(roleId);
		if(fighterId == null) return true;
		boolean ishost = BattleField.isHost(fighterId);
		
		// 发送战斗开始消息
		final SSendBattleStart snd = new SSendBattleStart();
		snd.battletype = battle.getBattletype();
		snd.battleconfigid = battle.getConfigid();
		snd.roundnum = (battle.getPhase() == BattleInfo.PHASE_WAITOPERATION) ? battle.getRound() - 1 : battle.getRound();
		snd.enemyside = (ishost)? 0 : 1;
		snd.friendsformation = (ishost)? battle.getHostform() :  battle.getGuestform();
		snd.enemyformation = (ishost)? battle.getGuestform() :  battle.getHostform();
		snd.friendsformationlevel = (ishost)? battle.getHostformlevel() :  battle.getHostformlevel();
		snd.enemyformationlevel = (ishost)? battle.getHostformlevel() :  battle.getHostformlevel();
		snd.background = (byte)battle.getBackground();
		snd.backmusic = (byte)battle.getBgm();
		mkdb.Procedure.psendWhileCommit(roleId, snd);
		
		//发送角色初始属性
		Fighter opfighter = battle.getFighterobjects().get(fighterId);
		SSendRoleInitAttrs sroleattr = new SSendRoleInitAttrs();
		sroleattr.roleinitattrs.putAll(opfighter.getFighterBean().getInitattrs());
		psendWhileCommit(roleId, sroleattr);
		opfighter.sendAlreadyUsedItems();
		
		xbean.Fighter petfighter = battle.getFighters().get(fighterId + 5);
		if(petfighter != null)
		{
			SSendPetInitAttrs spetattr = new SSendPetInitAttrs();
			spetattr.petinitattrs.putAll(petfighter.getInitattrs());
			psendWhileCommit(roleId, spetattr);
		}
		// 向上线者发送添加战场内的参战者信息
		final SSendAddFighters addsnd = new SSendAddFighters();
		for(xbean.Fighter f : battle.getFighters().values()){
			if(BattleField.checkOutBattle(battle, f.getBattleindex()))
				continue;//已经不在战斗中的不发
			FighterInfo fi=BattleField.getFighterInfo(f,battle);
			addsnd.fighterlist.add(fi);
		}
		int watchernum = Math.min(PropConf.Battle.WATCHERS_IN_BATTLE,  battle.getWatchers().size());
		for(int i = BattleField.FIRST_WATCHER_BATTLE_INDEX ; i < (BattleField.FIRST_WATCHER_BATTLE_INDEX + watchernum) ; i++)
		{
			xbean.Fighter f = battle.getWatchers().get(i);
			if (f!=null)
				addsnd.fighterlist.add(BattleField.getWatcherFighterInfo(f, battle));
		}
		mkdb.Procedure.psendWhileCommit(roleId, addsnd);
		
		//如果还在读秒
		if (battle.getPhase() == BattleInfo.PHASE_WAITOPERATION)
		{
			int remainTime = (int)(BattleField.ACTION_TIMEOUT_DELAY*battle.getGmactiontimetimes()/10.0 -  ((System.currentTimeMillis() - battle.getRoundtime()) / 1000));
			if(remainTime > 0)
			{
				SSendRoundStart roundstart = new SSendRoundStart();
				roundstart.time = remainTime;
				roundstart.environment = battle.getEnvironment();
				psendWhileCommit(roleId,roundstart);
				//向战斗内其他人发送上线的消息
				if(!battle.getDecisions().containsKey(fighterId))//角色还未操作
					psendWhileCommit(battle.getRoleids().keySet(),new SSendBattlerOperateState(fighterId,1));
				else
					psendWhileCommit(battle.getRoleids().keySet(),new SSendBattlerOperateState(fighterId,2));
				
				if(!battle.getDecisions().containsKey(fighterId+5))//宠物还未操作
					psendWhileCommit(battle.getRoleids().keySet(),new SSendBattlerOperateState(fighterId+5,1));
				else
					psendWhileCommit(battle.getRoleids().keySet(),new SSendBattlerOperateState(fighterId+5,2));
			}
		}
		else
			psendWhileCommit(battle.getRoleids().keySet(),new SSendBattlerOperateState(fighterId,0));//动画播放中则清状态
		
		battle.getOfflineroleids().remove(roleId);
		if(!battle.getFeedbacks().contains(roleId))//如果没有发过roundplayend的话，提交一个
			pexecuteWhileCommit(new PRoundPLayEnd(roleId,new LinkedList<Integer>(),true));
		
		return true;
	}
	
}
