package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/***
 * 公会等级
 * @author changhao
 *
 */
public class ClanRankData extends RankData implements IRankListData{

	ClanRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page) {
		
		xbean.ClanRankList ranlListBean = xtable.Clanranklist.select(1);
		if(null == ranlListBean){
			return null;
		}
		PropRole prole = new PropRole(this.requestRoleid, true);
		List<xbean.ClanRankRecord> clanRankList = ranlListBean.getRecords();
		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < clanRankList.size() ? 0:1;
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		int myrank = 0;
		long t = 0;
		int progress = 0;
		for(int i=0; i<clanRankList.size(); i++){
			xbean.ClanRankRecord current = clanRankList.get(i);
			if(i >= start && i < end) {
				OctetsStream os = new OctetsStream();
				os.marshal(i+1);
				os.marshal(current.getClankey());
				os.marshal(current.getClanname(), "UTF-16LE");
				//os.marshal(current.getMastername(), "UTF-16LE");
				os.marshal(t);
				os.marshal(progress);
				os.marshal(current.getLevel());
				os.marshal(current.getMembernum());
				os.marshal(current.getHotel());
				//os.marshal(arg0)
				
				octets.add(os);
			}
			if(current.getClankey() == prole.getClanKey()){
				myrank = i+1;
			}
		}
		response.myrank = myrank;
		response.hasmore = hasMorePage;
		response.page = page;
		
		return octets;
	}

}
