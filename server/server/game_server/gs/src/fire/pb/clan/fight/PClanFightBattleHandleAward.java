package fire.pb.clan.fight;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PAddClanPointProc;
import fire.pb.PAddExpProc;
import fire.pb.activity.clanfight.ActivityClanFightManager;
import fire.pb.clan.ClanUtils;
import fire.pb.main.ConfigManager;
import fire.pb.mission.ExecuteReqGoto;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.MapUtil;
import fire.pb.scene.SceneManager;
import fire.pb.scene.sPos.GridPos;
import fire.pb.util.BagUtil;

/**
 * 处理公会战斗奖励  by changhao
 */
public class PClanFightBattleHandleAward extends mkdb.Procedure
{
	long clanfightid;
	long attackerid;
	long attackerclanid;
	long hitclanid;
	java.util.List<Long> winners;
	java.util.List<Long> losers;
	
	public PClanFightBattleHandleAward(long attackerid, long clanfightid, java.util.List<Long> winners, java.util.List<Long> losers, long attackerclanid, long hitclanid)
	{
		this.attackerid = attackerid;
		this.clanfightid = clanfightid;
		this.winners = winners;
		this.losers = losers;
		this.attackerclanid = attackerclanid;
		this.hitclanid = hitclanid;
		
	}
	
	@Override
	/**
	 * lock clanid -> clanfightid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{
		long cur = System.currentTimeMillis();
		java.util.Set<Long> setclanids = new java.util.HashSet<Long>();
		setclanids.add(attackerclanid);
		setclanids.add(hitclanid);
		
		this.lock(mkdb.Lockeys.get(xtable.Locks.CLANS, setclanids)); //锁定2个公会 by changhao	
		
		xbean.ClanInfo attackerclan = xtable.Clans.get(attackerclanid);
		xbean.ClanInfo hitclan = xtable.Clans.get(hitclanid);
		
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf == null)
		{
			return true;
		}
		
		if (bf.getClanfightBean().getState() == xbean.ClanFightBattleField.STATE_CLAN_FIGHT_OVER) //结束了就不算了 by changhao
		{
			return true;
		}
		
		java.util.Set<Long> roleids = new java.util.HashSet<Long>();
		roleids.addAll(winners);
		roleids.addAll(losers);
		
		this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids)); //锁所有角色 by changhao
		
		//处理赢的 一边 奖励  by changhao
		if (winners.size() != 0)
		{
			int totalscroe = losers.size() * ActivityClanFightManager.ONE_PEOPLE_SCROE; //获得总积分 by changhao
			
			int whichwin = bf.getSideByRoleId(winners.get(0));
			
			boolean attackerside = false; //当前赢的这边是主动发起攻击 by changhao
			if (winners.contains(attackerid))
			{
				attackerside = true;
			}
			
			boolean first = true;
			for (Long f : winners)
			{
				//胜利的玩家要加积分 by changhao
				long roleid = f;
				
				//获取角色公会战时的统计 by changhao
				xbean.EClanFightStatistics statistics = xtable.Roleid2clanfightstatistics.get(roleid);
				if (statistics == null)
				{
					continue;
				}
				
				//胜利者加积分  by changhao
				statistics.setScore(statistics.getScore() + totalscroe / winners.size());
				bf.setRoleIdScore(roleid, statistics.getScore());
				bf.scroeSort(whichwin, false);
								
				statistics.setWinnum(statistics.getWinnum() + 1); //连胜加1 by changhao
				
				if (attackerside == true) //攻击方扣行动力 by changhao
				{
					Integer subact = ActivityClanFightManager.ATTACK_SUB_ACT;
					int v = statistics.getAct() - subact;
					if (v < 0)
					{
						v = 0;
					}
					
					statistics.setAct(v);
					
					java.util.List<String> p = new java.util.ArrayList<String>();
					String s = subact.toString();
					p.add(s);
					
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 410039, 0, p);													
				}
							
				SBattleFieldAct msg = new SBattleFieldAct();
				msg.roleact = statistics.getAct();
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				Integer addscroe = totalscroe;
				bf.addClanScore(whichwin, addscroe); //赢的一边加公会积分 by changhao
				
				//加积分提示 by changhao
				java.util.List<String> p = new java.util.ArrayList<String>();
				String s = addscroe.toString();
				p.add(s);
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 410033, 0, p);									
				if (statistics.getWinnum() == ActivityClanFightManager.WIN_NUM) //连胜宝箱 by changhao
				{
					BagUtil.addItem(roleid, ActivityClanFightManager.ITEM_ID_LIANSHENG_BOX, 1, "连胜宝箱", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_liansheng_reward, 1, true);
					ClanFightBattleField.logger.info("PClanFightBattleHandleAward: 角色:["+ roleid +"] 5连胜 ");
				}
				
				//广播连胜情况 by changhao
				if (statistics.getWinnum() >= ActivityClanFightManager.WIN_NUM)
				{
					java.util.List<String> param = new java.util.ArrayList<String>();
					if (first == true)
					{
						Integer v = statistics.getWinnum();
						String strnum = v.toString();
						String name = bf.getClanfightBean().getEnterroleids().get(roleid);
						if (name != null)
						{
							param.add(name);
						}
						
						param.add(strnum);
							
						if (attackerside == true)
						{	
							if (attackerclan != null)
							{
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(attackerclan.getMembers().keySet(), 410034, 0, param);								
							}
							
							if (hitclan != null)
							{
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(hitclan.getMembers().keySet(), 410035, 0, param);									
							}
						}
						else
						{
							if (attackerclan != null)
							{
								//<T t="敌方" c="ff261407"/><T t="$parameter1$" c="ff261407"/><T t="的队伍获得了" c="ff261407"/><T t="$parameter2$" c="ff261407"/><T t="连胜" c="ff261407"/> by changhao
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(attackerclan.getMembers().keySet(), 410035, 0, param);								
							}
							
							if (hitclan != null)
							{
								fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(hitclan.getMembers().keySet(), 410034, 0, param);									
							}							
						}
						
						first = false;
					}
				}
				
				if (statistics.getAct() <= 0) //行动力小于0离开战场 by changhao
				{	
					Double expAward = 400 * Math.min(50, 111 * 10 - 1051)	* 0.0392 * (0.4 + 0.1 * 111);
					pexecuteWhileCommit(new PAddExpProc(roleid, expAward.longValue(), PAddExpProc.INST_MAP, "公会踢出战场加经验"));
					mkdb.Procedure.pexecuteWhileCommit(new PClanFightLeaveClanFieldBattleField(clanfightid, roleid, true, true));
				}
				
				//加DKP by changhao
				mkdb.Procedure.pexecuteWhileCommit(new PAddClanPointProc(roleid, ActivityClanFightManager.CLAN_FIGHT_WIN_DKP, YYLoggerTuJingEnum.tujing_Value_clanfight_dkp, true, "公会战"));
			}		
		}
		
		//处理输的一边奖励 by changhao
		if (losers.size() != 0)
		{
			int whichlose = bf.getSideByRoleId(losers.get(0));
			
			boolean attackerside = false; //当前输的这边是主动发起攻击 by changhao
			if (losers.contains(attackerid))
			{
				attackerside = true;
			}
			
			boolean onepeople = false;
			if (losers.size() == 1)
			{
				onepeople = true;
			}
			
			for (Long f : losers)
			{
				//胜利的玩家要加积分 by changhao
				long roleid = f;
				
				//获取角色公会战时的统计 by changhao
				xbean.EClanFightStatistics statistics = xtable.Roleid2clanfightstatistics.get(roleid);
				if (statistics == null)
				{
					continue;
				}
				
				statistics.setWinnum(0);
				
				int oldact = statistics.getAct();
				
				int v = statistics.getAct() - ActivityClanFightManager.PK_FAIL_SUB_ACT;
				if (v < 0)
				{
					v = 0;
				}
				
				statistics.setAct(v);
								
				if (attackerside == true)
				{
					v = statistics.getAct() - ActivityClanFightManager.ATTACK_SUB_ACT;
					if (v < 0)
					{
						v = 0;
					}
					
					statistics.setAct(v);					
				}
				
				Integer subact = oldact - statistics.getAct();
				java.util.List<String> p = new java.util.ArrayList<String>();
				String s = subact.toString();
				p.add(s);
				
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 410039, 0, p);//扣除行动力 by changhao
						
				SBattleFieldAct msg = new SBattleFieldAct();
				msg.roleact = statistics.getAct();
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				if (onepeople == true)
				{
					Integer addscroe = ActivityClanFightManager.ONE_POPLE_FAIL_ADD_SCROE;

					statistics.setScore(statistics.getScore() + addscroe);
					bf.setRoleIdScore(roleid, statistics.getScore());					
					
					bf.addClanScore(whichlose, addscroe); //赢的一边加公会积分 by changhao
					bf.scroeSort(whichlose, false);
					
					//加积分提示 by changhao
					java.util.List<String> param = new java.util.ArrayList<String>();
					String strscroe = addscroe.toString();
					param.add(strscroe);
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 410033, 0, param);					
				}	
				
				if (statistics.getAct() <= 0)
				{
					Double expAward = 400 * Math.min(50, 111 * 10 - 1051)	* 0.0392 * (0.4 + 0.1 * 111);
					pexecuteWhileCommit(new PAddExpProc(roleid, expAward.longValue(), PAddExpProc.INST_MAP, "公会踢出战场加经验"));
					mkdb.Procedure.pexecuteWhileCommit(new PClanFightLeaveClanFieldBattleField(clanfightid, roleid, true, true));
				}
				else
				{
					bf.randomGotoPos(roleid);
				}
				
				statistics.setLastlosestamp(cur);
				
				//加DKP by changhao
				mkdb.Procedure.pexecuteWhileCommit(new PAddClanPointProc(roleid, ActivityClanFightManager.CLAN_FIGHT_LOSE_DKP, YYLoggerTuJingEnum.tujing_Value_clanfight_dkp, true, "公会战"));
			}		
		}		
		
		return true;
	}
}
