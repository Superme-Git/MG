package fire.pb.clan.fight;

import java.util.concurrent.TimeUnit;

import fire.pb.activity.clanfight.ActivityClanFightManager;
import xbean.ClanFightBattleField;
/**
 * 某场公会战结束谁赢了  by changhao
 */
public class PClanFightOver extends mkdb.Procedure
{
	long clanfightid;
	int whichwin;
	long datetime;
	long winclanid;
	
	public PClanFightOver(long clanfightid, int whichwin, long datetime, long winclanid)
	{
		this.clanfightid = clanfightid;
		this.whichwin = whichwin;
		this.datetime = datetime;
		this.winclanid = winclanid;
	}
	
	@Override
	/***
	 * locs clanid -> clanfights -> clanfightid by changhao
	 */
	protected boolean process() throws Exception
	{	
		long cur = System.currentTimeMillis();
		
		xbean.ClanInfo c = xtable.Clans.get(winclanid);
		if (c == null)
		{
			return false;
		}
		
		//更新战场输赢 by changhao
		xbean.ClanFights clanfights = xtable.Clanfights.get(datetime);
		
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf != null)
		{
			if (bf.getClanfightBean().getState() == ClanFightBattleField.STATE_CLAN_FIGHT_OVER) //战场已经结束了by changhao
			{
				return false;
			}
			
			if (cur > bf.getClanfightBean().getActivityendtime() + 3600 * 1000)
			{
				return false;
			}
			
			PClanFightRefreshBox refreshbox = new PClanFightRefreshBox(clanfightid, whichwin);	
			mkdb.Executor.getInstance().schedule(new Runnable() //60秒后开始刷宝箱 by changhao
			{
				@Override
				public void run()
				{
					refreshbox.submit();
				}
			}, ActivityClanFightManager.REFRESH_BOX_TIME, TimeUnit.SECONDS);
			
			PClanFightDestroyBox destroybox = new PClanFightDestroyBox(clanfightid);	
			mkdb.Executor.getInstance().schedule(new Runnable() //10分钟后开始销毁宝箱  by changhao
			{
				@Override
				public void run()
				{
					destroybox.submit();
				}
				
			}, ActivityClanFightManager.BOX_TIME + ActivityClanFightManager.REFRESH_BOX_TIME, TimeUnit.SECONDS);			
			
			
			bf.scroeSort(0, true);
			bf.scroeSort(1, true);
			
			//发送战场结束消息 by changhao
			SClanFightOver msg = new SClanFightOver();
			msg.status = whichwin;
			msg.overtimestamp = cur;

			bf.sendMsgToBattleRoleids(msg);

			if (clanfights != null)
			{
				xbean.ClanFight clanfight = clanfights.getClan1vschan2().get(clanfightid);
				if (clanfight != null)
				{
					clanfight.setWinner(whichwin);
					clanfight.setClan1scroe(bf.getClanfightBean().getClanscore1());
					clanfight.setClan2scroe(bf.getClanfightBean().getClanscore2());
				}
			}
			
			//清理玩家统计信息 by changhao
			PClanFightClearRolesStatistics clear = new PClanFightClearRolesStatistics(clanfightid);	
			mkdb.Executor.getInstance().schedule(new Runnable() //12分钟后清理玩家信息  by changhao
			{
				@Override
				public void run()
				{
					clear.submit();
				}
				
			}, ActivityClanFightManager.BOX_TIME + ActivityClanFightManager.REFRESH_BOX_TIME + 60 * 2, TimeUnit.SECONDS);			

			
			
			bf.getClanfightBean().setWhichwin(whichwin);
			bf.getClanfightBean().setState(ClanFightBattleField.STATE_CLAN_FIGHT_OVER);
			
			java.util.Map<Long, Integer> whichroleids = bf.getClanroleidsByWhich(whichwin);
			if (whichroleids != null)
			{
				long timecount = (bf.getClanfightBean().getActivityendtime() - cur) / (4 * 60 * 1000);
				if (timecount > 0)
				{
					bf.addClanScore(whichwin, fire.pb.activity.clanfight.ActivityClanFightManager.CLAN_FIGHT_ADD_SCROE_4_MINUTES * whichroleids.size() * (int)timecount);
					
					mkdb.Procedure.pexecuteWhileCommit(new PClanFightBalanceExp(clanfightid, whichwin));					
				}
			}
			
			//对方已经全军覆没,公会战提前结束，我方大获全胜,比赛场景会在一分钟后出现奖励。 by changhao
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(c.getMembers().keySet(), 410037, 0, null);
			
			//结束战场里的所有战斗 by changhao
			mkdb.Procedure.pexecuteWhileCommit(new PClanFightEndAllBattle(clanfightid));
			
			int oppwhich = 0;
			if (whichwin == 0)
				oppwhich = 1;
			
			fire.pb.clan.fight.PClanFightClearRoleOut clearout = new fire.pb.clan.fight.PClanFightClearRoleOut(clanfightid, oppwhich);	
			mkdb.Executor.getInstance().schedule(new Runnable() //20秒后离开战场 by changhao
			{
				@Override
				public void run()
				{
					clearout.submit();
				}
				
			}, 20, TimeUnit.SECONDS);			
			
			ActivityClanFightManager.logger.info("PClanFightOver: 战场over:[" + bf.getClanfightBean().getClanfightid() + "]win:[" + whichwin +"]");
		}
		
		return true;
	}
}
