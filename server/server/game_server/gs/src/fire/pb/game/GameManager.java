package fire.pb.game;

import java.util.HashMap;
import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.util.Misc;

public class GameManager {
	public static final int SCHOOLWHEEL = 1;
	
	public static final int SILVER_BOX = 3;
	public static final int GOLD_BOX = 2;
	
	public static int GOLD_BOX_KEY = 337943;
	public static int SILVER_BOX_KEY = 337942;
	
	Map<Integer, SSchoolWheel> schoolWheelCnf = ConfigManager.getInstance().getConf(SSchoolWheel.class);
	
	private static class SingletonHolder {
		private static GameManager singleton = new GameManager();
	}
	
	public static GameManager getInstance() {
		return SingletonHolder.singleton;
	}
	
	private Map<Integer, WheelItem> ItemCnf = new HashMap<Integer, WheelItem>();

	private GameManager() {
	}
	
	public void Init() {
		for (SSchoolWheel schoolWheel : schoolWheelCnf.values()) {
			WheelItem wheelItem = new WheelItem(schoolWheel.getItems());
			ItemCnf.put(schoolWheel.getId(), wheelItem);
		}
	}
	
	/**
	 * 获取转盘的物品
	 * @param awardIndex
	 * @return
	 */
	public WheelAwardItem getAwardItem(final int awardIndex, final int itemIndex) {
		if (itemIndex == -1)
			return null;
		
		WheelItem wheelItem = ItemCnf.get(awardIndex);
		
		int itemid = wheelItem.itemLst.get(itemIndex);
		int itemnum = wheelItem.numLst.get(itemIndex);
		int notice = wheelItem.notice.get(itemIndex);
		int msg = wheelItem.msg.get(itemIndex);
		
		int mustitem = schoolWheelCnf.get((Integer) awardIndex).getMustitem();
		int mustnum = schoolWheelCnf.get((Integer) awardIndex).getMustnum();
				
		WheelAwardItem awardItem = new WheelAwardItem(itemid, itemnum, notice, msg, mustitem, mustnum);
		return awardItem;
	}
	
	/**
	 * 获取转盘的物品索引
	 * @param awardIndex
	 * @return
	 */
	public int getAwardItemIndex(final int awardIndex) {
		WheelItem wheelItem = ItemCnf.get(awardIndex);
		int index = Misc.getProbability(wheelItem.rateLst);
		if (index != -1) {
			return index;
		}
		
		return -1;
	}
	
}
