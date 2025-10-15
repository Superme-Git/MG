package fire.pb.activity.clanfight;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import fire.pb.clan.Module;
import fire.pb.clan.fight.ClanFightFactory;
import fire.pb.clan.fight.PClanFightDestroyTask;
import fire.pb.clan.fight.SGetClearTime;
import fire.pb.main.ConfigManager;
import fire.pb.mission.activelist.SActivityOpen;
import fire.pb.util.DateValidate;

/**
 * 活动公会战 by changhao
 * @author changhao
 * @param <q>
 * @see
 */

public class ActivityClanFightManager
{
	public static int ITEM_ID_LIANSHENG_BOX = 331403; //连胜宝箱 by changhao
	public static int ONE_PEOPLE_SCROE = 60; //赢一个人积分 by changhao
	public static int ATTACK_SUB_ACT = 12; //单人扣除行动力 by changhao
	public static int PK_FAIL_SUB_ACT = 600;
	public static int ONE_POPLE_FAIL_ADD_SCROE = 15;
	public static int INIT_ACT = 2400;
	public static int INIT_ACT1 = 1200;
	public static int VICTORY_BOX = 180404;
	public static int CELEBRATE_BOX = 300002;
	public static int VICTORY_BOX_NUM = 10;
	public final static int WIN_NUM = 5; //连胜数量 by changhao
	public final static int REFRESH_BOX_TIME = 60; //多少后开始刷宝箱 by changhao
	public final static int BOX_TIME = 60 * 10;//宝箱存留多少秒 by changhao
	public static int CLAN_FIGHT_PEOPLE_NUM = 20;
	public static int CLAN_FIGHT_OVER_PEOPLE_NUM = 0;
	public static int CLAN_FIGHT_CLAN_LEVEL = 2;
	public static int CLAN_FIGHT_LOGIN_TIME = 7;
	public static int CLAN_FIGHT_JOIN_CLAN_TIME = 2;
	public static int CLAN_FIGHT_ROLE_LEVEL = 50;
	public static int CLAN_FIGHT_ADD_SCROE_4_MINUTES = 60;
	public static int CLAN_FIGHT_WIN_DKP = 10;
	public static int CLAN_FIGHT_LOSE_DKP = 5;
	public static int OUT_MAP_ID = 1615;
	public static int OUT_MAP_ID_X = 233;
	public static int OUT_MAP_ID_Y = 93;
	public static int NUMBER_ONE_CLAN_FIGHT_TITLE = 119;
	public static int CLAN_FIGHT_MONEY = 1000000;
	
	public static final Logger logger = Logger.getLogger("CLAN");

	private static ActivityClanFightManager instance = new ActivityClanFightManager();
	
	private boolean start;
	
	public java.util.concurrent.atomic.AtomicInteger curweek = new java.util.concurrent.atomic.AtomicInteger(-1);//当前轮数 从0开始 by changhao
	
	public class ClanFightStatisticsInfo
	{
		public long clanid;
		public int clanlevel;
		public String clanname;
		public int fightcount;
		public int wincount;
		public int totalscroe;
	}
		
	/***
	 * 服务器启动的时候初始化 by changhao
	 * @throws Exception
	 */
	public void init() throws Exception {

		new mkdb.Procedure() {
			
			@Override
			protected boolean process() throws Exception
			{
				//创建公会战匹配 by changhao
				fire.pb.clan.fight.PClanFightCreateMatch clanfightcreatematch = new fire.pb.clan.fight.PClanFightCreateMatch(false);	
				mkdb.Executor.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						clanfightcreatematch.submit();
					}
				}, 10, TimeUnit.SECONDS);
				
				
				fire.pb.clan.fight.PClanFightInit clanfightinit = new fire.pb.clan.fight.PClanFightInit();	
				mkdb.Executor.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						clanfightinit.submit();
					}
				}, 20, TimeUnit.SECONDS);
				
				fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(400);
				if (common != null)
				{
					ActivityClanFightManager.ITEM_ID_LIANSHENG_BOX = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(401);
				if (common != null)
				{
					ActivityClanFightManager.ONE_PEOPLE_SCROE = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(402);
				if (common != null)
				{
					ActivityClanFightManager.ATTACK_SUB_ACT = Integer.parseInt(common.getValue());		
				}				
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(403);
				if (common != null)
				{
					ActivityClanFightManager.PK_FAIL_SUB_ACT = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(404);
				if (common != null)
				{
					ActivityClanFightManager.ONE_POPLE_FAIL_ADD_SCROE = Integer.parseInt(common.getValue());		
				}		
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(405);
				if (common != null)
				{
					ActivityClanFightManager.INIT_ACT = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(406);
				if (common != null)
				{
					ActivityClanFightManager.INIT_ACT1 = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(407);
				if (common != null)
				{
					ActivityClanFightManager.VICTORY_BOX = Integer.parseInt(common.getValue());		
				}	
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(408);
				if (common != null)
				{
					ActivityClanFightManager.CELEBRATE_BOX = Integer.parseInt(common.getValue());		
				}	
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(409);
				if (common != null)
				{
					ActivityClanFightManager.VICTORY_BOX_NUM = Integer.parseInt(common.getValue());		
				}	
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(410);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_PEOPLE_NUM = Integer.parseInt(common.getValue());		
				}	
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(411);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_CLAN_LEVEL = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(412);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_LOGIN_TIME = Integer.parseInt(common.getValue());		
				}	
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(413);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_JOIN_CLAN_TIME = Integer.parseInt(common.getValue());		
				}	
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(414);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_ROLE_LEVEL = Integer.parseInt(common.getValue());		
				}	
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(415);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_ADD_SCROE_4_MINUTES = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(416);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_WIN_DKP = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(417);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_LOSE_DKP = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(418);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_OVER_PEOPLE_NUM = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(419);
				if (common != null)
				{
					ActivityClanFightManager.NUMBER_ONE_CLAN_FIGHT_TITLE = Integer.parseInt(common.getValue());		
				}
				
				common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(420);
				if (common != null)
				{
					ActivityClanFightManager.CLAN_FIGHT_MONEY = Integer.parseInt(common.getValue());		
				}
						
				return true;
			}

		}.submit();
	}
	
	private ActivityClanFightManager()
	{
		start = false;
	}

	public static ActivityClanFightManager getInstance()
	{
		return instance;
	}

	public boolean activityInit() throws Exception
	{				
		new mkdb.Procedure() {
			
			@Override
			protected boolean process() throws Exception
			{
				//创建公会战匹配 by changhao
				fire.pb.clan.fight.PClanFightCreateMatch clanfightcreatematch = new fire.pb.clan.fight.PClanFightCreateMatch(false);	
				mkdb.Executor.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						clanfightcreatematch.submit();
					}
				}, 1, TimeUnit.SECONDS);
										
				return true;
			}

		}.submit();
		
		return true;
	}
	
	/***
	 * 公会战活动结束处理 by changhao
	 * @param activityid
	 */
	public void stop(int activityid)
	{
		start = false;
						
		//注册战场活动结束计时器 by changhao
		long cur = System.currentTimeMillis();
		long monday1500 = ActivityClanFightManager.GetMonday1500ByTime(cur);
		long wednesday1500 = ActivityClanFightManager.GetWednesday1500ByTime(cur);
		
		//活动结束 开始排名 by changhao
		if(!DateValidate.inTheSameDay(cur, monday1500))
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
			}, 40, TimeUnit.SECONDS);			
		}
		
		if(!DateValidate.inTheSameDay(cur, wednesday1500))
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
			}, 50, TimeUnit.SECONDS);	
			
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
			}, 60, TimeUnit.SECONDS);
			
			//公会战历史排名 by changhao
			fire.pb.clan.fight.PClanFightHistroyRank p2 = new fire.pb.clan.fight.PClanFightHistroyRank();	
			mkdb.Executor.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
				   p2.submit();
				}
			}, 70, TimeUnit.SECONDS);	
		}
		
		long thursday0000 = ActivityClanFightManager.GetThursday0000ByTime(cur);
		if (DateValidate.inTheSameDay(cur, thursday0000))
		{
			//公会战第一名加title by changhao
			fire.pb.clan.fight.PClanFightAddTitle p2 = new fire.pb.clan.fight.PClanFightAddTitle();	
			mkdb.Executor.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
				   p2.submit();
				}
			}, 100, TimeUnit.SECONDS);	
		}
	}
	
	public void start()
	{
		start = true;
	}
	
	public boolean IsStart()
	{
		return start;
	}
	
	public void roleLoginCheck(final long roleId, int level)
	{
		new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{
				if (ActivityClanFightManager.getInstance().IsStart())
				{
					boolean ok = ActivityClanFightManager.checkClanFight(roleId, level);
					if (ok == true)
					{
			 			SActivityOpen so = new SActivityOpen();
		 				so.activityid = 281;
						
						mkdb.Procedure.psendWhileCommit(roleId, so);						
					}
				}
				
				return false;
				
			}
		}.submit();
	}
	
	/***
	 * 帮会战活动开启通知客户端 by changhao
	 */
	public void Pull()
	{
		if (start == true)
		{
			SActivityOpen msg = new SActivityOpen();
			
			msg.activityid = 281;
			gnet.link.Onlines.getInstance().broadcast(msg, 997);			
		}
	}
	
	/***
	 * 取公会综合实力排名 by changhao
	 * @param tmp
	 * @param sortmap
	 */
	void GetClanZongheList(java.util.ArrayList<xbean.ClanInfo> tmp, java.util.Map<Long, Integer> sortmap)
	{
		xbean.ClanZongHeRankList list = xtable.Clanzonghelist.select(1); //取出综合实力榜 by changhao
		if (list != null)
		{
			for (xbean.ClanZongHeRankRecord e : list.getRecords())
			{
				xbean.ClanInfo claninfo = xtable.Clans.select(e.getClankey());
				if (claninfo != null)
				{
					tmp.add(claninfo);
					sortmap.put(e.getClankey(), e.getZonghe());
				}
			}				
		}		
	}
	
	/***
	 * 计算出有资格的公会 by changhao
	 * @return
	 */
	java.util.ArrayList<xbean.ClanInfo> CalcQualificationClan(int which)
	{
		long cur = System.currentTimeMillis();
		
		int curweek = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().curweek.get();
		curweek = curweek % 24;
		
		long day1 = 24 * 3600 * 1000;
	
		java.util.Map<Long, Integer> sortmap = new java.util.TreeMap<Long, Integer>();
		java.util.ArrayList<xbean.ClanInfo> tmp = new java.util.ArrayList<xbean.ClanInfo>();
		
		if (curweek == 0 && which == 0) //第一轮的第一场看公会综合实力匹配  by changhao
		{
			GetClanZongheList(tmp, sortmap);
		}
		else
		{
			long datetime = ActivityClanFightManager.GetMonday0000ByTime(cur);	
			xbean.ClanFightRaceRankList ranlListBean = xtable.Clanfightracelist.select(datetime); //得到这周的轮排名 by changhao
			if (ranlListBean == null)
			{
				ranlListBean = xtable.Clanfightracelist.select(datetime - 7 * day1); //得到上周的轮排名 by changhao				
			}
			
			if(null != ranlListBean)
			{
				for (xbean.ClanFightRaceRankRecord e : ranlListBean.getRecords())
				{
					xbean.ClanInfo claninfo = xtable.Clans.select(e.getMarshaldata().getClanid());
					if (claninfo != null)
					{
						tmp.add(claninfo);
						sortmap.put(e.getMarshaldata().getClanid(), e.getMarshaldata().getScroe());
					}
				}
			}
			else
			{
				GetClanZongheList(tmp, sortmap);// 没有轮排行数据就按公会综合实力榜匹配 by changhao	
			}
		}
					
		java.util.ArrayList<xbean.ClanInfo> l = new java.util.ArrayList<xbean.ClanInfo>();
		java.util.ArrayList<xbean.ClanInfo> newclans = new java.util.ArrayList<xbean.ClanInfo>();
		
		for (xbean.ClanInfo e : Module.clanListMap.values())
		{
			if (e != null)
			{
				if (!sortmap.containsKey(e.getKey()))
				{
					newclans.add(e);
				}
			}
		}
		
		Collections.shuffle(newclans);
		tmp.addAll(newclans);
		
		for (xbean.ClanInfo e : tmp)
		{
			xbean.ClanInfo claninfo = xtable.Clans.select(e.getKey());
			if (claninfo == null)
			{
				continue;
			}
			
			if (claninfo.getClanlevel() < ActivityClanFightManager.CLAN_FIGHT_CLAN_LEVEL) //公会等级>=2 by changhao
			{
				continue;
			}
			
			if (claninfo.getMembers().size() < ActivityClanFightManager.CLAN_FIGHT_PEOPLE_NUM) //会员至少20人 by changhao
			{
				continue;
			}
			
			long count = 0;
			for (Long roleid : claninfo.getMembers().keySet())
			{
				xbean.Properties prop = xtable.Properties.select(roleid);
				if (prop != null)
				{
					if (prop.getLevel() >= ActivityClanFightManager.CLAN_FIGHT_ROLE_LEVEL)
					{						
						if (ActivityClanFightManager.CLAN_FIGHT_LOGIN_TIME == 0 || cur - day1 * ActivityClanFightManager.CLAN_FIGHT_LOGIN_TIME < prop.getOnlinetime()) //7天内登陆过 by changhao
						{
							count ++;
							if (count >= ActivityClanFightManager.CLAN_FIGHT_OVER_PEOPLE_NUM)
							{
								break;
							}
						}
					}
				}
			}
			
			if (count >= ActivityClanFightManager.CLAN_FIGHT_OVER_PEOPLE_NUM)
			{
				l.add(claninfo);
			}
		}
		
		int minscroe = 6000000; //找出最弱公会 by changhao
		xbean.ClanInfo minscroeclan = null;
		for (xbean.ClanInfo e : l)
		{
			if (e != null)
			{
				if (!sortmap.containsKey(e.getKey()))
				{
					if (e.getTotalscore() < minscroe)
					{
						minscroeclan = e;
						minscroe = e.getTotalscore();
					}
				}
			}
		}
		
		if (l.size() % 2 == 1) //移除奇数个 by changhao
		{
			if (minscroeclan != null) //移除最弱的 by changhao
			{
				l.remove(minscroeclan);
			}
			else
			{
				l.remove(l.size() - 1);				
			}			
		}
		
		//6人一组 随机打乱 by changhao
		java.util.ArrayList<xbean.ClanInfo> finaldata = new java.util.ArrayList<xbean.ClanInfo>();
		
		int size = (l.size() / 6) + 1;
		for (int i = 0; i < size; i ++)
		{
			java.util.List<xbean.ClanInfo> sixgroupdata = new java.util.ArrayList<xbean.ClanInfo>();
			for (int j = 0; j < 6; j ++)
			{
				if ((i * 6 + j) >= l.size())
				{
					continue;
				}
					
				xbean.ClanInfo c = l.get(i * 6 + j);
				sixgroupdata.add(c);
			}
			
			Collections.shuffle(sixgroupdata);
			
			finaldata.addAll(sixgroupdata);
		}
						
		return finaldata;
	}
	
	/***
	 * 计算当前已经打到第几轮了从0开始 by changhao
	 * @return
	 */
	public int CalcCurWeekNum() throws Exception
	{
		String str = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.clanfight.starttime");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		
		long starttime = sdf.parse(str).getTime();
		
		long cur = System.currentTimeMillis();
		
		long day1 = 24 * 60 * 60 * 1000;
		if (cur >= starttime)
		{
			long delta = cur - starttime;
			long num = delta / (day1 * 7);
			
			return (int)num;
		}
		
		return -1;
	}
	
	/***
	 * 创建公会对战匹配 by changhao
	 * lock clanfights
	 */
	void CreateClanFightMatch(int which, java.util.ArrayList<xbean.ClanInfo> list, boolean force)
	{
		if (curweek.get() == -1) //公会战开始时间没到 by changhao
		{
			return;
		}
		
		long cur = System.currentTimeMillis();
		
		long t = which == 0 ? GetMonday1500ByTime(cur) : GetWednesday1500ByTime(cur);
		xbean.ClanFights clanfights = xtable.Clanfights.get(t);
		
		if (clanfights == null || clanfights.getCreatetime() != t || force == true) //需要重新生成 by changhao
		{
			if (clanfights == null)
			{
				clanfights = xbean.Pod.newClanFights();
				xtable.Clanfights.insert(t, clanfights);
			}
			
			clanfights.getClan1vschan2().clear();
			clanfights.setCreatetime(t);
			
			int size = list.size();
			
			for (int i = 0; i < size / 2; i ++)
			{
				xbean.ClanFight clanfight = xbean.Pod.newClanFight();
				xbean.ClanInfo c1 = list.get(i * 2 + 0);
				
				xbean.ClanInfo c2 = list.get(i * 2 + 1);
				
				if (c1 != null && c2 != null)
				{
					clanfight.setClan1(c1.getKey());
					clanfight.setClanindex1(c1.getIndex());
					clanfight.setClanname1(c1.getClanname());
					clanfight.setClanlevel1(c1.getClanlevel());
					clanfight.setClan2(c2.getKey());
					clanfight.setClanindex2(c2.getIndex());
					clanfight.setClanname2(c2.getClanname());
					clanfight.setClanlevel2(c2.getClanlevel());
					clanfight.setWinner(-1);
					
					long onlyid = t + i; //生成公会战唯一id by changhao
					
					xbean.EClanfightids ids1 = xtable.Clanid2clanfightids.get(c1.getKey());
					if (ids1 == null)
					{
						ids1 = xbean.Pod.newEClanfightids();
						xtable.Clanid2clanfightids.insert(c1.getKey(), ids1);
					}
					else
					{
						java.util.List<Long> removelist = new java.util.ArrayList<Long>();
						for (Long o : ids1.getIds()) //遍历清除老的副本id by changhao
						{
							if (o < GetMonday1500ByTime(cur))
							{
								removelist.add(o);
							}
						}
						
						if (removelist.size() != 0)
						{
							ids1.getIds().removeAll(removelist);
						}
					}
					
					ids1.getIds().add(onlyid);
					
					xbean.EClanfightids ids2 = xtable.Clanid2clanfightids.get(c2.getKey());
					if (ids2 == null)
					{
						ids2 = xbean.Pod.newEClanfightids();
						xtable.Clanid2clanfightids.insert(c2.getKey(), ids2);
					}
					else
					{
						java.util.List<Long> removelist = new java.util.ArrayList<Long>();
						for (Long o : ids2.getIds()) //遍历清除老的副本id by changhao
						{
							if (o < GetMonday1500ByTime(cur))
							{
								removelist.add(o);
							}
						}
						
						if (removelist.size() != 0)
						{
							ids2.getIds().removeAll(removelist);
						}
					}
					
					ids2.getIds().add(onlyid);
					
					clanfights.getClan1vschan2().put(onlyid, clanfight);
					
					ClanFightFactory.createClanFightBattleField(200, onlyid, c1.getKey(), c2.getKey(), c1.getClanname(), c2.getClanname(), t);
					
					java.util.Date date = new java.util.Date();
					date.setTime(t);
					String l = date.toLocaleString();
					logger.info("CreateClanFightMatch:时间:[" + l + "]对阵:[" + c1.getClanname() + "] vs:[" + c2.getClanname() +"] id:[" + onlyid +"]");
					
				}
			}
			
			logger.info("CreateClanFightMatch:时间:[" + t + "] 组数:[" + size +"] CreateClanFightMatch ok");
		}
	}
	
	/***
	 * 得到周二00:00 by changhao
	 * @return
	 */
	public static long GetTuesday0000ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -5);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 4;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	/***
	 * 得到周四00:00 by changhao
	 * @return
	 */
	public static long GetThursday0000ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -3);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 2;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	/***
	 * 得到周三15:00 by changhao
	 * @return
	 */
	public static long GetWednesday1500ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -4);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 3;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	/***
	 * 得到周三00:00 by changhao
	 * @return
	 */
	public static long GetWednesday0000ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -4);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 3;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	/***
	 * 得到周一15:00 by changhao
	 * @return
	 */
	public static long GetMonday1500ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 5;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	/***
	 * 得到周一00:00 by changhao
	 * @return
	 */
	public static long GetMonday0000ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 5;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	/***
	 * 创建匹配 by changhao
	 * @throws Exception
	 */
	public void CreateClanFightMatch(boolean force) throws Exception
	{
		//计算出当前的轮数 by changhao
		curweek.set(CalcCurWeekNum());
		
		long cur = System.currentTimeMillis();
		
		java.util.ArrayList<xbean.ClanInfo> list1 = CalcQualificationClan(0); //计算有资格的公会 by changhao
		java.util.ArrayList<xbean.ClanInfo> list2 = CalcQualificationClan(1); //计算有资格的公会 by changhao
		
		java.util.Set<Long> setclanid = new java.util.HashSet<Long>();
		for (xbean.ClanInfo e : list1)
		{
			setclanid.add(e.getKey());
		}
		
		for (xbean.ClanInfo e : list2)
		{
			setclanid.add(e.getKey());
		}
		
		mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.CLANS, setclanid)); //锁定公会id by changhao
		

		long endtime2 = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetClanFightActivityEndTime(2);
		long starttime1 = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetPreClanFightActivityStartTime(1);
		
		long endtime4 = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetClanFightActivityEndTime(4);
		long starttime3 = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().GetPreClanFightActivityStartTime(3);
		
		java.util.Date d = new java.util.Date();
		d.setTime(starttime3);
		String s = d.toLocaleString();
		int k = 4;
		if (cur > starttime1 - 10 * 1000 && cur < endtime2)
		{
			CreateClanFightMatch(0, list1, force); //创建2组比赛 by changhao			
		}
		
		if (cur > starttime3 - 10 * 1000 && cur < endtime4)
		{
			CreateClanFightMatch(1, list2, force);				
		}	
	}
	
	/***
	 * 计算每个公会的 战斗统计信息 by changhao
	 */
	public java.util.Map<Long, ClanFightStatisticsInfo> CalcClanStatistics(int num) throws Exception
	{
		String str = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.clanfight.starttime");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		long starttime = sdf.parse(str).getTime();
		
		Date startdate = new Date(starttime);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startdate);
		
		long t = cal.getTimeInMillis();
		long monday1500 = GetMonday1500ByTime(t);
		long wednesday1500 = GetWednesday1500ByTime(t);
		
		java.util.List<Long> timelist = new java.util.ArrayList<Long>();
		timelist.add(monday1500);
		timelist.add(wednesday1500);
		
		long day1 = 24 * 60 * 60 * 1000;
				
		java.util.Map<Long, ClanFightStatisticsInfo> st = new java.util.HashMap<Long, ClanFightStatisticsInfo>();
		if (this.curweek.get() == -1)
		{
			return st;
		}
		
		int curweekvalue = this.curweek.get() % num;
		int curweekstart = (this.curweek.get() / num) * num;
		
		for (int i = curweekstart; i < curweekstart + curweekvalue + 1; i ++)
		{
			for (Long time : timelist)
			{
				xbean.ClanFights clanfights = xtable.Clanfights.select(time + day1 * 7 * i);
				if (clanfights != null)
				{
					for (xbean.ClanFight e : clanfights.getClan1vschan2().values())
					{
						if (e != null)
						{
							ClanFightStatisticsInfo info1 = st.get(e.getClan1());
							if (info1 == null)
							{
								info1 = new ClanFightStatisticsInfo();
								info1.clanid = e.getClan1();
								info1.clanname = e.getClanname1();
								info1.clanlevel = e.getClanlevel1();
								
								
								st.put(e.getClan1(), info1);
							}
							
							if (e.getWinner() != -1) //这场工会战已经结束 by changhao
							{
								info1.fightcount ++;
								info1.totalscroe += e.getClan1scroe();
							}
							
							if (e.getWinner() == 0)
							{
								info1.wincount ++;
							}
							
							ClanFightStatisticsInfo info2 = st.get(e.getClan2());
							if (info2 == null)
							{
								info2 = new ClanFightStatisticsInfo();
								info2.clanid = e.getClan2();
								info2.clanname = e.getClanname2();
								info2.clanlevel = e.getClanlevel2();
								st.put(e.getClan2(), info2);
							}
							
							if (e.getWinner() != -1) //这场工会战已经结束 by changhao
							{
								info2.fightcount ++;
								info2.totalscroe += e.getClan2scroe();
							}

							if (e.getWinner() == 1)
							{
								info2.wincount ++;
							}	
						}				
					}
				}				
			}
		}
		
		return st;
	}
	
	/***
	 * 某天的战场活动 开启时间 by changhao
	 * @param dayofweek
	 * @return
	 */
	public long GetClanFightActivityStartTime(int dayofweek) throws Exception
	{
		long cur = System.currentTimeMillis();
		fire.pb.timer.ScheculedActivity activity = fire.pb.timer.ActivityManager.getClanFightActivity().get(dayofweek);
		if (activity != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String startTimeStr = activity.startTime;
			Date startDate = sdf.parse(startTimeStr);
			int h = startDate.getHours();
			int m = startDate.getMinutes();
			int s = startDate.getSeconds();	
			
			long day = 0;
			if (dayofweek == 4)
			{
				day = GetThursday0000ByTime(cur);				
			}
			else if (dayofweek == 2)
			{
				day = GetTuesday0000ByTime(cur);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(day);
			cal.set(Calendar.HOUR_OF_DAY, h);				
			cal.set(Calendar.MINUTE, m);
			cal.set(Calendar.SECOND, s);
			cal.set(Calendar.MILLISECOND, 0);
		
			return cal.getTimeInMillis();
		}			
		
		return 0;
	}
	
	/***
	 * 某天的战场活动 结束时间 by changhao
	 * @param dayofweek
	 * @return
	 */
	public long GetClanFightActivityEndTime(int dayofweek) throws Exception
	{
		long cur = System.currentTimeMillis();
		fire.pb.timer.ScheculedActivity activity = fire.pb.timer.ActivityManager.getClanFightActivity().get(dayofweek);
		
		if (activity != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String endstr = activity.endTime;
			Date date = sdf.parse(endstr);
			int h = date.getHours();
			int m = date.getMinutes();
			int s = date.getSeconds();
			
			long day = 0;
			if (dayofweek == 4)
			{
				day = GetThursday0000ByTime(cur);				
			}
			else if (dayofweek == 2)
			{
				day = GetTuesday0000ByTime(cur);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(day);
			cal.set(Calendar.HOUR_OF_DAY, h);				
			cal.set(Calendar.MINUTE, m);
			cal.set(Calendar.SECOND, s);
			cal.set(Calendar.MILLISECOND, 0);
		
			return cal.getTimeInMillis();
		}			
		
		return 0;
	}
	
	/***
	 * 检查这个玩家是否可以公会战不考虑组队 by changhao
	 * lock clan -> clanfightid by changhao
	 * @param roleid
	 * @return
	 */
	public static boolean checkClanFight(long roleid, int level)
	{
		long cur = System.currentTimeMillis();
		
		if (level < CLAN_FIGHT_ROLE_LEVEL)
		{
			return false;
		}
		
		Long cid = xtable.Roleidclan.select(roleid);
		if (cid == null)
		{
			return false;
		}
		
		xbean.ClanInfo c = xtable.Clans.get(cid);
		if (c == null)
		{
			return false;
		}
		
		xbean.ClanMemberInfo mi = c.getMembers().get(roleid);
		if (mi == null)
		{
			return false;
		}
		
		long day1 = 24 * 60 * 60 * 1000;
		if (cur < mi.getJointime() + day1 * CLAN_FIGHT_JOIN_CLAN_TIME) //有人加入公会时间不足2天 by changhao
		{			
			return false;
		}		

		xbean.EClanfightids ids = xtable.Clanid2clanfightids.select(cid);
		if (ids == null)
		{
			return false;
		}
		
		mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.CLANFIGHTIDLOCK, ids.getIds())); //锁定公会战id by changhao
		
		for (Long id : ids.getIds())
		{
			fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(id, false);
			if (bf != null)
			{
				if (cur > bf.getClanfightBean().getActivitystarttime() && cur < bf.getClanfightBean().getActivitynoentertime())
				{
					return true;
				}				
			}			
		}
				
		return false;
	}
	
	/***
	 * 匹配战场活动 开启时间 by changhao
	 * @param dayofweek
	 * @return
	 */
	public long GetPreClanFightActivityStartTime(int dayofweek) throws Exception
	{
		long cur = System.currentTimeMillis();
		fire.pb.timer.ScheculedActivity activity = fire.pb.timer.ActivityManager.getClanFightPreActivity().get(dayofweek);
		if (activity != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String startTimeStr = activity.startTime;
			Date startDate = sdf.parse(startTimeStr);
			int h = startDate.getHours();
			int m = startDate.getMinutes();
			int s = startDate.getSeconds();	
			
			long day = 0;
			if (dayofweek == 3)
			{
				day = GetWednesday0000ByTime(cur);				
			}
			else if (dayofweek == 1)
			{
				day = GetMonday0000ByTime(cur);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(day);
			cal.set(Calendar.HOUR_OF_DAY, h);				
			cal.set(Calendar.MINUTE, m);
			cal.set(Calendar.SECOND, s);
			cal.set(Calendar.MILLISECOND, 0);
		
			return cal.getTimeInMillis();
		}			
		
		return 0;
	}
	
	/***
	 * 匹配战场活动结束时间 by changhao
	 * @param dayofweek
	 * @return
	 */
	public long GetPreClanFightActivityEndTime(int dayofweek) throws Exception
	{
		long cur = System.currentTimeMillis();
		fire.pb.timer.ScheculedActivity activity = fire.pb.timer.ActivityManager.getClanFightPreActivity().get(dayofweek);
		
		if (activity != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String endstr = activity.endTime;
			Date date = sdf.parse(endstr);
			int h = date.getHours();
			int m = date.getMinutes();
			int s = date.getSeconds();
			
			long day = 0;
			if (dayofweek == 3)
			{
				day = GetWednesday0000ByTime(cur);				
			}
			else if (dayofweek == 1)
			{
				day = GetMonday0000ByTime(cur);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(day);
			cal.set(Calendar.HOUR_OF_DAY, h);				
			cal.set(Calendar.MINUTE, m);
			cal.set(Calendar.SECOND, s);
			cal.set(Calendar.MILLISECOND, 0);
		
			return cal.getTimeInMillis();
		}			
		
		return 0;
	}
}
