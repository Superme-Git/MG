package robot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import robot.manager.MissionMgr;
import fire.pb.Item;
import fire.pb.Pet;
import fire.pb.game.MoneyType;
import fire.pb.item.BagTypes;
import fire.pb.item.CAppendItem;
import fire.pb.item.CDropItem;
import fire.pb.product.CResolveEquip;
import fire.pb.shop.MarketThreeTable;

/**
 * 背包
 * 
 * @author Bill_Ye 2016年1月19日 下午4:52:59
 */
public class RoleBag {
	private Lock lock;
	private LoginRole role;
	private Map<Byte, Long> currency; // 货币
	public ArrayList<Item> itemsList; // 物品列表
	private Map<Integer, Item> itemMap; // 物品Map(跟上面的数据应该是一样的)
	private int capacity; // 背包容量
	private java.util.ArrayList<fire.pb.Pet> petList; // 身上的宠物
	private LinkedList<Item> canMarketUpItems; // 可以摆摊的物品
	private LinkedList<Pet> canMarketUpPets; // 可以摆摊的宠物

	public RoleBag(LoginRole role, fire.pb.Bag bag,
			java.util.ArrayList<fire.pb.Pet> pets) {
		this.role = role;
		lock = new ReentrantLock();
		itemMap = new ConcurrentHashMap<Integer, Item>();
		currency = new ConcurrentHashMap<Byte, Long>(bag.currency);

		canMarketUpItems = new LinkedList<Item>();
		canMarketUpPets = new LinkedList<Pet>();

		capacity = bag.capacity;
		initBag(bag);
		initPets(pets);
	}

	private synchronized void initPets(ArrayList<fire.pb.Pet> pets) {
		petList = pets;
		for (Pet pet : pets) {
			if (MissionMgr.marketThreeConfMap.get(pet.id) != null) {
				canMarketUpPets.add(pet);
			}
		}

	}

	private synchronized void initBag(fire.pb.Bag bag) {
		itemsList = bag.items;
		for (Item item : bag.items) {
			itemMap.put(item.key, item);
			if (MissionMgr.marketThreeConfMap.get(item.id) != null) {
				canMarketUpItems.add(item);
			}
		}
	}

	public int getPetNum() {
		return petList.size();
	}

	public int getPetKey(int petTypeId) {
		for (Pet pet : petList) {
			if (pet.id == petTypeId && pet.level == 0) {
				return pet.key;
			}
		}
		return -1;
	}

	public boolean isHasPet(int petTypeId) {
		for (Pet pet : petList) {
			if (pet.id == petTypeId) {
				return true;
			}
		}
		return false;
	}

	public void addPet(fire.pb.Pet pet) {
		lock.lock();
		try {
			petList.add(pet);
			if (MissionMgr.marketThreeConfMap.get(pet.id) != null) {
				canMarketUpPets.add(pet);
			}
		} finally {
			lock.unlock();
		}
	}

	public void removePet(int petkey) {
		lock.lock();
		try {
			for (Pet pet : petList) {
				if (pet.key == petkey) {
					petList.remove(pet);
					break;
				}

			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 向背包增加物品
	 * 
	 * @param key
	 * @param item
	 */
	public synchronized void addItem(java.util.ArrayList<fire.pb.Item> items) {
		lock.lock();
		try {
			for (fire.pb.Item item : items) {
				itemsList.add(item);
				itemMap.put(item.key, item);

				if (MissionMgr.marketThreeConfMap.get(item.id) != null) {
					canMarketUpItems.add(item);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public fire.pb.Item getDropItem() {
		if (itemsList == null || itemsList.size() < 1)
			return null;
		return itemsList.get(itemsList.size() - 1);
	}

	public void setGold(long gold) {
		currency.put((byte) MoneyType.MoneyType_GoldCoin, gold);
	}

	public long getMoney() {
		Long money = currency.get((byte) MoneyType.MoneyType_GoldCoin);
		return (money == null) ? 0 : money;
	}

	public void setSliver(long sliver) {
		currency.put((byte) MoneyType.MoneyType_SilverCoin, sliver);
	}

	public long getSliver() {
		Long money = currency.get((byte) MoneyType.MoneyType_SilverCoin);
		return (money == null) ? 0 : money;
	}

	public void setCurrency(byte moneyType, long num) {
		if (moneyType <= MoneyType.MoneyType_None
				|| moneyType >= MoneyType.MoneyType_Num)
			return;
		currency.put(moneyType, num);
	}

	public long getCurrency(int moneyType) {
		if (moneyType <= MoneyType.MoneyType_None
				|| moneyType >= MoneyType.MoneyType_Num)
			return -1;
		Long value = currency.get((byte) moneyType);
		return value == null ? 0 : value;
	}

	public boolean hasItem(int itemId) {
		for (fire.pb.Item item : itemsList) {
			if (item == null) {
				continue;
			}

			if (item.id == itemId) {
				return true;
			}
		}
		return false;
	}

	public synchronized void useItem(int itemId) {
		for (fire.pb.Item item : itemsList) {
			if (item.id == itemId) {
				CAppendItem p = new CAppendItem();
				p.keyinpack = item.key;
				p.id = role.roleId;
				p.idtype = 0;
				role.sendProtocol(p);
				role.setCanJump(true);
				break;
			}
		}
	}

	/**
	 * 背包中的空格子数目
	 * 
	 * @return
	 */
	public int emptyNumbers() {
		return capacity - itemsList.size();
	}

	/**
	 * 背包是否已经空了
	 * 
	 * @return
	 */
	public boolean isEmptyBag() {
		return itemsList.size() == 0;
	}

	public int bagItemSize() {
		return itemsList.size();
	}

	/**
	 * 找一个非绑定的物品
	 * 
	 * @return
	 */
	public synchronized int getTradeItemKey() {
		for (fire.pb.Item item : itemsList) {
			if (item.flags == 0) {
				item.flags = fire.pb.Item.LOCK;
				return item.key;
			}
		}
		return -1;
	}

	public synchronized void removeItem(int itemkey) {
		lock.lock();
		try {
			Item item = itemMap.remove(itemkey);
			if (item != null) {
				itemsList.remove(item);
				if (MissionMgr.marketThreeConfMap.get(item.id) != null) {
					canMarketUpItems.remove(item);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 从包裹中获得一个非绑定的物品
	 * 
	 * @return
	 */
	public Item getUnBindItem() {
		if (itemsList == null || itemsList.size() < 1)
			return null;
		for (Item item : itemsList)
			if ((item.flags & fire.pb.Item.BIND) != fire.pb.Item.BIND)
				return item;
		return null;
	}

	// 把所有的物品上架了
	public synchronized void stallAllProps() {
		// for(Item item : itemsList){
		// CAddItem additem = new CAddItem();
		// SellUnitInfo info = new SellUnitInfo();
		// info.key = item.key;
		// info.unitprice = 1;
		// additem.iteminfo = info;
		// role.sendProtocol(additem);
		// }
	}

	public int getItemKey(int itemId) {
		for (Item item : itemsList) {
			if (item == null) {
				continue;
			}

			if (itemId == item.id)
				return item.key;
		}

		return -1;
	}

	public int getItemPos(int itemId) {
		for (Item item : itemsList)
			if (itemId == item.id)
				return item.position;
		return -1;
	}

	public int getItemNum(int itemId) {
		for (fire.pb.Item item : itemsList) {
			if (item == null) {
				continue;
			}

			if (item.id == itemId) {
				return item.number;
			}
		}
		return 0;
	}

	public synchronized int getCanMarketUpItemsNum() {
		return canMarketUpItems.size();
	}

	public synchronized int getCanMarketUpPetsNum() {
		return canMarketUpPets.size();
	}

	@SuppressWarnings("unchecked")
	public synchronized void checkMarketUpGoodsVaildTime() {
		long curTime = System.currentTimeMillis();
		for (Item item : (LinkedList<Item>) canMarketUpItems.clone()) {
			MarketThreeTable mtt = MissionMgr.marketThreeConfMap.get(item.id);
			if (mtt == null) {
				mkdb.Trace.error("checkMarketUpGoodsVaildTime()配置文件里没有ITEM_ID"
						+ item.id);
				canMarketUpItems.remove(item);
				continue;
			}

			if (item.markettime > curTime) {
				switch (mtt.itemtype) {
				case 2:
					mkdb.Trace.error("物品列表里怎么可能有宠物呢?");
					break;
				case 3: // 装备
					CResolveEquip cre = new CResolveEquip(item.key);
					role.sendProtocol(cre);
					break;
				default: // 非宠物
					CDropItem cdi = new CDropItem(BagTypes.BAG, item.key, 0);
					role.sendProtocol(cdi);
					break;
				}
			}
		}
	}

	public synchronized int[] randMarketUpOneItemOrPet() {
		int[] markUpInfo = new int[4];
		java.util.Random rand = new java.util.Random();
		Boolean isPet = rand.nextBoolean();
		if (isPet && getCanMarketUpPetsNum() > 0) {
			Pet needMarketPet = canMarketUpPets.get(rand
					.nextInt(canMarketUpPets.size()));
			markUpInfo[0] = 2;
			markUpInfo[1] = needMarketPet.key;
			markUpInfo[2] = 1;
			markUpInfo[3] = needMarketPet.id;
		}

		if (!isPet && getCanMarketUpItemsNum() > 0) {
			Item needMarketItem = canMarketUpItems.get(rand
					.nextInt(canMarketUpItems.size()));
			markUpInfo[0] = 1;
			markUpInfo[1] = needMarketItem.key;
			markUpInfo[2] = rand.nextInt(needMarketItem.number) + 1;
			markUpInfo[3] = needMarketItem.id;
		}
		return markUpInfo;
	}
}
