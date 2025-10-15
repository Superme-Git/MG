package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.PropRole;
import fire.pb.activity.clanfight.ActivityClanFightManager;
import fire.pb.clan.ClanUtils;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;

/***
 * 公会战竞赛排行
 * @author changhao
 *
 */
public class ClanFightRaceRankData extends RankData implements IRankListData{

	ClanFightRaceRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page) {
		
		long datetime = 0;
		long cur = System.currentTimeMillis();
		if (rankType == RankType.CLAN_FIGHT_2)
		{
			datetime = ActivityClanFightManager.GetMonday1500ByTime(cur);
		}
		else if (rankType == RankType.CLAN_FIGHT_4)
		{
			datetime = ActivityClanFightManager.GetWednesday1500ByTime(cur);
		}
		else if (rankType == RankType.CLAN_FIGHT_WEEK)
		{
			datetime = ActivityClanFightManager.GetMonday0000ByTime(cur);
		}
		else
		{
			return null;
		}
		
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
				
		int curweek = ActivityClanFightManager.getInstance().curweek.get();
		int curweekvalue = curweek % 8;
		
		xbean.ClanFightRaceRankList ranlListBean = xtable.Clanfightracelist.select(datetime);
		if (ranlListBean == null && curweekvalue != 0 && rankType == RankType.CLAN_FIGHT_WEEK)
		{
			long day1 = 24 * 3600 * 1000;
			ranlListBean = xtable.Clanfightracelist.select(datetime - day1 * 7);
		}
		
		if(null == ranlListBean)
		{
			response.extdata3 = Integer.toString(curweekvalue);
			return octets;
		}
		
		xbean.Properties prop = xtable.Properties.select(requestRoleid);
		if (prop == null)
		{
			return null;
		}
		
		List<xbean.ClanFightRaceRankRecord> list = ranlListBean.getRecords();
		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < list.size() ? 0:1;
		int myrank = 0;
		int myclanscore = 0;
		int myfightcount = 0;
		int mywincount = 0;
		int myclanlevel = 1;
		
		for(int i=0; i<list.size(); i++)
		{
			xbean.ClanFightRaceRankRecord current = list.get(i);
			if(i >= start && i < end)
			{
				OctetsStream os = new OctetsStream();
				os.marshal(i + 1);				
				os.marshal(current.getMarshaldata().getClanid());
				os.marshal(current.getMarshaldata().getClanname(), "UTF-16LE");
				os.marshal(current.getMarshaldata().getClanlevel());
				os.marshal(current.getMarshaldata().getFightcount());
				os.marshal(current.getMarshaldata().getWincount());
				os.marshal(current.getMarshaldata().getScroe());
				octets.add(os);
			}
			
			if(current.getMarshaldata().getClanid() == prop.getClankey())
			{
				myrank = i+1;
				myclanscore = current.getMarshaldata().getScroe();
				myfightcount = current.getMarshaldata().getFightcount();
				mywincount = current.getMarshaldata().getWincount();
				myclanlevel = current.getMarshaldata().getClanlevel();
			}
		}
		
		response.myrank = myrank;
		response.hasmore = hasMorePage;
		response.page = page;
		
		if (myrank == 0) //如果在榜外 by changhao
		{			
			response.extdata = 0;
		}
		else
		{
			response.extdata = myclanscore;
			response.extdata1 = myfightcount;
			response.extdata2 = mywincount;
			
			if (rankType != RankType.CLAN_FIGHT_WEEK)
			{
				response.extdata2 = myclanlevel;
			}
		}
		
		if (rankType == RankType.CLAN_FIGHT_WEEK)
		{
			response.extdata3 = Integer.toString(myclanlevel);			
		}
		else
		{
			response.extdata3 = Integer.toString(curweekvalue);			
		}
		
		return octets;
	}

}
