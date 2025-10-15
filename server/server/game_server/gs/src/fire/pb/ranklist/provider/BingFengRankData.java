package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import xbean.BingFengRank;
import fire.pb.PropRole;
import fire.pb.ranklist.SRequestRankList;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

public class BingFengRankData extends RankData implements IRankListData{

	BingFengRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page) {
		BingFengRank bfList = xtable.Bingfenglists.select(rankType);

		List<Octets> octets = new ArrayList<Octets>();
		String roleName = new PropRole(requestRoleid, true).getName();
		if (bfList != null) {
			for (xbean.BingFengRankList rankList : bfList.getRanks().values()) {
				for (int i = 0; i < rankList.getRecords().size(); i++) {
					OctetsStream os = new OctetsStream();
					xbean.BingFengListRecord record = rankList.getRecords().get(i);
					xbean.MarshalBingFengRecord marshalData = record.getMarshaldata();
					marshalData.setRank(i + 1);
					octets.add(os.marshal(marshalData));
					if (marshalData.getRolename().equals(roleName))
						response.myrank = i + 1;
				}
			}
		}
		response.hasmore = 1;
		return octets;
	}

}
