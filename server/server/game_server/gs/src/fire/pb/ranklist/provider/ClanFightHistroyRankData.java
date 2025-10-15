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
 * 公会战历史排行
 * @author changhao
 *
 */
public class ClanFightHistroyRankData extends RankData implements IRankListData{

	ClanFightHistroyRankData(int rankype, long requestRoleid)
	{
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page)
	{
		xbean.ClanFightHistroyRankList ranlListBean = xtable.Clanfighthistroylist.select(0);
		if(null == ranlListBean)
		{
			return null;
		}
		
		xbean.Properties prop = xtable.Properties.select(requestRoleid);
		if (prop == null)
		{
			return null;
		}
		
		List<xbean.ClanFightHistroyRankRecord> list = ranlListBean.getRecords();
		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < list.size() ? 0:1;
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		int myrank = 0;
		int myfightcount = 0;
		long mywincount = 0;
		int myclanlevel = 0;
		int curweek = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().curweek.get();
		
		for(int i=0; i<list.size(); i++)
		{
			xbean.ClanFightHistroyRankRecord current = list.get(i);
			if(i >= start && i < end)
			{
				OctetsStream os = new OctetsStream();
				os.marshal(i + 1);				
				os.marshal(current.getMarshaldata().getClanid());
				os.marshal(current.getMarshaldata().getClanname(), "UTF-16LE");
				os.marshal(current.getMarshaldata().getClanlevel());
				os.marshal(current.getMarshaldata().getFightcount());
				os.marshal(current.getMarshaldata().getWincount());
				octets.add(os);
			}
			
			if(current.getMarshaldata().getClanid() == prop.getClankey())
			{
				myrank = i+1;
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
			response.extdata = myfightcount;
			response.extdata1 = mywincount;
			xbean.ClanInfo claninfo = fire.pb.clan.ClanUtils.getClanInfoById(requestRoleid, true);
			if (claninfo != null)
			{
				response.extdata2 = claninfo.getClanlevel();
			}
			else
			{
				response.extdata2 = 1;				
			}
		}
		else
		{
			response.extdata = myfightcount;
			response.extdata1 = mywincount;
			response.extdata2 = myclanlevel;
		}
		
		response.extdata3 = Integer.toString(curweek);
		
		return octets;
	}

}
