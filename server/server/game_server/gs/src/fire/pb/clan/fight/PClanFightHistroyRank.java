package fire.pb.clan.fight;

import java.util.Comparator;
import fire.pb.activity.clanfight.ActivityClanFightManager;

/**
 * 公会排名  by changhao
 */
public class PClanFightHistroyRank extends mkdb.Procedure
{
	@Override
	protected boolean process() throws Exception
	{	
		long cur = System.currentTimeMillis();
		//计算出本赛季的公会战 胜率等统计 by changhao
		java.util.Map<Long, ActivityClanFightManager.ClanFightStatisticsInfo> infos = ActivityClanFightManager.getInstance().CalcClanStatistics(24);//24轮一个赛季 by changhao
		
		java.util.List<xbean.ClanFightHistroyRankRecord> sortlist = new java.util.ArrayList<xbean.ClanFightHistroyRankRecord>();
		
		xbean.ClanFightHistroyRankList list = xtable.Clanfighthistroylist.get(0);
		if (list == null)
		{
			list = xbean.Pod.newClanFightHistroyRankList();
			xtable.Clanfighthistroylist.insert(0, list);
		}
		
		list.getRecords().clear();
		
		for (ActivityClanFightManager.ClanFightStatisticsInfo e : infos.values())
		{
			if (e != null)
			{
				xbean.ClanFightHistroyRankRecord record = xbean.Pod.newClanFightHistroyRankRecord();
				record.setTriggertime(cur);
				record.getMarshaldata().setClanid(e.clanid);
				record.getMarshaldata().setClanlevel(e.clanlevel);
				record.getMarshaldata().setClanname(e.clanname);
				record.getMarshaldata().setFightcount(e.fightcount);			
				record.getMarshaldata().setWincount(e.wincount);
				record.getMarshaldata().setTotalscroe(e.totalscroe);
				
				sortlist.add(record);
			}			
		}
		
		//分数从大到小排序 by changhao
		sortlist.sort(new Comparator<xbean.ClanFightHistroyRankRecord>()
		{
			@Override
			public int compare(xbean.ClanFightHistroyRankRecord o1, xbean.ClanFightHistroyRankRecord o2)
			{
				int retscroe = o2.getMarshaldata().getTotalscroe() - o1.getMarshaldata().getTotalscroe();
				if (retscroe != 0)
				{
					return retscroe;
				}
				
				float o2winradio = 0;
				float o1winradio = 0;
				if (o2.getMarshaldata().getFightcount() != 0)
					o2winradio = (float)o2.getMarshaldata().getWincount() / (float)o2.getMarshaldata().getFightcount();
				
				if (o1.getMarshaldata().getFightcount() != 0)
					o1winradio = (float)o1.getMarshaldata().getWincount() / (float)o1.getMarshaldata().getFightcount();
				
				float winret = o2winradio - o1winradio;
				if (winret > 0)
				{
					return 1;
				}
				
				if (winret < 0)
				{
					return -1;
				}
				
				int ret = o2.getMarshaldata().getWincount() - o1.getMarshaldata().getWincount();
				if (ret != 0)
				{
					return ret;
				}
				else
				{
					long r = o1.getMarshaldata().getClanid() - o2.getMarshaldata().getClanid();
					return (int)r;
				}
			}
		});
		
		list.getRecords().addAll(sortlist);
		
		long d = System.currentTimeMillis() - cur;
		ActivityClanFightManager.logger.info("PClanFightHistroyRank.logger: 执行时间[" + d + "]排名完毕");
			
		return true;
	}
}
