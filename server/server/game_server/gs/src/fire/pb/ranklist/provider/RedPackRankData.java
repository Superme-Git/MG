package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.fushi.redpack.RedPackType;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;


/***
 * 红包排行榜
 * @author changhao
 *
 */
public class RedPackRankData extends RankData implements IRankListData{

	RedPackRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page)
	{
		xbean.RedPackRankList ranlListBean = xtable.Redpacklist.select(rankType);
				
		xbean.RedPack c = xtable.Roleid2worldredpacksendtotal.select(requestRoleid);
		int count = 0;
		if (c != null)
		{
			count += c.getWorldredpack();
		}
		
		if(null == ranlListBean)
		{			
			response.extdata = count;
			return null;
		}

		List<xbean.RedPackRecord> roleRankList = ranlListBean.getRecords();
		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < roleRankList.size() ? 0:1;
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		
		for(int i = 0; i < roleRankList.size(); i++)
		{
			xbean.RedPackRecord current = roleRankList.get(i);
			if(i >= start && i < end) {
				OctetsStream os = new OctetsStream();
				os.marshal(i+1);
				os.marshal(current.getMarshaldata().getRoleid());
				os.marshal(current.getMarshaldata().getName(), "UTF-16LE");
				os.marshal(current.getMarshaldata().getSchool());
				os.marshal(current.getMarshaldata().getNum());
				octets.add(os);
			}
			
			if(current.getMarshaldata().getRoleid() == requestRoleid)
			{
				response.myrank = i+1;
			}
		}
		
		response.mytitle = String.valueOf(new PropRole(this.requestRoleid, true).getProperties().getRolezonghemaxscore());
		response.hasmore = hasMorePage;
		response.page = page;
		
		//计算自己的红包 by changhao
		response.extdata = count;
		
		return octets;
	}

}
