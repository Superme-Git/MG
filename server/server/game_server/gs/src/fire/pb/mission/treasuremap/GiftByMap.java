package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;

import fire.pb.activity.award.RewardMgr;
import fire.pb.map.SActivityAwardItems;
import fire.pb.mission.instance.LevelGroupAward;
import fire.pb.util.Misc;

public class GiftByMap {
	public int id;
	public int mapId;
	public List<Integer> giftId = new ArrayList<Integer>();
	public List<Integer> giftRate = new ArrayList<Integer>();
	
	public List<Integer> noticeItemId = new ArrayList<Integer>();
	public List<LevelGroupAward> groupAward = new ArrayList<LevelGroupAward>();
	public int noticeId;
	public int noticeGetItemId;
	public int noticeGetMoneyId;
	public int moneyAward;
	
	public int getAwardId(int rolelevel) {
		int awardid = -1;
		if (groupAward.size() == 0)
			return 0;
		
		for (LevelGroupAward groupAward : groupAward) {
			if (rolelevel >= groupAward.levelmin && rolelevel <= groupAward.levelmax)
				awardid = groupAward.awardid;
		}
		
		if (awardid != -1) {
			SActivityAwardItems items = RewardMgr.getInstance().getAward2Map().get(awardid);
			if (items == null) {
				return -1;
			}
			
			int index = Misc.getProbability(items.itemsrate);
			
			return items.getItems().get(index);
		}
		
		return -1;
	}
	
	public boolean getNotice(int itemId) {
		if (noticeItemId.size() == 0) {
			return false;
		}
		
		if (noticeItemId.contains(itemId)) {
			return true;
		}
		
		return false;
	}
}
