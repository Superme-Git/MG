package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.PropRole;
import fire.pb.clan.ClanUtils;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;

/***
 * 公会综合排行
 * @author changhao
 *
 */
public class ClanZongHeRankData extends RankData implements IRankListData{

	ClanZongHeRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page) {
		
		xbean.ClanZongHeRankList ranlListBean = xtable.Clanzonghelist.select(1);
		if(null == ranlListBean){
			return null;
		}
		PropRole prole = new PropRole(this.requestRoleid, true);
		List<xbean.ClanZongHeRankRecord> clanZongHeRankList = ranlListBean.getRecords();
		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < clanZongHeRankList.size() ? 0:1;
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		int myrank = 0;
		long t = 0;
		int progress = 0;
		int myclanscore = 0;
		for(int i=0; i<clanZongHeRankList.size(); i++){
			xbean.ClanZongHeRankRecord current = clanZongHeRankList.get(i);
			if(i >= start && i < end) {
				OctetsStream os = new OctetsStream();
				os.marshal(i+1);
				os.marshal(current.getClankey());
				os.marshal(current.getClanname(), "UTF-16LE");
				os.marshal(t);
				os.marshal(progress);
				os.marshal(current.getLevel());
				os.marshal(current.getZonghe());
				os.marshal(0);				
				octets.add(os);
			}
			if(current.getClankey() == prole.getClanKey())
			{
				myrank = i+1;
				myclanscore = current.getZonghe();
			}
		}
		response.myrank = myrank;
		response.hasmore = hasMorePage;
		response.page = page;
		
		if (myrank == 0) //如果在榜外 by changhao
		{			
			xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(this.requestRoleid, true);
			if (clanInfo != null)
			{
				response.extdata = clanInfo.getTotalscore();	
			}
		}
		else
		{
			response.extdata = myclanscore;			
		}
		
		return octets;
	}

}
