package fire.pb.shop.srv.floating;

import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.CopyOnWriteArrayList;

import fire.pb.npc.NpcManager;
import fire.pb.shop.SNpcSale;
import fire.pb.shop.utils.FloatingType;

/**
 * 
 * 浮动价格商店筛选
 * 
 * @author liangyanpeng
 *
 */
public class FileterFloatingShop {
	
	
	private FileterFloatingShop() {
		super();
		this.refreshNpcSale();//初始化数据
	}

	public static FileterFloatingShop getInstance() {
		return FileterFloatingShopHolder.instance;
	}
	
	private static class FileterFloatingShopHolder {
		static FileterFloatingShop instance = new FileterFloatingShop();
	}

	private NavigableMap<Integer, SNpcSale> npcSaleMap = NpcManager.getInstance().getNpcSaleMap();
	private List<Long> floatingOneShopIds = new CopyOnWriteArrayList<>();//价格浮动类型One商店的shopId
	private List<Long> floatingTwoShopIds = new CopyOnWriteArrayList<>();//价格浮动类型Two商店的shopId
	private List<Long> noFloatingShopIds = new CopyOnWriteArrayList<>();//价格不浮动商店的shopId
	

	/**
	 * 刷新npc商店最新数据
	 */
	public synchronized void refreshNpcSale() {
		floatingOneShopIds.clear();//清除上次价格浮动one规则的商店
		noFloatingShopIds.clear();//清除上次价格不浮动的商店
		for (SNpcSale npcSale : this.npcSaleMap.values()) {
			if (npcSale.floating == FloatingType.NO_FLOATING) {
				noFloatingShopIds.add((long)npcSale.id);
				continue;
			}
			if (npcSale.floating == FloatingType.FLOATING_ONE) {
				floatingOneShopIds.add((long)npcSale.id);
			}
			if (npcSale.floating == FloatingType.FLOATING_TWO) {
				floatingTwoShopIds.add((long)npcSale.id);
			}
		}
	}
	
	/**
	 * @return 返回价格浮动one规则商店的shopId集合
	 */
	public List<Long> getFloatingOneShopIds() {
		return floatingOneShopIds;
	}
	
	/**
	 * @return 返回价格不浮动商店的shopId集合
	 */
	public List<Long> getNoFloatingShopIds() {
		return noFloatingShopIds;
	}
	
	/**
	 * @param shopId
	 * @return 是浮动价格商店返回true
	 */
	public boolean isFloatingOne(long shopId) {
		for (Long id : floatingOneShopIds) {
			if (id == shopId)
				return true;
		}
		return false;
	}
	
}
