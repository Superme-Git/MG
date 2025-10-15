package fire.pb.gm;

import java.util.concurrent.TimeUnit;

import fire.pb.activity.clanfight.ActivityClanFightManager;

/***
 * 公会战更新排行 by changhao
 */
public class GM_clanfightupdaterank extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}

		try {
			
		} catch (java.lang.NumberFormatException e) {
	
		}
	
		//注册战场活动结束计时器 by changhao
		long cur = System.currentTimeMillis();
		long monday1500 = ActivityClanFightManager.GetMonday1500ByTime(cur);
		long wednesday1500 = ActivityClanFightManager.GetWednesday1500ByTime(cur);
		
		//活动结束 开始排名 by changhao
		if(true)
		{
			java.util.List<Long> timelist = new java.util.ArrayList<Long>();
			timelist.add(monday1500);
			new fire.pb.clan.fight.PClanFightRaceRank(timelist).submit();
			
			fire.pb.clan.fight.PClanFightRaceRank p = new fire.pb.clan.fight.PClanFightRaceRank(timelist);	
			mkdb.Executor.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
				   p.submit();
				}
			}, 10, TimeUnit.SECONDS);			
		}
		
		if(true)
		{
			java.util.List<Long> timelist = new java.util.ArrayList<Long>();
			timelist.add(wednesday1500);
			
			fire.pb.clan.fight.PClanFightRaceRank p = new fire.pb.clan.fight.PClanFightRaceRank(timelist);	
			mkdb.Executor.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
				   p.submit();
				}
			}, 20, TimeUnit.SECONDS);	
			
			java.util.List<Long> timelist2 = new java.util.ArrayList<Long>();
			timelist2.add(monday1500);
			timelist2.add(wednesday1500);
			
			fire.pb.clan.fight.PClanFightRaceRank p1 = new fire.pb.clan.fight.PClanFightRaceRank(timelist2);	
			mkdb.Executor.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
				   p1.submit();
				}
			}, 30, TimeUnit.SECONDS);
			
			//公会战历史排名 by changhao
			fire.pb.clan.fight.PClanFightHistroyRank p2 = new fire.pb.clan.fight.PClanFightHistroyRank();	
			mkdb.Executor.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
				   p2.submit();
				}
			}, 40, TimeUnit.SECONDS);	
		}			
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
