package fire.pb.school.shouxi;

import java.util.List;

import fire.pb.PropRole;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.npc.NpcServiceManager;
import fire.pb.school.ShouXiMsgID;
import fire.pb.talk.MessageMgr;

/***
 * 职业领袖战后处理 by changhao
 */

public class ProfessionLeaderEndHandler extends BattleEndHandler {

	private final int npcid;
	private final long npckey;
	/**
	 * Creates a new instance of ShouxiEndHandler.
	 *
	 * @param npcid
	 */
	
	public ProfessionLeaderEndHandler(long npckey,int npcid) {
       this.npcid = npcid;
       this.npckey = npckey;
	}

	@Override
	protected boolean handleDeath()
	{
      handlePetFighters();
      List<xbean.Fighter> fighters = super.getDeadRoles();
		for (xbean.Fighter f : fighters)
		{
			//首席战斗死亡后跟替身娃娃效果是一样的,满血满魔原地复活 by changhao
			long rid = f.getUniqueid();
			// 回血回魔 by changhao
			BattleField.fullRoleHpMp(rid);
			
			MessageMgr.psendMsgNotifyWhileCommit(f.getUniqueid(), 190074,npcid, null);
		}
     
		return true;
	}
	
	@Override
	protected boolean handleCamera() {
		return true;
	}

	@Override
	protected boolean handleAward()
	{
		List<xbean.Fighter> fighters = super.getWinRoles();
		for (xbean.Fighter f : fighters)
		{
			//把玩家这次挑战成功的时间记录数据库中 by changhao
			long roleid=f.getUniqueid();
			xbean.ProfessionLeaderVoteInfo voteInfo = xtable.Professionleadervotechallenge.get(roleid);
			if(voteInfo == null)
			{
				voteInfo = xbean.Pod.newProfessionLeaderVoteInfo();
				xtable.Professionleadervotechallenge.insert(roleid, voteInfo);
			}
			
			voteInfo.setChallengetime(System.currentTimeMillis());
			PropRole role = new PropRole(roleid, true);
				
			java.util.ArrayList<String> sp = new java.util.ArrayList<String>();
			sp.add(role.getName());
			
			int npcId = NpcServiceManager.getNpcIDByKey(npckey);
			MessageMgr.sendMsgNotify(roleid, 170007, npcId, sp);
		}
		
		return true;
	}

	@Override
	public boolean handleOutBattle(int index,int petkey) throws Exception {
		xbean.Fighter f=battle.getFighters().get(index);
		 //MessageMgr.psendMsgNotifyWhileCommit(f.getUniqueid(), ShouXiMsgID.ChallengeLost,npcid, null);
		return super.handleOutBattle(index,petkey);
	}

	@Override
	protected boolean handleDrawBattle() {
		xbean.Fighter f=battle.getFighters().get(1);
		if (f!=null) 
		 MessageMgr.psendMsgNotifyWhileCommit(f.getUniqueid(), ShouXiMsgID.ChallengeLost,npcid, null);
		return super.handleDrawBattle();
	}
/*
 */
}
