package fire.pb.clan.fight;

import java.util.Comparator;

import fire.pb.activity.clanfight.ActivityClanFightManager;
import fire.pb.util.DateValidate;

/**
 * 公会竞赛排名  by changhao
 * lock   Clanfightracelist-> clanfights -> all clanfightids
 */
public class PClanFightRaceRank extends mkdb.Procedure
{
	java.util.List<Long> timelist; //计算哪天的战场 by changhao
	
	public PClanFightRaceRank(java.util.List<Long> timelist)
	{
		this.timelist = timelist;
	}
	
	@Override
	protected boolean process() throws Exception
	{	
		long cur = System.currentTimeMillis();
		//计算出本赛季的公会战 胜率等统计 by changhao
		java.util.Map<Long, ActivityClanFightManager.ClanFightStatisticsInfo> infos = ActivityClanFightManager.getInstance().CalcClanStatistics(8);//一轮的数据 by changhao
		
		java.util.List<xbean.ClanFightRaceRankRecord> sortlist = new java.util.ArrayList<xbean.ClanFightRaceRankRecord>();
		java.util.Map<Long, xbean.ClanFightRaceRankRecord> sortmap = new java.util.HashMap<Long, xbean.ClanFightRaceRankRecord>();
		
		if (timelist.size() == 2)
		{
			java.util.List<Long> locks = new java.util.ArrayList<Long>();
			locks.addAll(timelist);
			locks.add(ActivityClanFightManager.GetMonday0000ByTime(cur));
			
			this.lock(mkdb.Lockeys.get(xtable.Locks.CLANFIGHTRACELIST, locks)); //锁排行榜 by changhao
			this.lock(mkdb.Lockeys.get(xtable.Locks.CLANFIGHTS, locks)); //锁 by changhao			
		}
		
		xbean.ClanFightRaceRankList list = null;
		long datetime = 0;
		for (Long t : timelist)
		{
			if (timelist.size() == 2) //计算的是2场比赛的总排名 by changhao
			{
				datetime = ActivityClanFightManager.GetMonday0000ByTime(cur);
				list = xtable.Clanfightracelist.get(datetime);
				if (list != null)
				{
					list.getRecords().clear();
				}
			}
			else
			{
				datetime = t;
				list = xtable.Clanfightracelist.get(datetime);
				if (list != null)
				{
					list.getRecords().clear();
				}
			}
			
			if (list == null)
			{
				list = xbean.Pod.newClanFightRaceRankList();
				xtable.Clanfightracelist.insert(datetime, list);
			}
			
			if (timelist.size() == 2)
			{
				for (ActivityClanFightManager.ClanFightStatisticsInfo e : infos.values())
				{
					if (e != null)
					{
						xbean.ClanFightRaceRankRecord record = xbean.Pod.newClanFightRaceRankRecord();
						record.setTriggertime(cur);
						record.getMarshaldata().setClanid(e.clanid);
						record.getMarshaldata().setClanlevel(e.clanlevel);
						record.getMarshaldata().setClanname(e.clanname);
						record.getMarshaldata().setFightcount(e.fightcount);			
						record.getMarshaldata().setWincount(e.wincount);
						record.getMarshaldata().setScroe(e.totalscroe);
						
						sortlist.add(record);
					}			
				}
				
				break;
			}
			else
			{
				xbean.ClanFights clanfights = xtable.Clanfights.get(t);
				if (clanfights != null)
				{
					this.lock(mkdb.Lockeys.get(xtable.Locks.CLANFIGHTIDLOCK, clanfights.getClan1vschan2().keySet())); //锁所有公会战id by changhao
					
					for (Long clanfightid : clanfights.getClan1vschan2().keySet())
					{
						fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
						if (bf != null)
						{
							xbean.ClanFight clanfight = clanfights.getClan1vschan2().get(clanfightid);
							if (clanfight != null)
							{
								xbean.ClanFightRaceRankRecord record1 = null;
								record1 = sortmap.get(bf.getClanfightBean().getClanid1());
								if (record1 == null)
								{
									record1 = xbean.Pod.newClanFightRaceRankRecord();	
									record1.setTriggertime(cur);
								}
								
								record1.getMarshaldata().setClanid(bf.getClanfightBean().getClanid1());
								record1.getMarshaldata().setClanname(bf.getClanfightBean().getClanname1());
								int score1 = record1.getMarshaldata().getScroe();
								record1.getMarshaldata().setScroe(score1 + bf.getClanfightBean().getClanscore1());
								record1.getMarshaldata().setClanlevel(clanfight.getClanlevel1());
								
								ActivityClanFightManager.ClanFightStatisticsInfo info1 = infos.get(bf.getClanfightBean().getClanid1());
								if (info1 != null)
								{
									record1.getMarshaldata().setFightcount(info1.fightcount);
									record1.getMarshaldata().setWincount(info1.wincount);
								}
								
								if (sortmap.get(bf.getClanfightBean().getClanid1()) == null)
									sortlist.add(record1);
								
								sortmap.put(bf.getClanfightBean().getClanid1(), record1);
								
								xbean.ClanFightRaceRankRecord record2 = null;
								
								record2 = sortmap.get(bf.getClanfightBean().getClanid2());
								if (record2 == null)
								{
									record2 = xbean.Pod.newClanFightRaceRankRecord();
									record2.setTriggertime(cur);
								}
								
								record2.getMarshaldata().setClanid(bf.getClanfightBean().getClanid2());
								record2.getMarshaldata().setClanname(bf.getClanfightBean().getClanname2());
								int score2 = record2.getMarshaldata().getScroe();
								record2.getMarshaldata().setScroe(score2 + bf.getClanfightBean().getClanscore2());
								record2.getMarshaldata().setClanlevel(clanfight.getClanlevel2());
								
								ActivityClanFightManager.ClanFightStatisticsInfo info2 = infos.get(bf.getClanfightBean().getClanid2());
								if (info2 != null)
								{
									record2.getMarshaldata().setFightcount(info2.fightcount);
									record2.getMarshaldata().setWincount(info2.wincount);
								}
								
								if (sortmap.get(bf.getClanfightBean().getClanid2()) == null)
								{
									sortlist.add(record2);
								}
								
								sortmap.put(bf.getClanfightBean().getClanid2(), record2);
							}
						}	
					}
				}				
			}
		}
		
		if (timelist.size() == 2)
		{
			float totalcount = (float)((ActivityClanFightManager.getInstance().curweek.get() % 8) + 1) * 2.0f;
			//分数从大到小排序 by changhao
			sortlist.sort(new Comparator<xbean.ClanFightRaceRankRecord>()
			{
				@Override
				public int compare(xbean.ClanFightRaceRankRecord o1, xbean.ClanFightRaceRankRecord o2)
				{
					float o2winradio = 0;
					float o1winradio = 0;
					if (o2.getMarshaldata().getFightcount() != 0)
					{
						o2winradio = (float)o2.getMarshaldata().getWincount() / (float)o2.getMarshaldata().getFightcount();
						float radio = CalcFightCountRadio((float)o2.getMarshaldata().getFightcount(), totalcount);
						o2winradio = radio * o2winradio;
					}
					
					if (o1.getMarshaldata().getFightcount() != 0)
					{
						o1winradio = (float)o1.getMarshaldata().getWincount() / (float)o1.getMarshaldata().getFightcount();
						float radio = CalcFightCountRadio((float)o1.getMarshaldata().getFightcount(), totalcount);
						o1winradio = radio * o1winradio;
					}
					
					float winret = o2winradio - o1winradio;
					if (winret > 0)
					{
						return 1;
					}
					
					if (winret < 0)
					{
						return -1;
					}				
					
					int ret = o2.getMarshaldata().getScroe() - o1.getMarshaldata().getScroe();
					if (ret != 0)
					{
						return ret;
					}
					
					int ret2 = o2.getMarshaldata().getFightcount() - o1.getMarshaldata().getFightcount();
					if (ret2 != 0)
					{
						return ret2;
					}				
					else
					{
						long r = o1.getMarshaldata().getClanid() - o2.getMarshaldata().getClanid();
						return (int)r;
					}
				}
			});			
		}
		else
		{
			//分数从大到小排序 by changhao
			sortlist.sort(new Comparator<xbean.ClanFightRaceRankRecord>()
			{
				@Override
				public int compare(xbean.ClanFightRaceRankRecord o1, xbean.ClanFightRaceRankRecord o2)
				{
					int ret = o2.getMarshaldata().getScroe() - o1.getMarshaldata().getScroe();
					if (ret != 0)
					{
						return ret;
					}
										
					long r = o1.getMarshaldata().getClanid() - o2.getMarshaldata().getClanid();
					return (int)r;
				}
			});				
		}

		
		if (list != null)
		{
			list.getRecords().addAll(sortlist);
		}
		
		long d = System.currentTimeMillis() - cur;
		for (Long tt : timelist)
		{
			ActivityClanFightManager.logger.info("PClanFightRaceRank.logger: 战场时间[" + tt + "]执行时间[" + d + "]排名完毕");				
		}
		
		//给排名公会加工资 by changhao
		if (timelist.size() == 2)
		{
			long thursday0000 = ActivityClanFightManager.GetThursday0000ByTime(cur);
			if (DateValidate.inTheSameDay(cur, thursday0000))
			{
				mkdb.Procedure.pexecuteWhileCommit(new PClanFightAddRankMoney());				
			}		
		}
			
		return true;
	}
	
	float CalcFightCountRadio(float fightcount, float totalcount)
	{
		float radio = fightcount / totalcount;
		if (radio >= 0.8f)
		{
			radio = 1.0f;
		}
		else if (radio >= 0.6f)
		{
			radio = 0.8f;
		}
		else if (radio >= 0.4f)
		{
			radio = 0.6f;
		}
		else if (radio >= 0.2f)
		{
			radio = 0.4f;
		}
		else
		{
			radio = 0.2f;
		}		
		
		return radio;
	}
}
