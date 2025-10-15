package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/***
 * 公会进度排行
 * @author changhao
 *
 */
public class ClanProgressRankData extends RankData implements IRankListData{

	ClanProgressRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page) {
		
		xbean.ClanProgressRankList ranlListBean = xtable.Clanprogressranklist.select(rankType);
		
		PropRole prole = new PropRole(requestRoleid, true);
		if(null == ranlListBean)
		{
			response.extdata = 0;
			response.extdata1 = 0;
			response.extdata2 = 1.0f;
			response.extdata3 = "融核之心";
			return null;
		}

		List<xbean.ClanProgressRankRecord> roleRankList = ranlListBean.getRecords();
		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < roleRankList.size() ? 0:1;
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		
		for(int i=0; i<roleRankList.size(); i++){
			xbean.ClanProgressRankRecord current = roleRankList.get(i);
			if(i >= start && i < end) {
				OctetsStream os = new OctetsStream();
				os.marshal(i+1);
				os.marshal(current.getClankey());
				os.marshal(current.getClanname(), "UTF-16LE");
				os.marshal(current.getTime());
				os.marshal(current.getProgress());
				os.marshal(current.getClanmastername(), "UTF-16LE");
				os.marshal(current.getCopyname(), "UTF-16LE");
				os.marshal(current.getBosshp());			//BOSS血量百分比 by changhao	
				octets.add(os);
			}
			if(current.getClankey() == prole.getClanKey()){
				response.myrank = i+1;
			}
		}
		
		response.hasmore = hasMorePage;
		response.page = page;
		
		response.extdata = 0;
		response.extdata1 = 0;
		response.extdata2 = 1.0f;
		response.extdata3 = "融核之心";
		
		long factionkey = prole.getProperties().getClankey();
		if (response.myrank == 0 && factionkey > 0) //榜外 by changhao
		{
			xbean.ClanInfo clanInfo = xtable.Clans.select(factionkey);
			if (clanInfo != null)
			{
				java.util.ArrayList<Integer> v = new java.util.ArrayList<Integer>();
				v.add(111);
				v.add(110);
				
				int zoneid = 0;
				
				for (Integer e : v)
				{
					zoneid = e;
					xbean.ClanInstances clanInst = xtable.Claninstances.select(clanInfo.getKey());
					if (clanInst != null)
					{
						if (clanInst.getInstkey().containsKey(zoneid))
						{
							long zonekey = clanInst.getInstkey().get(zoneid);
							
							InstanceZone zone = InstanceZoneFactory.getInstanceZone(zonekey, true);
							if (null != zone)
							{
								if (zone instanceof FactionInstZone)
								{
									FactionInstZone fzone = (FactionInstZone)zone;
									long time = fzone.getSaveTime(fzone.getCurActiveSave() - 1);
									long starttime = fzone.getStartTime();
									
									response.extdata = fzone.getCurActiveSave() - 1;
									response.extdata1 = time - starttime;
									response.extdata2 = 1.0f;
									response.extdata3 = fzone.getZoneConfig().name;
									
									break;
								}
							}
						}
					}
				}
			}
		}
		else
		{
			if (response.myrank != 0)
			{
				
				xbean.ClanProgressRankRecord current = roleRankList.get(response.myrank - 1);
				if (current != null)
				{
					response.extdata = current.getProgress();
					response.extdata1 = current.getTime();
					response.extdata2 = current.getBosshp();
					response.extdata3 = current.getCopyname();
				}				
			}
		}
		
		return octets;
	}

}
