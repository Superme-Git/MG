package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;
import fire.pb.PropRole;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;
import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;


/***
 * 收花排行榜
 * @author changhao
 *
 */
public class ReceFlowerRankData extends RankData implements IRankListData{

	ReceFlowerRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page)
	{
		long t = RankListManager.GetMonday1Millisecond();		
		xbean.ReceFlowerRankList ranlListBean = xtable.Receflowerlist.select(t);
		int count = 0;		
		xbean.WeekGiveReceGift gift = xtable.Roleid2weekgiverecegift.select(requestRoleid);
		if (gift != null)
		{
			xbean.GiveReceGift weekgift = gift.getWeekdata().get(t);
			if (weekgift != null)
			{
				for (Integer v : weekgift.getRecegift().values())
				{
					count += v;
				}				
			}
		}
		
		if(null == ranlListBean)
		{			
			response.extdata = count;
			return null;
		}

		List<xbean.ReceFlowerRecord> roleRankList = ranlListBean.getRecords();
		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < roleRankList.size() ? 0:1;
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		
		for(int i = 0; i < roleRankList.size(); i++)
		{
			xbean.ReceFlowerRecord current = roleRankList.get(i);
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
		
		response.mytitle = "";
		response.hasmore = hasMorePage;
		response.page = page;
		
		//计算自己的红包 by changhao
		response.extdata = count;
		
		return octets;
	}

}
