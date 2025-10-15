package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.Misc;

public class GiftResult {
	public int id;
	public int itemId;
	public int levelgroup;
	public int minlevel;
	public int maxlevel;
	public List<Integer> awardId = new ArrayList<Integer>();
	public List<Integer> awardRate = new ArrayList<Integer>();
	
	public int getAwardId() {
		if (awardRate == null)
			return 0;
		
		int nIndex = Misc.getProbability(awardRate);
		if (nIndex < awardId.size()) {
			return awardId.get(nIndex);
		}
		
		return 0;
	}
	
	public void getAwardList(int rolelevel, int mapid, List<Integer> itemLst) {
		for (Integer awardid : awardId) {
			if (awardid > 100) {
				List<GiftByMap> itemAwardList = BaoTuMapManager.getInstance().getGiftMap(awardid);
				if (itemAwardList.size() == 1) {
					//没有地图设置
					GiftByMap gift = itemAwardList.get(0);
					int awardRetId = gift.getAwardId(rolelevel);
					itemLst.add(awardRetId);
				}
				else {
					//有地图设置
					for (GiftByMap giftByMap : itemAwardList) {
						if (giftByMap.mapId == mapid) {
							int awardRetId = giftByMap.getAwardId(rolelevel);
							itemLst.add(awardRetId);
						}
					}
				}
			}
			else {
				itemLst.add(awardid);
			}
		}
		
		int nCount = 20 - itemLst.size();
		while (nCount > 0) {
			int awardType = getAwardId();
			if (awardType > 100) { //触发奖励
				List<GiftByMap> itemAwardList = BaoTuMapManager.getInstance().getGiftMap(awardType);
				if (itemAwardList.size() == 1) {
					//没有地图设置
					GiftByMap gift = itemAwardList.get(0);
					int awardRetId = gift.getAwardId(rolelevel);
					itemLst.add(awardRetId);
				}
				else {
					//有地图设置
					for (GiftByMap giftByMap : itemAwardList) {
						if (giftByMap.mapId == mapid) {
							int awardRetId = giftByMap.getAwardId(rolelevel);
							itemLst.add(awardRetId);
						}
					}
				}
			}
			else {
				itemLst.add(awardType);
			}
			nCount --;
		}
	}
}
